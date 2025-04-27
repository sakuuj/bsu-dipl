package by.bsu.fpmi.cfg.dto;

import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

@Builder
public record RequestedPage<T>(
        List<T> content,
        int pageNumber,
        int pageSize,
        long totalCount,
        boolean isFirst,
        boolean isLast
) {

    public static<T> RequestedPage<T> fromPage(Page<T> page) {

        return RequestedPage.builder(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalCount(page.getTotalElements())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .build();
    }
    public static <S> RequestedPageBuilder<S> builder(List<S> content) {

        return new RequestedPageBuilder<S>().content(content);
    }

    public <R> RequestedPage<R> map(Function<T, R> mappingFunction) {

        List<R> mappedContent = content.stream().map(mappingFunction).toList();

        return RequestedPage.builder(mappedContent)
                .pageSize(pageSize)
                .pageNumber(pageNumber)
                .totalCount(totalCount)
                .isFirst(isFirst)
                .isLast(isLast)
                .build();
    }
}
