package oleborn.forumservice.controller;

import lombok.RequiredArgsConstructor;
import oleborn.forumservice.model.dto.common.PageResponseDto;
import oleborn.forumservice.model.dto.request.BranchCreateRequestDto;
import oleborn.forumservice.model.dto.request.BranchUpdateRequestDto;
import oleborn.forumservice.model.dto.response.BranchResponseDto;
import oleborn.forumservice.service.BranchService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @GetMapping
    public ResponseEntity<PageResponseDto<BranchResponseDto>> getBranches(
            @RequestParam(required = false) UUID topicId,
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) Boolean pinned,
            @RequestParam(required = false) Boolean closed,
            @RequestParam(required = false) String search,
            @PageableDefault(size = 20, sort = {"isPinned", "lastCommentDate"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {

        return ResponseEntity.ok(branchService.getBranches(
                topicId,
                userId,
                pinned,
                closed,
                search,
                pageable
        ));
    }

    @GetMapping("/{branchId}")
    public ResponseEntity<BranchResponseDto> getById(@PathVariable UUID branchId) {

        return ResponseEntity.ok(branchService.getById(branchId));
    }

    @PostMapping
    public ResponseEntity<BranchResponseDto> create(
            @RequestBody BranchCreateRequestDto request,
            @RequestHeader("X-Auth-UserId") UUID authUserId
    ) {

        BranchResponseDto created = branchService.create(request, authUserId);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{branchId}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{branchId}")
    public ResponseEntity<BranchResponseDto> update(
            @PathVariable UUID branchId,
            @RequestBody BranchUpdateRequestDto request,
            @RequestHeader("X-Auth-UserId") UUID authUserId
    ) {

        return ResponseEntity.ok(branchService.update(
                branchId,
                request,
                authUserId
        ));
    }

    @PostMapping("/{branchId}/close")
    public ResponseEntity<BranchResponseDto> close(
            @PathVariable UUID branchId,
            @RequestParam(required = false) String reason,
            @RequestHeader("X-Auth-UserId") UUID authUserId
    ) {

        return ResponseEntity.ok(branchService.close(
                branchId,
                reason,
                authUserId
        ));
    }

    @PostMapping("/{branchId}/open")
    public ResponseEntity<BranchResponseDto> open(
            @PathVariable UUID branchId,
            @RequestParam(required = false) String reason,
            @RequestHeader("X-Auth-UserId") UUID authUserId
    ) {

        return ResponseEntity.ok(branchService.open(
                branchId,
                reason,
                authUserId
        ));
    }

    @PostMapping("/{branchId}/pin")
    public ResponseEntity<BranchResponseDto> pin(
            @PathVariable UUID branchId,
            @RequestHeader("X-Auth-UserId") UUID authUserId
    ) {

        return ResponseEntity.ok(branchService.pin(branchId, authUserId));
    }

    @PostMapping("/{branchId}/unpin")
    public ResponseEntity<BranchResponseDto> unpin(
            @PathVariable UUID branchId,
            @RequestHeader("X-Auth-UserId") UUID authUserId
    ) {

        return ResponseEntity.ok(branchService.unpin(branchId, authUserId));
    }

    @DeleteMapping("/{branchId}")
    public ResponseEntity<Void> softDelete(
            @PathVariable UUID branchId,
            @RequestParam(required = false) String reason,
            @RequestHeader("X-Auth-UserId") UUID authUserId
    ) {

        branchService.softDelete(
                branchId,
                reason,
                authUserId
        );

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{branchId}/view")
    public ResponseEntity<Void> incrementViews(@PathVariable UUID branchId) {

        branchService.incrementViews(branchId);

        return ResponseEntity.noContent().build();
    }
}
