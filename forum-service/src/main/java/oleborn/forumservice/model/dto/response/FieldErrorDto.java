package oleborn.forumservice.model.dto.response;

/**
 * Ошибка валидации конкретного поля запроса.
 *
 * @author oleborn
 * @since 2026-10-24
 */
public record FieldErrorDto(
        String field,
        String message
) {
}
