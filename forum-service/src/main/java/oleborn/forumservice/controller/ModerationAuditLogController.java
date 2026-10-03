package oleborn.forumservice.controller;

import lombok.RequiredArgsConstructor;
import oleborn.forumservice.model.dto.common.PageResponseDto;
import oleborn.forumservice.model.dto.request.ModerationAuditLogFilterDto;
import oleborn.forumservice.model.dto.response.ModerationAuditLogResponseDto;
import oleborn.forumservice.service.ModerationAuditLogService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/moderation/audit")
@RequiredArgsConstructor
public class ModerationAuditLogController {

    private final ModerationAuditLogService moderationAuditLogService;

    @GetMapping
    public ResponseEntity<PageResponseDto<ModerationAuditLogResponseDto>> getAuditLog(
            @RequestHeader("X-Auth-UserId") UUID authUserId,
            @ModelAttribute("auditLogFilter") ModerationAuditLogFilterDto filter,
            @PageableDefault(size = 50, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {

        return ResponseEntity.ok(moderationAuditLogService.getAuditLog(
                authUserId,
                filter,
                pageable
        ));
    }
}
