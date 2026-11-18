package com.ermapsh.notifier.app.service.impl;

import com.ermapsh.notifier.app.dto.req.CreateReminderRequest;
import com.ermapsh.notifier.app.dto.res.ReminderResponse;
import com.ermapsh.notifier.app.service.ReminderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReminderServiceImpl  implements ReminderService {

    @Override
    public ReminderResponse create(UUID userId, CreateReminderRequest request) {
        log.info("userid: ${} type: ${}", userId, request.scheduleType());
        return null;
    }

}
