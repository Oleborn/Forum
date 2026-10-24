package oleborn.forumservice.model.dto.response;

import java.time.Instant;
import java.util.List;

/**
 * Тело ответа об ошибке.
 *
 * <p>Нюансы использования: {@code error} — имя константы HTTP-статуса
 * ({@code NOT_FOUND}, {@code BAD_REQUEST} и т.п.); {@code details} заполняется только
 * для ошибок валидации, в остальных случаях приходит {@code null}.
 *
 * @author oleborn
 * @since 2026-10-24
 */
public record ErrorResponseDto(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        List<FieldErrorDto> details
) {
}
