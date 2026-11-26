package com.ermapsh.notifier.app.repository;

import com.ermapsh.notifier.app.entity.FcmToken;
import com.ermapsh.notifier.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FcmTokenRepository extends JpaRepository<FcmToken, UUID> {
    Optional<FcmToken> findByFcmToken(String fcmToken);

    Optional<FcmToken> findByIdAndUser(UUID id, User user);

    List<FcmToken> findAllByUser_Id(UUID userId);

    void deleteByFcmToken(String fcmToken);
}