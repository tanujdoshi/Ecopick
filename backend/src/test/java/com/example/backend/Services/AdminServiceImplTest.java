package com.example.backend.Services;

import com.example.backend.dto.response.AdminResponse;
import com.example.backend.dto.response.SalesDTO;
import com.example.backend.entities.Order;
import com.example.backend.entities.OrderType;
import com.example.backend.entities.Role;
import com.example.backend.entities.User;
import com.example.backend.exception.ApiRequestException;
import com.example.backend.repository.FarmRepository;
import com.example.backend.repository.OrderRepository;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.services.impl.AdminServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    static final int ORDER_VALUE = 100;
    static final int ORDER_VALUE_SECOND = 200;
    static final int EXPECTED_ORDER_SALES = 4;

    @Mock
    private FarmRepository farmRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    @Test
    void testGetAllInfo() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("admin@example.com");

        User adminUser = new User();
        adminUser.setEmail("admin@example.com");
        adminUser.setRole(Role.valueOf("ADMIN"));

        when(userRepository.findByEmail("admin@example.com")).thenReturn(adminUser);
        when(farmRepository.findAll()).thenReturn(new ArrayList<>());
        when(productRepository.findAll()).thenReturn(new ArrayList<>());
        when(orderRepository.findAll()).thenReturn(new ArrayList<>());
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        AdminResponse adminResponse = adminService.getAllInfo(principal);

        assertNotNull(adminResponse);

    }

    @Test
    void testGetAllInfo_Unauthorized() {

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("user@example.com");

        User normalUser = new User();
        normalUser.setEmail("user@example.com");
        normalUser.setRole(Role.valueOf("CUSTOMER"));

        when(userRepository.findByEmail("user@example.com")).thenReturn(normalUser);

        assertThrows(ApiRequestException.class, () -> adminService.getAllInfo(principal));

        verifyNoInteractions(farmRepository, productRepository, orderRepository);
    }

    @Test
    void testGetOrderByMonth() {
        List<Order> orders = new ArrayList<>();
        Order order1 = new Order();
        order1.setOrderValue(ORDER_VALUE);
        order1.setOrderType(OrderType.valueOf("ORDER"));
        orders.add(order1);
        Order order2 = new Order();
        order2.setOrderValue(ORDER_VALUE_SECOND);
        order2.setOrderType(OrderType.valueOf("SUBSCRIPTION"));
        orders.add(order2);
        when(orderRepository.findByOrderDateBetween(any(), any())).thenReturn(orders);

        SalesDTO sales = adminService.getOrderByMonth();

        assertNotNull(sales);
    }

}
