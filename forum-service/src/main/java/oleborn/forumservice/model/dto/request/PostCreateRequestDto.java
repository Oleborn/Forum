package oleborn.forumservice.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Создание комментария к ветке.
 */
public record PostCreateRequestDto(
        @NotNull UUID branchId,
        UUID parentId,
        @NotBlank String content
) {
}
