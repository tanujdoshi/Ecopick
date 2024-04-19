package com.example.backend.services;

import com.example.backend.dto.response.AdminResponse;
import com.example.backend.dto.response.SalesDTO;
import com.example.backend.entities.Order;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface AdminService {
    AdminResponse getAllInfo(Principal principal);
}
