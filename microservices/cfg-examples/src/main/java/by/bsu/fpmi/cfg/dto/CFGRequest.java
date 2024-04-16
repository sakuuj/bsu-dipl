package by.bsu.fpmi.cfg.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class CFGRequest {

    @NotEmpty
    private List<String> nonTerminals;

    @NotEmpty
    private List<String> terminals;

    @NotEmpty
    private List<String> definingEquations;

    @NotNull
    private String startSymbol;
}
