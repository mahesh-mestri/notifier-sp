package com.ermapsh.notifier.app.service.impl;

import com.ermapsh.notifier.app.dto.req.CreateReminderRequest;
import com.ermapsh.notifier.app.dto.res.ReminderResponse;
import com.ermapsh.notifier.app.entity.Reminder;
import com.ermapsh.notifier.app.entity.Schedule;
import com.ermapsh.notifier.app.entity.User;
import com.ermapsh.notifier.app.exception.ResourceNotFoundException;
import com.ermapsh.notifier.app.repository.ReminderRepository;
import com.ermapsh.notifier.app.repository.UserRepository;
import com.ermapsh.notifier.app.scheduler.ScheduleAdapterRouter;
import com.ermapsh.notifier.app.service.ReminderService;
import com.ermapsh.notifier.common.mapper.ReminderResponseMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReminderServiceImpl implements ReminderService {

    private final ReminderRepository reminderRepository;
    private final UserRepository userRepository;
    private final ScheduleAdapterRouter router;
    private final ReminderResponseMapper reminderResponseMapper;

    @Override
    @Transactional
    public ReminderResponse create(UUID userId, CreateReminderRequest request) {
        log.info("userid: ${} type: ${}", userId, request.schedule());

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found for ID: " + userId)
                );

//        if (reminderRepository.existsByUserIdAndTitleIgnoreCaseAndActiveTrue(
//                userId,
//                request.title()
//        )) {
//            throw new IllegalArgumentException(
//                    "You already have an active reminder with this title"
//            );
//        }

        Reminder reminder = Reminder.builder().
                user(user).
                title(request.title()).
                description(request.description()).
                categoryId(request.categoryId()).
                scheduleType(request.scheduleType()).
                notificationEnabled(request.notificationEnabled()).
                build();

        reminder = reminderRepository.save(reminder);

        Schedule schedule = router.create(reminder, request.schedule());

        return reminderResponseMapper.toResponse(reminder, schedule);
    }

}
