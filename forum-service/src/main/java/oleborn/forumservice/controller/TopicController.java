package oleborn.forumservice.controller;

import lombok.RequiredArgsConstructor;
import oleborn.forumservice.model.dto.common.PageResponseDto;
import oleborn.forumservice.model.dto.request.AssignTopicModeratorRequestDto;
import oleborn.forumservice.model.dto.request.TopicRequestDto;
import oleborn.forumservice.model.dto.response.TopicResponseDto;
import oleborn.forumservice.service.TopicService;
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
@RequestMapping("/api/v1/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @GetMapping
    public ResponseEntity<PageResponseDto<TopicResponseDto>> getTopics(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean deleted,
            @PageableDefault(size = 50, sort = "sortOrder", direction = Sort.Direction.ASC) Pageable pageable
    ) {

        return ResponseEntity.ok(topicService.getTopics(
                search,
                deleted,
                pageable
        ));
    }

    @GetMapping("/{topicId}")
    public ResponseEntity<TopicResponseDto> getById(@PathVariable UUID topicId) {

        return ResponseEntity.ok(topicService.getById(topicId));
    }

    @PostMapping
    public ResponseEntity<TopicResponseDto> create(
            @RequestBody TopicRequestDto request,
            @RequestHeader("X-Auth-UserId") UUID authUserId
    ) {

        TopicResponseDto created = topicService.create(request, authUserId);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{topicId}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{topicId}")
    public ResponseEntity<TopicResponseDto> update(
            @PathVariable UUID topicId,
            @RequestBody TopicRequestDto request,
            @RequestHeader("X-Auth-UserId") UUID authUserId
    ) {

        return ResponseEntity.ok(topicService.update(
                topicId,
                request,
                authUserId
        ));
    }

    @PutMapping("/{topicId}/moderator")
    public ResponseEntity<TopicResponseDto> assignModerator(
            @PathVariable UUID topicId,
            @RequestBody AssignTopicModeratorRequestDto request,
            @RequestHeader("X-Auth-UserId") UUID authUserId
    ) {

        return ResponseEntity.ok(topicService.assignModerator(
                topicId,
                request,
                authUserId
        ));
    }

    @DeleteMapping("/{topicId}")
    public ResponseEntity<Void> softDelete(
            @PathVariable UUID topicId,
            @RequestParam(required = false) String reason,
            @RequestHeader("X-Auth-UserId") UUID authUserId
    ) {

        topicService.softDelete(
                topicId,
                reason,
                authUserId
        );

        return ResponseEntity.noContent().build();
    }
}
