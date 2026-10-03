package oleborn.forumservice.service.impl;

import lombok.RequiredArgsConstructor;
import oleborn.forumservice.exception.ResourceNotFoundException;
import oleborn.forumservice.mapper.BranchMapper;
import oleborn.forumservice.mapper.PageResponseMapper;
import oleborn.forumservice.model.dto.common.PageResponseDto;
import oleborn.forumservice.model.dto.request.BranchCreateRequestDto;
import oleborn.forumservice.model.dto.request.BranchFilterDto;
import oleborn.forumservice.model.dto.request.BranchUpdateRequestDto;
import oleborn.forumservice.model.dto.response.BranchResponseDto;
import oleborn.forumservice.model.entity.Branch;
import oleborn.forumservice.model.entity.ForumUser;
import oleborn.forumservice.model.entity.Post;
import oleborn.forumservice.model.entity.Topic;
import oleborn.forumservice.repository.BranchRepository;
import oleborn.forumservice.repository.ForumUserRepository;
import oleborn.forumservice.repository.PostRepository;
import oleborn.forumservice.repository.TopicRepository;
import oleborn.forumservice.service.BranchService;
import oleborn.forumservice.specification.BranchSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

/**
 * Реализация сервиса веток обсуждений.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;
    private final TopicRepository topicRepository;
    private final ForumUserRepository forumUserRepository;
    private final PostRepository postRepository;
    private final BranchMapper branchMapper;
    private final PageResponseMapper pageResponseMapper;

    @Override
    public PageResponseDto<BranchResponseDto> getBranches(
            BranchFilterDto filter,
            Pageable pageable
    ) {

        Page<Branch> page = branchRepository.findAll(
                BranchSpecification.of(filter),
                pageable
        );

        return pageResponseMapper.toResponse(page.map(branchMapper::toResponse));
    }

    @Override
    public BranchResponseDto getById(UUID branchId) {

        Branch branch = findBranch(branchId);

        return branchMapper.toResponse(branch);
    }

    @Override
    @Transactional
    public BranchResponseDto create(
            BranchCreateRequestDto request,
            UUID authUserId
    ) {

        ForumUser actor = findActor(authUserId);

        Topic topic = topicRepository.findById(request.topicId())
                .orElseThrow(() -> new ResourceNotFoundException("Раздел не найден: id=" + request.topicId()));

        Branch branch = branchMapper.toEntity(request);
        branch.setTopic(topic);
        branch.setUser(actor);
        branch.setIsPinned(false);
        branch.setIsClosed(false);
        branch.setViewsCount(0L);

        Branch saved = branchRepository.save(branch);

        Post starter = Post.builder()
                .branch(saved)
                .user(actor)
                .content(request.content())
                .build();

        Post savedStarter = postRepository.save(starter);

        saved.setLastPostId(savedStarter.getId());
        saved.setLastCommentDate(Instant.now());

        return branchMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public BranchResponseDto update(
            UUID branchId,
            BranchUpdateRequestDto request,
            UUID authUserId
    ) {

        findActor(authUserId);

        Branch branch = findBranch(branchId);

        branchMapper.updateEntity(request, branch);

        return branchMapper.toResponse(branch);
    }

    @Override
    @Transactional
    public BranchResponseDto close(
            UUID branchId,
            String reason,
            UUID authUserId
    ) {

        findActor(authUserId);

        Branch branch = findBranch(branchId);

        branch.setIsClosed(true);

        return branchMapper.toResponse(branch);
    }

    @Override
    @Transactional
    public BranchResponseDto open(
            UUID branchId,
            String reason,
            UUID authUserId
    ) {

        findActor(authUserId);

        Branch branch = findBranch(branchId);

        branch.setIsClosed(false);

        return branchMapper.toResponse(branch);
    }

    @Override
    @Transactional
    public BranchResponseDto pin(
            UUID branchId,
            UUID authUserId
    ) {

        findActor(authUserId);

        Branch branch = findBranch(branchId);

        branch.setIsPinned(true);

        return branchMapper.toResponse(branch);
    }

    @Override
    @Transactional
    public BranchResponseDto unpin(
            UUID branchId,
            UUID authUserId
    ) {

        findActor(authUserId);

        Branch branch = findBranch(branchId);

        branch.setIsPinned(false);

        return branchMapper.toResponse(branch);
    }

    @Override
    @Transactional
    public void softDelete(
            UUID branchId,
            String reason,
            UUID authUserId
    ) {

        findActor(authUserId);

        Branch branch = findBranch(branchId);

        if (branch.getDeletedAt() != null) {
            return;
        }

        branch.setDeletedAt(Instant.now());
        branch.setDeletedReason(reason);
    }

    @Override
    @Transactional
    public void incrementViews(UUID branchId) {

        int updated = branchRepository.incrementViews(branchId);

        if (updated == 0) {
            throw new ResourceNotFoundException("Ветка не найдена: id=" + branchId);
        }
    }

    private Branch findBranch(UUID branchId) {

        return branchRepository.findById(branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Ветка не найдена: id=" + branchId));
    }

    private ForumUser findActor(UUID authUserId) {

        return forumUserRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Актор не найден: authUserId=" + authUserId));
    }
}
