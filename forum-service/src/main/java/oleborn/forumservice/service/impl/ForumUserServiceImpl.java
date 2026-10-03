package oleborn.forumservice.service.impl;

import lombok.RequiredArgsConstructor;
import oleborn.forumservice.exception.ResourceNotFoundException;
import oleborn.forumservice.mapper.ForumUserMapper;
import oleborn.forumservice.mapper.PageResponseMapper;
import oleborn.forumservice.model.dto.common.PageResponseDto;
import oleborn.forumservice.model.dto.request.BanUserRequestDto;
import oleborn.forumservice.model.dto.request.ChangeUserRoleRequestDto;
import oleborn.forumservice.model.dto.request.ForumUserFilterDto;
import oleborn.forumservice.model.dto.response.ForumUserResponseDto;
import oleborn.forumservice.model.entity.ForumUser;
import oleborn.forumservice.repository.ForumUserRepository;
import oleborn.forumservice.service.ForumUserService;
import oleborn.forumservice.specification.ForumUserSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Реализация сервиса пользователей форума.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ForumUserServiceImpl implements ForumUserService {

    private final ForumUserRepository forumUserRepository;
    private final ForumUserMapper forumUserMapper;
    private final PageResponseMapper pageResponseMapper;

    @Override
    public ForumUserResponseDto getMe(UUID authUserId) {

        ForumUser actor = findActor(authUserId);

        return forumUserMapper.toResponse(actor);
    }

    @Override
    public PageResponseDto<ForumUserResponseDto> getUsers(
            ForumUserFilterDto filter,
            Pageable pageable
    ) {

        Page<ForumUser> page = forumUserRepository.findAll(
                ForumUserSpecification.of(filter),
                pageable
        );

        return pageResponseMapper.toResponse(page.map(forumUserMapper::toResponse));
    }

    @Override
    public ForumUserResponseDto getById(UUID userId) {

        ForumUser user = findUser(userId);

        return forumUserMapper.toResponse(user);
    }

    @Override
    @Transactional
    public ForumUserResponseDto banUser(
            UUID userId,
            BanUserRequestDto request,
            UUID authUserId
    ) {

        findActor(authUserId);

        ForumUser user = findUser(userId);

        user.setBannedUntil(request.bannedUntil());
        user.setBanReason(request.reason());

        return forumUserMapper.toResponse(user);
    }

    @Override
    @Transactional
    public ForumUserResponseDto unbanUser(
            UUID userId,
            UUID authUserId
    ) {

        findActor(authUserId);

        ForumUser user = findUser(userId);

        user.setBannedUntil(null);
        user.setBanReason(null);

        return forumUserMapper.toResponse(user);
    }

    @Override
    @Transactional
    public ForumUserResponseDto changeRole(
            UUID userId,
            ChangeUserRoleRequestDto request,
            UUID authUserId
    ) {

        findActor(authUserId);

        ForumUser user = findUser(userId);

        user.setRole(request.role());

        return forumUserMapper.toResponse(user);
    }

    private ForumUser findActor(UUID authUserId) {

        return forumUserRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Актор не найден: authUserId=" + authUserId));
    }

    private ForumUser findUser(UUID userId) {

        return forumUserRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден: id=" + userId));
    }
}
