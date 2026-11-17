package com.ermapsh.notifier.app.controller;

import com.ermapsh.notifier.common.dto.res.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/")
    public ResponseEntity<ApiResponse<Void>> health() {
        return ApiResponse.ok("Live", null);
    }
}
