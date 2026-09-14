package oleborn.forumservice.model.dto.response;

import oleborn.forumservice.dictionary.Role;

import java.time.Instant;
import java.util.UUID;

/**
 * Профиль пользователя форума.
 */
public record ForumUserResponseDto(
        UUID id,
        UUID profileUserId,
        String username,
        String avatarUrl,
        Role role,
        Instant bannedUntil,
        String banReason,
        Instant createdAt,
        Instant updatedAt
) {
}
