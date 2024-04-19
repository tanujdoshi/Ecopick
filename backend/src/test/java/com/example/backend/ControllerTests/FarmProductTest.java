package com.example.backend.ControllerTests;

import com.example.backend.controller.FarmerController;
import com.example.backend.controller.ProductController;
import com.example.backend.controller.WalletController;
import com.example.backend.dto.request.AddFarmRequest;
import com.example.backend.dto.request.AddProductRequest;
import com.example.backend.dto.request.EditFarmRequest;
import com.example.backend.dto.request.WalletRequest;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.FarmDto;
import com.example.backend.dto.response.GetFarmByIdResponse;
import com.example.backend.dto.response.ProductDto;
import com.example.backend.entities.Wallet;
import com.example.backend.services.FarmerService;
import com.example.backend.services.ProductService;
import com.example.backend.services.WalletService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FarmProductTest {

    @InjectMocks
    private FarmerController farmerController;

    @Mock
    private FarmerService farmerService;

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @InjectMocks
    private WalletController walletController;

    @Value("${frontend.endpoint}")
    private String frontendEndpoint;

    private static double WALLET_AMOUNT = 100.0;
    @Mock
    private WalletService walletService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddFarm() {
        AddFarmRequest farmRequest = new AddFarmRequest();
        MultipartFile[] files = new MockMultipartFile[1];
        Principal principal = mock(Principal.class);

        List<FarmDto> expectedFarmList = new ArrayList<>();
        when(farmerService.addFarm(farmRequest, files,
                principal)).thenReturn(expectedFarmList);

        ResponseEntity<Map> responseEntity = farmerController.addFarm(farmRequest,
                files, principal);

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testEditFarm() {
        // Mocking parameters
        EditFarmRequest farmRequest = new EditFarmRequest();
        MultipartFile[] files = new MockMultipartFile[1];
        Principal principal = mock(Principal.class);

        // Mocking farmerService.editFarm() method
        String expectedEditFarmResponse = "Success"; // Assuming editFarmResponse
        when(farmerService.editFarm(farmRequest, files,
                principal)).thenReturn(expectedEditFarmResponse);

        // Call the method under test
        ResponseEntity<Map> responseEntity = farmerController.editFarm(farmRequest,
                files, principal);

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testDeleteFarm() {
        int farmId = 1;
        doNothing().when(farmerService).deleteFarm(farmId);

        // Act
        ResponseEntity<ApiResponse> responseEntity = farmerController.deleteFarm(farmId);

        // Assert
        String actualMessage = responseEntity.getBody().getMessage();
        assertEquals("Farm deleted successfully", actualMessage);
    }

    @Test
    public void getFarmById_Success() {
        // Mocked data
        int farmId = 1;
        GetFarmByIdResponse expectedResponse = new GetFarmByIdResponse();

        when(farmerService.getFarmById(farmId)).thenReturn(expectedResponse);

        ResponseEntity<GetFarmByIdResponse> response = farmerController.getFarmById(farmId);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testAddProduct() {
        AddProductRequest productRequest = new AddProductRequest();
        MultipartFile[] files = new MultipartFile[1]; // mock files if necessary
        Principal principal = mock(Principal.class);
        List<ProductDto> allFarmProducts = new ArrayList<>(); // create sample

        // Assuming productService returns products without any issues
        when(productService.addProduct(productRequest, files,
                principal)).thenReturn(allFarmProducts);

        // Act
        ResponseEntity<Map> responseEntity = productController.addProduct(productRequest, files, principal);

        // Assert
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testDeleteProduct() {
        // Assuming productService deletes the product without any issues
        doNothing().when(productService).deleteProduct(1);

        // Act
        ResponseEntity<ApiResponse> responseEntity = productController.deleteProduct(1);

        // Assert
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testTopUp() {
        WalletRequest request = new WalletRequest();
        request.setEmail("test@example.com");
        request.setAmount(WALLET_AMOUNT);

        Mockito.doNothing().when(walletService).addMoney(request.getEmail(),
                request.getAmount());

        // Call the method under test
        RedirectView result = walletController.topUp(request);

        // Assertions
        assertEquals(frontendEndpoint + "/wallet", result.getUrl());
    }

    @Test
    public void testOrderHistory() {
        Principal principal = () -> "user@example.com";
        List<Wallet> walletHistory = new ArrayList<>();
        walletHistory.add(new Wallet());

        when(walletService.gethistory(principal)).thenReturn(walletHistory);

        ResponseEntity<List<Wallet>> response = walletController.walletHistory(principal);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    void createPaymentIntent_Success() {
        // Mocked data
        String amount = "100";
        Principal principal = () -> "user@example.com";
        String sessionId = "session_id";

        when(walletService.createPaymentIntent(amount,
                principal)).thenReturn(sessionId);

        // Call the method under test
        ResponseEntity<Map> response = walletController.createPaymentIntent(amount,
                principal);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
