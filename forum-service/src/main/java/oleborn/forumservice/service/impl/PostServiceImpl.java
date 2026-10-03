package oleborn.forumservice.service.impl;

import lombok.RequiredArgsConstructor;
import oleborn.forumservice.exception.AccessDeniedOperationException;
import oleborn.forumservice.exception.ResourceNotFoundException;
import oleborn.forumservice.mapper.PageResponseMapper;
import oleborn.forumservice.mapper.PostMapper;
import oleborn.forumservice.model.dto.common.PageResponseDto;
import oleborn.forumservice.model.dto.request.PostCreateRequestDto;
import oleborn.forumservice.model.dto.request.PostUpdateRequestDto;
import oleborn.forumservice.model.dto.response.PostResponseDto;
import oleborn.forumservice.model.entity.Branch;
import oleborn.forumservice.model.entity.ForumUser;
import oleborn.forumservice.model.entity.Post;
import oleborn.forumservice.repository.BranchRepository;
import oleborn.forumservice.repository.ForumUserRepository;
import oleborn.forumservice.repository.PostRepository;
import oleborn.forumservice.service.PostService;
import oleborn.forumservice.specification.PostSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

/**
 * Реализация сервиса сообщений: стартовые посты веток и комментарии.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final BranchRepository branchRepository;
    private final ForumUserRepository forumUserRepository;
    private final PostMapper postMapper;
    private final PageResponseMapper pageResponseMapper;

    @Override
    public PostResponseDto getStarter(UUID branchId) {

        Post starter = postRepository.findFirstByBranchIdAndParentIsNull(branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Стартовое сообщение не найдено: branchId=" + branchId));

        return postMapper.toResponse(starter);
    }

    @Override
    public PageResponseDto<PostResponseDto> getComments(
            UUID branchId,
            Pageable pageable
    ) {

        Page<Post> page = postRepository.findAll(
                PostSpecification.of(
                        branchId,
                        null,
                        true
                ),
                pageable
        );

        return pageResponseMapper.toResponse(page.map(postMapper::toResponse));
    }

    @Override
    public PageResponseDto<PostResponseDto> getMyPosts(
            UUID authUserId,
            UUID branchId,
            Pageable pageable
    ) {

        ForumUser actor = findActor(authUserId);

        Page<Post> page = postRepository.findAll(
                PostSpecification.of(
                        branchId,
                        actor.getId(),
                        null
                ),
                pageable
        );

        return pageResponseMapper.toResponse(page.map(postMapper::toResponse));
    }

    @Override
    @Transactional
    public PostResponseDto createComment(
            PostCreateRequestDto request,
            UUID authUserId
    ) {

        ForumUser actor = findActor(authUserId);

        Branch branch = branchRepository.findById(request.branchId())
                .orElseThrow(() -> new ResourceNotFoundException("Ветка не найдена: id=" + request.branchId()));

        Post parent = resolveParent(request.parentId(), branch);

        Post post = postMapper.toEntity(request);
        post.setBranch(branch);
        post.setUser(actor);
        post.setParent(parent);

        Post saved = postRepository.save(post);

        branch.setLastPostId(saved.getId());
        branch.setLastCommentDate(Instant.now());

        return postMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public PostResponseDto update(
            UUID postId,
            PostUpdateRequestDto request,
            UUID authUserId
    ) {

        ForumUser actor = findActor(authUserId);

        Post post = findPost(postId);

        checkOwnership(post, actor);

        post.setContent(request.content());
        post.setEditedAt(Instant.now());

        return postMapper.toResponse(post);
    }

    @Override
    @Transactional
    public void softDelete(
            UUID postId,
            String reason,
            UUID authUserId
    ) {

        ForumUser actor = findActor(authUserId);

        Post post = findPost(postId);

        checkOwnership(post, actor);

        if (post.getDeletedAt() != null) {
            return;
        }

        post.setDeletedAt(Instant.now());
        post.setDeletedReason(reason);
    }

    private Post resolveParent(UUID parentId, Branch branch) {

        if (parentId == null) {
            return null;
        }

        Post parent = findPost(parentId);

        boolean anotherBranch = !parent.getBranch().getId().equals(branch.getId());
        boolean nestedParent = parent.getParent() != null;

        if (anotherBranch || nestedParent) {
            throw new IllegalArgumentException("Родитель должен быть стартовым сообщением этой ветки: id=" + parentId);
        }

        return parent;
    }

    private void checkOwnership(Post post, ForumUser actor) {

        if (!post.getUser().getId().equals(actor.getId())) {
            throw new AccessDeniedOperationException("Нет прав на изменение сообщения: id=" + post.getId());
        }
    }

    private Post findPost(UUID postId) {

        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Сообщение не найдено: id=" + postId));
    }

    private ForumUser findActor(UUID authUserId) {

        return forumUserRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Актор не найден: authUserId=" + authUserId));
    }
}
