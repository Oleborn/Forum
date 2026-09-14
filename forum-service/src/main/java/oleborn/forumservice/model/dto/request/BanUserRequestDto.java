package oleborn.forumservice.model.dto.request;

import java.time.Instant;

/**
 * Блокировка пользователя.
 */
public record BanUserRequestDto(
        Instant bannedUntil,
        String reason
) {
}
