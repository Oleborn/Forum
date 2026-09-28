package oleborn.forumservice.repository;

import oleborn.forumservice.model.entity.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

/**
 * Подписки пользователя на ветки.
 */
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {

    @Query("""
            select s from Subscription s
            where s.user.id = :userId
              and (:topicId is null or s.branch.topic.id = :topicId)
            """)
    Page<Subscription> findAllByUserFilter(
            UUID userId,
            UUID topicId,
            Pageable pageable
    );

    Optional<Subscription> findByUserIdAndBranchId(
            UUID userId,
            UUID branchId
    );
}
