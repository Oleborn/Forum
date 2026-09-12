package oleborn.forumservice.model.dto.request;

/**
 * Создание и обновление раздела форума.
 */
public record TopicRequestDto(
        String title,
        String description,
        Integer sortOrder
) {
}
