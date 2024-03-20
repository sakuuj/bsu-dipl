package by.bsu.fpmi.firstkservice.mapper;

import by.bsu.fpmi.firstkservice.dto.ContextFreeGrammarRequest;
import by.bsu.fpmi.firstkservice.model.ContextFreeGrammar;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Component
public class ContextFreeGrammarMapper {
    public ContextFreeGrammar fromRequest(ContextFreeGrammarRequest request) {
        Map<Character, Set<String>> nonTerminalsToTransformationOptions = getNonTerminalsToTransformationOptions(
                request.getNonTerminals(),
                request.getDefiningEquations()
        );

        return ContextFreeGrammar.builder()
                .terminals(request.getTerminals())
                .nonTerminalsToTransformationOptions(nonTerminalsToTransformationOptions)
                .startSymbol(request.getStartSymbol())
                .build();
    }

    private static Map<Character, Set<String>> getNonTerminalsToTransformationOptions(Set<Character> reqNonTerminals,
                                                                                      Set<String> reqDefiningEquations) {
        Map<Character, Set<String>> nonTerminalsToTransformationOptions = new LinkedHashMap<>();

        reqNonTerminals.forEach(nt -> nonTerminalsToTransformationOptions.put(nt, new LinkedHashSet<>()));
        reqDefiningEquations.forEach(equation -> {
            String[] equationSplitByEquals = equation.split("=");
            Character terminal = equationSplitByEquals[0].strip().charAt(0);

            Collection<String> transformationOptions = Arrays.stream(equationSplitByEquals[1].split("\\|"))
                    .map(String::strip)
                    .toList();

            nonTerminalsToTransformationOptions.get(terminal).addAll(transformationOptions);
        });

        return nonTerminalsToTransformationOptions;
    }
}
