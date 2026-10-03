package oleborn.forumservice.specification;

import jakarta.persistence.criteria.Expression;
import oleborn.forumservice.dictionary.Role;
import oleborn.forumservice.model.dto.request.ForumUserFilterDto;
import oleborn.forumservice.model.entity.ForumUser;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Фильтры выборки пользователей форума.
 */
public final class ForumUserSpecification {

    private ForumUserSpecification() {
    }

    /**
     * Условия выборки по фильтрам запроса: непереданный фильтр не накладывает
     * ограничения.
     */
    public static Specification<ForumUser> of(ForumUserFilterDto filter) {

        List<Specification<ForumUser>> specifications = new ArrayList<>();

        if (filter.search() != null && !filter.search().isBlank()) {
            specifications.add(hasUsernameLike(filter.search()));
        }

        if (filter.role() != null) {
            specifications.add(hasRole(filter.role()));
        }

        if (filter.banned() != null) {
            specifications.add(byBanned(filter.banned()));
        }

        return Specification.allOf(specifications);
    }

    private static Specification<ForumUser> hasUsernameLike(String search) {

        return (root, query, criteriaBuilder) -> {

            String pattern = "%" + search.toLowerCase() + "%";

            Expression<String> username = criteriaBuilder.lower(root.get("username"));

            return criteriaBuilder.like(username, pattern);
        };
    }

    private static Specification<ForumUser> hasRole(Role role) {

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("role"), role);
    }

    private static Specification<ForumUser> byBanned(Boolean banned) {

        if (Boolean.TRUE.equals(banned)) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get("bannedUntil"));
        }

        return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("bannedUntil"));
    }
}
