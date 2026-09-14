package oleborn.forumservice.model.dto.response;

import oleborn.forumservice.dictionary.ReactionType;

/**
 * Количество реакций одного типа.
 */
public record ReactionCountResponseDto(
        ReactionType type,
        long count
) {
}
