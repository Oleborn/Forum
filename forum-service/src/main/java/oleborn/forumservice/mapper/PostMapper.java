package oleborn.forumservice.mapper;

import oleborn.forumservice.model.dto.request.PostCreateRequestDto;
import oleborn.forumservice.model.dto.response.PostResponseDto;
import oleborn.forumservice.model.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Маппинг сообщения форума.
 */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = ForumUserMapper.class
)
public interface PostMapper {

    @Mapping(target = "branchId", source = "branch.id")
    @Mapping(target = "author", source = "user")
    @Mapping(target = "parentId", source = "parent.id")
    PostResponseDto toResponse(Post post);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "editedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedReason", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Post toEntity(PostCreateRequestDto request);
}
