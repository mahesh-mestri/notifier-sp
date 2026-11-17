package com.ermapsh.notifier.gateway.entity;

import com.ermapsh.notifier.gateway.enums.PaymentActor;
import com.ermapsh.notifier.gateway.enums.PaymentEvent;
import com.ermapsh.notifier.gateway.enums.PaymentStatus;
import com.ermapsh.notifier.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        indexes = {
                @Index(name = "idx_payment_transition_log_payment_id", columnList = "id, payment_id")
        }
)
public class PaymentTransitionLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "payment_id")
    private Payment payment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentEvent paymentEvent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus toStatus;

    @Column(name = "actor", length = 100)
    @Enumerated(EnumType.STRING)
    private PaymentActor actor;

    @Column(nullable = false)
    private LocalDateTime occurredAt;
}
