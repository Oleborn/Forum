package oleborn.forumservice.model.entity;

import jakarta.persistence.*;
import lombok.*;
import oleborn.forumservice.dictionary.ModerationAction;
import oleborn.forumservice.dictionary.ModerationTargetType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

/**
 * Журнал действий модераторов.
 */
@Entity
@Table(
        name = "moderation_audit_logs",
        indexes = {
                @Index(name = "idx_audit_moderator", columnList = "moderator_id"),
                @Index(name = "idx_audit_target", columnList = "target_type, target_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "action", "targetType"})
public class ModerationAuditLog {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moderator_id", nullable = false, foreignKey = @ForeignKey(name = "fk_audit_moderator"))
    private ForumUser moderator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_user_id", foreignKey = @ForeignKey(name = "fk_audit_target_user"))
    private ForumUser targetUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false, length = 20)
    private ModerationTargetType targetType;

    @Column(name = "target_id")
    private UUID targetId;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false, length = 20)
    private ModerationAction action;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
