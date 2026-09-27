package oleborn.forumservice.model.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Назначение модератора раздела.
 */
public record AssignTopicModeratorRequestDto(
        @NotNull UUID moderatorUserId
) {
}
