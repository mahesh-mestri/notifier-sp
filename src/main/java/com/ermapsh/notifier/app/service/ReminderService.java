package com.ermapsh.notifier.app.service;

import com.ermapsh.notifier.app.dto.req.CreateReminderRequest;
import com.ermapsh.notifier.app.dto.res.ReminderResponse;

import java.util.UUID;

public interface ReminderService {
    ReminderResponse create(UUID userId, CreateReminderRequest request);
}
