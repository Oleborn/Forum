package oleborn.forumservice.model.dto.response;

import java.time.Instant;
import java.util.UUID;

/**
 * Раздел форума.
 */
public record TopicResponseDto(
        UUID id,
        String title,
        String description,
        UserShortResponseDto moderator,
        Integer sortOrder,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt,
        String deletedReason
) {
}
