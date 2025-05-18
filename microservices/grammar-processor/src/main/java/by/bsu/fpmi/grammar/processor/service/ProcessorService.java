package by.bsu.fpmi.grammar.processor.service;

import by.bsu.fpmi.grammar.processor.dto.*;
import by.bsu.fpmi.grammar.processor.exception.MalformedGrammarException;
import by.bsu.fpmi.grammar.processor.mapper.ToDtoGrammarMapper;
import by.bsu.fpmi.grammar.processor.mapper.ToEntityGrammarMapper;
import by.bsu.fpmi.grammar.processor.model.Grammar;
import by.bsu.fpmi.grammar.processor.model.Symbol;
import by.bsu.fpmi.grammar.processor.model.Word;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessorService {

    private final ToDtoGrammarMapper toDtoGrammarMapper;
    private final ToEntityGrammarMapper toEntityGrammarMapper;

    public First1Response normalizeAndCalculateFirst1(GrammarRequest request) {

        Grammar grammar = getNormalizedGrammar(request);

        log.info(grammar.toString());

        Map<Symbol, Set<Symbol>> first1Map = First1Service.first1(grammar);

        return toDtoGrammarMapper.toFirst1Response(grammar, first1Map);
    }

    public FollowResponse normalizeAndCalculateFollow(GrammarRequest request) {

        Grammar grammar = getNormalizedGrammar(request);

        log.info(grammar.toString());

        Map<Symbol, Set<Symbol>> first1Map = First1Service.first1(grammar);
        Map<Symbol, Set<Symbol>> followMap = FollowService.follow(grammar, first1Map);

        return toDtoGrammarMapper.toFollowResponse(grammar, first1Map, followMap);
    }

    private Grammar getNormalizedGrammar(GrammarRequest request) {
        Grammar grammar = toEntityGrammarMapper.toEntity(request);

        NormalizationService.removeUselessNonTerminals(grammar);

        if (grammar.getStartSymbol() == null) {
            throw new MalformedGrammarException("некорректно заданная грамматика, стартовый нетерминал - бесполезный символ " + "(непорождающий)");
        }
        return grammar;
    }

    public GrammarResponse retainGeneratingNonTerminals(GrammarRequest request) {

        Grammar grammar = toEntityGrammarMapper.toEntity(request);

        NormalizationService.retainGeneratingNonTerminals(grammar);

        return toDtoGrammarMapper.toGrammarResponse(grammar);
    }

    public GrammarResponse retainReachableNonTerminals(GrammarRequest request) {

        Grammar grammar = toEntityGrammarMapper.toEntity(request);

        NormalizationService.retainReachableNonTerminals(grammar);

        return toDtoGrammarMapper.toGrammarResponse(grammar);
    }

    public ParsingResponse parseInput(ParsingRequest request) {

        Grammar grammar = toEntityGrammarMapper.toEntity(request.grammarRequest());
        System.out.println(grammar);
        NormalizationService.removeUselessNonTerminals(grammar);
        System.out.println(grammar);
        List<Symbol> tokenizedText = new ArrayList<>(LexicalAnalyzerService.tokenizeText(
                request.text(),
                grammar.getNonTerminals(),
                grammar.getTerminals()
        ));
        tokenizedText.add(Symbol.RESERVED_SYMBOL);

        Map<Symbol, Set<Symbol>> first1 = First1Service.first1(grammar);
        Map<Symbol, Set<Symbol>> follow = FollowService.follow(grammar, first1);

        LL1ParserService.validateThatGrammarIsLL1(first1, follow, grammar);

        Map<LL1ParserService.ParsingTableKey, Word> parsingTable = LL1ParserService
                .createPredictiveParsingTable(grammar, first1, follow);
        ParsingMetadata parsingMetadata = LL1ParserService.parseInputUsingTable(tokenizedText, grammar, parsingTable);

        GrammarResponse grammarResponse = toDtoGrammarMapper.toGrammarResponse(grammar);
        return ParsingResponse.builder()
                .grammarResponse(grammarResponse)
                .parsingMetadata(parsingMetadata)
                .build();
    }
}
