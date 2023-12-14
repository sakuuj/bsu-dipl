package by.bsu.fpmi.firstkservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class ContextFreeGrammarRequest {
    @NotEmpty
    private Set<Character> nonTerminals;
    @NotEmpty
    private Set<Character> terminals;
    @NotEmpty
    private Set<String> definingEquations;
    @NotNull
    private Character startSymbol;
}
