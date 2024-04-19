package com.example.backend.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WalletRequest {
    private String email;
    private Double amount;
}
