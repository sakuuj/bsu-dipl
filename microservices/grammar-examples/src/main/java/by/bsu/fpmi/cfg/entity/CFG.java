package by.bsu.fpmi.cfg.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("grammars")
public class CFG {

    @Id
    private String id;

    private List<String> nonTerminals;

    private List<String> terminals;

    private List<String> definingEquations;

    private String startSymbol;
}
