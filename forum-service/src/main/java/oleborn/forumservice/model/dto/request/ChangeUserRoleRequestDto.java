package oleborn.forumservice.model.dto.request;

import jakarta.validation.constraints.NotNull;
import oleborn.forumservice.dictionary.Role;

/**
 * Смена роли пользователя.
 */
public record ChangeUserRoleRequestDto(
        @NotNull Role role
) {
}
