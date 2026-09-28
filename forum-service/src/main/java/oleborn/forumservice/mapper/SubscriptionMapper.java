package oleborn.forumservice.mapper;

import oleborn.forumservice.model.dto.response.SubscriptionResponseDto;
import oleborn.forumservice.model.entity.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Маппинг подписки на ветку.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SubscriptionMapper {

    @Mapping(target = "branchId", source = "branch.id")
    @Mapping(target = "branchTitle", source = "branch.title")
    @Mapping(target = "topicId", source = "branch.topic.id")
    @Mapping(target = "topicTitle", source = "branch.topic.title")
    SubscriptionResponseDto toResponse(Subscription subscription);
}
