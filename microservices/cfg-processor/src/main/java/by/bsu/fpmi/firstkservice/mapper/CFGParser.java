package by.bsu.fpmi.firstkservice.mapper;

import by.bsu.fpmi.firstkservice.dto.CFGRequest;
import by.bsu.fpmi.firstkservice.model.OrderedCFG;
import by.bsu.fpmi.firstkservice.model.Symbol;
import by.bsu.fpmi.firstkservice.model.Word;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CFGParser {
    public OrderedCFG fromRequest(CFGRequest request) {

        List<Symbol> nonTerminals = request.getNonTerminals().stream()
                .map(String::strip)
                .map(Symbol::new)
                .collect(Collectors.toCollection(ArrayList::new));

        List<Symbol> terminals = request.getTerminals().stream()
                .map(String::strip)
                .map(Symbol::new)
                .collect(Collectors.toCollection(ArrayList::new));

        Map<Symbol, Set<Word>> nonTerminalsToTransformationOptions = getNonTerminalsToTransformationOptions(
                nonTerminals,
                new HashSet<>(request.getDefiningEquations())
        );

        return OrderedCFG.builder()
                .terminals(terminals)
                .nonTerminals(nonTerminals)
                .definingEquations(nonTerminalsToTransformationOptions)
                .startSymbol(new Symbol(request.getStartSymbol()))
                .build();
    }

    private static Map<Symbol, Set<Word>> getNonTerminalsToTransformationOptions(List<Symbol> reqNonTerminals,
                                                                                      Set<String> reqDefiningEquations) {
        Map<Symbol, Set<Word>> nonTerminalsToTransformationOptions = new HashMap<>();
        reqNonTerminals.forEach(nt -> nonTerminalsToTransformationOptions.put(nt, new HashSet<>()));

        reqDefiningEquations.forEach(equation -> {
            String[] equationSplitByEquals = equation.split("=");
            Symbol nonTerminal = new Symbol(equationSplitByEquals[0].strip());

            Collection<Word> transformationOptions = Arrays.stream(equationSplitByEquals[1].split("\\|"))
                    .map(String::strip)
                    .map(s -> {
                        String[] split = s.split("");
                        return Arrays.stream(split)
                                .map(Symbol::new)
                                .collect(new WordCollector());
                    })
                    .collect(Collectors.toCollection(HashSet::new));

            nonTerminalsToTransformationOptions.get(nonTerminal).addAll(transformationOptions);
        });

        return nonTerminalsToTransformationOptions;
    }
}
