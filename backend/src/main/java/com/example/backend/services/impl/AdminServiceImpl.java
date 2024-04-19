package com.example.backend.services.impl;

import com.example.backend.dto.response.*;
import com.example.backend.entities.Farms;
import com.example.backend.entities.Order;
import com.example.backend.entities.Product;
import com.example.backend.entities.User;
import com.example.backend.exception.ApiRequestException;
import com.example.backend.repository.FarmRepository;
import com.example.backend.repository.OrderRepository;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.services.AdminService;
import com.example.backend.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalTime;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final FarmRepository farmRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    static final int TOTAL_MONTH = 12;
    static final int MONTH_STATISTICS = 4;

    /**
     * Gets information about all farms, products, users, orders and sales for the admin dashboard
     * @param principal principal required to check user authority
     * @return AdminResponseDTO
     */
    @Override
    public AdminResponse getAllInfo(Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        if (!Objects.equals(String.valueOf(user.getRole()), "ADMIN")) {
            throw new ApiRequestException("Unauthorized");
        }
        AdminResponse adminResponse = new AdminResponse();
        List<Farms> allFarms = farmRepository.findAll();
        List<FarmDto> farms = allFarms.stream().map(ResponseUtils::convertFarmResponse).toList();
        adminResponse.setFarms(farms);

        List<Product> allProducts = productRepository.findAll();
        List<ProductDto> products = allProducts.stream().map(ResponseUtils::convertProductResponse)
                .toList();
        adminResponse.setProducts(products);

        List<Order> allOrders = orderRepository.findAll();
        List<OrderDto> orders = allOrders.stream().map(ResponseUtils::convertOrderResponse).toList();
        adminResponse.setOrders(orders);

        List<User> allUsers = userRepository.findAll();
        List<UserDTO> users = allUsers.stream().map(ResponseUtils::convertUserResponse)
                .toList();
        adminResponse.setUsers(users);
        adminResponse.setSales(getOrderByMonth());
        return adminResponse;
    }

    /**
     * Gets the sales received for the last 4 months from orders and subscriptions
     * @return SalesDTO(contains 2 hashmaps one for subscription one for order
     */
    public SalesDTO getOrderByMonth() {

        LocalDate endDate = LocalDate.now(TimeZone.getTimeZone("AST").toZoneId());
        int year = endDate.getYear();
        int month = endDate.getMonthValue() + 1;
        SalesDTO sale = new SalesDTO();
        for (int i = 0; i < MONTH_STATISTICS; i++) {
            month -= 1;
            if (month == 0) {
                year -= 1;
                month = TOTAL_MONTH;
            }
            double orderSalesValue = 0;
            double subscriptionSalesValue = 0;
            LocalDate startDate = LocalDate.of(year, month, 1);
            String currentMonthString = Month.of(month).name();

            LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));

            List<Order> orders = orderRepository.findByOrderDateBetween(startDate.atStartOfDay(),
                    endDateTime);
            for (Order order : orders) {
                if (Objects.equals(String.valueOf(order.getOrderType()), "SUBSCRIPTION")) {
                    subscriptionSalesValue += order.getOrderValue();
                } else {
                    orderSalesValue += order.getOrderValue();
                }
            }
            sale.addValue(currentMonthString, orderSalesValue, "ORDER");
            sale.addValue(currentMonthString, subscriptionSalesValue, "SUBSCRIPTION");
            endDate = startDate;
        }
        return sale;
    }
}
