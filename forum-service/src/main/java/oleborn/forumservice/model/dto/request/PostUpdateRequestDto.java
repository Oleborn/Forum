package oleborn.forumservice.model.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Редактирование сообщения.
 */
public record PostUpdateRequestDto(
        @NotBlank String content
) {
}
