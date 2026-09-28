package oleborn.forumservice.mapper;

import oleborn.forumservice.model.dto.request.BranchCreateRequestDto;
import oleborn.forumservice.model.dto.request.BranchUpdateRequestDto;
import oleborn.forumservice.model.dto.response.BranchResponseDto;
import oleborn.forumservice.model.entity.Branch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

/**
 * Маппинг ветки обсуждения.
 */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = ForumUserMapper.class
)
public interface BranchMapper {

    @Mapping(target = "topicId", source = "topic.id")
    @Mapping(target = "topicTitle", source = "topic.title")
    @Mapping(target = "author", source = "user")
    @Mapping(target = "pinned", source = "isPinned")
    @Mapping(target = "closed", source = "isClosed")
    BranchResponseDto toResponse(Branch branch);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "topic", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "isPinned", ignore = true)
    @Mapping(target = "isClosed", ignore = true)
    @Mapping(target = "viewsCount", ignore = true)
    @Mapping(target = "lastPostId", ignore = true)
    @Mapping(target = "lastCommentDate", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedReason", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Branch toEntity(BranchCreateRequestDto request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "topic", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "isPinned", ignore = true)
    @Mapping(target = "isClosed", ignore = true)
    @Mapping(target = "viewsCount", ignore = true)
    @Mapping(target = "lastPostId", ignore = true)
    @Mapping(target = "lastCommentDate", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedReason", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntity(
            BranchUpdateRequestDto request,
            @MappingTarget Branch branch
    );
}
