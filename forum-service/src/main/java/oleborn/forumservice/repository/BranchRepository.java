package oleborn.forumservice.repository;

import oleborn.forumservice.model.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

/**
 * Ветки обсуждений.
 */
public interface BranchRepository extends JpaRepository<Branch, UUID>, JpaSpecificationExecutor<Branch> {

    @Modifying(clearAutomatically = true)
    @Query("update Branch b set b.viewsCount = b.viewsCount + 1 where b.id = :branchId")
    int incrementViews(UUID branchId);
}
