package oleborn.forumservice.service.impl;

import lombok.RequiredArgsConstructor;
import oleborn.forumservice.exception.ResourceNotFoundException;
import oleborn.forumservice.mapper.PageResponseMapper;
import oleborn.forumservice.mapper.TopicMapper;
import oleborn.forumservice.model.dto.common.PageResponseDto;
import oleborn.forumservice.model.dto.request.AssignTopicModeratorRequestDto;
import oleborn.forumservice.model.dto.request.TopicRequestDto;
import oleborn.forumservice.model.dto.response.TopicResponseDto;
import oleborn.forumservice.model.entity.ForumUser;
import oleborn.forumservice.model.entity.Topic;
import oleborn.forumservice.repository.ForumUserRepository;
import oleborn.forumservice.repository.TopicRepository;
import oleborn.forumservice.service.TopicService;
import oleborn.forumservice.specification.TopicSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

/**
 * Реализация сервиса разделов форума.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private final ForumUserRepository forumUserRepository;
    private final TopicMapper topicMapper;
    private final PageResponseMapper pageResponseMapper;

    @Override
    public PageResponseDto<TopicResponseDto> getTopics(
            String search,
            Boolean deleted,
            Pageable pageable
    ) {

        Page<Topic> page = topicRepository.findAll(
                TopicSpecification.of(
                        search,
                        deleted
                ),
                pageable
        );

        return pageResponseMapper.toResponse(page.map(topicMapper::toResponse));
    }

    @Override
    public TopicResponseDto getById(UUID topicId) {

        Topic topic = findTopic(topicId);

        return topicMapper.toResponse(topic);
    }

    @Override
    @Transactional
    public TopicResponseDto create(
            TopicRequestDto request,
            UUID authUserId
    ) {

        findActor(authUserId);

        Topic topic = topicMapper.toEntity(request);

        Topic saved = topicRepository.save(topic);

        return topicMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public TopicResponseDto update(
            UUID topicId,
            TopicRequestDto request,
            UUID authUserId
    ) {

        findActor(authUserId);

        Topic topic = findTopic(topicId);

        topicMapper.updateEntity(request, topic);

        return topicMapper.toResponse(topic);
    }

    @Override
    @Transactional
    public TopicResponseDto assignModerator(
            UUID topicId,
            AssignTopicModeratorRequestDto request,
            UUID authUserId
    ) {

        findActor(authUserId);

        Topic topic = findTopic(topicId);

        ForumUser moderator = forumUserRepository.findById(request.moderatorUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Пользователь не найден: id=" + request.moderatorUserId()
                ));

        topic.setModerator(moderator);

        return topicMapper.toResponse(topic);
    }

    @Override
    @Transactional
    public void softDelete(
            UUID topicId,
            String reason,
            UUID authUserId
    ) {

        findActor(authUserId);

        Topic topic = findTopic(topicId);

        if (topic.getDeletedAt() != null) {
            return;
        }

        topic.setDeletedAt(Instant.now());
        topic.setDeletedReason(reason);
    }

    private Topic findTopic(UUID topicId) {

        return topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Раздел не найден: id=" + topicId));
    }

    private ForumUser findActor(UUID authUserId) {

        return forumUserRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Актор не найден: authUserId=" + authUserId));
    }
}
