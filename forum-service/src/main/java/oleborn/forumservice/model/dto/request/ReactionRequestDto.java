package oleborn.forumservice.model.dto.request;

import oleborn.forumservice.dictionary.ReactionType;

/**
 * Постановка или смена реакции на пост.
 */
public record ReactionRequestDto(
        ReactionType type
) {
}
