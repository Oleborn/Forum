package oleborn.forumservice.service;

import oleborn.forumservice.exception.ResourceNotFoundException;
import oleborn.forumservice.model.dto.common.PageResponseDto;
import oleborn.forumservice.model.dto.request.AssignTopicModeratorRequestDto;
import oleborn.forumservice.model.dto.request.TopicRequestDto;
import oleborn.forumservice.model.dto.response.TopicResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Разделы форума.
 * <p>
 * Актор определяется по {@code authUserId} — идентификатору, который gateway
 * прокидывает в заголовке {@code X-Auth-UserId}. Ролевые ограничения на этом этапе
 * не проверяются: для административных операций достаточно, чтобы актор существовал
 * как пользователь форума.
 */
public interface TopicService {

    /**
     * Постраничный список разделов с фильтрацией.
     * <p>
     * Удалённые разделы отсекаются по {@code deletedAt}: при {@code deleted == null}
     * или {@code deleted == false} в выборку попадают только не удалённые разделы,
     * при {@code deleted == true} — только удалённые.
     *
     * @param search   подстрока заголовка без учёта регистра; {@code null} или пустая
     *                 строка — фильтр не применяется
     * @param deleted  признак мягкого удаления; {@code null} равнозначно {@code false}
     * @param pageable параметры пагинации и сортировки
     * @return страница разделов в контракте {@link PageResponseDto}
     */
    PageResponseDto<TopicResponseDto> getTopics(
            String search,
            Boolean deleted,
            Pageable pageable
    );

    /**
     * Раздел по идентификатору.
     * <p>
     * Мягко удалённый раздел возвращается как есть — признак удаления присутствует
     * в ответе.
     *
     * @param topicId идентификатор раздела
     * @return раздел
     * @throws ResourceNotFoundException если раздел не найден
     */
    TopicResponseDto getById(UUID topicId);

    /**
     * Создание раздела.
     * <p>
     * Заголовок, описание и порядок сортировки берутся из запроса. Модератор при
     * создании не назначается — для этого служит {@link #assignModerator}.
     *
     * @param request    данные раздела
     * @param authUserId идентификатор актора
     * @return созданный раздел
     * @throws ResourceNotFoundException если актор не найден
     */
    TopicResponseDto create(
            TopicRequestDto request,
            UUID authUserId
    );

    /**
     * Обновление раздела.
     * <p>
     * Перезаписываются заголовок, описание и порядок сортировки. Модератор и признак
     * удаления не затрагиваются.
     *
     * @param topicId    идентификатор раздела
     * @param request    новые данные раздела
     * @param authUserId идентификатор актора
     * @return обновлённый раздел
     * @throws ResourceNotFoundException если раздел или актор не найден
     */
    TopicResponseDto update(
            UUID topicId,
            TopicRequestDto request,
            UUID authUserId
    );

    /**
     * Назначение модератора раздела.
     *
     * @param topicId    идентификатор раздела
     * @param request    содержит {@code moderatorUserId} — идентификатор пользователя
     *                   форума, назначаемого модератором
     * @param authUserId идентификатор актора
     * @return раздел с назначенным модератором
     * @throws ResourceNotFoundException если раздел, актор или назначаемый
     *                                   пользователь не найден
     */
    TopicResponseDto assignModerator(
            UUID topicId,
            AssignTopicModeratorRequestDto request,
            UUID authUserId
    );

    /**
     * Мягкое удаление раздела.
     * <p>
     * Проставляются {@code deletedAt} и {@code deletedReason}; строка из БД не
     * удаляется. Операция идемпотентна: повторный вызов для уже удалённого раздела
     * ничего не меняет.
     *
     * @param topicId    идентификатор раздела
     * @param reason     причина удаления; {@code null} допустим
     * @param authUserId идентификатор актора
     * @throws ResourceNotFoundException если раздел или актор не найден
     */
    void softDelete(
            UUID topicId,
            String reason,
            UUID authUserId
    );
}
