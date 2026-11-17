package com.ermapsh.notifier.app.service;

import com.ermapsh.notifier.app.dto.req.AppUserLogInRequest;
import com.ermapsh.notifier.app.dto.req.AppUserSignupRequest;
import com.ermapsh.notifier.app.dto.req.RefreshTokenRequest;
import com.ermapsh.notifier.app.dto.res.AppUserLogInResponse;
import com.ermapsh.notifier.app.dto.res.AppUserSignupResponse;
import com.ermapsh.notifier.app.dto.res.RefreshTokenResponse;
import jakarta.validation.Valid;

public interface AuthService {
    AppUserSignupResponse signup(AppUserSignupRequest request);
    AppUserLogInResponse login(AppUserLogInRequest request);
    RefreshTokenResponse refreshToken(RefreshTokenRequest request);
}
