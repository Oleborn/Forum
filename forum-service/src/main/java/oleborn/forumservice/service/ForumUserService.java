package oleborn.forumservice.service;

import oleborn.forumservice.dictionary.Role;
import oleborn.forumservice.model.dto.common.PageResponseDto;
import oleborn.forumservice.model.dto.request.BanUserRequestDto;
import oleborn.forumservice.model.dto.request.ChangeUserRoleRequestDto;
import oleborn.forumservice.model.dto.response.ForumUserResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Пользователи форума.
 */
public interface ForumUserService {

    ForumUserResponseDto getMe(UUID authUserId);

    PageResponseDto<ForumUserResponseDto> getUsers(
            String search,
            Role role,
            Boolean banned,
            Pageable pageable
    );

    ForumUserResponseDto getById(UUID userId);

    ForumUserResponseDto banUser(
            UUID userId,
            BanUserRequestDto request,
            UUID authUserId
    );

    ForumUserResponseDto unbanUser(
            UUID userId,
            UUID authUserId
    );

    ForumUserResponseDto changeRole(
            UUID userId,
            ChangeUserRoleRequestDto request,
            UUID authUserId
    );
}
