package oleborn.forumservice.mapper;

import oleborn.forumservice.model.dto.response.ForumUserResponseDto;
import oleborn.forumservice.model.dto.response.UserShortResponseDto;
import oleborn.forumservice.model.entity.ForumUser;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * Маппинг пользователя форума.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ForumUserMapper {

    ForumUserResponseDto toResponse(ForumUser user);

    UserShortResponseDto toShortResponse(ForumUser user);
}
