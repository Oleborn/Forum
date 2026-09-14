package oleborn.forumservice.model.dto.response;

import java.time.Instant;
import java.util.UUID;

/**
 * Подписка пользователя на ветку.
 */
public record SubscriptionResponseDto(
        UUID id,
        UUID branchId,
        String branchTitle,
        UUID topicId,
        String topicTitle,
        Instant createdAt
) {
}
