package oleborn.forumservice.controller;

import lombok.RequiredArgsConstructor;
import oleborn.forumservice.model.dto.common.PageResponseDto;
import oleborn.forumservice.model.dto.response.SubscriptionResponseDto;
import oleborn.forumservice.service.SubscriptionService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping("/users/me/subscriptions")
    public ResponseEntity<PageResponseDto<SubscriptionResponseDto>> getMySubscriptions(
            @RequestHeader("X-Auth-UserId") UUID authUserId,
            @RequestParam(required = false) UUID topicId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {

        return ResponseEntity.ok(subscriptionService.getMySubscriptions(
                authUserId,
                topicId,
                pageable
        ));
    }

    @PostMapping("/branches/{branchId}/subscriptions")
    public ResponseEntity<SubscriptionResponseDto> subscribe(
            @PathVariable UUID branchId,
            @RequestHeader("X-Auth-UserId") UUID authUserId
    ) {

        SubscriptionResponseDto created = subscriptionService.subscribe(branchId, authUserId);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{subscriptionId}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @DeleteMapping("/branches/{branchId}/subscriptions")
    public ResponseEntity<Void> unsubscribe(
            @PathVariable UUID branchId,
            @RequestHeader("X-Auth-UserId") UUID authUserId
    ) {

        subscriptionService.unsubscribe(branchId, authUserId);

        return ResponseEntity.noContent().build();
    }
}
