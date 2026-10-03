package oleborn.forumservice.specification;

import jakarta.persistence.criteria.Expression;
import oleborn.forumservice.model.dto.request.BranchFilterDto;
import oleborn.forumservice.model.entity.Branch;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Фильтры выборки веток обсуждений. Удалённые ветки в выборку не попадают.
 */
public final class BranchSpecification {

    private BranchSpecification() {
    }

    /**
     * Условия выборки по фильтрам запроса: непереданный фильтр не накладывает
     * ограничения.
     */
    public static Specification<Branch> of(BranchFilterDto filter) {

        List<Specification<Branch>> specifications = new ArrayList<>();

        if (filter.topicId() != null) {
            specifications.add(hasTopicId(filter.topicId()));
        }

        if (filter.userId() != null) {
            specifications.add(hasUserId(filter.userId()));
        }

        if (filter.pinned() != null) {
            specifications.add(hasPinned(filter.pinned()));
        }

        if (filter.closed() != null) {
            specifications.add(hasClosed(filter.closed()));
        }

        if (filter.search() != null && !filter.search().isBlank()) {
            specifications.add(hasTitleLike(filter.search()));
        }

        specifications.add(isNotDeleted());

        return Specification.allOf(specifications);
    }

    private static Specification<Branch> hasTopicId(UUID topicId) {

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("topic").get("id"), topicId);
    }

    private static Specification<Branch> hasUserId(UUID userId) {

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user").get("id"), userId);
    }

    private static Specification<Branch> hasPinned(Boolean pinned) {

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isPinned"), pinned);
    }

    private static Specification<Branch> hasClosed(Boolean closed) {

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isClosed"), closed);
    }

    private static Specification<Branch> hasTitleLike(String search) {

        return (root, query, criteriaBuilder) -> {

            String pattern = "%" + search.toLowerCase() + "%";

            Expression<String> title = criteriaBuilder.lower(root.get("title"));

            return criteriaBuilder.like(title, pattern);
        };
    }

    private static Specification<Branch> isNotDeleted() {

        return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("deletedAt"));
    }
}
