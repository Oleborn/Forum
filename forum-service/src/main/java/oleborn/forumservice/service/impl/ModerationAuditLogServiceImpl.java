package oleborn.forumservice.service.impl;

import lombok.RequiredArgsConstructor;
import oleborn.forumservice.exception.ResourceNotFoundException;
import oleborn.forumservice.mapper.ModerationAuditLogMapper;
import oleborn.forumservice.mapper.PageResponseMapper;
import oleborn.forumservice.model.dto.common.PageResponseDto;
import oleborn.forumservice.model.dto.request.ModerationAuditLogFilterDto;
import oleborn.forumservice.model.dto.response.ModerationAuditLogResponseDto;
import oleborn.forumservice.model.entity.ModerationAuditLog;
import oleborn.forumservice.repository.ForumUserRepository;
import oleborn.forumservice.repository.ModerationAuditLogRepository;
import oleborn.forumservice.service.ModerationAuditLogService;
import oleborn.forumservice.specification.ModerationAuditLogSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Реализация сервиса журнала действий модераторов.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ModerationAuditLogServiceImpl implements ModerationAuditLogService {

    private final ModerationAuditLogRepository moderationAuditLogRepository;
    private final ForumUserRepository forumUserRepository;
    private final ModerationAuditLogMapper moderationAuditLogMapper;
    private final PageResponseMapper pageResponseMapper;

    @Override
    public PageResponseDto<ModerationAuditLogResponseDto> getAuditLog(
            UUID authUserId,
            ModerationAuditLogFilterDto filter,
            Pageable pageable
    ) {

        findActor(authUserId);

        Page<ModerationAuditLog> page = moderationAuditLogRepository.findAll(
                ModerationAuditLogSpecification.of(filter),
                pageable
        );

        return pageResponseMapper.toResponse(page.map(moderationAuditLogMapper::toResponse));
    }

    private void findActor(UUID authUserId) {

        forumUserRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Актор не найден: authUserId=" + authUserId));
    }
}
