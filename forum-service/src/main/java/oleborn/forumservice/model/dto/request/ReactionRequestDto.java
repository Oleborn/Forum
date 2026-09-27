package oleborn.forumservice.model.dto.request;

import jakarta.validation.constraints.NotNull;
import oleborn.forumservice.dictionary.ReactionType;

/**
 * Постановка или смена реакции на пост.
 */
public record ReactionRequestDto(
        @NotNull ReactionType type
) {
}
