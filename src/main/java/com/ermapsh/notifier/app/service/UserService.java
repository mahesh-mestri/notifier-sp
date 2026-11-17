package com.ermapsh.notifier.app.service;

import com.ermapsh.notifier.app.dto.res.UserProfileResponse;

import java.util.UUID;

public interface UserService {
    UserProfileResponse getProfile(UUID userId);
}
