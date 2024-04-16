package by.bsu.fpmi.cfg.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageRequest {
    private long pageNumber;
    private long perPageCount;
}
