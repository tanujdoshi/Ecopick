package com.example.backend.Services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.security.Principal;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.backend.dto.request.OrderRequest;
import com.example.backend.entities.*;
import com.example.backend.exception.ApiRequestException;
import com.example.backend.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.example.backend.dto.response.OrderDto;
import com.example.backend.services.impl.OrderServiceImpl;

public class OrderServiceTest {
        static final int WALLET_BALANCE = 200;
        static final int PRODUCT_PRICE = 100;
        static final int INSUFF_BALANCE = 10;

        @Mock
        private UserRepository userRepository;

        @Mock
        private UserMetaRepository userMetaRepository;

        @Mock
        private OrderRepository orderRepository;

        @Mock
        private ProductRepository productRepository;

        @Mock
        private FarmRepository farmRepository;

        @Mock
        private OrderServiceImpl orderService;

        @Mock
        private WalletRepository walletRepository;

        private User mockUser;
        private List<Order> mockOrders;
        private Principal principal;

        static final double ORDER_VALUE = 100.0;
        static final int ORDER_QUANTITY = 2;

        // @BeforeEach
        // public void setUp() {
        // MockitoAnnotations.openMocks(this);
        // }

        @BeforeEach
        void setUp() {
                int orderId = 1;
                int productid = 1;
                MockitoAnnotations.openMocks(this);

                orderService = new OrderServiceImpl(userRepository, userMetaRepository, productRepository,
                                farmRepository, orderRepository, walletRepository);
                mockUser = new User();
                mockUser.setEmail("test@example.com");

                mockOrders = new ArrayList<>();
                Order order1 = new Order();
                order1.setId(orderId);

                Images image1 = new Images();
                ArrayList<Images> imgArr = new ArrayList<>();
                imgArr.add(image1);
                Product product = new Product();
                product.setId(productid);
                product.setProductName("Test Product");
                product.setProductDescription("Test Product Description");
                product.setImages(imgArr);

                Farms farm = new Farms();
                farm.setId(1);
                farm.setName("Test Farm");

                order1.setProduct(product);
                order1.setFarm(farm);
                order1.setOrderDate(LocalDateTime.now());
                order1.setOrderValue(ORDER_VALUE);
                order1.setQuantity(ORDER_QUANTITY);

                mockOrders.add(order1);
                principal = mock(Principal.class);

        }

        @Test
        public void testOrderHistory() {
                when(userRepository.findByEmail(anyString())).thenReturn(mockUser);
                when(orderRepository.findByUser(any(User.class))).thenReturn(mockOrders);

                Principal mockPrincipal = () -> "test@example.com";
                List<OrderDto> orderDtoList = orderService.orderHistory(mockPrincipal);

                assertEquals(mockOrders.size(), orderDtoList.size());
        }

        @Test
        public void testWhenUSerNotPresent() {
                when(userRepository.findByEmail(anyString())).thenReturn(null);
                when(orderRepository.findByUser(any(User.class))).thenReturn(new ArrayList<>());
                Principal mockNullPrincipal = mock(Principal.class);
                when(mockNullPrincipal.getName()).thenReturn(null);
                assertThrows(ApiRequestException.class, () -> orderService.orderHistory(mockNullPrincipal),
                                "User not Found");

        }

        @Test
        public void testPlaceOrderSuccess() {
                OrderRequest orderRequest = new OrderRequest();
                orderRequest.setProduct_id(1);
                orderRequest.setFarm_id(1);
                orderRequest.setQuantity(1);

                UserMeta userMeta = new UserMeta();
                userMeta.setWallet_balance(WALLET_BALANCE);
                User user = new User();
                user.setUserMeta(userMeta);

                Product product = new Product();
                product.setStock(1);
                product.setPrice(PRODUCT_PRICE);

                Farms farms = new Farms();
                String email = "test@gmail.com";
                when(principal.getName()).thenReturn(email);
                when(userRepository.findByEmail(email)).thenReturn(user);
                when(userMetaRepository.findByUser(user)).thenReturn(userMeta);
                when(productRepository.findById(anyInt())).thenReturn(product);
                when(farmRepository.findById(orderRequest.getFarm_id())).thenReturn(farms);

                orderService.placeOrder(orderRequest, principal);

                verify(orderRepository, times(1)).save(any(Order.class));
                verify(userMetaRepository, times(1)).save(any(UserMeta.class));
                verify(productRepository, times(1)).save(any(Product.class));
                assertEquals(0, product.getStock());
        }

        @Test
        public void testProductNull() {
                OrderRequest orderRequest = new OrderRequest();

                when(productRepository.findById(anyInt())).thenReturn(null);

                // ASSERT
                assertThrows(
                                ApiRequestException.class,
                                () -> orderService.placeOrder(orderRequest, principal));

                // assertEquals("Product not found", thrown.getMessage());
        }

        @Test
        public void testFarmNull() {
                OrderRequest orderRequest = new OrderRequest();
                Product product = new Product();

                when(productRepository.findById(anyInt())).thenReturn(product);
                when(farmRepository.findById(anyInt())).thenReturn(null);

                // ASSERT
                assertThrows(ApiRequestException.class,
                                () -> orderService.placeOrder(orderRequest, principal));

        }

        @Test
        public void testOutOfStock() {
                OrderRequest orderRequest = new OrderRequest();
                orderRequest.setProduct_id(1);
                orderRequest.setFarm_id(1);
                orderRequest.setQuantity(ORDER_QUANTITY);

                Product product = new Product();
                product.setStock(1);

                Farms farms = new Farms();

                when(productRepository.findById(anyInt())).thenReturn(product);
                when(farmRepository.findById(anyInt())).thenReturn(farms);

                // ASSERT
                assertThrows(ApiRequestException.class,
                                () -> orderService.placeOrder(orderRequest, principal));
        }

        @Test
        public void TestInsufficientBalance() {
                OrderRequest orderRequest = new OrderRequest();
                orderRequest.setProduct_id(1);
                orderRequest.setFarm_id(1);
                orderRequest.setQuantity(1);

                UserMeta userMeta = new UserMeta();
                userMeta.setWallet_balance(INSUFF_BALANCE);
                User user = new User();
                user.setUserMeta(userMeta);

                Product product = new Product();
                product.setStock(1);
                product.setPrice(PRODUCT_PRICE);

                Farms farms = new Farms();
                String email = "test@gmail.com";
                when(principal.getName()).thenReturn(email);
                when(userRepository.findByEmail(email)).thenReturn(user);
                when(userMetaRepository.findByUser(user)).thenReturn(userMeta);
                when(productRepository.findById(anyInt())).thenReturn(product);
                when(farmRepository.findById(orderRequest.getFarm_id())).thenReturn(farms);

                // ASSERT
                assertThrows(
                                ApiRequestException.class,
                                () -> orderService.placeOrder(orderRequest, principal));
        }
}
