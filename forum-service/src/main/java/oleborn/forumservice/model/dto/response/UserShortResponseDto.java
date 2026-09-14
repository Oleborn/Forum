package oleborn.forumservice.model.dto.response;

import java.util.UUID;

/**
 * Краткое представление пользователя для встраивания в другие ответы.
 */
public record UserShortResponseDto(
        UUID id,
        String username,
        String avatarUrl
) {
}
