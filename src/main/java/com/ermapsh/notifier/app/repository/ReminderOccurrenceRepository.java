
package com.ermapsh.notifier.app.repository;

import com.ermapsh.notifier.app.entity.ReminderOccurrence;
import com.ermapsh.notifier.app.enums.OccurrenceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ReminderOccurrenceRepository
        extends JpaRepository<ReminderOccurrence, UUID> {

    @Query("""
        SELECT o FROM ReminderOccurrence o
        WHERE o.status = :status
        AND o.scheduledAt <= :now
        ORDER BY o.scheduledAt ASC
        """)
    List<ReminderOccurrence> findDueOccurrences(
            @Param("status") OccurrenceStatus status,
            @Param("now") Instant now
    );


    @Query("""
    SELECT o FROM ReminderOccurrence o
    WHERE o.status = :status
      AND o.scheduledAt <= :cutoff
    ORDER BY o.scheduledAt ASC
""")
    List<ReminderOccurrence> fetchBeforeMinuteReminderOccurrence(
            @Param("status") OccurrenceStatus status,
            @Param("cutoff") Instant cutoff
    );

}
