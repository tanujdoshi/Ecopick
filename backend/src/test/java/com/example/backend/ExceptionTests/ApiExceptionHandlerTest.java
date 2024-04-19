package com.example.backend.ExceptionTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.example.backend.entities.UserMeta;
import com.example.backend.exception.ApiException;
import com.example.backend.exception.ApiExceptionHandler;
import com.example.backend.exception.ApiRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ApiExceptionHandlerTest {

    @InjectMocks
    private ApiExceptionHandler exceptionHandler;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testHandleRequestExceptionWithoutThrowable() {
        ApiRequestException exception = new ApiRequestException("Test Exception Message");

        ResponseEntity<Object> responseEntity = exceptionHandler.handleRequestionException(exception);

        ApiException apiException = (ApiException) responseEntity.getBody();

        assertEquals("Test Exception Message", apiException.getMessage());

    }

    @Test
    void testHandleRequestExceptionWithThrowable() {
        ApiRequestException exception = new ApiRequestException("Test Exception Message", new Throwable());
        ResponseEntity<Object> responseEntity = exceptionHandler.handleRequestionException(exception);

        ZonedDateTime dateTime = ZonedDateTime.now();

        ApiException apiException = (ApiException) responseEntity.getBody();
        assertEquals("Test Exception Message", apiException.getMessage());

    }
}
