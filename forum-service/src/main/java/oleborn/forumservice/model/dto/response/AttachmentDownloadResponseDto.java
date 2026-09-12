package oleborn.forumservice.model.dto.response;

import org.springframework.core.io.Resource;

/**
 * Содержимое вложения для стриминга в ответе.
 */
public record AttachmentDownloadResponseDto(
        Resource resource,
        String fileName,
        String mimeType,
        long contentLength
) {
}
