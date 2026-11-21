package com.ermapsh.notifier.app.operations;

import com.ermapsh.notifier.app.repository.OnceScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReminderSchedulerDelivery {

    private final OnceScheduleRepository onceScheduleRepository;

    @Scheduled(fixedDelay = 60000)
    public void onceScheduler(){

    }
}
