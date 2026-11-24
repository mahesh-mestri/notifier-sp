
package com.ermapsh.notifier.app.repository;

import com.ermapsh.notifier.app.entity.ReminderOccurrence;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ReminderOccurrenceRepository
        extends JpaRepository<ReminderOccurrence, UUID> {

}
