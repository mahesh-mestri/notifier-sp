
package com.ermapsh.notifier.app.entity;

import com.ermapsh.notifier.app.enums.Platform;
import com.ermapsh.notifier.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(
        name = "user_fcm_tokens",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_fcm_token",
                        columnNames = "fcm_token"
                )
        },
        indexes = {
                @Index(
                        name = "idx_user_fcm_user_id",
                        columnList = "user_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FcmToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "fcm_token", nullable = false, length = 512)
    private String fcmToken;

    @Column(nullable = false)
    private Platform platform;
}
