package oleborn.forumservice.model.dto.request;

import oleborn.forumservice.dictionary.ModerationAction;
import oleborn.forumservice.dictionary.ModerationTargetType;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.util.UUID;

/**
 * Фильтры выборки журнала действий модераторов.
 * <p>
 * Связывается из query-параметров через {@code @ModelAttribute}. Непереданный
 * параметр остаётся {@code null} — соответствующий фильтр не применяется.
 * Период задаётся в ISO-8601 ({@code yyyy-MM-ddTHH:mm:ssZ}).
 */
public record ModerationAuditLogFilterDto(
        ModerationTargetType targetType,
        UUID targetId,
        UUID moderatorId,
        ModerationAction action,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to
) {
}
