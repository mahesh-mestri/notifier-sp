package com.ermapsh.notifier.app.dto.res;

import com.ermapsh.notifier.app.enums.ReminderCategory;
import com.ermapsh.notifier.app.enums.ReminderScheduleType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ReminderResponse(
        UUID id,
        String title,
        String description,
        ReminderCategory categoryId,
        ReminderScheduleType scheduleType,
        boolean notificationEnabled,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
