package oleborn.forumservice.service;

import oleborn.forumservice.model.dto.common.PageResponseDto;
import oleborn.forumservice.model.dto.request.BranchCreateRequestDto;
import oleborn.forumservice.model.dto.request.BranchUpdateRequestDto;
import oleborn.forumservice.model.dto.response.BranchResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Ветки обсуждений.
 */
public interface BranchService {

    PageResponseDto<BranchResponseDto> getBranches(
            UUID topicId,
            UUID userId,
            Boolean pinned,
            Boolean closed,
            String search,
            Pageable pageable
    );

    BranchResponseDto getById(UUID branchId);

    BranchResponseDto create(
            BranchCreateRequestDto request,
            UUID authUserId
    );

    BranchResponseDto update(
            UUID branchId,
            BranchUpdateRequestDto request,
            UUID authUserId
    );

    BranchResponseDto close(
            UUID branchId,
            String reason,
            UUID authUserId
    );

    BranchResponseDto open(
            UUID branchId,
            String reason,
            UUID authUserId
    );

    BranchResponseDto pin(
            UUID branchId,
            UUID authUserId
    );

    BranchResponseDto unpin(
            UUID branchId,
            UUID authUserId
    );

    void softDelete(
            UUID branchId,
            String reason,
            UUID authUserId
    );

    void incrementViews(UUID branchId);
}
