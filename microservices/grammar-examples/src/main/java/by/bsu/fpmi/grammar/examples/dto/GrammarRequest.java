package by.bsu.fpmi.grammar.examples.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.List;

@Builder
public record GrammarRequest(

        @NotBlank(message = "идентификатор грамматики не должен быть пустым")
        String id,
        @NotEmpty(message = "множество нетерминалов не должно быть пустым")
        List<String> nonTerminals,
        @NotEmpty(message = "множество терминалов не должно быть пустым")
        List<String> terminals,
        @NotEmpty(message = "множество определяющих уравнений не должно быть пустым")
        List<String> definingEquations,
        @NotBlank(message = "стартовый символ не должен быть пустым")
        String startSymbol
) {
}
