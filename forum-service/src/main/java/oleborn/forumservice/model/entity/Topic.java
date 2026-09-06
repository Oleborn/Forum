package oleborn.forumservice.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

/**
 * Раздел/категория форума.
 */
@Entity
@Table(
        name = "topics",
        indexes = {
                @Index(name = "idx_topic_moderator", columnList = "moderator_id"),
                @Index(name = "idx_topic_sort_order", columnList = "sort_order")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "title", "sortOrder"})
public class Topic {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moderator_id", foreignKey = @ForeignKey(name = "fk_topic_moderator"))
    private ForumUser moderator;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Column(name = "deleted_reason", columnDefinition = "TEXT")
    private String deletedReason;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;
}
