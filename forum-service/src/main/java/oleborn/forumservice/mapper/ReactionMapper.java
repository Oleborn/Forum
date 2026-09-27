package oleborn.forumservice.mapper;

import oleborn.forumservice.model.dto.request.ReactionRequestDto;
import oleborn.forumservice.model.dto.response.ReactionCountResponseDto;
import oleborn.forumservice.model.dto.response.ReactionResponseDto;
import oleborn.forumservice.model.entity.Reaction;
import oleborn.forumservice.repository.ReactionRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Маппинг реакции на сообщение.
 */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = ForumUserMapper.class
)
public interface ReactionMapper {

    @Mapping(target = "postId", source = "post.id")
    ReactionResponseDto toResponse(Reaction reaction);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "post", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Reaction toEntity(ReactionRequestDto request);

    ReactionCountResponseDto toCountResponse(ReactionRepository.ReactionCountProjection projection);
}
