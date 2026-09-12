package oleborn.forumservice.model.dto.request;

import java.util.UUID;

/**
 * Создание комментария к ветке.
 */
public record PostCreateRequestDto(
        UUID branchId,
        UUID parentId,
        String content
) {
}
