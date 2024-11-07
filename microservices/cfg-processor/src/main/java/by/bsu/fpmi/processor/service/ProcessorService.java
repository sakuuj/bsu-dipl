package by.bsu.fpmi.processor.service;

import by.bsu.fpmi.processor.dto.CFGRequest;
import by.bsu.fpmi.processor.dto.CFGResponse;
import by.bsu.fpmi.processor.exception.MalformedGrammarException;
import by.bsu.fpmi.processor.model.CFG;
import by.bsu.fpmi.processor.model.OrderedCFG;
import by.bsu.fpmi.processor.model.Symbol;
import by.bsu.fpmi.processor.model.Word;
import by.bsu.fpmi.processor.parser.CFGParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessorService {

    private final CFGParser cfgParser;
    private final NormalizationService normalizationService;
    private final First1Service first1Service;

    public CFGResponse normalizeAndCalculateFirstK(CFGRequest request) {
        OrderedCFG orderedCfg = cfgParser.fromRequest(request);

        CFG cfg = CFG.builder()
                .definingEquations(orderedCfg.getDefiningEquations())
                .startSymbol(orderedCfg.getStartSymbol())
                .nonTerminals(new HashSet<>(orderedCfg.getNonTerminals()))
                .terminals(new HashSet<>(orderedCfg.getTerminals()))
                .build();

        normalizationService.removeUselessNonTerminals(cfg);
        if (cfg.getStartSymbol() == null) {
            throw new MalformedGrammarException("некорректно заданная грамматика, стартовый нетерминал - бесполезный символ " + "(непорождающий)");
        }

        orderedCfg.setDefiningEquations(cfg.getDefiningEquations());
        orderedCfg.getNonTerminals().retainAll(cfg.getNonTerminals());

        log.info(cfg.toString());

        Map<Symbol, Set<Symbol>> nonTerminalToSolution = first1Service.first1(cfg);
        Map<String, Set<String>> nonTerminalToSolutionResponse = new HashMap<>();

        nonTerminalToSolution.forEach((key, value) ->
        {
            Set<String> collected = value.stream().map(Symbol::toString).collect(Collectors.toCollection(LinkedHashSet::new));
            nonTerminalToSolutionResponse.put(key.toString(), collected);
        });

        return CFGResponse.builder()
                .nonTerminals(orderedCfg.getNonTerminals().stream().map(Symbol::toString).toList())
                .terminals(orderedCfg.getTerminals().stream().map(Symbol::toString).toList())
                .startSymbol(orderedCfg.getStartSymbol().toString())
                .nonTerminalToSolution(nonTerminalToSolutionResponse)
                .build();
    }
}
