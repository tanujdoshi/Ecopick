package com.example.backend.ControllerTests;

import com.example.backend.controller.AdminController;
import com.example.backend.dto.response.AdminResponse;
import com.example.backend.services.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.security.Principal;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class AdminControllerTest {
    @Mock
    private AdminService adminService;
    @InjectMocks
    private AdminController adminController;
    @Mock
    Principal principal;
    @Mock
    AdminResponse mockResponse;
    @Mock
    ResponseEntity responseEntity;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        responseEntity = adminController.getallinformation(principal);
        when(adminService.getAllInfo(principal)).thenReturn(mockResponse);

    }

    @Test
    public void testGetAllInformation() {
        verify(adminService, times(1)).getAllInfo(principal);
        assertNotNull(responseEntity);

    }

    @Test
    public void testGetAllInformationResponseBody() {
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

}
