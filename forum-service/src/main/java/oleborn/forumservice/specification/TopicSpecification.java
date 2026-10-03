package oleborn.forumservice.specification;

import jakarta.persistence.criteria.Expression;
import oleborn.forumservice.model.entity.Topic;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Фильтры выборки разделов форума.
 */
public final class TopicSpecification {

    private TopicSpecification() {
    }

    /**
     * Разделы по подстроке заголовка и признаку удаления.
     * {@code deleted == null} или {@code false} — только не удалённые.
     */
    public static Specification<Topic> of(
            String search,
            Boolean deleted
    ) {

        List<Specification<Topic>> specifications = new ArrayList<>();

        if (search != null && !search.isBlank()) {
            specifications.add(hasTitleLike(search));
        }

        specifications.add(byDeleted(deleted));

        return Specification.allOf(specifications);
    }

    private static Specification<Topic> hasTitleLike(String search) {

        return (root, query, criteriaBuilder) -> {

            String pattern = "%" + search.toLowerCase() + "%";

            Expression<String> title = criteriaBuilder.lower(root.get("title"));

            return criteriaBuilder.like(title, pattern);
        };
    }

    private static Specification<Topic> byDeleted(Boolean deleted) {

        if (Boolean.TRUE.equals(deleted)) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get("deletedAt"));
        }

        return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("deletedAt"));
    }
}
