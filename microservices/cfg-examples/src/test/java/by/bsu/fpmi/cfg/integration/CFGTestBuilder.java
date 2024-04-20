package by.bsu.fpmi.cfg.integration;

import by.bsu.fpmi.cfg.dto.CFGResponse;
import by.bsu.fpmi.cfg.entity.CFG;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

@Data
@With
@Builder
@NoArgsConstructor(staticName = "newCFG")
@AllArgsConstructor
public class CFGTestBuilder {
    private long id = 10L;

    private String content = """
            {
                "startSymbol" : "R",
                "nonTerminals": [
                    "E", "T", "R", "S", "L"
                ],
                "terminals": [
                    "+", "-", "a", "i", "(", ")", "#", "_"
                ],
                "definingEquations": [
                    "L = LSSLT | LTR | S+",
                    "S = S- | TRS",
                    "E = TR",
                    "R = +TR|-TR| _",
                    "T = a|i|(E)|-TR|+T#T"
                ]
            }
            """;

    private LocalDateTime localDateTime = LocalDateTime.of(
            LocalDate.of(2024, Month.APRIL, 15),
            LocalTime.of(11, 11, 11)
    );

    public CFG build() {
        return new CFG(id, content, localDateTime);
    }
}
