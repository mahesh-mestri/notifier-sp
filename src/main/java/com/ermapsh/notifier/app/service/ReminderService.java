package com.ermapsh.notifier.app.service;

import com.ermapsh.notifier.app.dto.req.CreateReminderRequest;
import com.ermapsh.notifier.app.dto.res.PageResponse;
import com.ermapsh.notifier.app.dto.res.ReminderResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ReminderService {
    ReminderResponse create(UUID userId, CreateReminderRequest request);

    PageResponse<ReminderResponse> getAllByUserId(UUID userId, Pageable pageable);
}
