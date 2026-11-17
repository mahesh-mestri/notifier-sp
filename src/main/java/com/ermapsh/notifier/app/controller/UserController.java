package com.ermapsh.notifier.app.controller;

import com.ermapsh.notifier.app.dto.res.UserProfileResponse;
import com.ermapsh.notifier.app.security.UserContext;
import com.ermapsh.notifier.app.service.UserService;
import com.ermapsh.notifier.common.dto.res.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserContext userContext;
    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getProfile() {
        UserProfileResponse response = userService.getProfile(userContext.getUserId());
        return ApiResponse.created(
                "User fetch successfully",
                response
        );
    }

//    @PutMapping("/")
//    public ResponseEntity<UserResponse> updateProfile(
//            @Valid @RequestBody UpdateUserRequest request) {
//        return ResponseEntity.ok(
//                userService.updateProfile(request)
//        );
//    }
}
