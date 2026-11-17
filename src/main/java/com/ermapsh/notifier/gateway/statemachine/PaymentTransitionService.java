package com.ermapsh.notifier.gateway.statemachine;

import com.ermapsh.notifier.gateway.entity.Payment;
import com.ermapsh.notifier.gateway.entity.PaymentTransitionLog;
import com.ermapsh.notifier.gateway.enums.PaymentActor;
import com.ermapsh.notifier.gateway.enums.PaymentEvent;
import com.ermapsh.notifier.gateway.enums.PaymentStatus;
import com.ermapsh.notifier.gateway.repository.PaymentTransitionLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentTransitionService {
    private final PaymentTransitionLogRepository paymentTransitionLogRepository;
    private final PaymentStateMachine paymentStateMachine;

    public PaymentStatus apply(Payment payment, PaymentEvent paymentEvent) {

        PaymentStatus fromStatus = payment.getPaymentStatus();

        PaymentStatus toStatus = paymentStateMachine.transition(fromStatus, paymentEvent);

        PaymentTransitionLog log = PaymentTransitionLog.builder()
                .payment(payment)
                .fromStatus(fromStatus)
                .paymentEvent(paymentEvent)
                .toStatus(toStatus)
                .actor(PaymentActor.SYSTEM)
                .occurredAt(LocalDateTime.now())
                .build();

        payment.setPaymentStatus(toStatus);

        paymentTransitionLogRepository.save(log);

        return toStatus;
    }
}
