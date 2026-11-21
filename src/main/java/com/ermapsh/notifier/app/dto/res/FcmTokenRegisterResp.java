package com.ermapsh.notifier.app.dto.res;

import java.util.UUID;

public record FcmTokenRegisterResp(
        UUID deviceId
) {
}
