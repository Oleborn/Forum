package oleborn.forumservice.repository;

import oleborn.forumservice.dictionary.Role;
import oleborn.forumservice.model.entity.ForumUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

/**
 * Пользователи форума.
 */
public interface ForumUserRepository extends JpaRepository<ForumUser, UUID> {

    Optional<ForumUser> findByAuthUserId(UUID authUserId);

    @Query("""
            select u from ForumUser u
            where (:search is null or lower(u.username) like lower(concat('%', :search, '%')))
              and (:role is null or u.role = :role)
              and (:banned is null
                   or (:banned = true and u.bannedUntil is not null)
                   or (:banned = false and u.bannedUntil is null))
            """)
    Page<ForumUser> findAllByFilter(
            String search,
            Role role,
            Boolean banned,
            Pageable pageable
    );
}
