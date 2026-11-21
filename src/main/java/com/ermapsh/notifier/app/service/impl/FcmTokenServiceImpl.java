
package com.ermapsh.notifier.app.service.impl;

import com.ermapsh.notifier.app.dto.req.FcmTokenRegisterReq;
import com.ermapsh.notifier.app.dto.res.FcmTokenRegisterResp;
import com.ermapsh.notifier.app.entity.FcmToken;
import com.ermapsh.notifier.app.entity.User;
import com.ermapsh.notifier.app.exception.ResourceNotFoundException;
import com.ermapsh.notifier.app.repository.FcmTokenRepository;
import com.ermapsh.notifier.app.repository.UserRepository;
import com.ermapsh.notifier.app.security.UserContext;
import com.ermapsh.notifier.app.service.FcmTokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FcmTokenServiceImpl implements FcmTokenService {

    private final FcmTokenRepository fcmTokenRepository;
    private final UserRepository userRepository;
    private final UserContext userContext;


    @Override
    public FcmTokenRegisterResp register(FcmTokenRegisterReq request) {

        User user = getAuthenticatedUser();

        FcmToken fcmToken = fcmTokenRepository
                .findByFcmToken(request.fcmToken())
                .orElseGet(() -> FcmToken.builder()
                        .fcmToken(request.fcmToken())
                        .build());

        fcmToken.setUser(user);
        fcmToken.setPlatform(request.platform());

        FcmToken savedToken = fcmTokenRepository.save(fcmToken);

        log.info(
                "FCM device registered successfully for user: {}",
                user.getId()
        );

        return new FcmTokenRegisterResp(
                savedToken.getId()
        );
    }

    @Override
    public void unregister(UUID deviceId) {

        User user = getAuthenticatedUser();

        FcmToken fcmToken = fcmTokenRepository
                .findByIdAndUser(deviceId, user)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Device registration not found"
                ));

        fcmTokenRepository.delete(fcmToken);

        log.info(
                "FCM device unregistered successfully: {}",
                deviceId
        );
    }

    private User getAuthenticatedUser() {
        return userRepository.findById(userContext.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );
    }
}
