package oleborn.forumservice.exception;

import org.springframework.http.HttpStatus;

/**
 * Операция запрещена: актор не владеет ресурсом.
 *
 * @author oleborn
 * @since 2026-10-24
 */
public class AccessDeniedOperationException extends ApiException {

    /**
     * Создаёт исключение с ответом 403.
     *
     * <p>Нюансы использования: текст сообщения уходит клиенту без изменений; проверки
     * ролей на этом этапе нет, запрет выдаётся только по владению ресурсом.
     *
     * @param message описание запрещённой операции
     * @author oleborn
     * @since 2026-10-24
     */
    public AccessDeniedOperationException(String message) {

        super(HttpStatus.FORBIDDEN, message);
    }
}
