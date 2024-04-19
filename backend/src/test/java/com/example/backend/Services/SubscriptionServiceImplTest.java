package com.example.backend.Services;

import com.example.backend.dto.request.ProductSubscribeRequest;
import com.example.backend.dto.response.GetSubscriptionResponse;
import com.example.backend.entities.*;
import com.example.backend.exception.ApiRequestException;
import com.example.backend.repository.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import com.example.backend.services.impl.SubscriptionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubscriptionServiceImplTest {
        static final int PRODUCT_ID = 100;
        static final int PRODUCT_PRICE = 100;
        static final int PRODUCT_PRICE_FOR_CRON = 10;
        static final int SECOND_PRODUCT_ID = 2;
        static final int AMOUNT_ADD_WALLET = 100;
        static final int EXPECTED_NUMBER_OF_CALLS = 7;
        static final int EXPECTED_SUBSCRIPTION = 2;

        @BeforeEach
        public void setUp() {
                MockitoAnnotations.openMocks(this);
        }

        @Mock
        private ProductRepository productRepositoryMock;

        @Mock
        private FarmRepository farmRepositoryMock;

        @Mock
        private OrderRepository orderRepositoryMock;

        @Mock
        private SubscriptionRepository subscriptionRepositoryMock;

        @Mock
        private UserRepository userRepositoryMock;

        @Mock
        private UserMetaRepository userMetaRepositoryMock;

        @Mock
        private Principal principal;

        @InjectMocks
        private SubscriptionServiceImpl subscriptionServiceImpl;

        @Mock
        Subscription subscription;
        @Mock
        Product product;
        @Mock
        User user;

        @Test
        public void testProductNull() {
                ProductSubscribeRequest request = new ProductSubscribeRequest();
                request.setProduct_id(PRODUCT_ID);
                when(productRepositoryMock.findById(PRODUCT_ID)).thenReturn(null);
                assertThrows(ApiRequestException.class,
                                () -> subscriptionServiceImpl.subscribeProduct(request, mock(Principal.class)));

        }

        @Test
        public void testFarmNull() {
                ProductSubscribeRequest request = new ProductSubscribeRequest();
                Product product = new Product();
                when(productRepositoryMock.findById(anyInt())).thenReturn(product);
                when(farmRepositoryMock.findById(anyInt())).thenReturn(null);
                assertThrows(ApiRequestException.class,
                                () -> subscriptionServiceImpl.subscribeProduct(request, mock(Principal.class)));
        }

        @Test
        public void testUserAlreadySubscribed() {
                ProductSubscribeRequest request = new ProductSubscribeRequest();
                when(productRepositoryMock.findById(anyInt())).thenReturn(new Product());
                when(farmRepositoryMock.findById(anyInt())).thenReturn(new Farms());
                Principal principal = mock(Principal.class);
                when(principal.getName()).thenReturn("abc@gmail.com");
                when(userRepositoryMock.findByEmail(anyString())).thenReturn(new User());
                List<Subscription> subscriptions = new ArrayList<>();
                Subscription subscription = new Subscription();
                subscriptions.add(subscription);
                when(subscriptionRepositoryMock.findAllByUserIdAndProductId(anyInt(), anyInt()))
                                .thenReturn(subscriptions);
                assertThrows(ApiRequestException.class,
                                () -> subscriptionServiceImpl.subscribeProduct(request, principal));
        }

        @Test
        public void testUserDoesNotHaveEnoughBalance() {
                ProductSubscribeRequest request = new ProductSubscribeRequest();
                Product product = new Product();
                product.setPrice(PRODUCT_PRICE);
                when(productRepositoryMock.findById(anyInt())).thenReturn(product);
                when(farmRepositoryMock.findById(anyInt())).thenReturn(new Farms());
                Principal principal = mock(Principal.class);
                when(principal.getName()).thenReturn("abc@gmail.com");
                when(userRepositoryMock.findByEmail(anyString())).thenReturn(new User());
                List<Subscription> subscriptions = new ArrayList<>();
                when(subscriptionRepositoryMock.findAllByUserIdAndProductId(anyInt(), anyInt()))
                                .thenReturn(subscriptions);
                UserMeta userMeta = new UserMeta();
                userMeta.setWallet_balance(0);
                when(userMetaRepositoryMock.findByUser(any())).thenReturn(userMeta);
                assertThrows(ApiRequestException.class,
                                () -> subscriptionServiceImpl.subscribeProduct(request, principal));
        }

        @Test
        public void testProductSubscription() {
                ProductSubscribeRequest request = new ProductSubscribeRequest();
                request.setMon(1);
                request.setThu(1);
                request.setTue(1);
                request.setWed(1);
                request.setFri(1);
                request.setSat(1);
                request.setSun(1);
                Product product = new Product();
                product.setPrice(PRODUCT_PRICE);

                when(productRepositoryMock.findById(anyInt())).thenReturn(product);
                when(farmRepositoryMock.findById(anyInt())).thenReturn(new Farms());
                Principal principal = mock(Principal.class);
                when(principal.getName()).thenReturn("abc@gmail.com");
                when(userRepositoryMock.findByEmail(anyString())).thenReturn(new User());
                List<Subscription> subscriptions = new ArrayList<>();
                when(subscriptionRepositoryMock.findAllByUserIdAndProductId(anyInt(), anyInt()))
                                .thenReturn(subscriptions);

                UserMeta userMeta = new UserMeta();
                userMeta.setWallet_balance(product.getPrice() + AMOUNT_ADD_WALLET);
                when(userMetaRepositoryMock.findByUser(any())).thenReturn(userMeta);
                subscriptionServiceImpl.subscribeProduct(request, principal);
                verify(subscriptionRepositoryMock, times(EXPECTED_NUMBER_OF_CALLS)).save(any(Subscription.class));
        }

        @Test
        public void testCronForMakeOrderSuccess() {
                UserMeta mockuserMeta = new UserMeta();
                mockuserMeta.setWallet_balance(AMOUNT_ADD_WALLET);
                User mockUser = new User();
                mockUser.setEmail("user@example.com");
                mockUser.setUserMeta(mockuserMeta);

                Product product = new Product();
                product.setPrice(PRODUCT_PRICE_FOR_CRON);
                List<Subscription> subscriptions = new ArrayList<>();
                Subscription subscription = new Subscription();
                subscription.setProduct(product);
                subscription.setUser(mockUser);
                subscriptions.add(subscription);

                when(subscriptionRepositoryMock.findByDays(any())).thenReturn(subscriptions);
                subscriptionServiceImpl.CronForMakeOrder();
                Mockito.verify(orderRepositoryMock,
                                Mockito.times(subscriptions.size())).save(Mockito.any(Order.class));
        }

        @Test
        public void testGetOwnSubscription() {
                User mockUser = new User();
                mockUser.setEmail("user@example.com");

                // Mock Principal
                Principal mockPrincipal = mock(Principal.class);
                when(mockPrincipal.getName()).thenReturn(mockUser.getEmail());

                // Mock repository behavior
                when(userRepositoryMock.findByEmail(mockUser.getEmail())).thenReturn(mockUser);

                Images image1 = new Images();
                ArrayList<Images> imgArr = new ArrayList<>();
                imgArr.add(image1);

                Product mockProduct1 = new Product();
                mockProduct1.setId(1);
                mockProduct1.setProductName("Product 1");
                mockProduct1.setProductDescription("Description 1");
                mockProduct1.setImages(imgArr);
                // Set other product properties

                Product mockProduct2 = new Product();
                mockProduct2.setId(SECOND_PRODUCT_ID);
                mockProduct2.setProductName("Product 2");
                mockProduct2.setProductDescription("Description 2");
                mockProduct2.setImages(imgArr);
                // Set other product properties

                Subscription mockSubscription1 = new Subscription();
                mockSubscription1.setName(SubscriptionName.CUSTOM);
                mockSubscription1.setDays(Days.MONDAY);
                mockSubscription1.setProduct(mockProduct1);

                Subscription mockSubscription2 = new Subscription();
                mockSubscription2.setName(SubscriptionName.CUSTOM);
                mockSubscription2.setDays(Days.MONDAY);
                mockSubscription2.setProduct(mockProduct2);

                Subscription mockSubscription3 = new Subscription();
                mockSubscription3.setName(SubscriptionName.CUSTOM);
                mockSubscription3.setDays(Days.TUESDAY);
                mockSubscription3.setProduct(mockProduct2);

                List<Subscription> mockSubscriptions = new ArrayList<>();
                mockSubscriptions.add(mockSubscription1);
                mockSubscriptions.add(mockSubscription2);
                mockSubscriptions.add(mockSubscription3);

                when(subscriptionRepositoryMock.findByUser(mockUser)).thenReturn(mockSubscriptions);

                // Call the method under test
                List<GetSubscriptionResponse> responses = subscriptionServiceImpl.getOwnSubscription(mockPrincipal);

                // Assertions
                assertEquals(EXPECTED_SUBSCRIPTION, responses.size()); // Expecting two subscriptions

        }

        @Test
        public void testGetSubscribedProduct() {
                User mockUser = new User();
                mockUser.setEmail("user@example.com");

                // Mock Principal
                Principal mockPrincipal = mock(Principal.class);
                when(mockPrincipal.getName()).thenReturn(mockUser.getEmail());

                // Mock repository behavior
                when(userRepositoryMock.findByEmail(mockUser.getEmail())).thenReturn(mockUser);

                Images image1 = new Images();
                ArrayList<Images> imgArr = new ArrayList<>();
                imgArr.add(image1);

                Product mockProduct1 = new Product();
                mockProduct1.setId(1);
                mockProduct1.setProductName("Product 1");
                mockProduct1.setProductDescription("Description 1");
                mockProduct1.setImages(imgArr);
                // Set other product properties

                Product mockProduct2 = new Product();
                mockProduct2.setId(SECOND_PRODUCT_ID);
                mockProduct2.setProductName("Product 2");
                mockProduct2.setProductDescription("Description 2");
                mockProduct2.setImages(imgArr);
                // Set other product properties

                User mockUser1 = new User();
                mockUser1.setId(1);
                mockUser1.setFirstname("Tanuj");
                mockUser1.setLastname("Doshi");

                User mockUser2 = new User();
                mockUser2.setId(2);
                mockUser2.setFirstname("Tanuj");
                mockUser2.setLastname("Doshi");

                Subscription mockSubscription1 = new Subscription();
                mockSubscription1.setName(SubscriptionName.CUSTOM);
                mockSubscription1.setDays(Days.MONDAY);
                mockSubscription1.setProduct(mockProduct1);
                mockSubscription1.setUser(mockUser1);


                Subscription mockSubscription2 = new Subscription();
                mockSubscription2.setName(SubscriptionName.CUSTOM);
                mockSubscription2.setDays(Days.MONDAY);
                mockSubscription2.setProduct(mockProduct2);
                mockSubscription2.setUser(mockUser2);

                Subscription mockSubscription3 = new Subscription();
                mockSubscription3.setName(SubscriptionName.CUSTOM);
                mockSubscription3.setDays(Days.TUESDAY);
                mockSubscription3.setProduct(mockProduct2);
                mockSubscription3.setUser(mockUser2);

                List<Subscription> mockSubscriptions = new ArrayList<>();
                mockSubscriptions.add(mockSubscription1);
                mockSubscriptions.add(mockSubscription2);
                mockSubscriptions.add(mockSubscription3);

                Farms farm = new Farms();
                farm.setSubscriptions(mockSubscriptions);
                List<Farms> farms = new ArrayList<>();
                farms.add(farm);
                mockUser.setFarms(farms);

                when(subscriptionRepositoryMock.findByUser(mockUser)).thenReturn(mockSubscriptions);

                // Call the method under test
                List<GetSubscriptionResponse> responses = subscriptionServiceImpl.getMySubscribedProduct(mockPrincipal);

                // Assertions
                assertEquals(EXPECTED_SUBSCRIPTION, responses.size()); // Expecting two subscriptions

        }

        @Test
        public void testCancelSubscription(){
                when(principal.getName()).thenReturn("testemail");
                when(userRepositoryMock.findByEmail("testemail")).thenReturn(user);
                when(productRepositoryMock.findById(anyInt())).thenReturn(product);
                subscription.setUser(user);
                subscription.setProduct(product);
                when(subscriptionRepositoryMock.findByUser(any(User.class))).thenReturn(new ArrayList<>(Arrays.asList(subscription)));
                assertEquals("UnSubscribed Successfully",subscriptionServiceImpl.cancelSubscription(principal,product.getId()));
        }

}
