package oleborn.forumservice.controller;

import lombok.RequiredArgsConstructor;
import oleborn.forumservice.model.dto.request.ReactionRequestDto;
import oleborn.forumservice.model.dto.response.ReactionResponseDto;
import oleborn.forumservice.model.dto.response.ReactionSummaryResponseDto;
import oleborn.forumservice.service.ReactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts/{postId}/reactions")
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionService reactionService;

    @PostMapping
    public ResponseEntity<ReactionResponseDto> react(
            @PathVariable UUID postId,
            @RequestBody ReactionRequestDto request,
            @RequestHeader("X-Auth-UserId") UUID authUserId
    ) {

        return ResponseEntity.ok(reactionService.react(
                postId,
                request,
                authUserId
        ));
    }

    @DeleteMapping
    public ResponseEntity<Void> removeReaction(
            @PathVariable UUID postId,
            @RequestHeader("X-Auth-UserId") UUID authUserId
    ) {

        reactionService.removeReaction(postId, authUserId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<ReactionSummaryResponseDto> getSummary(
            @PathVariable UUID postId,
            @RequestHeader("X-Auth-UserId") UUID authUserId
    ) {

        return ResponseEntity.ok(reactionService.getSummary(postId, authUserId));
    }
}
