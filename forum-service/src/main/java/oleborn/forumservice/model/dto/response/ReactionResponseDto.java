package oleborn.forumservice.model.dto.response;

import oleborn.forumservice.dictionary.ReactionType;

import java.time.Instant;
import java.util.UUID;

/**
 * Реакция пользователя на пост.
 */
public record ReactionResponseDto(
        UUID id,
        UUID postId,
        UserShortResponseDto user,
        ReactionType type,
        Instant createdAt,
        Instant updatedAt
) {
}
