package by.bsu.fpmi.processor.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class CFGRequest {

    @NotEmpty(message = "множество нетерминалов не может быть пустым")
    private List<String> nonTerminals;

    @NotEmpty(message = "множество терминалов не может быть пустым")
    private List<String> terminals;

    @NotEmpty(message = "множество определяющих уравнений не может быть пустым")
    private List<String> definingEquations;

    @NotEmpty(message = "начальный нетерминал не может быть пустым")
    private String startSymbol;
}
