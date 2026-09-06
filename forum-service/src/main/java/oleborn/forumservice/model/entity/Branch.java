package oleborn.forumservice.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

/**
 * Ветка обсуждения (Thread).
 */
@Entity
@Table(
        name = "branches",
        indexes = {
                @Index(name = "idx_branch_topic", columnList = "topic_id"),
                @Index(name = "idx_branch_user", columnList = "user_id"),
                @Index(name = "idx_branch_pinned", columnList = "is_pinned"),
                @Index(name = "idx_branch_closed", columnList = "is_closed"),
                @Index(name = "idx_branch_last_comment", columnList = "last_comment_date"),
                @Index(name = "idx_branch_deleted", columnList = "deleted_at"),
                @Index(name = "idx_branch_topic_pinned_date", columnList = "topic_id, is_pinned DESC, last_comment_date DESC")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "title", "isPinned", "isClosed"})
public class Branch {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false, foreignKey = @ForeignKey(name = "fk_branch_topic"))
    private Topic topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_branch_user"))
    private ForumUser user;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "is_pinned", nullable = false)
    private Boolean isPinned = false;

    @Column(name = "is_closed", nullable = false)
    private Boolean isClosed = false;

    @Column(name = "views_count", nullable = false)
    private Long viewsCount = 0L;

    @Column(name = "last_post_id")
    private UUID lastPostId;

    @Column(name = "last_comment_date")
    private Instant lastCommentDate;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Column(name = "deleted_reason", columnDefinition = "TEXT")
    private String deletedReason;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;
}
