package oleborn.forumservice.repository;

import oleborn.forumservice.model.entity.ForumUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

/**
 * Пользователи форума.
 */
public interface ForumUserRepository
        extends JpaRepository<ForumUser, UUID>, JpaSpecificationExecutor<ForumUser> {

    Optional<ForumUser> findByAuthUserId(UUID authUserId);
}
