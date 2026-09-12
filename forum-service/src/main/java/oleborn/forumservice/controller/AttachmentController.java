package oleborn.forumservice.controller;

import lombok.RequiredArgsConstructor;
import oleborn.forumservice.model.dto.response.AttachmentDownloadResponseDto;
import oleborn.forumservice.model.dto.response.AttachmentResponseDto;
import oleborn.forumservice.service.AttachmentService;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @GetMapping("/posts/{postId}/attachments")
    public ResponseEntity<List<AttachmentResponseDto>> getByPost(@PathVariable UUID postId) {

        return ResponseEntity.ok(attachmentService.getByPost(postId));
    }

    @PostMapping(value = "/posts/{postId}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AttachmentResponseDto> upload(
            @PathVariable UUID postId,
            @RequestParam("file") MultipartFile file,
            @RequestHeader("X-Auth-UserId") UUID authUserId
    ) {

        AttachmentResponseDto created = attachmentService.upload(
                postId,
                file,
                authUserId
        );

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{attachmentId}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/attachments/{attachmentId}/download")
    public ResponseEntity<Resource> download(@PathVariable UUID attachmentId) {

        AttachmentDownloadResponseDto download = attachmentService.download(attachmentId);

        ContentDisposition disposition = ContentDisposition.attachment()
                .filename(download.fileName(), StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .contentType(MediaType.parseMediaType(download.mimeType()))
                .contentLength(download.contentLength())
                .body(download.resource());
    }

    @DeleteMapping("/attachments/{attachmentId}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID attachmentId,
            @RequestHeader("X-Auth-UserId") UUID authUserId
    ) {

        attachmentService.delete(attachmentId, authUserId);

        return ResponseEntity.noContent().build();
    }
}
