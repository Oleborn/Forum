package oleborn.forumservice.service;

import oleborn.forumservice.exception.ResourceNotFoundException;
import oleborn.forumservice.model.dto.common.PageResponseDto;
import oleborn.forumservice.model.dto.response.SubscriptionResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Подписки пользователя на ветки.
 * <p>
 * Повторная подписка на одну ветку невозможна: в БД уникальный ключ
 * {@code (user_id, branch_id)}.
 * <p>
 * Актор определяется по {@code authUserId} — идентификатору, который gateway
 * прокидывает в заголовке {@code X-Auth-UserId}.
 */
public interface SubscriptionService {

    /**
     * Постраничный список подписок актора.
     *
     * @param authUserId идентификатор актора
     * @param topicId    опциональное ограничение по разделу, к которому относится
     *                   ветка подписки; {@code null} — по всем разделам
     * @param pageable   параметры пагинации и сортировки
     * @return страница подписок в контракте {@link PageResponseDto}
     * @throws ResourceNotFoundException если актор не найден
     */
    PageResponseDto<SubscriptionResponseDto> getMySubscriptions(
            UUID authUserId,
            UUID topicId,
            Pageable pageable
    );

    /**
     * Подписка на ветку.
     * <p>
     * Операция идемпотентна: если актор уже подписан на эту ветку, возвращается
     * существующая подписка без создания дубликата.
     *
     * @param branchId   идентификатор ветки
     * @param authUserId идентификатор актора
     * @return подписка
     * @throws ResourceNotFoundException если ветка или актор не найден
     */
    SubscriptionResponseDto subscribe(
            UUID branchId,
            UUID authUserId
    );

    /**
     * Отписка от ветки.
     * <p>
     * Операция идемпотентна: если подписки нет, ничего не происходит и ошибка не
     * выбрасывается.
     *
     * @param branchId   идентификатор ветки
     * @param authUserId идентификатор актора
     * @throws ResourceNotFoundException если актор не найден
     */
    void unsubscribe(
            UUID branchId,
            UUID authUserId
    );
}
