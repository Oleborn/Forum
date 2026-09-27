package oleborn.forumservice.model.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

/**
 * Блокировка пользователя.
 */
public record BanUserRequestDto(
        @NotNull @Future Instant bannedUntil,
        String reason
) {
}
