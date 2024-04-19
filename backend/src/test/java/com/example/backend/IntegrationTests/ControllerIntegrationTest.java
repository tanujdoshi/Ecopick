package com.example.backend.IntegrationTests;

import com.example.backend.dto.request.ResetPasswordRequest;
import com.example.backend.dto.request.SignInRequest;
import com.example.backend.dto.response.LoginResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerIntegrationTest {
    private static String token;
    private static final String ADMIN_EMAIL = "admin123@gmail.com";
    static final double LONGITUDE = 789.012;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() throws Exception {
        token = getToken();
        // createLoginUser();
    }

    public String getToken() throws Exception {
        SignInRequest request = new SignInRequest();
        request.setEmail(ADMIN_EMAIL);
        request.setPassword("Test@123");

        String credentialsDtoJson = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(credentialsDtoJson))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        LoginResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), LoginResponse.class);
        System.out.println("RESSSS???" + response.getToken());
        return response.getToken();

    }

    @Test
    public void testGetStatisticsAdmin() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/Admin/info-page")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertNotNull(responseBody);
    }

    @Test
    public void testGetCategory() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/category/list")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertNotNull(responseBody);
    }

    @Test
    public void testCustomerListFarm() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/customer/listfarms")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .param("farmName", ""))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertNotNull(responseBody);
    }

    @Test
    public void testHomeMeta() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/home/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertNotNull(responseBody);
    }

    @Test
    public void testOrderHistory() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/order/orderHistory")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertNotNull(responseBody);
    }

    @Test
    public void testGetOwnSubscription() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/subscribe/my-subscription")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertNotNull(responseBody);
    }

    @Test
    public void getMySubscribedProducts() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/subscribe/my-subscribed-products")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertNotNull(responseBody);
    }

    @Test
    public void getOwnFarms() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/farmer/own-farms")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertNotNull(responseBody);
    }

}
