package oleborn.forumservice.service.impl;

import lombok.RequiredArgsConstructor;
import oleborn.forumservice.exception.AccessDeniedOperationException;
import oleborn.forumservice.exception.ResourceNotFoundException;
import oleborn.forumservice.mapper.AttachmentMapper;
import oleborn.forumservice.model.dto.response.AttachmentDownloadResponseDto;
import oleborn.forumservice.model.dto.response.AttachmentResponseDto;
import oleborn.forumservice.model.entity.Attachment;
import oleborn.forumservice.model.entity.ForumUser;
import oleborn.forumservice.model.entity.Post;
import oleborn.forumservice.repository.AttachmentRepository;
import oleborn.forumservice.repository.ForumUserRepository;
import oleborn.forumservice.repository.PostRepository;
import oleborn.forumservice.service.AttachmentService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * Реализация сервиса вложений к сообщениям.
 * Хранение содержимого — мок: метаданные пишутся в БД, байты не сохраняются.
 * Замена на S3 — отдельный этап.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttachmentServiceImpl implements AttachmentService {

    private static final String MOCK_PATH_PREFIX = "mock/";

    private final AttachmentRepository attachmentRepository;
    private final PostRepository postRepository;
    private final ForumUserRepository forumUserRepository;
    private final AttachmentMapper attachmentMapper;

    @Override
    public List<AttachmentResponseDto> getByPost(UUID postId) {

        findPost(postId);

        return attachmentRepository.findByPostId(postId)
                .stream()
                .map(attachmentMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public AttachmentResponseDto upload(
            UUID postId,
            MultipartFile file,
            UUID authUserId
    ) {

        ForumUser actor = findActor(authUserId);

        Post post = findPost(postId);

        checkOwnership(post, actor);

        String fileName = file.getOriginalFilename();

        Attachment attachment = Attachment.builder()
                .post(post)
                .fileName(fileName)
                .filePath(MOCK_PATH_PREFIX + UUID.randomUUID() + "/" + fileName)
                .mimeType(file.getContentType())
                .fileSize(file.getSize())
                .build();

        Attachment saved = attachmentRepository.save(attachment);

        return attachmentMapper.toResponse(saved);
    }

    @Override
    public AttachmentDownloadResponseDto download(UUID attachmentId) {

        Attachment attachment = findAttachment(attachmentId);

        Resource resource = new ByteArrayResource(new byte[0]);

        return attachmentMapper.toDownload(attachment, resource);
    }

    @Override
    @Transactional
    public void delete(
            UUID attachmentId,
            UUID authUserId
    ) {

        ForumUser actor = findActor(authUserId);

        Attachment attachment = findAttachment(attachmentId);

        checkOwnership(attachment.getPost(), actor);

        attachmentRepository.delete(attachment);
    }

    private void checkOwnership(Post post, ForumUser actor) {

        if (!post.getUser().getId().equals(actor.getId())) {
            throw new AccessDeniedOperationException("Нет прав на изменение вложений сообщения: id=" + post.getId());
        }
    }

    private Attachment findAttachment(UUID attachmentId) {

        return attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Вложение не найдено: id=" + attachmentId));
    }

    private Post findPost(UUID postId) {

        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Сообщение не найдено: id=" + postId));
    }

    private ForumUser findActor(UUID authUserId) {

        return forumUserRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Актор не найден: authUserId=" + authUserId));
    }
}
