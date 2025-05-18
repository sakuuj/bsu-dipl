package by.bsu.fpmi.grammar.processor.service;

import by.bsu.fpmi.grammar.processor.dto.ParsingMetadata;
import by.bsu.fpmi.grammar.processor.enums.ParsingStatus;
import by.bsu.fpmi.grammar.processor.exception.MultipleProductionsExistException;
import by.bsu.fpmi.grammar.processor.exception.NotLL1GrammarException;
import by.bsu.fpmi.grammar.processor.model.Grammar;
import by.bsu.fpmi.grammar.processor.model.Symbol;
import by.bsu.fpmi.grammar.processor.model.Word;
import lombok.Builder;
import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.stream.Collectors;

@UtilityClass
public class LL1ParserService {

    @Builder
    public record ParsingTableKey(Symbol nonTerminal, Symbol terminal) {
    }

    @Builder
    private record ProductionToFirstSet(Word production, Set<Symbol> firstSet) {

    }

    public static void validateThatGrammarIsLL1(
            Map<Symbol, Set<Symbol>> first,
            Map<Symbol, Set<Symbol>> follow,
            Grammar grammar
    ) {
        for (var entry : grammar.getDefiningEquations().entrySet()) {

            Symbol nonTerminal = entry.getKey();
            Set<Word> productions = entry.getValue();

            List<ProductionToFirstSet> productionToFirstSetList = productions.stream()
                    .map(production -> ProductionToFirstSet.builder()
                            .firstSet(First1Service.first1OverWord(production, first, grammar.getNonTerminals()))
                            .production(production)
                            .build())
                    .toList();

            checkFirstSetsAreDisjoint(productionToFirstSetList, nonTerminal);
            checkFirstAndFollowSetsAreDisjointIfNeeded(follow, productionToFirstSetList, nonTerminal);
        }
    }

    private static void checkFirstAndFollowSetsAreDisjointIfNeeded(Map<Symbol, Set<Symbol>> follow, List<ProductionToFirstSet> productionToFirstSetList, Symbol nonTerminal) {
        ProductionToFirstSet pairToDeriveEmptySymbolFrom = null;
        for (ProductionToFirstSet pair : productionToFirstSetList) {
            if (pair.firstSet().contains(Symbol.EMPTY_SYMBOL)) {
                pairToDeriveEmptySymbolFrom = pair;
                break;
            }
        }

        if (pairToDeriveEmptySymbolFrom != null) {

            Set<Symbol> followSet = follow.get(nonTerminal);
            for (ProductionToFirstSet pair : productionToFirstSetList) {
                if (!pair.firstSet().contains(Symbol.EMPTY_SYMBOL) && !Collections.disjoint(pair.firstSet(), followSet)) {
                    String message = """
                            Грамматика не относится к классу LL(1) : \
                            несоответствие для нетерминала %s. Множество FIRST продукции %s перескается с множеством FOLLOW нетерминала, \
                            где множество FIRST = %s, FOLLOW = %s.\
                            При этом существует продукция %s, в множестве FIRST которой содержится пустой символ."""
                            .formatted(
                                    nonTerminal,
                                    pair.production(),
                                    pair.firstSet(),
                                    followSet,
                                    pairToDeriveEmptySymbolFrom.production
                            );
                    throw new NotLL1GrammarException(message);
                }
            }
        }
    }

    private static void checkFirstSetsAreDisjoint(List<ProductionToFirstSet> productionToFirstSetList, Symbol nonTerminal) {
        int size = productionToFirstSetList.size();
        for (int i = 0; i < size; i++) {

            ProductionToFirstSet firstPair = productionToFirstSetList.get(i);
            for (int j = i + 1; j < size; j++) {

                ProductionToFirstSet secondPair = productionToFirstSetList.get(j);
                if (!Collections.disjoint(firstPair.firstSet(), secondPair.firstSet())) {
                    String message = """
                            Грамматика не относится к классу LL(1) : \
                            несоответствие для нетерминала %s. \
                            Продукции %s и %s имеют пересекающиеся множества FIRST %s и %s соответственно"""
                            .formatted(
                                    nonTerminal,
                                    firstPair.production(),
                                    secondPair.production(),
                                    firstPair.firstSet,
                                    secondPair.firstSet
                            );
                    throw new NotLL1GrammarException(message);
                }
            }
        }
    }

    public static Map<ParsingTableKey, Word> createPredictiveParsingTable(
            Grammar grammar,
            Map<Symbol, Set<Symbol>> first1Set,
            Map<Symbol, Set<Symbol>> followSet
    ) {
        Map<ParsingTableKey, Word> parsingTable = new HashMap<>();

        Map<Symbol, Set<Word>> definingEquations = grammar.getDefiningEquations();

        for (var entry : definingEquations.entrySet()) {
            Symbol nonTerminal = entry.getKey();
            Set<Word> productionBodies = entry.getValue();
            for (Word productionBody : productionBodies) {

                Set<Symbol> productionBodyFirst1 = First1Service.first1OverWord(productionBody, first1Set, grammar.getNonTerminals());
                for (var terminal : productionBodyFirst1) {
                    addProductionBodyToTable(productionBody, terminal, parsingTable, nonTerminal);
                }

                if (!productionBodyFirst1.contains(Symbol.EMPTY_SYMBOL)) {
                    continue;
                }

                Set<Symbol> nonTerminalFollowSet = followSet.get(nonTerminal);
                for (var terminal : nonTerminalFollowSet) {
                    addProductionBodyToTable(productionBody, terminal, parsingTable, nonTerminal);

                }

                if (nonTerminalFollowSet.contains(Symbol.RESERVED_SYMBOL)
                        && productionBodyFirst1.contains(Symbol.EMPTY_SYMBOL)) {

                    addProductionBodyToTable(productionBody, Symbol.RESERVED_SYMBOL, parsingTable, nonTerminal);
                }
            }
        }

        return parsingTable;
    }

