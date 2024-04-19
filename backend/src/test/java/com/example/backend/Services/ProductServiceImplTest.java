package com.example.backend.Services;

import com.example.backend.dto.request.AddProductRequest;
import com.example.backend.dto.request.EditProductRequest;
import com.example.backend.dto.response.ProductDto;
import com.example.backend.entities.*;
import com.example.backend.exception.ApiRequestException;
import com.example.backend.repository.*;
import com.example.backend.services.impl.ProductServiceImpl;
import com.example.backend.utils.Awsutils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.security.Principal;
import java.util.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private FarmRepository farmRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ImagesRepository imagesRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private Awsutils awsutils;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private AddProductRequest addProductRequest;
    @InjectMocks
    ProductServiceImpl productService;
    @InjectMocks
    Farms farm;
    @InjectMocks
    Category category;
    @InjectMocks
    Images images;

    @Mock
    Product product;

    @InjectMocks
    ProductDto productDto;

    @InjectMocks
    User user;

    static final int id = 1;
    static final int PRODUCT_ID = 2;
    static final double PRODUCT_PRICE = 99.99;
    static final int PRODUCT_STOCK = 100;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
        addProductRequest.setFarm_id(id);
        farm.setId(addProductRequest.getFarm_id());
        addProductRequest.setCategory_id(id);
        category.setId(addProductRequest.getCategory_id());
        product = new Product();
        when(modelMapper.map(addProductRequest, Product.class)).thenReturn(product);
        Product savedProduct = new Product();
        savedProduct.setId(1);
        when(productRepository.save(any())).thenReturn(savedProduct);
        when(farmRepository.findById(anyInt())).thenReturn(farm);
        when(categoryRepository.findById(anyInt())).thenReturn(category);
        when(awsutils.uploadFileToS3(any(MultipartFile.class), anyString(), anyInt())).thenReturn("test-url");
        product.setId(id);
        ArrayList<Images> image = new ArrayList<>();
        image.add(images);
        imagesRepository.save(images);
        product.setImages(image);
    }

    @Test
    void testAddProduct() {
        Principal principal = () -> "test@example.com";
        MockMultipartFile[] mockFiles = { new MockMultipartFile("file1", new byte[0]),
                new MockMultipartFile("file2", new byte[0]) };
        List<ProductDto> productAdded = productService.addProduct(addProductRequest, mockFiles, principal);
        assertTrue(productAdded instanceof List<ProductDto>);
    }

    @Test
    void testDeleteProduct() {

        when(productRepository.findById(id)).thenReturn(product);
        productService.deleteProduct(id);
        verify(productRepository, times(id)).deleteById(id);

    }

    @Test
    void testDeleteProductWhenNullTest() {
        when(productRepository.findById(anyInt())).thenReturn(null);
        assertThrows(ApiRequestException.class, () -> productService.deleteProduct(anyInt()));
    }

    @Test
    void testEditProduct() {

        EditProductRequest mockEditProductRequest = new EditProductRequest();
        mockEditProductRequest.setId(PRODUCT_ID); // Set valid ID for testing
        mockEditProductRequest.setProductName("Updated Product Name");
        mockEditProductRequest.setProductDescription("Updated Product Description");
        mockEditProductRequest.setPrice(PRODUCT_PRICE);
        mockEditProductRequest.setStock(PRODUCT_STOCK);
        mockEditProductRequest.setUnit("kg");

        MultipartFile[] mockFiles = {
                new MockMultipartFile("file1", "test.txt", "text/plain", "test file".getBytes()) };
        Principal mockPrincipal = () -> "user1";

        when(productRepository.findById(anyInt())).thenReturn(product);
        when(awsutils.uploadFileToS3(any(MultipartFile.class), anyString(), anyInt()))
                .thenReturn("https://test-URL/test/file1");

        Product updatedProduct = productService.editProduct(mockEditProductRequest, mockFiles, mockPrincipal);

        assertNotNull(updatedProduct);
    }

    @Test
    void testEditProductWhenNull() {
        MultipartFile[] mockFiles = {
                new MockMultipartFile("file1", "test.txt", "text/plain", "test file".getBytes()) };
        Principal mockPrincipal = () -> "user1";
        EditProductRequest mockEditProductRequest = new EditProductRequest();
        when(productRepository.findById(anyInt())).thenReturn(null);
        assertThrows(ApiRequestException.class,
                () -> productService.editProduct(mockEditProductRequest, mockFiles, mockPrincipal));
    }

    @Test
    void testGetProductById() {
        when(productRepository.findById(id)).thenReturn(product);
        Object result = productService.getProductById(id);
        assertEquals(productService.getProductById(id).getId(), productDto.getId());
    }

    @Test
    void testGetProductByIdWhenNull() {
        when(productRepository.findById(anyInt())).thenReturn(null);
        assertThrows(ApiRequestException.class, () -> productService.getProductById(id));
    }

    @Test
    void testGetFarmerProducts() {
        Principal mockPrincipal = () -> "testUser@ecopick.com";
        user.setEmail("testUser@ecopick.com");
        farm.setId(id);
        product.setId(id);
        List<Farms> mockFarmArr = new ArrayList<>();
        mockFarmArr.add(farm);
        List<Product> mockProductsArr = new ArrayList<>();
        mockProductsArr.add(product);
        when(userRepository.findByEmail(anyString())).thenReturn(user);
        when(farmRepository.findByUser(any(User.class))).thenReturn(mockFarmArr);
        when(productRepository.findByFarm(any(Farms.class))).thenReturn(mockProductsArr);
        List<ProductDto> testArr = productService.getFarmerProducts(mockPrincipal, null);
        assertNotNull(testArr);

        verify(userRepository, times(1)).findByEmail(anyString());
        verify(farmRepository, times(1)).findByUser(any(User.class));
        verify(productRepository, times(1)).findByFarm(any(Farms.class));

    }

    @Test
    void testGetFarmerProductsWhenNull() {
        Principal mockPrincipal = () -> "testUser@ecopick.com";
        when(userRepository.findById(anyInt())).thenReturn(null);
        assertThrows(ApiRequestException.class, () -> productService.getFarmerProducts(mockPrincipal, null));
    }

    @Test
    void testGetAllProducts() {
        String searchTerm = "";
        product.setId(id);
        product.setProductName("TestProduct");
        List<Product> testProductArr = new ArrayList<>(Arrays.asList(product));
        when(productRepository.findByProductNameContaining(product.getProductName())).thenReturn(testProductArr);
        when(productRepository.findAll()).thenReturn(testProductArr);
        List<ProductDto> testArr = productService.getAllProducts(searchTerm);
        System.out.println(testArr.toString());
        assertNotNull(testArr);
    }

    @Test
    void testGetAllProductsWhenEmpty() {
        String searchTerm = "";
        product.setProductName("");
        List<Product> testProductArr = new ArrayList<>(Arrays.asList(product));
        when(productRepository.findAll()).thenReturn(testProductArr);
        List<ProductDto> testArr = productService.getAllProducts(searchTerm);
        assertNotNull(testArr);

    }

}
