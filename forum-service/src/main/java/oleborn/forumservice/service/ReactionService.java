package oleborn.forumservice.service;

import oleborn.forumservice.exception.ResourceNotFoundException;
import oleborn.forumservice.model.dto.request.ReactionRequestDto;
import oleborn.forumservice.model.dto.response.ReactionResponseDto;
import oleborn.forumservice.model.dto.response.ReactionSummaryResponseDto;

import java.util.UUID;

/**
 * Реакции на сообщения.
 * <p>
 * У одного пользователя может быть не более одной реакции на сообщение — это
 * обеспечено уникальным ключом {@code (user_id, post_id)} в БД.
 * <p>
 * Актор определяется по {@code authUserId} — идентификатору, который gateway
 * прокидывает в заголовке {@code X-Auth-UserId}.
 */
public interface ReactionService {

    /**
     * Постановка или смена реакции на сообщение.
     * <p>
     * Если реакции пользователя на это сообщение ещё нет — она создаётся; если есть —
     * у неё перезаписывается тип (upsert). Отдельного метода для смены типа не нужно.
     *
     * @param postId     идентификатор сообщения
     * @param request    тип реакции
     * @param authUserId идентификатор актора
     * @return сохранённая реакция
     * @throws ResourceNotFoundException если сообщение или актор не найден
     */
    ReactionResponseDto react(
            UUID postId,
            ReactionRequestDto request,
            UUID authUserId
    );

    /**
     * Снятие реакции с сообщения.
     * <p>
     * Операция идемпотентна: если реакции нет, ничего не происходит и ошибка не
     * выбрасывается. Сообщение при этом не проверяется на существование.
     *
     * @param postId     идентификатор сообщения
     * @param authUserId идентификатор актора
     * @throws ResourceNotFoundException если актор не найден
     */
    void removeReaction(
            UUID postId,
            UUID authUserId
    );

    /**
     * Сводка реакций на сообщение.
     * <p>
     * Возвращаются агрегированные количества по типам реакций (порядок типов
     * гарантирован) и реакция текущего актора, если он её ставил.
     *
     * @param postId     идентификатор сообщения
     * @param authUserId идентификатор актора
     * @return сводка реакций на сообщение
     * @throws ResourceNotFoundException если сообщение или актор не найден
     */
    ReactionSummaryResponseDto getSummary(
            UUID postId,
            UUID authUserId
    );
}
