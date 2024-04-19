package com.example.backend.services.impl;

import static org.mockito.ArgumentMatchers.nullable;

import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.example.backend.dto.request.ProductSubscribeRequest;
import com.example.backend.dto.response.GetSubscriptionResponse;
import com.example.backend.dto.response.ProductSubscriptionResponse;
import com.example.backend.entities.Days;
import com.example.backend.entities.Farms;
import com.example.backend.entities.Images;
import com.example.backend.entities.Order;
import com.example.backend.entities.OrderType;
import com.example.backend.entities.Product;
import com.example.backend.entities.Subscription;
import com.example.backend.entities.User;
import com.example.backend.entities.UserMeta;
import com.example.backend.exception.ApiRequestException;
import com.example.backend.repository.FarmRepository;
import com.example.backend.repository.OrderRepository;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.SubscriptionRepository;
import com.example.backend.repository.UserMetaRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.services.SubscriptionService;

import jakarta.transaction.Transactional;

import java.time.LocalDate;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {

    static final int SUBSCTRING_END_INDEX = 3;
    private final UserRepository userRepository;
    private final UserMetaRepository userMetaRepository;
    private final ProductRepository productRepository;
    private final FarmRepository farmRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final OrderRepository orderRepository;

    /**
     * Subscribes to a product
     * @param request contains the information for subscribing
     * @param principal user token
     */
    @Override
    public void subscribeProduct(ProductSubscribeRequest request, Principal principal) {

        Product product = productRepository.findById(request.getProduct_id());
        if (product == null) {
            throw new ApiRequestException("Product not found");
        }

        Farms farm = farmRepository.findById(request.getFarm_id());

        if (farm == null) {
            throw new ApiRequestException("Farm not found");
        }
        User user = userRepository.findByEmail(principal.getName());
        List<Subscription> s = subscriptionRepository.findAllByUserIdAndProductId(user.getId(),
                product.getId());

        if (s.size() > 0) {
            throw new ApiRequestException("User has already subscribed to this product");
        }

        UserMeta userDetails = userMetaRepository.findByUser(user);

        // check if customer has valid amount of balance to subscribe the product
        if (userDetails.getWallet_balance() < product.getPrice()) {
            throw new ApiRequestException("You do not have sufficient balance to buy this product");
        }

        List<String> subscribeDays = getSubScribedDays(request);

        for (String day : subscribeDays) {
            Subscription subscription = new Subscription();
            Days enumDay = Days.valueOf(day);
            subscription.setDays(enumDay);
            subscription.setName(request.getName());
            subscription.setSubscriptionDate(LocalDateTime.now());
            subscription.setUser(user);
            subscription.setProduct(product);
            subscription.setFarm(farm);
            subscriptionRepository.save(subscription);
        }
    }

    /**
     * runs the CRON job, available on admin dashboard
     */
    public void runCron() {
        CronForMakeOrder();
    }

    /**
     * Gets the subscriptions made by a user
     * @param principal user token
     * @return returns the list of all subscriptions made by the user
     */
    @Override
    public List<GetSubscriptionResponse> getOwnSubscription(Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        List<Subscription> subscriptions = subscriptionRepository.findByUser(user);
        List<GetSubscriptionResponse> responses = new ArrayList<>();
        for (Subscription subscription : subscriptions) {
            Product product = subscription.getProduct();

            boolean productAlreadyExists = false;
            for (GetSubscriptionResponse response : responses) {
                if (response.getProductId() == product.getId()) {
                    ArrayList<String> days = response.getDays();
                    days.add(subscription.getDays().toString());
                    response.setDays(days);
                    productAlreadyExists = true;
                    break;
                }
            }

            if (!productAlreadyExists) {
                GetSubscriptionResponse generatedResponse = new GetSubscriptionResponse();
                ProductSubscriptionResponse productResponse = new ProductSubscriptionResponse();
                productResponse.setProductName(product.getProductName());
                productResponse.setProductDescription(product.getProductDescription());
                productResponse.setPrice(product.getPrice());
                productResponse.setStock(product.getStock());
                productResponse.setUnit(product.getUnit());

                for (Images images : product.getImages()) {
                    productResponse.addImage(images);
                }

                generatedResponse.setName(subscription.getName());
                ArrayList<String> days = new ArrayList<>();
                days.add(subscription.getDays().toString());
                generatedResponse.setDays(days);
                generatedResponse.setProductId(product.getId());
                generatedResponse.setProduct(productResponse);
                generatedResponse.setSubscriptionDate(subscription.getSubscriptionDate());
                responses.add(generatedResponse);
            }
        }
        return responses;
    }

    /**
     * Gets the products of a farmer that have been subscribed to
     * @param principal user token
     * @return list of all products that belong to a farmer that have at least one active subscription
     */
    @Override
    public List<GetSubscriptionResponse> getMySubscribedProduct(Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        List<Farms> userFarms = user.getFarms();

        List<Subscription> subscriptions = new ArrayList<Subscription>();
        for (Farms f : userFarms) {
            List<Subscription> farmSubscription = f.getSubscriptions();
            subscriptions.addAll(farmSubscription);
        }

        List<GetSubscriptionResponse> responses = new ArrayList<>();
        for (Subscription subscription : subscriptions) {
            Product product = subscription.getProduct();
            User currentUser = subscription.getUser();

            boolean productAlreadyExists = false;
            for (GetSubscriptionResponse response : responses) {
                if (response.getProductId() == product.getId() && response.getUserId() == currentUser.getId()) {
                    ArrayList<String> days = response.getDays();
                    days.add(subscription.getDays().toString());
                    response.setDays(days);
                    productAlreadyExists = true;
                    break;
                }
            }

            if (!productAlreadyExists) {
                GetSubscriptionResponse generatedResponse = new GetSubscriptionResponse();
                ProductSubscriptionResponse productResponse = new ProductSubscriptionResponse();
                productResponse.setProductName(product.getProductName());
                productResponse.setProductDescription(product.getProductDescription());
                productResponse.setPrice(product.getPrice());
                productResponse.setStock(product.getStock());
                productResponse.setUnit(product.getUnit());

                for (Images images : product.getImages()) {
                    productResponse.addImage(images);
                }

                generatedResponse.setName(subscription.getName());
                ArrayList<String> days = new ArrayList<>();
                days.add(subscription.getDays().toString());
                generatedResponse.setDays(days);
                generatedResponse.setProductId(product.getId());
                generatedResponse.setProduct(productResponse);
                generatedResponse.setSubscriptionDate(subscription.getSubscriptionDate());
                generatedResponse.setCustomerName(currentUser.getFirstname() + " " + currentUser.getLastname());
                generatedResponse.setUserId(currentUser.getId());
                responses.add(generatedResponse);
            }
        }
        return responses;
    }

    /**
     * Cron Job that runs everyday at 11:55 pm
     */
    // @Scheduled(cron = "0 * * * * *") // running every minute
    // @Scheduled(cron = "* * * * * *") // every second
    @Scheduled(cron = "55 23 * * * *") // Runs everyday at 11:55 PM
    public void CronForMakeOrder() {
        DayOfWeek currentDayOfWeek = LocalDate.now().getDayOfWeek();

        // next Day
        DayOfWeek upcomingDay = currentDayOfWeek.plus(1);
        List<Subscription> upcomingSubscriptions = subscriptionRepository
                .findByDays(Days.valueOf(upcomingDay.toString()));

        // creating order for all upcoming subscription
        for (Subscription subscription : upcomingSubscriptions) {
            User user = subscription.getUser();
            UserMeta userMeta = user.getUserMeta();
            Product product = subscription.getProduct();
            if (userMeta.getWallet_balance() < product.getPrice()) {
                throw new ApiRequestException("Can not make order, user does not have balance");
            } else {
                Order order = new Order();
                order.setUser(subscription.getUser());
                order.setProduct(subscription.getProduct());
                order.setFarm(subscription.getFarm());
                order.setOrderDate(LocalDateTime.now());
                order.setOrderValue(product.getPrice());
                order.setQuantity(1);
                order.setOrderPaymentMethod("Wallet");
                order.setOrderType(OrderType.SUBSCRIPTION);
                order.setSubscription(subscription);
                orderRepository.save(order);
                userMeta.setWallet_balance(userMeta.getWallet_balance() - product.getPrice());
                userMetaRepository.save(userMeta);
            }

        }
    }

    /**
     * gets days of that are part of the subscription
     * @param request DTO containing the subscription information
     * @return a list of days
     */
    public List<String> getSubScribedDays(ProductSubscribeRequest request) {
        List<String> subscribeDays = new ArrayList<>();

        for (Days day : Days.values()) {
            try {
                // Get the field corresponding to the day using reflection
                String firstchar = day.name().substring(0, 1).toUpperCase();
                String remainingChar = day.name().substring(1, SUBSCTRING_END_INDEX).toLowerCase();
                String methodName = "get" + firstchar + remainingChar;
                int fieldValue = (int) request.getClass().getMethod(methodName).invoke(request);

                if (fieldValue == 1) {
                    subscribeDays.add(day.name());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return subscribeDays;
    }

    public String cancelSubscription(Principal principal, int productId) {
        User user = userRepository.findByEmail(principal.getName());
        Product product = productRepository.findById(productId);
        if (user == null)
            throw new ApiRequestException("user not present");
        if (product == null)
            throw new ApiRequestException("product not found for this subscription");
        List<Subscription> subscription = subscriptionRepository.findByUser(user);
        if (subscription.isEmpty())
            throw new ApiRequestException("No Subscription found for you " + user.getFirstname());
        subscriptionRepository.deleteByUserIdAndProductId(user.getId(), product.getId());
        return "UnSubscribed Successfully";
    }
}
