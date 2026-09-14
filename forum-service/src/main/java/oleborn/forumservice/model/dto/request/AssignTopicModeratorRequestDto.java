package oleborn.forumservice.model.dto.request;

import java.util.UUID;

/**
 * Назначение модератора раздела.
 */
public record AssignTopicModeratorRequestDto(
        UUID moderatorUserId
) {
}
