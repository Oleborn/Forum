package oleborn.forumservice.repository;

import oleborn.forumservice.dictionary.ModerationAction;
import oleborn.forumservice.dictionary.ModerationTargetType;
import oleborn.forumservice.model.entity.ModerationAuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.UUID;

/**
 * Журнал действий модераторов.
 */
public interface ModerationAuditLogRepository extends JpaRepository<ModerationAuditLog, UUID> {

    @Query("""
            select a from ModerationAuditLog a
            where (:targetType is null or a.targetType = :targetType)
              and (:targetId is null or a.targetId = :targetId)
              and (:moderatorId is null or a.moderator.id = :moderatorId)
              and (:action is null or a.action = :action)
              and (:fromDate is null or a.createdAt >= :fromDate)
              and (:toDate is null or a.createdAt <= :toDate)
            """)
    Page<ModerationAuditLog> findAllByFilter(
            ModerationTargetType targetType,
            UUID targetId,
            UUID moderatorId,
            ModerationAction action,
            Instant fromDate,
            Instant toDate,
            Pageable pageable
    );
}
