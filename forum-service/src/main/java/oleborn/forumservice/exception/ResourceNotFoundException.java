package oleborn.forumservice.exception;

import org.springframework.http.HttpStatus;

/**
 * Запрашиваемый ресурс не найден.
 *
 * @author oleborn
 * @since 2026-10-24
 */
public class ResourceNotFoundException extends ApiException {

    /**
     * Создаёт исключение с ответом 404.
     *
     * <p>Нюансы использования: текст сообщения уходит клиенту без изменений, поэтому
     * формулировка должна быть понятна внешнему потребителю API.
     *
     * @param message описание ненайденного ресурса
     * @author oleborn
     * @since 2026-10-24
     */
    public ResourceNotFoundException(String message) {

        super(HttpStatus.NOT_FOUND, message);
    }
}
