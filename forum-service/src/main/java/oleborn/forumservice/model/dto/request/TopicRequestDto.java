package oleborn.forumservice.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 * Создание и обновление раздела форума.
 */
public record TopicRequestDto(
        @NotBlank @Size(max = 255) String title,
        String description,
        @NotNull @PositiveOrZero Integer sortOrder
) {
}
