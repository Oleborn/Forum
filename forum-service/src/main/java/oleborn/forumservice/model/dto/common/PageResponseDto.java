package oleborn.forumservice.model.dto.common;

import java.util.List;

/**
 * Универсальная обёртка страницы для ответов API.
 */
public record PageResponseDto<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
}
