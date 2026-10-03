package oleborn.forumservice.service;

import oleborn.forumservice.exception.ResourceNotFoundException;
import oleborn.forumservice.model.dto.common.PageResponseDto;
import oleborn.forumservice.model.dto.request.ModerationAuditLogFilterDto;
import oleborn.forumservice.model.dto.response.ModerationAuditLogResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Журнал действий модераторов.
 * <p>
 * Сервис работает только на чтение: записи в журнал пока не добавляются, это
 * отдельный этап. Актор определяется по {@code authUserId} — идентификатору, который
 * gateway прокидывает в заголовке {@code X-Auth-UserId}; ролевые ограничения на
 * доступ к журналу не проверяются.
 */
public interface ModerationAuditLogService {

    /**
     * Постраничный список записей журнала с фильтрацией.
     * <p>
     * Фильтры приходят одним объектом {@link ModerationAuditLogFilterDto}, который
     * связывается из query-параметров запроса; непереданный параметр остаётся
     * {@code null} и не накладывает ограничения по соответствующему признаку.
     * <p>
     * Период задаётся включительно: {@code from} — нижняя граница {@code createdAt},
     * {@code to} — верхняя.
     *
     * @param authUserId идентификатор актора
     * @param filter     фильтры выборки: тип и идентификатор цели, модератор, действие,
     *                   период
     * @param pageable   параметры пагинации и сортировки
     * @return страница записей журнала в контракте {@link PageResponseDto}
     * @throws ResourceNotFoundException если актор не найден
     */
    PageResponseDto<ModerationAuditLogResponseDto> getAuditLog(
            UUID authUserId,
            ModerationAuditLogFilterDto filter,
            Pageable pageable
    );
}
