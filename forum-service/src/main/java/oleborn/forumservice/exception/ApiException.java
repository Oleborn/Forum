package oleborn.forumservice.exception;

import org.springframework.http.HttpStatus;

/**
 * Базовое исключение приложения: несёт HTTP-статус, который вернётся клиенту.
 *
 * @author oleborn
 * @since 2026-10-24
 */
public abstract class ApiException extends RuntimeException {

    private final HttpStatus status;

    /**
     * Создаёт исключение с заданным HTTP-статусом.
     *
     * <p>Нюансы использования: статус задаёт наследник и после создания исключения
     * он не меняется — {@link GlobalExceptionHandler} отдаёт его клиенту как есть.
     *
     * @param status  HTTP-статус ответа
     * @param message текст ошибки для клиента
     * @author oleborn
     * @since 2026-10-24
     */
    protected ApiException(
            HttpStatus status,
            String message
    ) {

        super(message);

        this.status = status;
    }

    /**
     * Отдаёт HTTP-статус ошибки.
     *
     * <p>Нюансы использования: используется только обработчиком исключений; в тело
     * ответа статус попадает и числом, и именем константы.
     *
     * @return HTTP-статус ответа
     * @author oleborn
     * @since 2026-10-24
     */
    public HttpStatus getStatus() {

        return status;
    }
}
