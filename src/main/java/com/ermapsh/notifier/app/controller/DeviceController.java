
package com.ermapsh.notifier.app.controller;

import com.ermapsh.notifier.app.dto.req.FcmTokenRegisterReq;
import com.ermapsh.notifier.app.dto.res.FcmTokenRegisterResp;
import com.ermapsh.notifier.app.service.FcmTokenService;
import com.ermapsh.notifier.common.dto.res.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final FcmTokenService fcmTokenService;

    /**
     * Register or update device FCM token.
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<FcmTokenRegisterResp>> register(
            @Valid @RequestBody FcmTokenRegisterReq request
    ) {
        return ApiResponse.created(
                "Device registered successfully",
                fcmTokenService.register(request)
        );
    }

    /**
     * Unregister a device.
     */
    @DeleteMapping("/{deviceId}")
    public ResponseEntity<ApiResponse<Void>> unregister(
            @PathVariable UUID deviceId
    ) {
        fcmTokenService.unregister(deviceId);

        return ApiResponse.accepted(
                "Device unregistered successfully",
                null
        );
    }
}
