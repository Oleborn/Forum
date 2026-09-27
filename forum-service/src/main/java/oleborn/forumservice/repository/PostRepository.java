package oleborn.forumservice.repository;

import oleborn.forumservice.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

/**
 * Сообщения: стартовые посты веток и комментарии.
 */
public interface PostRepository extends JpaRepository<Post, UUID> {

    Optional<Post> findFirstByBranchIdAndParentIsNull(UUID branchId);

    Page<Post> findByBranchIdAndParentIsNotNull(
            UUID branchId,
            Pageable pageable
    );

    @Query("""
            select p from Post p
            where p.user.id = :userId
              and (:branchId is null or p.branch.id = :branchId)
            """)
    Page<Post> findAllByUserFilter(
            UUID userId,
            UUID branchId,
            Pageable pageable
    );
}
