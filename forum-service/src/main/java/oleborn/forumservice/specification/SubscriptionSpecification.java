package oleborn.forumservice.specification;

import oleborn.forumservice.model.entity.Subscription;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Фильтры выборки подписок пользователя.
 */
public final class SubscriptionSpecification {

    private SubscriptionSpecification() {
    }

    /**
     * Подписки пользователя с опциональным ограничением по разделу.
     */
    public static Specification<Subscription> of(
            UUID userId,
            UUID topicId
    ) {

        List<Specification<Subscription>> specifications = new ArrayList<>();

        specifications.add(hasUserId(userId));

        if (topicId != null) {
            specifications.add(hasTopicId(topicId));
        }

        return Specification.allOf(specifications);
    }

    private static Specification<Subscription> hasUserId(UUID userId) {

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user").get("id"), userId);
    }

    private static Specification<Subscription> hasTopicId(UUID topicId) {

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get("branch").get("topic").get("id"),
                topicId
        );
    }
}
