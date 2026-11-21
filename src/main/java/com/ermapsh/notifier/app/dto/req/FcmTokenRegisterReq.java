
package com.ermapsh.notifier.app.dto.req;

import com.ermapsh.notifier.app.enums.Platform;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FcmTokenRegisterReq(

        @NotBlank(message = "FCM token is required")
        @Size(max = 512, message = "FCM token cannot exceed 512 characters")
        String fcmToken,

        @NotNull(message = "Device platform is required")
        Platform platform
) {
}
