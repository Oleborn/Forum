package oleborn.forumservice.service;

import oleborn.forumservice.exception.ResourceNotFoundException;
import oleborn.forumservice.model.dto.common.PageResponseDto;
import oleborn.forumservice.model.dto.request.BranchCreateRequestDto;
import oleborn.forumservice.model.dto.request.BranchFilterDto;
import oleborn.forumservice.model.dto.request.BranchUpdateRequestDto;
import oleborn.forumservice.model.dto.response.BranchResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Ветки обсуждений.
 * <p>
 * Актор определяется по {@code authUserId} — идентификатору, который gateway
 * прокидывает в заголовке {@code X-Auth-UserId}. Ролевые ограничения на этом этапе
 * не проверяются: для модерационных операций достаточно, чтобы актор существовал
 * как пользователь форума.
 */
public interface BranchService {

    /**
     * Постраничный список веток с фильтрацией.
     * <p>
     * Фильтры приходят одним объектом {@link BranchFilterDto}, который связывается
     * из query-параметров запроса; непереданный параметр остаётся {@code null}
     * и не накладывает ограничения по соответствующему признаку.
     * <p>
     * Мягко удалённые ветки в выборку не попадают никогда — параметра для их показа
     * у этого метода нет.
     *
     * @param filter   фильтры выборки: раздел, автор, закрепление, закрытие, подстрока
     *                 заголовка
     * @param pageable параметры пагинации и сортировки
     * @return страница веток в контракте {@link PageResponseDto}
     */
    PageResponseDto<BranchResponseDto> getBranches(
            BranchFilterDto filter,
            Pageable pageable
    );

    /**
     * Ветка по идентификатору.
     * <p>
     * Мягко удалённая ветка возвращается как есть — признак удаления присутствует
     * в ответе.
     *
     * @param branchId идентификатор ветки
     * @return ветка
     * @throws ResourceNotFoundException если ветка не найдена
     */
    BranchResponseDto getById(UUID branchId);

    /**
     * Создание ветки вместе со стартовым сообщением.
     * <p>
     * В одной транзакции создаются ветка и её стартовый пост: автором обоих
     * становится актор, {@code content} из запроса уходит в текст стартового поста
     * ({@code parent} у него пустой). В ветке проставляются
     * {@code lastPostId} и {@code lastCommentDate}.
     *
     * @param request    раздел, заголовок ветки и текст стартового сообщения
     * @param authUserId идентификатор актора
     * @return созданная ветка
     * @throws ResourceNotFoundException если раздел или актор не найден
     */
    BranchResponseDto create(
            BranchCreateRequestDto request,
            UUID authUserId
    );

    /**
     * Обновление заголовка ветки.
     *
     * @param branchId   идентификатор ветки
     * @param request    новый заголовок
     * @param authUserId идентификатор актора
     * @return обновлённая ветка
     * @throws ResourceNotFoundException если ветка или актор не найден
     */
    BranchResponseDto update(
            UUID branchId,
            BranchUpdateRequestDto request,
            UUID authUserId
    );

    /**
     * Закрытие ветки — проставляется признак {@code isClosed = true}.
     * <p>
     * {@code reason} в БД не сохраняется: в сущности ветки нет поля под причину.
     * Параметр оставлен в контракте под будущий аудит.
     *
     * @param branchId   идентификатор ветки
     * @param reason     причина закрытия; {@code null} допустим
     * @param authUserId идентификатор актора
     * @return обновлённая ветка
     * @throws ResourceNotFoundException если ветка или актор не найден
     */
    BranchResponseDto close(
            UUID branchId,
            String reason,
            UUID authUserId
    );

    /**
     * Открытие ветки — проставляется признак {@code isClosed = false}.
     * <p>
     * {@code reason} в БД не сохраняется: в сущности ветки нет поля под причину.
     * Параметр оставлен в контракте под будущий аудит.
     *
     * @param branchId   идентификатор ветки
     * @param reason     причина открытия; {@code null} допустим
     * @param authUserId идентификатор актора
     * @return обновлённая ветка
     * @throws ResourceNotFoundException если ветка или актор не найден
     */
    BranchResponseDto open(
            UUID branchId,
            String reason,
            UUID authUserId
    );

    /**
     * Закрепление ветки — проставляется признак {@code isPinned = true}.
     *
     * @param branchId   идентификатор ветки
     * @param authUserId идентификатор актора
     * @return обновлённая ветка
     * @throws ResourceNotFoundException если ветка или актор не найден
     */
    BranchResponseDto pin(
            UUID branchId,
            UUID authUserId
    );

    /**
     * Открепление ветки — проставляется признак {@code isPinned = false}.
     *
     * @param branchId   идентификатор ветки
     * @param authUserId идентификатор актора
     * @return обновлённая ветка
     * @throws ResourceNotFoundException если ветка или актор не найден
     */
    BranchResponseDto unpin(
            UUID branchId,
            UUID authUserId
    );

    /**
     * Мягкое удаление ветки.
     * <p>
     * Проставляются {@code deletedAt} и {@code deletedReason}; строка из БД не
     * удаляется. Операция идемпотентна: повторный вызов для уже удалённой ветки
     * ничего не меняет.
     *
     * @param branchId   идентификатор ветки
     * @param reason     причина удаления; {@code null} допустим
     * @param authUserId идентификатор актора
     * @throws ResourceNotFoundException если ветка или актор не найден
     */
    void softDelete(
            UUID branchId,
            String reason,
            UUID authUserId
    );

    /**
     * Инкремент счётчика просмотров ветки.
     * <p>
     * Выполняется одним UPDATE-запросом без загрузки сущности в память, поэтому
     * конфликтов по оптимистичной блокировке ({@code @Version}) не возникает.
     *
     * @param branchId идентификатор ветки
     * @throws ResourceNotFoundException если ветка не найдена
     */
    void incrementViews(UUID branchId);
}
