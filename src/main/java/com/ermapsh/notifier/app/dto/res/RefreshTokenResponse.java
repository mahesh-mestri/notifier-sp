package com.ermapsh.notifier.app.dto.res;

public record RefreshTokenResponse(
        String accessToken,
        String refreshToken,
        String tokenType
) {
}
