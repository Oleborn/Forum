package oleborn.forumservice.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Обновление заголовка ветки.
 */
public record BranchUpdateRequestDto(
        @NotBlank @Size(max = 255) String title
) {
}
