package com.ermapsh.notifier.app.service;

import com.google.firebase.messaging.FirebaseMessagingException;

import java.util.Map;

public interface FirebaseNotificationService {
    void sendNotification(Map<String, Object> payload) throws FirebaseMessagingException;
}
