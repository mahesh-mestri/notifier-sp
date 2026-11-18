package com.ermapsh.notifier.app.controller;

import com.ermapsh.notifier.app.dto.req.CreateReminderRequest;
import com.ermapsh.notifier.app.dto.res.ReminderResponse;
import com.ermapsh.notifier.app.security.UserContext;
import com.ermapsh.notifier.app.service.ReminderService;
import com.ermapsh.notifier.common.dto.res.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reminder")
@RequiredArgsConstructor
public class ReminderController {

    private final ReminderService reminderService;
    private final UserContext userContext;

    @PostMapping("")
    public ResponseEntity<ApiResponse<ReminderResponse>> createReminder(
            @Valid @RequestBody CreateReminderRequest request) {
        ReminderResponse response = reminderService.create(
                userContext.getUserId(),
                request
        );

        return ApiResponse.created(
                "Reminder created successfully",
                response
        );
    }
}
