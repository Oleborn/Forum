package oleborn.forumservice.service;

import oleborn.forumservice.dictionary.ModerationAction;
import oleborn.forumservice.dictionary.ModerationTargetType;
import oleborn.forumservice.model.dto.common.PageResponseDto;
import oleborn.forumservice.model.dto.response.ModerationAuditLogResponseDto;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.UUID;

/**
 * Журнал действий модераторов.
 */
public interface ModerationAuditLogService {

    PageResponseDto<ModerationAuditLogResponseDto> getAuditLog(
            UUID authUserId,
            ModerationTargetType targetType,
            UUID targetId,
            UUID moderatorId,
            ModerationAction action,
            Instant from,
            Instant to,
            Pageable pageable
    );
}
