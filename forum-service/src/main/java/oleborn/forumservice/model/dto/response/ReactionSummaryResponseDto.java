package oleborn.forumservice.model.dto.response;

import oleborn.forumservice.dictionary.ReactionType;

import java.util.List;
import java.util.UUID;

/**
 * Агрегированные реакции по посту.
 *
 * @param reactions  список типов реакций с количеством (порядок гарантирован)
 * @param myReaction реакция текущего пользователя, если он её ставил
 */
public record ReactionSummaryResponseDto(
        UUID postId,
        List<ReactionCountResponseDto> reactions,
        ReactionType myReaction
) {
}
