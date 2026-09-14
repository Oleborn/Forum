package oleborn.forumservice.controller;

import lombok.RequiredArgsConstructor;
import oleborn.forumservice.dictionary.Role;
import oleborn.forumservice.model.dto.common.PageResponseDto;
import oleborn.forumservice.model.dto.request.BanUserRequestDto;
import oleborn.forumservice.model.dto.request.ChangeUserRoleRequestDto;
import oleborn.forumservice.model.dto.response.ForumUserResponseDto;
import oleborn.forumservice.service.ForumUserService;
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

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class ForumUserController {

    private final ForumUserService forumUserService;

    @GetMapping("/me")
    public ResponseEntity<ForumUserResponseDto> getMe(@RequestHeader("X-Auth-UserId") UUID authUserId) {

        return ResponseEntity.ok(forumUserService.getMe(authUserId));
    }

    @GetMapping
    public ResponseEntity<PageResponseDto<ForumUserResponseDto>> getUsers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) Boolean banned,
            @PageableDefault(size = 20, sort = "username", direction = Sort.Direction.ASC) Pageable pageable
    ) {

        return ResponseEntity.ok(forumUserService.getUsers(
                search,
                role,
                banned,
                pageable
        ));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ForumUserResponseDto> getById(@PathVariable UUID userId) {

        return ResponseEntity.ok(forumUserService.getById(userId));
    }

    @PostMapping("/{userId}/ban")
    public ResponseEntity<ForumUserResponseDto> banUser(
            @PathVariable UUID userId,
            @RequestBody BanUserRequestDto request,
            @RequestHeader("X-Auth-UserId") UUID authUserId
    ) {

        return ResponseEntity.ok(forumUserService.banUser(
                userId,
                request,
                authUserId
        ));
    }

    @DeleteMapping("/{userId}/ban")
    public ResponseEntity<ForumUserResponseDto> unbanUser(
            @PathVariable UUID userId,
            @RequestHeader("X-Auth-UserId") UUID authUserId
    ) {

        return ResponseEntity.ok(forumUserService.unbanUser(userId, authUserId));
    }

    @PutMapping("/{userId}/role")
    public ResponseEntity<ForumUserResponseDto> changeRole(
            @PathVariable UUID userId,
            @RequestBody ChangeUserRoleRequestDto request,
            @RequestHeader("X-Auth-UserId") UUID authUserId
    ) {

        return ResponseEntity.ok(forumUserService.changeRole(
                userId,
                request,
                authUserId
        ));
    }
}
