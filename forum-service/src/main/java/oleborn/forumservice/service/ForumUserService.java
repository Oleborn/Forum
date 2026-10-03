package oleborn.forumservice.service;

import oleborn.forumservice.exception.ResourceNotFoundException;
import oleborn.forumservice.model.dto.common.PageResponseDto;
import oleborn.forumservice.model.dto.request.BanUserRequestDto;
import oleborn.forumservice.model.dto.request.ChangeUserRoleRequestDto;
import oleborn.forumservice.model.dto.request.ForumUserFilterDto;
import oleborn.forumservice.model.dto.response.ForumUserResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Пользователи форума.
 * <p>
 * Записи {@code forum_users} — локальная проекция пользователей из Auth и Profile
 * сервисов; считаются уже существующими, синхронизация через Feign появится отдельным
 * этапом.
 * <p>
 * Актор определяется по {@code authUserId} — идентификатору, который gateway
 * прокидывает в заголовке {@code X-Auth-UserId}. Ролевые ограничения на этом этапе
 * не проверяются: для блокировки и смены роли достаточно, чтобы актор существовал.
 */
public interface ForumUserService {

    /**
     * Профиль текущего актора.
     *
     * @param authUserId идентификатор актора
     * @return профиль пользователя форума
     * @throws ResourceNotFoundException если пользователь с таким {@code authUserId}
     *                                   не найден
     */
    ForumUserResponseDto getMe(UUID authUserId);

    /**
     * Постраничный список пользователей с фильтрацией.
     * <p>
     * Фильтры приходят одним объектом {@link ForumUserFilterDto}, который связывается
     * из query-параметров запроса; непереданный параметр остаётся {@code null}
     * и не накладывает ограничения по соответствующему признаку.
     * <p>
     * Блокировка определяется по непустому {@code bannedUntil}: при
     * {@code banned == true} возвращаются заблокированные, при {@code false} — не
     * заблокированные. Уже истёкшие сроки блокировки по времени не пересчитываются.
     *
     * @param filter   фильтры выборки: подстрока имени, роль, признак блокировки
     * @param pageable параметры пагинации и сортировки
     * @return страница пользователей в контракте {@link PageResponseDto}
     */
    PageResponseDto<ForumUserResponseDto> getUsers(
            ForumUserFilterDto filter,
            Pageable pageable
    );

    /**
     * Профиль пользователя по идентификатору записи форума.
     *
     * @param userId идентификатор записи {@code forum_users}
     * @return профиль пользователя форума
     * @throws ResourceNotFoundException если пользователь не найден
     */
    ForumUserResponseDto getById(UUID userId);

    /**
     * Блокировка пользователя.
     * <p>
     * Проставляются {@code bannedUntil} и {@code banReason}. Повторный вызов
     * перезаписывает срок и причину, то есть продлевает блокировку.
     *
     * @param userId     идентификатор записи {@code forum_users}
     * @param request    срок блокировки и причина
     * @param authUserId идентификатор актора
     * @return обновлённый профиль пользователя
     * @throws ResourceNotFoundException если пользователь или актор не найден
     */
    ForumUserResponseDto banUser(
            UUID userId,
            BanUserRequestDto request,
            UUID authUserId
    );

    /**
     * Снятие блокировки — {@code bannedUntil} и {@code banReason} очищаются.
     * <p>
     * Операция идемпотентна: для не заблокированного пользователя ничего не меняется.
     *
     * @param userId     идентификатор записи {@code forum_users}
     * @param authUserId идентификатор актора
     * @return обновлённый профиль пользователя
     * @throws ResourceNotFoundException если пользователь или актор не найден
     */
    ForumUserResponseDto unbanUser(
            UUID userId,
            UUID authUserId
    );

    /**
     * Смена роли пользователя.
     *
     * @param userId     идентификатор записи {@code forum_users}
     * @param request    новая роль
     * @param authUserId идентификатор актора
     * @return обновлённый профиль пользователя
     * @throws ResourceNotFoundException если пользователь или актор не найден
     */
    ForumUserResponseDto changeRole(
            UUID userId,
            ChangeUserRoleRequestDto request,
            UUID authUserId
    );
}
