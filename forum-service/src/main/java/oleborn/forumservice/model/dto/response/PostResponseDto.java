package oleborn.forumservice.model.dto.response;

import java.time.Instant;
import java.util.UUID;

/**
 * Сообщение: стартовый пост ветки или комментарий.
 */
public record PostResponseDto(
        UUID id,
        UUID branchId,
        UserShortResponseDto author,
        UUID parentId,
        String content,
        Instant editedAt,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt,
        String deletedReason
) {
}
