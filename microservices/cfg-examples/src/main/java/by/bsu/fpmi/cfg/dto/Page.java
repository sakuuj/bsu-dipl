package by.bsu.fpmi.cfg.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@AllArgsConstructor
public class Page<T> {

    private List<T> content;

    private long pageNumber;
    private long totalCount;
    private long perPageCount;

    private boolean isFirst;
    private boolean isLast;

    public <R> Page<R> map(Function<T, R> mappingFunction) {

        List<R> mapped = content.stream().map(mappingFunction).toList();

        return new Page<>(mapped, pageNumber, totalCount, perPageCount, isFirst, isLast);
    }
}
