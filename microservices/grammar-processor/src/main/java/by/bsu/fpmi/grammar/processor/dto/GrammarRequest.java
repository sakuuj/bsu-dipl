package by.bsu.fpmi.grammar.processor.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Builder
public record GrammarRequest(
        @Size(max = 20, message = "мощность множества нетерминалов должна быть <= 20")
        @NotEmpty(message = "множество нетерминалов не может быть пустым")
        List<@Length(max = 15, message = "длина строки символа нетерминала должна быть <= 15") String> nonTerminals,

        @Size(max = 100, message = "мощность множества терминалов должна быть <= 100")
        @NotEmpty(message = "множество терминалов не может быть пустым")
        List<@Length(max = 15, message = "длина строки символа терминала должна быть <= 15") String> terminals,

        @Size(max = 20, message = "мощность множества определяющих уравнений должна быть <= 20")
        @NotEmpty(message = "множество определяющих уравнений не может быть пустым")
        List<@Length(max = 300, message = "длина строки правой части определяющего уравнения должна быть <= 300") String>
        definingEquations,

        @NotEmpty(message = "начальный нетерминал не может быть пустым")
        String startSymbol
) {
}
