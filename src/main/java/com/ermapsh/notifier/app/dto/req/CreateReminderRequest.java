package com.ermapsh.notifier.app.dto.req;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ermapsh.notifier.app.enums.ReminderCategory;
import com.ermapsh.notifier.app.enums.ReminderScheduleType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateReminderRequest(

        @NotBlank
        @Size(max = 100)
        String title,

        @Size(max = 500)
        String description,

        @NotNull
        ReminderCategory categoryId,

        @NotNull
        ReminderScheduleType scheduleType,

        @NotNull
        Boolean notificationEnabled,

        @NotNull
        @Valid
        @JsonTypeInfo(
                use = JsonTypeInfo.Id.NAME,
                include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
                property = "scheduleType"
        )
        @JsonSubTypes({
                @JsonSubTypes.Type(value = OnceScheduleRequest.class, name = "ONCE"),
                @JsonSubTypes.Type(value = IntervalScheduleRequest.class, name = "INTERVAL"),
                @JsonSubTypes.Type(value = DailyScheduleRequest.class, name = "DAILY"),
                @JsonSubTypes.Type(value = WeeklyScheduleRequest.class, name = "WEEKLY"),
                @JsonSubTypes.Type(value = MonthlyScheduleRequest.class, name = "MONTHLY")
        })
        ReminderScheduleRequest schedule

) {
}