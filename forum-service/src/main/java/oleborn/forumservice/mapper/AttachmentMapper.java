package oleborn.forumservice.mapper;

import oleborn.forumservice.model.dto.response.AttachmentDownloadResponseDto;
import oleborn.forumservice.model.dto.response.AttachmentResponseDto;
import oleborn.forumservice.model.entity.Attachment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.query.Param;

/**
 * Маппинг вложения к сообщению.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AttachmentMapper {

    @Mapping(target = "postId", source = "post.id")
    AttachmentResponseDto toResponse(Attachment attachment);

    @Mapping(target = "resource", source = "resource")
    @Mapping(target = "fileName", source = "attachment.fileName")
    @Mapping(target = "mimeType", source = "attachment.mimeType")
    @Mapping(target = "contentLength", source = "attachment.fileSize")
    AttachmentDownloadResponseDto toDownload(
            @Param("attachment") Attachment attachment,
            @Param("resource") Resource resource
    );
}
