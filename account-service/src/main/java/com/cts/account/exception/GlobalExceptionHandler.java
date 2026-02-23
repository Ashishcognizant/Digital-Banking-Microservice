package com.cts.account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleAccountNotFound(AccountNotFoundException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", OffsetDateTime.now().toString());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());
        // Optionally include a path if you have access to HttpServletRequest
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    // Optionally handle other exceptions:
    // @ExceptionHandler(Exception.class)
    // public ResponseEntity<Map<String, Object>> handleOther(Exception ex) { ... }
}
