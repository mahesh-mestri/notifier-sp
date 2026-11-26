package com.ermapsh.notifier.app.service.impl;

import com.ermapsh.notifier.app.entity.FcmToken;
import com.ermapsh.notifier.app.entity.Reminder;
import com.ermapsh.notifier.app.repository.FcmTokenRepository;
import com.ermapsh.notifier.app.repository.ReminderRepository;
import com.ermapsh.notifier.app.service.FirebaseNotificationService;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MessagingErrorCode;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseNotificationServiceImpl implements FirebaseNotificationService {

    private static final String DEFAULT_CHANNEL_ID = "reminders_default";

    private final FcmTokenRepository fcmTokenRepository;
    private final ReminderRepository reminderRepository;

    @Override
    @Transactional
    public void sendNotification(Map<String, Object> payload)
            throws FirebaseMessagingException {

        Object rawReminderId = payload.get("reminderId");
        if (rawReminderId == null) {
            throw new IllegalArgumentException(
                    "Missing 'reminderId' in notification payload"
            );
        }

        UUID reminderId = rawReminderId instanceof UUID uuid
                ? uuid
                : UUID.fromString(rawReminderId.toString());

        Reminder reminder = reminderRepository.findById(reminderId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Reminder not found: " + reminderId
                ));

        if (reminder.getUser() == null || reminder.getUser().getId() == null) {
            throw new IllegalStateException(
                    "Reminder user is missing for reminder: " + reminderId
            );
        }

        UUID userId = reminder.getUser().getId();

        String title = payload.containsKey("title")
                ? Objects.toString(payload.get("title"), "")
                : Objects.toString(reminder.getTitle(), "");

        String body = payload.containsKey("body")
                ? Objects.toString(payload.get("body"), "")
                : Objects.toString(reminder.getDescription(), title);

        List<FcmToken> tokens = fcmTokenRepository.findAllByUser_Id(userId);
        List<String> deviceTokens = tokens.stream()
                .map(FcmToken::getFcmToken)
                .filter(token -> token != null && !token.isBlank())
                .distinct()
                .toList();

        if (deviceTokens.isEmpty()) {
            throw new IllegalStateException(
                    "No valid FCM tokens registered for user: " + userId
            );
        }

        Map<String, String> data = Map.of(
                "occurrenceId", Objects.toString(payload.get("occurrenceId"), ""),
                "scheduledAt", Objects.toString(payload.get("scheduledAt"), ""),
                "reminderId", reminderId.toString(),
                "status", Objects.toString(payload.get("status"), "")
        );

        MulticastMessage message = MulticastMessage.builder()
                .addAllTokens(deviceTokens)
                .setNotification(
                        Notification.builder()
                                .setTitle(title)
                                .setBody(body)
                                .build()
                )
                .putAllData(data)
                .setAndroidConfig(
                        AndroidConfig.builder()
                                .setPriority(AndroidConfig.Priority.HIGH)
                                .setNotification(
                                        AndroidNotification.builder()
                                                .setChannelId(DEFAULT_CHANNEL_ID)
                                                .build()
                                )
                                .build()
                )
                .build();

        BatchResponse response = FirebaseMessaging.getInstance()
                .sendEachForMulticast(message);

        log.info(
                "FCM result for user {}: success={}, failure={}",
                userId,
                response.getSuccessCount(),
                response.getFailureCount()
        );

        for (int i = 0; i < response.getResponses().size(); i++) {
            var sendResponse = response.getResponses().get(i);

            if (!sendResponse.isSuccessful()) {
                FirebaseMessagingException exception = sendResponse.getException();
                MessagingErrorCode errorCode = exception != null
                        ? exception.getMessagingErrorCode()
                        : null;

                log.error(
                        "FCM failed for target at index {}, code={}, error: {}",
                        i,
                        errorCode,
                        exception != null ? exception.getMessage() : "unknown"
                );

                if (errorCode == MessagingErrorCode.UNREGISTERED
                        || errorCode == MessagingErrorCode.INVALID_ARGUMENT) {
                    fcmTokenRepository.deleteByFcmToken(deviceTokens.get(i));
                }
            }
        }

        if (response.getSuccessCount() == 0 && response.getFailureCount() > 0) {
            throw new IllegalStateException(
                    "FCM failed for all " + response.getFailureCount() + " device(s)"
            );
        }
    }
}