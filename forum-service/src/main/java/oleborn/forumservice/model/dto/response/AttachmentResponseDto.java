package oleborn.forumservice.model.dto.response;

import java.time.Instant;
import java.util.UUID;

/**
 * Вложение к сообщению.
 */
public record AttachmentResponseDto(
        UUID id,
        UUID postId,
        String fileName,
        String mimeType,
        Long fileSize,
        Instant createdAt
) {
}
