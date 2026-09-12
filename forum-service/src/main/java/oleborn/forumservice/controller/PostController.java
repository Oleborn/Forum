package oleborn.forumservice.controller;

import lombok.RequiredArgsConstructor;
import oleborn.forumservice.model.dto.common.PageResponseDto;
import oleborn.forumservice.model.dto.request.PostCreateRequestDto;
import oleborn.forumservice.model.dto.request.PostUpdateRequestDto;
import oleborn.forumservice.model.dto.response.PostResponseDto;
import oleborn.forumservice.service.PostService;
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
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/branches/{branchId}/starter")
    public ResponseEntity<PostResponseDto> getStarter(@PathVariable UUID branchId) {

        return ResponseEntity.ok(postService.getStarter(branchId));
    }

    @GetMapping("/branches/{branchId}/comments")
    public ResponseEntity<PageResponseDto<PostResponseDto>> getComments(
            @PathVariable UUID branchId,
            @PageableDefault(size = 30, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable
    ) {

        return ResponseEntity.ok(postService.getComments(branchId, pageable));
    }

    @GetMapping("/users/me/posts")
    public ResponseEntity<PageResponseDto<PostResponseDto>> getMyPosts(
            @RequestHeader("X-Auth-UserId") UUID authUserId,
            @RequestParam(required = false) UUID branchId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {

        return ResponseEntity.ok(postService.getMyPosts(
                authUserId,
                branchId,
                pageable
        ));
    }

    @PostMapping("/posts")
    public ResponseEntity<PostResponseDto> createComment(
            @RequestBody PostCreateRequestDto request,
            @RequestHeader("X-Auth-UserId") UUID authUserId
    ) {

        PostResponseDto created = postService.createComment(request, authUserId);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{postId}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostResponseDto> update(
            @PathVariable UUID postId,
            @RequestBody PostUpdateRequestDto request,
            @RequestHeader("X-Auth-UserId") UUID authUserId
    ) {

        return ResponseEntity.ok(postService.update(
                postId,
                request,
                authUserId
        ));
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> softDelete(
            @PathVariable UUID postId,
            @RequestParam(required = false) String reason,
            @RequestHeader("X-Auth-UserId") UUID authUserId
    ) {

        postService.softDelete(
                postId,
                reason,
                authUserId
        );

        return ResponseEntity.noContent().build();
    }
}
