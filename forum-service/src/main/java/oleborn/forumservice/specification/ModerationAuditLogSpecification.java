package oleborn.forumservice.specification;

import oleborn.forumservice.dictionary.ModerationAction;
import oleborn.forumservice.dictionary.ModerationTargetType;
import oleborn.forumservice.model.dto.request.ModerationAuditLogFilterDto;
import oleborn.forumservice.model.entity.ModerationAuditLog;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Фильтры выборки журнала действий модераторов.
 */
public final class ModerationAuditLogSpecification {

    private ModerationAuditLogSpecification() {
    }

    /**
     * Условия выборки по фильтрам запроса: непереданный фильтр не накладывает
     * ограничения. Период включительный по обеим границам.
     */
    public static Specification<ModerationAuditLog> of(ModerationAuditLogFilterDto filter) {

        List<Specification<ModerationAuditLog>> specifications = new ArrayList<>();

        if (filter.targetType() != null) {
            specifications.add(hasTargetType(filter.targetType()));
        }

        if (filter.targetId() != null) {
            specifications.add(hasTargetId(filter.targetId()));
        }

        if (filter.moderatorId() != null) {
            specifications.add(hasModeratorId(filter.moderatorId()));
        }

        if (filter.action() != null) {
            specifications.add(hasAction(filter.action()));
        }

        if (filter.from() != null) {
            specifications.add(createdFrom(filter.from()));
        }

        if (filter.to() != null) {
            specifications.add(createdTo(filter.to()));
        }

        return Specification.allOf(specifications);
    }

    private static Specification<ModerationAuditLog> hasTargetType(ModerationTargetType targetType) {

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("targetType"), targetType);
    }

    private static Specification<ModerationAuditLog> hasTargetId(UUID targetId) {

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("targetId"), targetId);
    }

    private static Specification<ModerationAuditLog> hasModeratorId(UUID moderatorId) {

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("moderator").get("id"), moderatorId);
    }

    private static Specification<ModerationAuditLog> hasAction(ModerationAction action) {

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("action"), action);
    }

    private static Specification<ModerationAuditLog> createdFrom(Instant from) {

        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.<Instant>get("createdAt"), from);
    }

    private static Specification<ModerationAuditLog> createdTo(Instant to) {

        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.<Instant>get("createdAt"), to);
    }
}
