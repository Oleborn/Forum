package oleborn.forumservice.controller;

import lombok.RequiredArgsConstructor;
import oleborn.forumservice.dictionary.ModerationAction;
import oleborn.forumservice.dictionary.ModerationTargetType;
import oleborn.forumservice.model.dto.common.PageResponseDto;
import oleborn.forumservice.model.dto.response.ModerationAuditLogResponseDto;
import oleborn.forumservice.service.ModerationAuditLogService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/moderation/audit")
@RequiredArgsConstructor
public class ModerationAuditLogController {

    private final ModerationAuditLogService moderationAuditLogService;

    @GetMapping
    public ResponseEntity<PageResponseDto<ModerationAuditLogResponseDto>> getAuditLog(
            @RequestHeader("X-Auth-UserId") UUID authUserId,
            @RequestParam(required = false) ModerationTargetType targetType,
            @RequestParam(required = false) UUID targetId,
            @RequestParam(required = false) UUID moderatorId,
            @RequestParam(required = false) ModerationAction action,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @PageableDefault(size = 50, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {

        return ResponseEntity.ok(moderationAuditLogService.getAuditLog(
                authUserId,
                targetType,
                targetId,
                moderatorId,
                action,
                from,
                to,
                pageable
        ));
    }
}
