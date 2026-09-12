package oleborn.forumservice.service;

import oleborn.forumservice.model.dto.common.PageResponseDto;
import oleborn.forumservice.model.dto.request.PostCreateRequestDto;
import oleborn.forumservice.model.dto.request.PostUpdateRequestDto;
import oleborn.forumservice.model.dto.response.PostResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Сообщения: стартовые посты веток и комментарии.
 */
public interface PostService {

    PostResponseDto getStarter(UUID branchId);

    PageResponseDto<PostResponseDto> getComments(
            UUID branchId,
            Pageable pageable
    );

    PageResponseDto<PostResponseDto> getMyPosts(
            UUID authUserId,
            UUID branchId,
            Pageable pageable
    );

    PostResponseDto createComment(
            PostCreateRequestDto request,
            UUID authUserId
    );

    PostResponseDto update(
            UUID postId,
            PostUpdateRequestDto request,
            UUID authUserId
    );

    void softDelete(
            UUID postId,
            String reason,
            UUID authUserId
    );
}
