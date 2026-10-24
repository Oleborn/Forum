package oleborn.forumservice.exception;

import org.springframework.http.HttpStatus;

/**
 * Операция не может быть выполнена при текущем состоянии данных.
 *
 * @author oleborn
 * @since 2026-10-24
 */
public class InvalidOperationException extends ApiException {

    /**
     * Создаёт исключение с ответом 400.
     *
     * <p>Нюансы использования: применяется для нарушений бизнес-правил, которые не
     * ловятся валидацией контракта (например, ссылка на родительское сообщение из
     * чужой ветки); текст сообщения уходит клиенту без изменений.
     *
     * @param message описание нарушения бизнес-правила
     * @author oleborn
     * @since 2026-10-24
     */
    public InvalidOperationException(String message) {

        super(HttpStatus.BAD_REQUEST, message);
    }
}
