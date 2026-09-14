package oleborn.forumservice.model.dto.request;

import oleborn.forumservice.dictionary.Role;

/**
 * Смена роли пользователя.
 */
public record ChangeUserRoleRequestDto(
        Role role
) {
}
