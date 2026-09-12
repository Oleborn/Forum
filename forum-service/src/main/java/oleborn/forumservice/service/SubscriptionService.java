package oleborn.forumservice.service;

import oleborn.forumservice.model.dto.common.PageResponseDto;
import oleborn.forumservice.model.dto.response.SubscriptionResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Подписки пользователя на ветки.
 */
public interface SubscriptionService {

    PageResponseDto<SubscriptionResponseDto> getMySubscriptions(
            UUID authUserId,
            UUID topicId,
            Pageable pageable
    );

    SubscriptionResponseDto subscribe(
            UUID branchId,
            UUID authUserId
    );

    void unsubscribe(
            UUID branchId,
            UUID authUserId
    );
}
