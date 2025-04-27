package by.bsu.fpmi.processor.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.List;

@Builder
public record GrammarRequest(
        @NotEmpty(message = "множество нетерминалов не может быть пустым")
        List<String> nonTerminals,

        @NotEmpty(message = "множество терминалов не может быть пустым")
        List<String> terminals,

        @NotEmpty(message = "множество определяющих уравнений не может быть пустым")
        List<String> definingEquations,

        @NotEmpty(message = "начальный нетерминал не может быть пустым")
        String startSymbol
) {
}
