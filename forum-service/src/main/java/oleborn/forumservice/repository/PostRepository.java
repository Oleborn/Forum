package oleborn.forumservice.repository;

import oleborn.forumservice.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

/**
 * Сообщения: стартовые посты веток и комментарии.
 */
public interface PostRepository extends JpaRepository<Post, UUID>, JpaSpecificationExecutor<Post> {

    Optional<Post> findFirstByBranchIdAndParentIsNull(UUID branchId);
}
