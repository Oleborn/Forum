package oleborn.forumservice.service;

import oleborn.forumservice.model.dto.request.ReactionRequestDto;
import oleborn.forumservice.model.dto.response.ReactionResponseDto;
import oleborn.forumservice.model.dto.response.ReactionSummaryResponseDto;

import java.util.UUID;

/**
 * Реакции на сообщения.
 */
public interface ReactionService {

    ReactionResponseDto react(
            UUID postId,
            ReactionRequestDto request,
            UUID authUserId
    );

    void removeReaction(
            UUID postId,
            UUID authUserId
    );

    ReactionSummaryResponseDto getSummary(
            UUID postId,
            UUID authUserId
    );
}
