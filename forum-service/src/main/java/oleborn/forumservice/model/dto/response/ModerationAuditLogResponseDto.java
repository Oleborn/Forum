package oleborn.forumservice.model.dto.response;

import oleborn.forumservice.dictionary.ModerationAction;
import oleborn.forumservice.dictionary.ModerationTargetType;

import java.time.Instant;
import java.util.UUID;

/**
 * Запись журнала действий модераторов.
 */
public record ModerationAuditLogResponseDto(
        UUID id,
        UserShortResponseDto moderator,
        UserShortResponseDto targetUser,
        ModerationTargetType targetType,
        UUID targetId,
        ModerationAction action,
        String reason,
        Instant createdAt
) {
}
