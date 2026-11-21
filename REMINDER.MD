# Notifier – Reminder API Documentation

## 1. Create Reminder

**Endpoint:** `POST /api/v1/reminder`

**Description:** Creates a reminder for the authenticated user with the selected schedule configuration.

### Request structure

| Field | Type | Required | Description |
|---|---|---|---|
| title | String | Yes | Reminder title |
| description | String | No | Reminder description |
| categoryId | Enum | Yes | PERSONAL, WORK, HEALTH, OTHER |
| scheduleType | Enum | Yes | ONCE, INTERVAL, DAILY, WEEKLY, MONTHLY |
| notificationEnabled | Boolean | Yes | Enable or disable notifications |
| schedule | Object | Yes | Schedule-specific configuration |

---

## 2. Schedule types

### A. ONCE

Triggers a reminder one time on a specific date and time.

```json
{
  "title": "Doctor Appointment",
  "description": "Visit the clinic",
  "categoryId": "HEALTH",
  "scheduleType": "ONCE",
  "notificationEnabled": true,
  "schedule": {
    "reminderDate": "2026-11-20",
    "reminderTime": "10:30:00",
    "timeZone": "Asia/Kolkata"
  }
}
```

| Schedule field | Type |
|---|---|
| reminderDate | LocalDate |
| reminderTime | LocalTime |
| timeZone | String |

### B. INTERVAL

Triggers reminders repeatedly at a configured interval within a daily time window.

```json
{
  "title": "Drink Water",
  "description": "Stay hydrated",
  "categoryId": "HEALTH",
  "scheduleType": "INTERVAL",
  "notificationEnabled": true,
  "schedule": {
    "startDate": "2026-11-20",
    "endDate": "2026-11-30",
    "intervalValue": 2,
    "intervalUnit": "HOURS",
    "windowStartTime": "09:00:00",
    "windowEndTime": "18:00:00",
    "timeZone": "Asia/Kolkata"
  }
}
```

| Schedule field | Type |
|---|---|
| startDate | LocalDate |
| endDate | LocalDate (Optional) |
| intervalValue | Integer |
| intervalUnit | MINUTES / HOURS |
| windowStartTime | LocalTime |
| windowEndTime | LocalTime |
| timeZone | String |

### C. DAILY

Triggers a reminder every day at a specific time.

```json
{
  "title": "Morning Exercise",
  "description": "Daily workout",
  "categoryId": "PERSONAL",
  "scheduleType": "DAILY",
  "notificationEnabled": true,
  "schedule": {
    "startDate": "2026-11-20",
    "endDate": "2026-12-20",
    "reminderTime": "07:00:00",
    "timeZone": "Asia/Kolkata"
  }
}
```

| Schedule field | Type |
|---|---|
| startDate | LocalDate |
| endDate | LocalDate (Optional) |
| reminderTime | LocalTime |
| timeZone | String |

### D. WEEKLY

Triggers a reminder on selected weekdays.

Weekday values:
- 0 – Sunday
- 1 – Monday
- 2 – Tuesday
- 3 – Wednesday
- 4 – Thursday
- 5 – Friday
- 6 – Saturday

```json
{
  "title": "Team Meeting",
  "description": "Weekly team sync",
  "categoryId": "WORK",
  "scheduleType": "WEEKLY",
  "notificationEnabled": true,
  "schedule": {
    "startDate": "2026-11-20",
    "endDate": "2026-12-31",
    "reminderTime": "10:00:00",
    "weekdays": [1, 3, 5],
    "timeZone": "Asia/Kolkata"
  }
}
```

| Schedule field | Type |
|---|---|
| startDate | LocalDate |
| endDate | LocalDate (Optional) |
| reminderTime | LocalTime |
| weekdays | Set of Integer |
| timeZone | String |

### E. MONTHLY

Triggers a reminder on a particular day of every month.

```json
{
  "title": "Pay Electricity Bill",
  "description": "Monthly electricity bill payment",
  "categoryId": "PERSONAL",
  "scheduleType": "MONTHLY",
  "notificationEnabled": true,
  "schedule": {
    "startDate": "2026-11-20",
    "endDate": "2027-11-20",
    "reminderTime": "09:00:00",
    "dayOfMonth": 20,
    "timeZone": "Asia/Kolkata"
  }
}
```

| Schedule field | Type |
|---|---|
| startDate | LocalDate |
| endDate | LocalDate (Optional) |
| reminderTime | LocalTime |
| dayOfMonth | Integer (1–31) |
| timeZone | String |

---

## 3. Common validation rules

| Field | Validation |
|---|---|
| title | Required, maximum 100 characters |
| categoryId | Must be a valid ReminderCategory |
| scheduleType | Must be a supported schedule type |
| schedule | Must match the selected scheduleType |
| intervalValue | Must be greater than zero |
| intervalUnit | MINUTES or HOURS |
| weekdays | Must contain valid values from 0–6 |
| dayOfMonth | Must be between 1 and 31 |
| endDate | Must not be earlier than startDate |
| timeZone | Must be a valid timezone identifier |

## 4. Response structure

**Current ReminderResponse**

```json
{
  "id": "a2f3d6b1-1234-4567-8901-123456789012",
  "title": "Doctor Appointment",
  "description": "Visit the clinic",
  "categoryId": "HEALTH",
  "scheduleType": "ONCE",
  "notificationEnabled": true,
  "active": true,
  "createdAt": "2026-11-18T10:30:00",
  "updatedAt": "2026-11-18T10:30:00"
}
```

Note: This response contains reminder fields only. Schedule details are not currently included.

## 5. Important implementation notes

1. The authenticated user's ID must come from the security context, not the request body.
2. Each reminder must have exactly one matching schedule configuration.
3. Each schedule entity references its parent Reminder through `reminder_id`.
4. Schedule entities inherit audit fields from `BaseEntity`.
5. Store the timezone with the schedule configuration.
6. Generate actual notification occurrences separately from schedule persistence.
7. Define the monthly behavior for dates that do not exist in every month (e.g. February 30) before implementing occurrence generation.

