package oleborn.forumservice.service;

import oleborn.forumservice.model.dto.common.PageResponseDto;
import oleborn.forumservice.model.dto.request.AssignTopicModeratorRequestDto;
import oleborn.forumservice.model.dto.request.TopicRequestDto;
import oleborn.forumservice.model.dto.response.TopicResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Разделы форума.
 */
public interface TopicService {

    PageResponseDto<TopicResponseDto> getTopics(
            String search,
            Boolean deleted,
            Pageable pageable
    );

    TopicResponseDto getById(UUID topicId);

    TopicResponseDto create(
            TopicRequestDto request,
            UUID authUserId
    );

    TopicResponseDto update(
            UUID topicId,
            TopicRequestDto request,
            UUID authUserId
    );

    TopicResponseDto assignModerator(
            UUID topicId,
            AssignTopicModeratorRequestDto request,
            UUID authUserId
    );

    void softDelete(
            UUID topicId,
            String reason,
            UUID authUserId
    );
}
