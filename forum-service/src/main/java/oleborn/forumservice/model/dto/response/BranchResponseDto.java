package oleborn.forumservice.model.dto.response;

import java.time.Instant;
import java.util.UUID;

/**
 * Ветка обсуждения.
 */
public record BranchResponseDto(
        UUID id,
        UUID topicId,
        String topicTitle,
        UserShortResponseDto author,
        String title,
        Boolean pinned,
        Boolean closed,
        Long viewsCount,
        UUID lastPostId,
        Instant lastCommentDate,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt,
        String deletedReason
) {
}
