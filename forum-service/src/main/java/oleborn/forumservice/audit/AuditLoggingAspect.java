package oleborn.forumservice.audit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oleborn.forumservice.model.entity.ForumUser;
import oleborn.forumservice.model.entity.ModerationAuditLog;
import oleborn.forumservice.repository.ForumUserRepository;
import oleborn.forumservice.repository.ModerationAuditLogRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

/**
 * Аспект журнала действий: пишет запись в {@code moderation_audit_logs} после успешного
 * вызова метода, помеченного {@link Auditable}.
 *
 * <p>Нюансы использования: аспект объявлен с {@link Ordered#HIGHEST_PRECEDENCE}, поэтому
 * находится снаружи транзакции сервиса — запись сохраняется уже после коммита
 * бизнес-операции и откат бизнес-транзакции её не удаляет. Ошибки записи аудита не
 * пробрасываются в вызывающий код: журнал не должен ломать бизнес-операцию, поэтому
 * любая проблема пишется в лог уровнем {@code error}. Полные аргументы и результат
 * вызова попадают только в лог, в таблице хранится структурная часть записи.
 *
 * @author oleborn
 * @since 2026-11-08
 */
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
@Slf4j
public class AuditLoggingAspect {

    private final ModerationAuditLogRepository moderationAuditLogRepository;
    private final ForumUserRepository forumUserRepository;

    /**
     * Выполняет целевой метод и после успешного возврата записывает действие в журнал.
     *
     * <p>Нюансы использования: при исключении в целевом методе запись не создаётся, а
     * исключение пробрасывается дальше без изменений; аргументы читаются по именам
     * параметров метода, поэтому сборка должна идти с флагом {@code -parameters}.
     *
     * @param joinPoint точка соединения с целевым методом
     * @param auditable аннотация целевого метода
     * @return результат целевого метода
     * @throws Throwable исключение целевого метода
     * @author oleborn
     * @since 2026-11-08
     */
    @Around("@annotation(auditable)")
    public Object audit(
            ProceedingJoinPoint joinPoint,
            Auditable auditable
    ) throws Throwable {

        Object result = joinPoint.proceed();

        writeAuditLog(
                joinPoint,
                auditable,
                result
        );

        return result;
    }

    /**
     * Собирает и сохраняет запись журнала по аннотации и фактическим аргументам вызова.
     *
     * <p>Нюансы использования: метод не бросает исключений — при любой ошибке пишется
     * запись в лог, а вызывающий код получает результат целевого метода; если актор не
     * найден по {@code authUserId}, запись не создаётся и пишется предупреждение.
     *
     * @param joinPoint точка соединения с целевым методом
     * @param auditable аннотация целевого метода
     * @param result    результат целевого метода, может быть {@code null}
     * @author oleborn
     * @since 2026-11-08
     */
    private void writeAuditLog(
            ProceedingJoinPoint joinPoint,
            Auditable auditable,
            Object result
    ) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        String[] parameterNames = signature.getParameterNames();
        Object[] arguments = joinPoint.getArgs();

        try {

            UUID authUserId = readUuid(
                    parameterNames,
                    arguments,
                    auditable.actorParam()
            );

            UUID targetId = readUuid(
                    parameterNames,
                    arguments,
                    auditable.targetIdParam()
            );

            String reason = readString(
                    parameterNames,
                    arguments,
                    auditable.reasonParam()
            );

            Optional<ForumUser> actor = forumUserRepository.findByAuthUserId(authUserId);

            if (actor.isEmpty()) {

                log.warn(
                        "Аудит не записан: актор не найден, action={}, method={}",
                        auditable.action(),
                        signature.toShortString()
                );

                return;
            }

            ModerationAuditLog auditLog = ModerationAuditLog.builder()
                    .moderator(actor.get())
                    .targetType(auditable.targetType())
                    .targetId(targetId)
                    .action(auditable.action())
                    .reason(reason)
                    .build();

            moderationAuditLogRepository.save(auditLog);

            log.info(
                    "Аудит: action={}, targetType={}, targetId={}, moderatorId={}, args={}, result={}",
                    auditable.action(),
                    auditable.targetType(),
                    targetId,
                    actor.get().getId(),
                    Arrays.toString(arguments),
                    result
            );
        } catch (RuntimeException exception) {

            log.error(
                    "Аудит не записан: action={}, method={}",
                    auditable.action(),
                    signature.toShortString(),
                    exception
            );
        }
    }

    /**
     * Достаёт значение параметра-идентификатора из аргументов вызова.
     *
     * <p>Нюансы использования: имя параметра обязано быть в сигнатуре метода и иметь тип
     * {@link UUID}; при нарушении выбрасывается {@link IllegalStateException}, которую
     * перехватывает {@link #writeAuditLog(ProceedingJoinPoint, Auditable, Object)} —
     * бизнес-операция при этом выполнится.
     *
     * @param parameterNames имена параметров целевого метода
     * @param arguments      фактические аргументы вызова
     * @param parameterName  имя искомого параметра
     * @return значение параметра
     * @author oleborn
     * @since 2026-11-08
     */
    private UUID readUuid(
            String[] parameterNames,
            Object[] arguments,
            String parameterName
    ) {

        int index = indexOf(
                parameterNames,
                parameterName
        );

        if (index < 0) {
            throw new IllegalStateException("Параметр не найден в сигнатуре метода: " + parameterName);
        }

        Object value = arguments[index];

        if (value instanceof UUID uuid) {
            return uuid;
        }

        throw new IllegalStateException("Параметр не является UUID: " + parameterName);
    }

    /**
     * Достаёт строковое значение параметра из аргументов вызова.
     *
     * <p>Нюансы использования: причина необязательна, поэтому при отсутствии параметра в
     * сигнатуре или при {@code null} возвращается {@code null}; нестроковый тип считается
     * ошибкой конфигурации аннотации.
     *
     * @param parameterNames имена параметров целевого метода
     * @param arguments      фактические аргументы вызова
     * @param parameterName  имя искомого параметра
     * @return значение параметра или {@code null}
     * @author oleborn
     * @since 2026-11-08
     */
    private String readString(
            String[] parameterNames,
            Object[] arguments,
            String parameterName
    ) {

        int index = indexOf(
                parameterNames,
                parameterName
        );

        if (index < 0) {
            return null;
        }

        Object value = arguments[index];

        if (value == null || value instanceof String) {
            return (String) value;
        }

        throw new IllegalStateException("Параметр не является String: " + parameterName);
    }

    /**
     * Ищет индекс параметра по имени в сигнатуре целевого метода.
     *
     * <p>Нюансы использования: отсутствие параметра — не ошибка (возвращается {@code -1}),
     * так как причина действия необязательна; {@link IllegalStateException} бросается только
     * когда имена параметров недоступны из-за сборки без флага {@code -parameters}.
     *
     * @param parameterNames имена параметров целевого метода, могут быть {@code null}
     * @param parameterName  имя искомого параметра
     * @return индекс параметра в массиве аргументов или {@code -1}
     * @author oleborn
     * @since 2026-11-08
     */
    private int indexOf(
            String[] parameterNames,
            String parameterName
    ) {

        if (parameterNames == null) {
            throw new IllegalStateException("Имена параметров недоступны: сборка без флага -parameters");
        }

        for (int index = 0; index < parameterNames.length; index++) {

            if (parameterName.equals(parameterNames[index])) {
                return index;
            }
        }

        return -1;
    }
}
