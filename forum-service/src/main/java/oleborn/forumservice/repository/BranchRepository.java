package oleborn.forumservice.repository;

import oleborn.forumservice.model.entity.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

/**
 * Ветки обсуждений.
 */
public interface BranchRepository extends JpaRepository<Branch, UUID> {

    @Query("""
            select b from Branch b
            where (:topicId is null or b.topic.id = :topicId)
              and (:userId is null or b.user.id = :userId)
              and (:pinned is null or b.isPinned = :pinned)
              and (:closed is null or b.isClosed = :closed)
              and (:search is null or lower(b.title) like lower(concat('%', :search, '%')))
            """)
    Page<Branch> findAllByFilter(
            UUID topicId,
            UUID userId,
            Boolean pinned,
            Boolean closed,
            String search,
            Pageable pageable
    );

    @Modifying(clearAutomatically = true)
    @Query("update Branch b set b.viewsCount = b.viewsCount + 1 where b.id = :branchId")
    int incrementViews(UUID branchId);
}
