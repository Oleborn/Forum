package oleborn.forumservice.model.dto.request;

import java.util.UUID;

/**
 * Фильтры выборки веток обсуждений.
 * <p>
 * Связывается из query-параметров через {@code @ModelAttribute}. Непереданный
 * параметр остаётся {@code null} — соответствующий фильтр не применяется.
 */
public record BranchFilterDto(
        UUID topicId,
        UUID userId,
        Boolean pinned,
        Boolean closed,
        String search
) {
}
