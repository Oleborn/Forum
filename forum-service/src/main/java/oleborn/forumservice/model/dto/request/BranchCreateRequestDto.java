package oleborn.forumservice.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * Создание ветки вместе со стартовым постом.
 */
public record BranchCreateRequestDto(
        @NotNull UUID topicId,
        @NotBlank @Size(max = 255) String title,
        @NotBlank String content
) {
}
