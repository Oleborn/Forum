package oleborn.forumservice.specification;

import oleborn.forumservice.model.entity.Post;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Фильтры выборки сообщений. Удалённые сообщения в выборку не попадают.
 */
public final class PostSpecification {

    private PostSpecification() {
    }

    /**
     * Сообщения по ветке и автору.
     * {@code commentsOnly == true} — только комментарии, {@code false} — только
     * стартовые сообщения, {@code null} — без ограничения по вложенности.
     */
    public static Specification<Post> of(
            UUID branchId,
            UUID userId,
            Boolean commentsOnly
    ) {

        List<Specification<Post>> specifications = new ArrayList<>();

        if (branchId != null) {
            specifications.add(hasBranchId(branchId));
        }

        if (userId != null) {
            specifications.add(hasUserId(userId));
        }

        if (commentsOnly != null) {
            specifications.add(byParent(commentsOnly));
        }

        specifications.add(isNotDeleted());

        return Specification.allOf(specifications);
    }

    private static Specification<Post> hasBranchId(UUID branchId) {

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("branch").get("id"), branchId);
    }

    private static Specification<Post> hasUserId(UUID userId) {

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user").get("id"), userId);
    }

    private static Specification<Post> byParent(Boolean commentsOnly) {

        if (Boolean.TRUE.equals(commentsOnly)) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get("parent"));
        }

        return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("parent"));
    }

    private static Specification<Post> isNotDeleted() {

        return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("deletedAt"));
    }
}
