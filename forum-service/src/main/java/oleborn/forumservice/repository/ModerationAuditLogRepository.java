package oleborn.forumservice.repository;

import oleborn.forumservice.model.entity.ModerationAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

/**
 * Журнал действий модераторов.
 */
public interface ModerationAuditLogRepository
        extends JpaRepository<ModerationAuditLog, UUID>, JpaSpecificationExecutor<ModerationAuditLog> {
}
