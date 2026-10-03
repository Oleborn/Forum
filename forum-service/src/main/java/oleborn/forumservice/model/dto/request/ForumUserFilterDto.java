package oleborn.forumservice.model.dto.request;

import oleborn.forumservice.dictionary.Role;

/**
 * Фильтры выборки пользователей форума.
 * <p>
 * Связывается из query-параметров через {@code @ModelAttribute}. Непереданный
 * параметр остаётся {@code null} — соответствующий фильтр не применяется.
 */
public record ForumUserFilterDto(
        String search,
        Role role,
        Boolean banned
) {
}
