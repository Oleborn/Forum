package oleborn.forumservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import oleborn.forumservice.model.dto.response.ErrorResponseDto;
import oleborn.forumservice.model.dto.response.FieldErrorDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.Instant;
import java.util.List;

/**
 * Глобальный обработчик исключений: переводит любое исключение в единый контракт
 * {@link ErrorResponseDto}.
 *
 * <p>Нюансы использования: обработчик ловит и {@link Exception}, поэтому фреймворковые
 * ошибки (неизвестный путь, неподдерживаемый метод или тип содержимого) не должны
 * превращаться в 500 — для них статус берётся из {@link ErrorResponse}.
 *
 * @author oleborn
 * @since 2026-10-24
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает исключения приложения с собственным HTTP-статусом.
     *
     * <p>Нюансы использования: покрывает 404, 403 и 400; статус берётся из исключения,
     * поэтому новый наследник {@link ApiException} не требует правки обработчика.
     *
     * @param exception исключение приложения
     * @param request   текущий запрос
     * @return ответ с телом {@link ErrorResponseDto}
     * @author oleborn
     * @since 2026-10-24
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponseDto> handleApiException(
            ApiException exception,
            HttpServletRequest request
    ) {

        return build(
                exception.getStatus().value(),
                exception.getMessage(),
                null,
                request,
                exception
        );
    }

    /**
     * Обрабатывает ошибки валидации и привязки параметров запроса.
     *
     * <p>Нюансы использования: метод объявлен на {@link BindException}, поэтому
     * покрывает и {@code MethodArgumentNotValidException} для {@code @Valid @RequestBody},
     * и ошибки биндинга {@code @ModelAttribute}-фильтров; ошибки полей попадают
     * в {@code details}.
     *
     * @param exception исключение привязки с результатом валидации
     * @param request   текущий запрос
     * @return ответ 400 с перечнем ошибок полей
     * @author oleborn
     * @since 2026-10-24
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponseDto> handleBindException(
            BindException exception,
            HttpServletRequest request
    ) {

        List<FieldErrorDto> details = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toFieldError)
                .toList();

        return build(
                HttpStatus.BAD_REQUEST.value(),
                "Ошибка валидации запроса",
                details,
                request,
                exception
        );
    }

    /**
     * Обрабатывает несовпадение типа параметра запроса.
     *
     * <p>Нюансы использования: срабатывает на некорректный UUID в пути или
     * query-параметре и на неизвестное значение enum.
     *
     * @param exception исключение приведения типа
     * @param request   текущий запрос
     * @return ответ 400 с именем и значением параметра
     * @author oleborn
     * @since 2026-10-24
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException exception,
            HttpServletRequest request
    ) {

        String message = "Некорректное значение параметра: "
                + exception.getName()
                + "="
                + exception.getValue();

        return build(
                HttpStatus.BAD_REQUEST.value(),
                message,
                null,
                request,
                exception
        );
    }

    /**
     * Обрабатывает отсутствие обязательного заголовка запроса.
     *
     * <p>Нюансы использования: на этом этапе основной сценарий — отсутствующий
     * {@code X-Auth-UserId}, который gateway прокидывает после аутентификации.
     *
     * @param exception исключение отсутствующего заголовка
     * @param request   текущий запрос
     * @return ответ 400 с именем заголовка
     * @author oleborn
     * @since 2026-10-24
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponseDto> handleMissingRequestHeader(
            MissingRequestHeaderException exception,
            HttpServletRequest request
    ) {

        String message = "Отсутствует обязательный заголовок: " + exception.getHeaderName();

        return build(
                HttpStatus.BAD_REQUEST.value(),
                message,
                null,
                request,
                exception
        );
    }

    /**
     * Обрабатывает отсутствие обязательного параметра запроса.
     *
     * <p>Нюансы использования: срабатывает, когда не передан обязательный
     * {@code @RequestParam}, например {@code file} при загрузке вложения.
     *
     * @param exception исключение отсутствующего параметра
     * @param request   текущий запрос
     * @return ответ 400 с именем параметра
     * @author oleborn
     * @since 2026-10-24
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponseDto> handleMissingServletRequestParameter(
            MissingServletRequestParameterException exception,
            HttpServletRequest request
    ) {

        String message = "Отсутствует обязательный параметр: " + exception.getParameterName();

        return build(
                HttpStatus.BAD_REQUEST.value(),
                message,
                null,
                request,
                exception
        );
    }

    /**
     * Обрабатывает нечитаемое тело запроса.
     *
     * <p>Нюансы использования: срабатывает на отсутствующее тело, битый JSON и
     * неизвестное значение enum в теле; конкретную причину клиенту не раскрываем.
     *
     * @param exception исключение разбора тела
     * @param request   текущий запрос
     * @return ответ 400
     * @author oleborn
     * @since 2026-10-24
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpMessageNotReadable(
            HttpMessageNotReadableException exception,
            HttpServletRequest request
    ) {

        return build(
                HttpStatus.BAD_REQUEST.value(),
                "Тело запроса отсутствует или содержит некорректный JSON",
                null,
                request,
                exception
        );
    }

    /**
     * Обрабатывает превышение допустимого размера загружаемого файла.
     *
     * <p>Нюансы использования: лимит задаётся стандартными свойствами
     * {@code spring.servlet.multipart}; ответ 413.
     *
     * @param exception исключение превышения размера
     * @param request   текущий запрос
     * @return ответ 413
     * @author oleborn
     * @since 2026-10-24
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponseDto> handleMaxUploadSizeExceeded(
            MaxUploadSizeExceededException exception,
            HttpServletRequest request
    ) {

        return build(
                HttpStatus.PAYLOAD_TOO_LARGE.value(),
                "Размер загружаемого файла превышает допустимый",
                null,
                request,
                exception
        );
    }

    /**
     * Обрабатывает нарушение ограничений базы данных.
     *
     * <p>Нюансы использования: чаще всего это гонка на уникальном индексе; клиенту
     * отдаётся 409 без деталей, причина пишется в лог.
     *
     * @param exception исключение целостности данных
     * @param request   текущий запрос
     * @return ответ 409
     * @author oleborn
     * @since 2026-10-24
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleDataIntegrityViolation(
            DataIntegrityViolationException exception,
            HttpServletRequest request
    ) {

        log.warn("Нарушение целостности данных: {}", exception.getMostSpecificCause().getMessage());

        return build(
                HttpStatus.CONFLICT.value(),
                "Операция нарушает целостность данных",
                null,
                request,
                exception
        );
    }

    /**
     * Обрабатывает всё, что не попало в остальные методы.
     *
     * <p>Нюансы использования: если исключение реализует {@link ErrorResponse}
     * (неизвестный путь, неподдерживаемый метод, неподдерживаемый тип содержимого),
     * клиенту возвращается его собственный статус; иначе — 500 без деталей, чтобы
     * не раскрывать внутреннее состояние приложения.
     *
     * @param exception необработанное исключение
     * @param request   текущий запрос
     * @return ответ с исходным статусом исключения или 500
     * @author oleborn
     * @since 2026-10-24
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(
            Exception exception,
            HttpServletRequest request
    ) {

        if (exception instanceof ErrorResponse errorResponse) {
            return build(
                    errorResponse.getStatusCode().value(),
                    resolveDetail(
                            errorResponse,
                            exception
                    ),
                    null,
                    request,
                    exception
            );
        }

        return build(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Внутренняя ошибка сервера",
                null,
                request,
                exception
        );
    }

    /**
     * Собирает тело ответа об ошибке и пишет запись в лог.
     *
     * <p>Нюансы использования: 5xx логируются с стектрейсом, 4xx — без него, чтобы
     * журнал не разрастался на клиентских ошибках; статус дублируется числом и именем
     * константы.
     *
     * @param status  HTTP-статус ответа
     * @param message текст ошибки для клиента
     * @param details ошибки полей; {@code null}, если ошибка не про поля
     * @param request текущий запрос
     * @param cause   исходное исключение
     * @return ответ с телом {@link ErrorResponseDto}
     * @author oleborn
     * @since 2026-10-24
     */
    private ResponseEntity<ErrorResponseDto> build(
            int status,
            String message,
            List<FieldErrorDto> details,
            HttpServletRequest request,
            Exception cause
    ) {

        ErrorResponseDto body = new ErrorResponseDto(
                Instant.now(),
                status,
                HttpStatus.valueOf(status).name(),
                message,
                request.getRequestURI(),
                details
        );

        if (status >= HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            log.error(
                    "{} {} -> {}: {}",
                    request.getMethod(),
                    request.getRequestURI(),
                    status,
                    message,
                    cause
            );
        } else {
            log.warn(
                    "{} {} -> {}: {}",
                    request.getMethod(),
                    request.getRequestURI(),
                    status,
                    message
            );
        }

        return ResponseEntity.status(status).body(body);
    }

    /**
     * Достаёт описание проблемы из фреймворкового исключения.
     *
     * <p>Нюансы использования: у {@link ErrorResponse} описание лежит в теле
     * ProblemDetail и может быть {@code null} — тогда берётся сообщение исключения.
     *
     * @param errorResponse исключение, реализующее {@link ErrorResponse}
     * @param exception     исходное исключение
     * @return описание проблемы для клиента
     * @author oleborn
     * @since 2026-10-24
     */
    private String resolveDetail(
            ErrorResponse errorResponse,
            Exception exception
    ) {

        String detail = errorResponse.getBody().getDetail();

        return detail != null ? detail : exception.getMessage();
    }

    /**
     * Переводит ошибку поля в контракт ответа.
     *
     * <p>Нюансы использования: берётся только имя поля и текст сообщения; код ошибки
     * и отклонённое значение клиенту не отдаются.
     *
     * @param fieldError ошибка поля из результата валидации
     * @return ошибка поля в контракте {@link FieldErrorDto}
     * @author oleborn
     * @since 2026-10-24
     */
    private FieldErrorDto toFieldError(FieldError fieldError) {

        return new FieldErrorDto(
                fieldError.getField(),
                fieldError.getDefaultMessage()
        );
    }
}
