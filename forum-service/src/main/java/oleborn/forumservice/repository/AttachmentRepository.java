package oleborn.forumservice.repository;

import oleborn.forumservice.model.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Вложения к сообщениям.
 */
public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {

    List<Attachment> findByPostId(UUID postId);
}
