package com.example.backend.Services;

import com.example.backend.dto.response.HomeResponse;
import com.example.backend.entities.Farms;
import com.example.backend.entities.Images;
import com.example.backend.entities.Product;
import com.example.backend.repository.FarmRepository;
import com.example.backend.repository.ProductRepository;
import com.example.backend.services.impl.HomeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HomeServiceImplTest {

    static final int EXPECTED_LIST_RESULT = 8;
    @Mock
    private FarmRepository farmRepositoryMock;

    @Mock
    private ProductRepository productRepositoryMock;

    @InjectMocks
    private HomeServiceImpl homeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetHomeMeta() {
        List<Farms> mockedFarms = new ArrayList<>();

        Images image = new Images();
        image.setImg_url("image_url.jpg");
        List<Images> images = new ArrayList<>();
        images.add(image);

        for (int i = 1; i <= EXPECTED_LIST_RESULT; i++) {
            Farms farm = new Farms();
            farm.setId(i);
            farm.setImages(images);
            mockedFarms.add(farm);
        }
        when(farmRepositoryMock.findTop8ByOrderByIdDesc()).thenReturn(mockedFarms);

        List<Product> mockedProducts = new ArrayList<>();
        for (int i = 1; i <= EXPECTED_LIST_RESULT; i++) {
            Product product = new Product();
            product.setId(i);
            product.setImages(images);
            mockedProducts.add(product);
        }
        when(productRepositoryMock.findTop8ByOrderByIdDesc()).thenReturn(mockedProducts);

        // Act
        HomeResponse homeResponse = homeService.getHomeMeta();

        // Assert
        assertEquals(EXPECTED_LIST_RESULT, homeResponse.getFarms().size());

        verify(farmRepositoryMock, times(1)).findTop8ByOrderByIdDesc();
        verify(productRepositoryMock, times(1)).findTop8ByOrderByIdDesc();
    }

}