    private static void addProductionBodyToTable(Word productionBody, Symbol terminal, Map<ParsingTableKey, Word> parsingTable, Symbol nonTerminal) {
        var previousValue = parsingTable.put(
                ParsingTableKey.builder()
                        .nonTerminal(nonTerminal)
                        .terminal(terminal)
                        .build(),
                productionBody
        );
        if (previousValue != null && previousValue != productionBody) {
            throw new MultipleProductionsExistException(nonTerminal, terminal, previousValue, productionBody);
        }
    }

    public static ParsingMetadata parseInputUsingTable(List<Symbol> input, Grammar grammar, Map<ParsingTableKey, Word> parsingTable) {

        List<ParsingMetadata.ParsingStep> parsingSteps = new ArrayList<>();

        Queue<Symbol> stack = Collections.asLifoQueue(new ArrayDeque<>());
        stack.add(Symbol.RESERVED_SYMBOL);
        stack.add(grammar.getStartSymbol());

        ParsingMetadata.ParsingStep initialStep = createParsingStep(input, 0, stack, "");
        parsingSteps.add(initialStep);

        int inputIndex = 0;
        Symbol topStackSymbol = stack.element();

        while (!topStackSymbol.equals(Symbol.RESERVED_SYMBOL)) {

            if (topStackSymbol.equals(input.get(inputIndex))) {

                String actionDescription = "Совпадение с %s".formatted(topStackSymbol);

                stack.remove();
                inputIndex++;

                ParsingMetadata.ParsingStep parsingStep = createParsingStep(input, inputIndex, stack, actionDescription);
                parsingSteps.add(parsingStep);

            } else if (grammar.getTerminals().contains(topStackSymbol)) {

                String action = "Символ %s на стеке не совпадает с имеющимся %s"
                        .formatted(topStackSymbol, input.get(inputIndex));
                ParsingMetadata.ParsingStep parsingStep = createParsingStep(input, inputIndex, stack, action);
                parsingSteps.add(parsingStep);

                return ParsingMetadata.builder()
                        .parsingStatus(ParsingStatus.FAILURE)
                        .parsingSteps(parsingSteps)
                        .build();
            } else {
                ParsingTableKey parsingTableKey = ParsingTableKey.builder()
                        .nonTerminal(topStackSymbol)
                        .terminal(input.get(inputIndex))
                        .build();
                Word parsingTableProduction = parsingTable.get(parsingTableKey);
                if (parsingTableProduction == null) {

                    String action = "Продукция не найдена для пары нетерминал %s и терминал %s"
                            .formatted(topStackSymbol, input.get(inputIndex));

                    ParsingMetadata.ParsingStep parsingStep = createParsingStep(input, inputIndex, stack, action);
                    parsingSteps.add(parsingStep);

                    return ParsingMetadata.builder()
                            .parsingStatus(ParsingStatus.FAILURE)
                            .parsingSteps(parsingSteps)
                            .build();

                } else {

                    String actionDescription = "Найдено правило: %s -> %s"
                            .formatted(topStackSymbol, parsingTableProduction);

                    stack.remove();
                    if (!parsingTableProduction.equals(Word.EMPTY_WORD)) {
                        for (int i = parsingTableProduction.size() - 1; i >= 0; i--) {
                            stack.add(parsingTableProduction.getAt(i));
                        }
                    }

                    ParsingMetadata.ParsingStep parsingStep = createParsingStep(input, inputIndex, stack, actionDescription);
                    parsingSteps.add(parsingStep);
                }
            }
            topStackSymbol = stack.element();
        }

        return ParsingMetadata.builder()
                .parsingStatus(ParsingStatus.SUCCESS)
                .parsingSteps(parsingSteps)
                .build();
    }

    private static ParsingMetadata.ParsingStep createParsingStep(List<Symbol> input, int inputIndex, Queue<Symbol> stack, String actionDescription) {
        String unmatchedInput;
        String stackContent;
        unmatchedInput = input.subList(inputIndex, input.size()).stream()
                .map(Objects::toString)
                .collect(Collectors.joining(" "));
        stackContent = stack.stream()
                .map(Object::toString)
                .collect(Collectors.joining(" "));
        return ParsingMetadata.ParsingStep.builder()
                .stackContent(stackContent)
                .unmatchedInput(unmatchedInput)
                .actionDescription(actionDescription)
                .build();
    }
}
