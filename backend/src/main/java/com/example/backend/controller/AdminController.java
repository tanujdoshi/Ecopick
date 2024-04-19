package com.example.backend.controller;

import com.example.backend.dto.response.AdminResponse;
import com.example.backend.dto.response.ProductDto;
import com.example.backend.dto.response.SalesDTO;
import com.example.backend.entities.Order;
import com.example.backend.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/Admin")
public class AdminController {

    private final AdminService adminService;

    /**
     * End point to fetch all information for the admin
     * @param principal user token
     * @return ResponseEntity with the admin response
     */
    @GetMapping("info-page")
    public ResponseEntity<AdminResponse> getallinformation(Principal principal) {
        AdminResponse response = adminService.getAllInfo(principal);
        return ResponseEntity.ok(response);
    }
//    @GetMapping("ordersbymonth")
//    public ResponseEntity<List<SalesDTO>> getOrderByMonth(Principal principal){
//        List<SalesDTO> ordersBetweenTimeFrame = adminService.getOrderByMonth(principal);
//        return ResponseEntity.ok(ordersBetweenTimeFrame);
//    }
}
