package oleborn.forumservice.service;

import oleborn.forumservice.exception.AccessDeniedOperationException;
import oleborn.forumservice.exception.ResourceNotFoundException;
import oleborn.forumservice.model.dto.response.AttachmentDownloadResponseDto;
import oleborn.forumservice.model.dto.response.AttachmentResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * Вложения к сообщениям.
 * <p>
 * Хранение содержимого пока мок: в БД пишутся реальные метаданные файла
 * (имя, MIME-тип, размер), а сам файл никуда не сохраняется — в {@code filePath}
 * записывается маркер {@code mock/{uuid}/{fileName}}, при скачивании отдаётся пустое
 * содержимое. Замена на S3 — отдельный этап.
 * <p>
 * Актор определяется по {@code authUserId} — идентификатору, который gateway
 * прокидывает в заголовке {@code X-Auth-UserId}. Управлять вложениями может только
 * автор сообщения.
 */
public interface AttachmentService {

    /**
     * Вложения сообщения.
     * <p>
     * Без пагинации: у одного сообщения ожидается небольшое число файлов.
     *
     * @param postId идентификатор сообщения
     * @return список вложений сообщения в порядке, определённом репозиторием
     * @throws ResourceNotFoundException если сообщение не найдено
     */
    List<AttachmentResponseDto> getByPost(UUID postId);

    /**
     * Загрузка вложения к сообщению.
     * <p>
     * Сохраняются метаданные файла; содержимое пока не сохраняется (см. описание
     * интерфейса). Загружать вложения можно только к своему сообщению.
     *
     * @param postId     идентификатор сообщения
     * @param file       загружаемый файл
     * @param authUserId идентификатор актора
     * @return метаданные сохранённого вложения
     * @throws ResourceNotFoundException   если сообщение или актор не найден
     * @throws AccessDeniedOperationException если актор не является автором сообщения
     */
    AttachmentResponseDto upload(
            UUID postId,
            MultipartFile file,
            UUID authUserId
    );

    /**
     * Скачивание вложения.
     * <p>
     * Возвращаются метаданные и поток содержимого. Пока содержимое пустое: реального
     * хранилища нет, поэтому ответ содержит имя файла, MIME-тип и размер из БД, но
     * нулевой поток.
     *
     * @param attachmentId идентификатор вложения
     * @return метаданные вложения и ресурс с содержимым
     * @throws ResourceNotFoundException если вложение не найдено
     */
    AttachmentDownloadResponseDto download(UUID attachmentId);

    /**
     * Удаление вложения.
     * <p>
     * Запись удаляется физически. Удалять вложения можно только у своего сообщения.
     *
     * @param attachmentId идентификатор вложения
     * @param authUserId   идентификатор актора
     * @throws ResourceNotFoundException   если вложение или актор не найден
     * @throws AccessDeniedOperationException если актор не является автором сообщения,
     *                                     к которому прикреплён файл
     */
    void delete(
            UUID attachmentId,
            UUID authUserId
    );
}
