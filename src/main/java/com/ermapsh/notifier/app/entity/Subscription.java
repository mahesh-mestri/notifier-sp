package com.ermapsh.notifier.app.entity;

import com.ermapsh.notifier.app.enums.SubscriptionPlan;
import com.ermapsh.notifier.app.enums.SubscriptionStatus;
import com.ermapsh.notifier.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "subscriptions",
        indexes = {
                @Index(name = "idx_subscription_user_id", columnList = "user_id"),
                @Index(name = "idx_subscription_status", columnList = "status")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true
    )
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private SubscriptionPlan plan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private SubscriptionStatus status;

    @Column(nullable = false)
    private Instant startedAt;

    private Instant expiresAt;

    @Column(nullable = false)
    private boolean autoRenewing;

    private Instant cancelledAt;
}