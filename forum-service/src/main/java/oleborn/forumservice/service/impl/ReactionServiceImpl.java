package oleborn.forumservice.service.impl;

import lombok.RequiredArgsConstructor;
import oleborn.forumservice.dictionary.ReactionType;
import oleborn.forumservice.exception.ResourceNotFoundException;
import oleborn.forumservice.mapper.ReactionMapper;
import oleborn.forumservice.model.dto.request.ReactionRequestDto;
import oleborn.forumservice.model.dto.response.ReactionCountResponseDto;
import oleborn.forumservice.model.dto.response.ReactionResponseDto;
import oleborn.forumservice.model.dto.response.ReactionSummaryResponseDto;
import oleborn.forumservice.model.entity.ForumUser;
import oleborn.forumservice.model.entity.Post;
import oleborn.forumservice.model.entity.Reaction;
import oleborn.forumservice.repository.ForumUserRepository;
import oleborn.forumservice.repository.PostRepository;
import oleborn.forumservice.repository.ReactionRepository;
import oleborn.forumservice.service.ReactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Реализация сервиса реакций на сообщения.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReactionServiceImpl implements ReactionService {

    private final ReactionRepository reactionRepository;
    private final PostRepository postRepository;
    private final ForumUserRepository forumUserRepository;
    private final ReactionMapper reactionMapper;

    @Override
    @Transactional
    public ReactionResponseDto react(
            UUID postId,
            ReactionRequestDto request,
            UUID authUserId
    ) {

        ForumUser actor = findActor(authUserId);

        Post post = findPost(postId);

        Reaction reaction = reactionRepository.findByUserIdAndPostId(actor.getId(), postId)
                .orElseGet(() -> newReaction(actor, post));

        reaction.setType(request.type());

        Reaction saved = reactionRepository.save(reaction);

        return reactionMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void removeReaction(
            UUID postId,
            UUID authUserId
    ) {

        ForumUser actor = findActor(authUserId);

        reactionRepository.findByUserIdAndPostId(actor.getId(), postId)
                .ifPresent(reactionRepository::delete);
    }

    @Override
    public ReactionSummaryResponseDto getSummary(
            UUID postId,
            UUID authUserId
    ) {

        ForumUser actor = findActor(authUserId);

        findPost(postId);

        List<ReactionCountResponseDto> reactions = reactionRepository.findCountsByPostId(postId)
                .stream()
                .map(reactionMapper::toCountResponse)
                .toList();

        ReactionType myReaction = reactionRepository.findTypeByUserIdAndPostId(actor.getId(), postId)
                .orElse(null);

        return new ReactionSummaryResponseDto(
                postId,
                reactions,
                myReaction
        );
    }

    private Reaction newReaction(ForumUser actor, Post post) {

        return Reaction.builder()
                .user(actor)
                .post(post)
                .build();
    }

    private Post findPost(UUID postId) {

        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Сообщение не найдено: id=" + postId));
    }

    private ForumUser findActor(UUID authUserId) {

        return forumUserRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Актор не найден: authUserId=" + authUserId));
    }
}
