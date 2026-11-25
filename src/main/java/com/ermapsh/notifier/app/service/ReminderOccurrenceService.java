package com.ermapsh.notifier.app.service;

import com.ermapsh.notifier.app.entity.ReminderOccurrence;

import java.util.List;

public interface ReminderOccurrenceService {
    void generateUpcomingOccurrences();
    List<ReminderOccurrence> findOccurrencesDueWithinNextMinute();
}
