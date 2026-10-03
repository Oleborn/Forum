package oleborn.forumservice.service;

import oleborn.forumservice.exception.AccessDeniedOperationException;
import oleborn.forumservice.exception.ResourceNotFoundException;
import oleborn.forumservice.model.dto.common.PageResponseDto;
import oleborn.forumservice.model.dto.request.PostCreateRequestDto;
import oleborn.forumservice.model.dto.request.PostUpdateRequestDto;
import oleborn.forumservice.model.dto.response.PostResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Сообщения: стартовые посты веток и комментарии.
 * <p>
 * Вложенность ограничена одним уровнем: стартовое сообщение ветки имеет пустой
 * {@code parentId}, комментарий ссылается на стартовое сообщение.
 * <p>
 * Актор определяется по {@code authUserId} — идентификатору, который gateway
 * прокидывает в заголовке {@code X-Auth-UserId}. Изменять и удалять сообщение может
 * только его автор: ролевые ограничения на этом этапе не проверяются.
 */
public interface PostService {

    /**
     * Стартовое сообщение ветки.
     *
     * @param branchId идентификатор ветки
     * @return стартовое сообщение ветки ({@code parentId == null})
     * @throws ResourceNotFoundException если стартовое сообщение не найдено
     */
    PostResponseDto getStarter(UUID branchId);

    /**
     * Постраничный список комментариев ветки.
     * <p>
     * Возвращаются только комментарии (сообщения с непустым {@code parentId});
     * стартовое сообщение в выборку не попадает. Мягко удалённые комментарии
     * отсекаются.
     *
     * @param branchId идентификатор ветки
     * @param pageable параметры пагинации и сортировки
     * @return страница комментариев в контракте {@link PageResponseDto}
     */
    PageResponseDto<PostResponseDto> getComments(
            UUID branchId,
            Pageable pageable
    );

    /**
     * Постраничный список сообщений актора.
     * <p>
     * Возвращаются и стартовые сообщения, и комментарии автора. Мягко удалённые
     * сообщения отсекаются.
     *
     * @param authUserId идентификатор актора
     * @param branchId   опциональное ограничение по ветке; {@code null} — по всем веткам
     * @param pageable   параметры пагинации и сортировки
     * @return страница сообщений актора в контракте {@link PageResponseDto}
     * @throws ResourceNotFoundException если актор не найден
     */
    PageResponseDto<PostResponseDto> getMyPosts(
            UUID authUserId,
            UUID branchId,
            Pageable pageable
    );

    /**
     * Создание комментария к ветке.
     * <p>
     * Автором становится актор. Если указан {@code parentId}, родитель должен быть
     * стартовым сообщением той же ветки — вложенные комментарии и ссылки на чужие
     * ветки запрещены. После сохранения в ветке обновляются {@code lastPostId}
     * и {@code lastCommentDate}.
     *
     * @param request    ветка, опциональный родитель и текст сообщения
     * @param authUserId идентификатор актора
     * @return созданное сообщение
     * @throws ResourceNotFoundException если ветка, актор или родительское сообщение
     *                                   не найдены
     * @throws IllegalArgumentException  если родитель относится к другой ветке или сам
     *                                   является комментарием
     */
    PostResponseDto createComment(
            PostCreateRequestDto request,
            UUID authUserId
    );

    /**
     * Редактирование сообщения.
     * <p>
     * Перезаписывается текст и проставляется {@code editedAt}.
     *
     * @param postId     идентификатор сообщения
     * @param request    новый текст
     * @param authUserId идентификатор актора
     * @return обновлённое сообщение
     * @throws ResourceNotFoundException   если сообщение или актор не найден
     * @throws AccessDeniedOperationException если актор не является автором сообщения
     */
    PostResponseDto update(
            UUID postId,
            PostUpdateRequestDto request,
            UUID authUserId
    );

    /**
     * Мягкое удаление сообщения.
     * <p>
     * Проставляются {@code deletedAt} и {@code deletedReason}; строка из БД не
     * удаляется. Операция идемпотентна: повторный вызов для уже удалённого сообщения
     * ничего не меняет.
     *
     * @param postId     идентификатор сообщения
     * @param reason     причина удаления; {@code null} допустим
     * @param authUserId идентификатор актора
     * @throws ResourceNotFoundException   если сообщение или актор не найден
     * @throws AccessDeniedOperationException если актор не является автором сообщения
     */
    void softDelete(
            UUID postId,
            String reason,
            UUID authUserId
    );
}
