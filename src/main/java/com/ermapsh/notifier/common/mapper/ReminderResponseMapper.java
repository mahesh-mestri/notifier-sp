package com.ermapsh.notifier.common.mapper;

import com.ermapsh.notifier.app.dto.res.ReminderResponse;
import com.ermapsh.notifier.app.entity.Reminder;
import com.ermapsh.notifier.app.entity.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReminderResponseMapper {

    @Mapping(target = "id", source = "reminder.id")
    ReminderResponse toResponse(Reminder reminder, Schedule schedule);
}