package oleborn.forumservice.mapper;

import oleborn.forumservice.model.dto.response.ModerationAuditLogResponseDto;
import oleborn.forumservice.model.entity.ModerationAuditLog;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * Маппинг записи журнала действий модераторов.
 */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = ForumUserMapper.class
)
public interface ModerationAuditLogMapper {

    ModerationAuditLogResponseDto toResponse(ModerationAuditLog auditLog);
}
