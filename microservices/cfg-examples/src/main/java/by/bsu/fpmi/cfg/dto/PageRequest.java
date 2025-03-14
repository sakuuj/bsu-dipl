package by.bsu.fpmi.cfg.dto;

import lombok.Builder;
import org.springframework.data.domain.Pageable;

@Builder
public record PageRequest(int pageNumber, int pageSize) {

    public static PageRequest fromPageable(Pageable pageable) {

        return PageRequest.builder()
                .pageNumber(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .build();
    }
}
