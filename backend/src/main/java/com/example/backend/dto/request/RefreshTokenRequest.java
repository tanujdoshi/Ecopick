package com.example.backend.dto.request;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequest {
    private String token;
}
