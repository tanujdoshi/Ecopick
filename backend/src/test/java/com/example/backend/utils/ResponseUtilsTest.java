package com.example.backend.utils;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.example.backend.dto.response.*;
import com.example.backend.entities.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ResponseUtilsTest {
    static final double LATITUDE = 10.1234;
    static final double LONGITUDE = 20.5678;
    static final double FARM_RES = 0.0001;
    static final double PRODUCT_PRICE = 10.50;
    static final double PRODUCTDTO_PRICE = 0.01;
    static final int PRODUCT_STOCK = 100;
    static final double ORDER_VALUE = 100.0;
    static final int ORDER_QTY = 5;

    @Mock
    private Farms farms;

    @Mock
    private Product product;

    @Mock
    private User user;

    @Mock
    private Order order;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConvertFarmResponse() {
        when(farms.getId()).thenReturn(1);
        when(farms.getName()).thenReturn("Farm Name");
        when(farms.getAddress()).thenReturn("Farm Address");
        when(farms.getDescription()).thenReturn("Farm Description");
        when(farms.getLat()).thenReturn(LATITUDE);
        when(farms.getLng()).thenReturn(LONGITUDE);

        FarmDto farmDto = ResponseUtils.convertFarmResponse(farms);

        assertEquals(farms.getId(), farmDto.getId());
    }

    @Test
    public void testConvertProductResponse() {
        when(product.getId()).thenReturn(1);
        when(product.getProductName()).thenReturn("Product Name");
        when(product.getStock()).thenReturn(PRODUCT_STOCK);
        when(product.getPrice()).thenReturn(PRODUCT_PRICE);
        when(product.getUnit()).thenReturn("kg");
        when(product.getProductDescription()).thenReturn("Product Description");

        Category category = new Category();
        category.setId(1);
        category.setName("Category Name");

        when(product.getCategory()).thenReturn(category);

        List<Images> imagesList = new ArrayList<>();
        Images image = new Images();
        imagesList.add(image);
        when(product.getImages()).thenReturn(imagesList);

        ProductDto productDto = ResponseUtils.convertProductResponse(product);

        assertEquals(product.getId(), productDto.getId());
    }

    @Test
    public void testConvertUserResponse() {
        when(user.getId()).thenReturn(1);
        when(user.getEmail()).thenReturn("user@example.com");
        when(user.getFirstname()).thenReturn("John");
        when(user.getLastname()).thenReturn("Doe");
        when(user.getRole()).thenReturn(Role.valueOf("CUSTOMER"));

        UserDTO userDTO = ResponseUtils.convertUserResponse(user);

        assertEquals(user.getId(), userDTO.getId());
    }

    @Test
    public void testConvertOrderResponse() {
        when(order.getId()).thenReturn(1);
        String str = "2001-10-26 12:30";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        when(order.getOrderDate()).thenReturn(LocalDateTime.parse(str, formatter));
        when(order.getOrderValue()).thenReturn(ORDER_VALUE);
        when(order.getQuantity()).thenReturn(ORDER_QTY);
        when(order.getOrderPaymentMethod()).thenReturn("Cash");

        Product product = mock(Product.class);
        when(product.getProductName()).thenReturn("Product Name");
        when(product.getProductDescription()).thenReturn("Product Description");

        Images image = mock(Images.class);

        List<Images> imagesList = new ArrayList<>();
        imagesList.add(image);
        when(product.getImages()).thenReturn(imagesList);

        when(order.getProduct()).thenReturn(product);

        Farms farms = mock(Farms.class);
        when(farms.getName()).thenReturn("Farm Name");

        when(order.getFarm()).thenReturn(farms);

        OrderDto orderDto = ResponseUtils.convertOrderResponse(order);

        assertEquals(order.getId(), orderDto.getId());
    }

}
