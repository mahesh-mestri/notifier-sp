package com.ermapsh.notifier.app.service.impl;

import com.ermapsh.notifier.app.entity.Reminder;
import com.ermapsh.notifier.app.entity.ReminderOccurrence;
import com.ermapsh.notifier.app.entity.Schedule;
import com.ermapsh.notifier.app.enums.OccurrenceStatus;
import com.ermapsh.notifier.app.repository.ReminderOccurrenceRepository;
import com.ermapsh.notifier.app.repository.ReminderRepository;
import com.ermapsh.notifier.app.scheduler.ScheduleAdapter;
import com.ermapsh.notifier.app.scheduler.ScheduleAdapterRouter;
import com.ermapsh.notifier.app.service.ReminderOccurrenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class ReminderOccurrenceImpl implements ReminderOccurrenceService {
    private final ReminderRepository reminderRepository;
    private final ReminderOccurrenceRepository reminderOccurrenceRepository;
    private final ScheduleAdapterRouter scheduleAdapterRouter;


    @Override
    @Transactional
    public void generateUpcomingOccurrences() {
        // 1. Fetch active reminders that occurrence not present in reminder_occurrence

        List<Reminder> reminders = reminderRepository.findActiveRemindersWithoutOccurrences();
        log.info("number of count of reminders: {}", reminders.size());

        // 2. Loop through reminders
        for (Reminder reminder : reminders) {
            log.info(
                    "Processing reminder: id={}, title={}, type={}",
                    reminder.getId(),
                    reminder.getTitle(),
                    reminder.getScheduleType()
            );

            // 3. Get adapter using reminder.getScheduleType
            ScheduleAdapter adapter = scheduleAdapterRouter.getAdapter(reminder.getScheduleType());

            // 4. Find schedule configuration
            Schedule schedule = adapter.findByReminderId(reminder);


            List<ReminderOccurrence> reminderOccurrences = adapter.arrangeReminderOccurrence(
                    reminder,
                    schedule
            );

            log.info("reminderOccurrence is {}", reminderOccurrences.size());

            if (reminderOccurrences.isEmpty()) {
                continue;
            }

            List<ReminderOccurrence> savedOccurrences =
                    reminderOccurrenceRepository.saveAll(reminderOccurrences);

            log.info(
                    "Successfully saved {} occurrences for reminder {}",
                    savedOccurrences.size(),
                    reminder.getId()
            );
        }

    }

    @Override
    @Transactional(readOnly = true)
    public List<ReminderOccurrence> findOccurrencesDueWithinNextMinute() {

        Instant cutoff = Instant.now().plus(1, ChronoUnit.MINUTES);

        return reminderOccurrenceRepository
                .fetchBeforeMinuteReminderOccurrence(
                        OccurrenceStatus.PENDING,
                        cutoff
                );
    }
}
