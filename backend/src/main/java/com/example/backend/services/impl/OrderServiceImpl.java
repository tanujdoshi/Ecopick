package com.example.backend.services.impl;

import org.springframework.stereotype.Service;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.backend.dto.request.OrderRequest;
import com.example.backend.dto.response.OrderDto;
import com.example.backend.entities.Farms;
import com.example.backend.entities.Images;
import com.example.backend.entities.Order;
import com.example.backend.entities.OrderType;
import com.example.backend.entities.Product;
import com.example.backend.entities.User;
import com.example.backend.entities.UserMeta;
import com.example.backend.entities.Wallet;
import com.example.backend.exception.ApiException;
import com.example.backend.exception.ApiRequestException;
import com.example.backend.repository.FarmRepository;
import com.example.backend.repository.OrderRepository;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.UserMetaRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.WalletRepository;
import com.example.backend.services.OrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final UserMetaRepository userMetaRepository;
    private final ProductRepository productRepository;
    private final FarmRepository farmRepository;
    private final OrderRepository orderRepository;
    private final WalletRepository walletRepository;

    /**
     * Place new order
     * @param orderRequest Order Request, contains order details
     * @param principal contain user details
     */
    @Override
    public void placeOrder(OrderRequest orderRequest, Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        UserMeta userDetails = userMetaRepository.findByUser(user);
        Product product = productRepository.findById(orderRequest.getProduct_id());
        Farms farm = farmRepository.findById(orderRequest.getFarm_id());
        if (product == null) {
            throw new ApiRequestException("Product not found");
        }

        if (farm == null) {
            throw new ApiRequestException("Farm not found");
        }

        if (orderRequest.getQuantity() > product.getStock()) {
            throw new ApiRequestException("Order cannot have more quantity than total stock");
        }

        double product_price = product.getPrice();
        double total_price = product_price * orderRequest.getQuantity();

        // check if customer has valid amount of
        if (userDetails.getWallet_balance() < total_price) {
            throw new ApiRequestException("You do not have sufficient balance to buy this product");
        }
        Order order = new Order();
        order.setUser(user);
        order.setProduct(product);
        order.setFarm(farm);
        order.setQuantity(orderRequest.getQuantity());
        order.setOrderDate(LocalDateTime.now());
        order.setOrderValue(total_price);
        order.setOrderPaymentMethod("Wallet");
        order.setOrderType(OrderType.ORDER);
        orderRepository.save(order);

        Wallet wallet = new Wallet();
        wallet.setAmount(total_price);
        wallet.setUser(user);
        wallet.setPaymnent_Method_Reference("Order");
        walletRepository.save(wallet);

        userDetails.setWallet_balance(userDetails.getWallet_balance() - total_price);
        userMetaRepository.save(userDetails);

        product.setStock(product.getStock() - orderRequest.getQuantity());
        productRepository.save(product);
    }

    /**
     * Retrieves the order history of a user
     * @param principal user token
     * @return list of orders placed by the user
     */
    @Override
    public List<OrderDto> orderHistory(Principal principal) {
        // Order order = new Order();
        User user = userRepository.findByEmail(principal.getName());
        ArrayList<OrderDto> orderDtoList = new ArrayList<>();
        // UserMeta userDetails = userMetaRepository.findByUser(user);
        if (user == null) {
            throw new ApiRequestException("User not Found");
        }

        List<Order> orders = orderRepository.findByUser(user);
        for (Order orderPlaced : orders) {
            OrderDto orderDto = new OrderDto();
            orderDto.setId(orderPlaced.getId());
            orderDto.setProduct(orderPlaced.getProduct());
            orderDto.setProductName(orderPlaced.getProduct().getProductName());
            orderDto.setProductDescription(orderPlaced.getProduct().getProductDescription());
            orderDto.setFarm(orderPlaced.getFarm());
            orderDto.setFarmName(orderPlaced.getFarm().getName());
            orderDto.setOrderDate(orderPlaced.getOrderDate());
            orderDto.setOrderValue(orderPlaced.getOrderValue());
            orderDto.setQuantity(orderPlaced.getQuantity());
            orderDto.setOrderPaymentMethod(orderPlaced.getOrderPaymentMethod());
            orderDto.setOrderType(String.valueOf(orderPlaced.getOrderType()));

            for (Images image : orderPlaced.getProduct().getImages()) {
                orderDto.addImage(image);
            }
            orderDtoList.add(orderDto);
        }
        return orderDtoList;
    }

}