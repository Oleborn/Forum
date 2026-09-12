package oleborn.forumservice.model.dto.request;

import java.util.UUID;

/**
 * Создание ветки вместе со стартовым постом.
 */
public record BranchCreateRequestDto(
        UUID topicId,
        String title,
        String content
) {
}
