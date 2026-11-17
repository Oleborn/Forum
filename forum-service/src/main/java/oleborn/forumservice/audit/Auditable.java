package oleborn.forumservice.audit;

import oleborn.forumservice.dictionary.ModerationAction;
import oleborn.forumservice.dictionary.ModerationTargetType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Помечает метод, успешный вызов которого нужно записать в журнал действий.
 *
 * <p>Нюансы использования: аннотацию читает {@link AuditLoggingAspect}; работает только
 * на публичных методах Spring-бинов, вызываемых через прокси, — вызов через {@code this}
 * внутри того же класса не перехватывается. Запись создаётся после нормального возврата
 * метода: если метод бросил исключение, аудит не пишется. Имена параметров из
 * {@link #targetIdParam()}, {@link #actorParam()} и {@link #reasonParam()} сверяются с
 * именами параметров метода, поэтому сборка должна идти с флагом {@code -parameters}
 * (в Spring Boot он включён по умолчанию).
 *
 * @author oleborn
 * @since 2026-11-08
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auditable {

    /**
     * Действие, которое попадает в журнал.
     *
     * <p>Нюансы использования: значение должно соответствовать смыслу операции, потому что
     * по нему фильтруется журнал; тип цели задаётся отдельно в {@link #targetType()}.
     *
     * @return действие, выполненное над объектом
     * @author oleborn
     * @since 2026-11-08
     */
    ModerationAction action();

    /**
     * Тип объекта, над которым выполняется действие.
     *
     * <p>Нюансы использования: для действий над сообщением указывается
     * {@link ModerationTargetType#POST}, для действий над веткой —
     * {@link ModerationTargetType#BRANCH}; поле обязательно, так как в журнале цель
     * хранится полиморфно парой «тип + идентификатор».
     *
     * @return тип объекта модерации
     * @author oleborn
     * @since 2026-11-08
     */
    ModerationTargetType targetType();

    /**
     * Имя параметра метода с идентификатором объекта, над которым выполняется действие.
     *
     * <p>Нюансы использования: параметр обязан иметь тип {@link java.util.UUID}; если имя
     * не совпадёт с сигнатурой, аудит не запишется, а в лог уйдёт ошибка — бизнес-операция
     * при этом выполнится.
     *
     * @return имя параметра метода
     * @author oleborn
     * @since 2026-11-08
     */
    String targetIdParam();

    /**
     * Имя параметра метода с идентификатором актора.
     *
     * <p>Нюансы использования: по умолчанию {@code authUserId} — идентификатор из заголовка
     * {@code X-Auth-UserId}; аспект ищет по нему запись в {@code forum_users}.
     *
     * @return имя параметра метода
     * @author oleborn
     * @since 2026-11-08
     */
    String actorParam() default "authUserId";

    /**
     * Имя параметра метода с причиной действия.
     *
     * <p>Нюансы использования: причина необязательна — если параметра нет или он
     * {@code null}, в журнал попадает {@code null}.
     *
     * @return имя параметра метода
     * @author oleborn
     * @since 2026-11-08
     */
    String reasonParam() default "reason";
}
