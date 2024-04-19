package com.example.backend.ExceptionTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Map;

import com.example.backend.exception.ApplicationExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

class ApplicationExceptionHandlerTest {

    @Mock
    private BindingResult bindingResult;

    static final int EXPECTED_MAP_SIZE = 2;

    @InjectMocks
    private ApplicationExceptionHandler exceptionHandler;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testHandleInvalidArgument() {
        // Create a mock MethodArgumentNotValidException
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        // Mock the behavior of bindingResult.getFieldErrors()
        FieldError fieldError1 = new FieldError("objectName", "field1", "Error message 1");
        FieldError fieldError2 = new FieldError("objectName", "field2", "Error message 2");

        when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(fieldError1, fieldError2));

        // Invoke the method under test
        Map<String, String> errorMap = exceptionHandler.handleInvalidArgument(ex);

        // Verify the response
        assertEquals(EXPECTED_MAP_SIZE, errorMap.size());
    }
}
