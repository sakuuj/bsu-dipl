package by.bsu.fpmi.grammar.examples.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import org.springframework.data.domain.Pageable;

@Builder
public record PageRequest(
        @PositiveOrZero
        int pageNumber,

        @Positive
        @Max(100)
        int pageSize
) {
    public static PageRequest fromPageable(Pageable pageable) {

        return PageRequest.builder()
                .pageNumber(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .build();
    }
}
