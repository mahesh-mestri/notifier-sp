
package com.ermapsh.notifier.app.service.impl;

import com.ermapsh.notifier.app.dto.req.AppUserLogInRequest;
import com.ermapsh.notifier.app.dto.req.AppUserSignupRequest;
import com.ermapsh.notifier.app.dto.req.RefreshTokenRequest;
import com.ermapsh.notifier.app.dto.res.AppUserLogInResponse;
import com.ermapsh.notifier.app.dto.res.AppUserSignupResponse;
import com.ermapsh.notifier.app.dto.res.RefreshTokenResponse;
import com.ermapsh.notifier.app.entity.RefreshToken;
import com.ermapsh.notifier.app.entity.User;
import com.ermapsh.notifier.app.exception.DuplicateResourceException;
import com.ermapsh.notifier.app.exception.ResourceNotFoundException;
import com.ermapsh.notifier.app.repository.RefreshTokenRepository;
import com.ermapsh.notifier.app.repository.UserRepository;
import com.ermapsh.notifier.app.security.JwtUtil;
import com.ermapsh.notifier.app.security.RefreshTokenUtil;
import com.ermapsh.notifier.app.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private static final long REFRESH_TOKEN_EXPIRATION =
            7L * 24 * 60 * 60;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenUtil refreshTokenUtil;


    // SIGNUP

    @Override
    @Transactional
    public AppUserSignupResponse signup(AppUserSignupRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException(
                    "Email already exists: " + request.email()
            );
        }

        User user = User.builder()
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .build();

        user = userRepository.save(user);

        String accessToken = jwtUtil.generateAccessToken(
                user.getEmail(),
                user.getId()
        );

        String refreshToken = refreshTokenUtil.generateRefreshToken();

        saveRefreshToken(user, refreshToken);

        return new AppUserSignupResponse(
                user.getId(),
                user.getEmail(),
                accessToken,
                refreshToken,
                "Bearer"
        );
    }


    // LOGIN

    @Override
    @Transactional
    public AppUserLogInResponse login(AppUserLogInRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        String accessToken = jwtUtil.generateAccessToken(
                user.getEmail(),
                user.getId()
        );

        String refreshToken = refreshTokenUtil.generateRefreshToken();

        saveRefreshToken(user, refreshToken);

        return new AppUserLogInResponse(
                user.getId(),
                user.getEmail(),
                accessToken,
                refreshToken,
                "Bearer"
        );
    }


    // REFRESH TOKEN

    @Override
    @Transactional
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {

        String tokenHash = refreshTokenUtil.hashToken(
                request.refreshToken()
        );

        RefreshToken storedToken = refreshTokenRepository
                .findByTokenHashForUpdate(tokenHash)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Invalid refresh token")
                );

        if (storedToken.isRevoked()
                || !storedToken.getExpiresAt().isAfter(Instant.now())) {

            throw new ResourceNotFoundException("Invalid refresh token");
        }

        User user = storedToken.getUser();

        String accessToken = jwtUtil.generateAccessToken(
                user.getEmail(),
                user.getId()
        );

        String newRefreshToken = refreshTokenUtil.generateRefreshToken();

        // Revoke old refresh token
        storedToken.setRevoked(true);
        refreshTokenRepository.save(storedToken);

        // Save replacement refresh token
        saveRefreshToken(user, newRefreshToken);

        return new RefreshTokenResponse(
                accessToken,
                newRefreshToken,
                "Bearer"
        );
    }


    // SAVE REFRESH TOKEN
    private void saveRefreshToken(User user, String refreshToken) {

        Instant now = Instant.now();

        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .user(user)
                .tokenHash(refreshTokenUtil.hashToken(refreshToken))
                .expiresAt(now.plusSeconds(REFRESH_TOKEN_EXPIRATION))
                .revoked(false)
                .createdAt(now)
                .build();

        refreshTokenRepository.save(refreshTokenEntity);
    }
}
