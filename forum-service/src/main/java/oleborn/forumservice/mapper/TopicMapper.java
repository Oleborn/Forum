package oleborn.forumservice.mapper;

import oleborn.forumservice.model.dto.request.TopicRequestDto;
import oleborn.forumservice.model.dto.response.TopicResponseDto;
import oleborn.forumservice.model.entity.Topic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

/**
 * Маппинг раздела форума.
 */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = ForumUserMapper.class
)
public interface TopicMapper {

    TopicResponseDto toResponse(Topic topic);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "moderator", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedReason", ignore = true)
    @Mapping(target = "version", ignore = true)
    Topic toEntity(TopicRequestDto request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "moderator", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedReason", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntity(
            TopicRequestDto request,
            @MappingTarget Topic topic
    );
}
