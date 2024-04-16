package by.bsu.fpmi.cfg.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CFG {

    private long id;

    private String content;

    private LocalDateTime timeCreated;
}
