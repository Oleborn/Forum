package oleborn.forumservice.mapper;

import oleborn.forumservice.model.dto.common.PageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * Обёртка страницы Spring Data в контракт API.
 */
@Component
public class PageResponseMapper {

    public <T> PageResponseDto<T> toResponse(Page<T> page) {

        return new PageResponseDto<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }
}
