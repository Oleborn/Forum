package oleborn.forumservice.repository;

import oleborn.forumservice.model.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

/**
 * Подписки пользователя на ветки.
 */
public interface SubscriptionRepository
        extends JpaRepository<Subscription, UUID>, JpaSpecificationExecutor<Subscription> {

    Optional<Subscription> findByUserIdAndBranchId(
            UUID userId,
            UUID branchId
    );
}
