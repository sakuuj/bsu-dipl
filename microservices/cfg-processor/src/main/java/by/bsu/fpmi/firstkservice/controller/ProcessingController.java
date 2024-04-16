package by.bsu.fpmi.firstkservice.controller;

import by.bsu.fpmi.firstkservice.dto.CFGRequest;
import by.bsu.fpmi.firstkservice.dto.CFGResponse;
import by.bsu.fpmi.firstkservice.mapper.CFGParser;
import by.bsu.fpmi.firstkservice.model.CFG;
import by.bsu.fpmi.firstkservice.model.OrderedCFG;
import by.bsu.fpmi.firstkservice.model.Symbol;
import by.bsu.fpmi.firstkservice.model.Word;
import by.bsu.fpmi.firstkservice.service.FirstKService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/processor")
public class ProcessingController {
    private final CFGParser cfgParser;

    @GetMapping("/first-k/{k}")
    public CFGResponse findFirstK(@RequestBody @Valid CFGRequest request,
                                             @PathVariable("k") int k) {
        OrderedCFG orderedCfg = cfgParser.fromRequest(request);
        CFG cfg = CFG.builder()
                .definingEquations(orderedCfg.getDefiningEquations())
                .startSymbol(orderedCfg.getStartSymbol())
                .nonTerminals(new HashSet<>(orderedCfg.getNonTerminals()))
                .terminals(new HashSet<>(orderedCfg.getTerminals()))
                .build();


        Map<Symbol, Set<Word>> nonTerminalToSolution = FirstKService.firstK(cfg, k);
        Map<String, Set<String>> nonTerminalToSolutionResponse = new HashMap<>();

        nonTerminalToSolution.forEach((key, value) -> {
            Set<String> collected = value.stream().map(Word::toString).collect(Collectors.toSet());
            nonTerminalToSolutionResponse.put(key.toString(), collected);
        });

        CFGResponse cfgResponse = CFGResponse.builder()
                .nonTerminals(orderedCfg.getNonTerminals()
                        .stream()
                        .map(Symbol::toString)
                        .toList())
                .terminals(orderedCfg.getTerminals()
                        .stream()
                        .map(Symbol::toString)
                        .toList())
                .startSymbol(orderedCfg.getStartSymbol().toString())
                .nonTerminalToSolution(nonTerminalToSolutionResponse)
                .build();

        return cfgResponse;
    }

}
