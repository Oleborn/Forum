package oleborn.forumservice.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

/**
 * Сообщение: стартовое сообщение ветки или комментарий.
 * Вложенность ограничена одним уровнем (parent_id может быть NULL или ссылаться
 * на стартовое сообщение, у которого parent_id = NULL).
 */
@Entity
@Table(
        name = "posts",
        indexes = {
                @Index(name = "idx_post_branch", columnList = "branch_id"),
                @Index(name = "idx_post_user", columnList = "user_id"),
                @Index(name = "idx_post_parent", columnList = "parent_id"),
                @Index(name = "idx_post_deleted", columnList = "deleted_at")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "content"})
public class Post {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false, foreignKey = @ForeignKey(name = "fk_post_branch"))
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_post_user"))
    private ForumUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", foreignKey = @ForeignKey(name = "fk_post_parent"))
    private Post parent;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "edited_at")
    private Instant editedAt;

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
