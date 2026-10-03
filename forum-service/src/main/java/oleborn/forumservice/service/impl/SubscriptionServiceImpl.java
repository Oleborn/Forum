package oleborn.forumservice.service.impl;

import lombok.RequiredArgsConstructor;
import oleborn.forumservice.exception.ResourceNotFoundException;
import oleborn.forumservice.mapper.PageResponseMapper;
import oleborn.forumservice.mapper.SubscriptionMapper;
import oleborn.forumservice.model.dto.common.PageResponseDto;
import oleborn.forumservice.model.dto.response.SubscriptionResponseDto;
import oleborn.forumservice.model.entity.Branch;
import oleborn.forumservice.model.entity.ForumUser;
import oleborn.forumservice.model.entity.Subscription;
import oleborn.forumservice.repository.BranchRepository;
import oleborn.forumservice.repository.ForumUserRepository;
import oleborn.forumservice.repository.SubscriptionRepository;
import oleborn.forumservice.service.SubscriptionService;
import oleborn.forumservice.specification.SubscriptionSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Реализация сервиса подписок пользователя на ветки.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final BranchRepository branchRepository;
    private final ForumUserRepository forumUserRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final PageResponseMapper pageResponseMapper;

    @Override
    public PageResponseDto<SubscriptionResponseDto> getMySubscriptions(
            UUID authUserId,
            UUID topicId,
            Pageable pageable
    ) {

        ForumUser actor = findActor(authUserId);

        Page<Subscription> page = subscriptionRepository.findAll(
                SubscriptionSpecification.of(
                        actor.getId(),
                        topicId
                ),
                pageable
        );

        return pageResponseMapper.toResponse(page.map(subscriptionMapper::toResponse));
    }

    @Override
    @Transactional
    public SubscriptionResponseDto subscribe(
            UUID branchId,
            UUID authUserId
    ) {

        ForumUser actor = findActor(authUserId);

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Ветка не найдена: id=" + branchId));

        Subscription subscription = subscriptionRepository.findByUserIdAndBranchId(actor.getId(), branchId)
                .orElseGet(() -> createSubscription(actor, branch));

        return subscriptionMapper.toResponse(subscription);
    }

    @Override
    @Transactional
    public void unsubscribe(
            UUID branchId,
            UUID authUserId
    ) {

        ForumUser actor = findActor(authUserId);

        subscriptionRepository.findByUserIdAndBranchId(actor.getId(), branchId)
                .ifPresent(subscriptionRepository::delete);
    }

    private Subscription createSubscription(ForumUser actor, Branch branch) {

        Subscription subscription = Subscription.builder()
                .user(actor)
                .branch(branch)
                .build();

        return subscriptionRepository.save(subscription);
    }

    private ForumUser findActor(UUID authUserId) {

        return forumUserRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Актор не найден: authUserId=" + authUserId));
    }
}
