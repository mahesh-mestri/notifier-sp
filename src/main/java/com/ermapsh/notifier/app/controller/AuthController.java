package com.ermapsh.notifier.app.controller;


import com.ermapsh.notifier.app.dto.req.AppUserLogInRequest;
import com.ermapsh.notifier.app.dto.req.AppUserSignupRequest;
import com.ermapsh.notifier.app.dto.req.RefreshTokenRequest;
import com.ermapsh.notifier.app.dto.res.AppUserLogInResponse;
import com.ermapsh.notifier.app.dto.res.AppUserSignupResponse;
import com.ermapsh.notifier.app.dto.res.RefreshTokenResponse;
import com.ermapsh.notifier.common.dto.res.ApiResponse;
import com.ermapsh.notifier.app.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("signup")
    public ResponseEntity<ApiResponse<AppUserSignupResponse>> signup(@RequestBody @Valid AppUserSignupRequest request) {
        AppUserSignupResponse response = authService.signup(request);
        return ApiResponse.created(
                "User created successfully",
                response
        );
    }

    @PostMapping("login")
    public ResponseEntity<ApiResponse<AppUserLogInResponse>> login(@RequestBody @Valid AppUserLogInRequest request) {
        AppUserLogInResponse response = authService.login(request);
        return ApiResponse.created(
                "App user fetch successfully",
                response
        );
    }

    @PostMapping("refresh-token")
    public ResponseEntity<ApiResponse<RefreshTokenResponse>> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        RefreshTokenResponse response = authService.refreshToken(request);
        return ApiResponse.created(
                "App user fetch successfully",
                response
        );
    }

}
