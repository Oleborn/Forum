package oleborn.forumservice.service;

import oleborn.forumservice.model.dto.response.AttachmentDownloadResponseDto;
import oleborn.forumservice.model.dto.response.AttachmentResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * Вложения к сообщениям.
 */
public interface AttachmentService {

    List<AttachmentResponseDto> getByPost(UUID postId);

    AttachmentResponseDto upload(
            UUID postId,
            MultipartFile file,
            UUID authUserId
    );

    AttachmentDownloadResponseDto download(UUID attachmentId);

    void delete(
            UUID attachmentId,
            UUID authUserId
    );
}
