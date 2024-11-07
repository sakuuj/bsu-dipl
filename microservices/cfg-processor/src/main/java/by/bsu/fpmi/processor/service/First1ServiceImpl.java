package by.bsu.fpmi.processor.service;

import by.bsu.fpmi.processor.model.CFG;
import by.bsu.fpmi.processor.model.Symbol;
import by.bsu.fpmi.processor.model.Word;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class First1ServiceImpl implements First1Service {

    @Override
    public Set<Symbol> first1OverWord(Word word, Map<Symbol, Set<Symbol>> grammarFirst1, Set<Symbol> terminals) {

        Set<Symbol> result = new HashSet<>();

        for (Symbol symbol : word) {

            if (terminals.contains(symbol)) {
                result.add(symbol);
                return result;
            }

            Set<Symbol> symbolFirst1 = grammarFirst1.get(symbol);

            result.addAll(symbolFirst1);

            if (symbolFirst1.contains(Symbol.EMPTY_SYMBOL)) {

                result.remove(Symbol.EMPTY_SYMBOL);

            } else {
                return result;
            }
        }

        return result;
    }

    @Override
    public Map<Symbol, Set<Symbol>> first1(CFG cfg) {

        Set<Symbol> nonTerminals = cfg.getNonTerminals();
        Map<Symbol, Set<Word>> definingEquations = cfg.getDefiningEquations();

        Map<Symbol, Set<Word>> currResult = new HashMap<>();
        Map<Symbol, Set<Word>> prevResult = new HashMap<>();

        for (Symbol nt : nonTerminals) {
            currResult.put(nt, new HashSet<>());
            prevResult.put(nt, new HashSet<>());
        }

        while (true) {
            for (Symbol nt : nonTerminals) {
                iterateOnNonTerminal(nt, nonTerminals, definingEquations.get(nt), currResult, prevResult);
            }

            if (prevResult.equals(currResult)) {
                break;
            }

            for (Symbol nt : nonTerminals) {
                prevResult.get(nt).addAll(currResult.get(nt));
            }

        }

        return new HashMap<>(currResult.entrySet().stream()
                .flatMap(e -> {
                    Set<Word> words = e.getValue();
                    return words.stream()
                            .map(w -> w.getAt(0))
                            .map(s -> Map.entry(e.getKey(), s));
                })
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.mapping(Map.Entry::getValue, Collectors.toSet()))));
    }

    private static void iterateOnNonTerminal(
            Symbol nonTerminal,
            Set<Symbol> nonTerminals,
            Set<Word> defEquationOptions,
            Map<Symbol, Set<Word>> currResult,
            Map<Symbol, Set<Word>> prevResult
    ) {
        for (Word option : defEquationOptions) {

            if (option.length() == 1 && option.getAt(0).equals(nonTerminal)) {
                continue;
            }

            boolean hasConcatWithEmptySet = false;

            for (Symbol symbol : option) {

                if (!nonTerminals.contains(symbol)) {
                    continue;
                }

                if (currResult.get(symbol).isEmpty()) {
                    hasConcatWithEmptySet = true;
                    break;
                }
            }

            if (hasConcatWithEmptySet) {
                continue;
            }

            Word derivation = new Word();

            int size = option.size();
            for (int i = 0; i < size; i++) {

                Symbol symbol = option.getAt(i);

                if (!nonTerminals.contains(symbol)) {
                    derivation.append(symbol);

                    if (derivation.length() == 1 || i == size - 1) {
                        currResult.get(nonTerminal).add(derivation);
                        break;
                    }
                    continue;
                }

                iterateOnNonTerminalRecursive(
                        nonTerminal,
                        nonTerminals,
                        currResult,
                        prevResult,
                        option,
                        derivation,
                        symbol,
                        i
                );

            }
        }

    }


    private static void iterateOnNonTerminalRecursive(
            Symbol nonTerminal,
            Set<Symbol> nonTerminals,
            Map<Symbol, Set<Word>> currResult,
            Map<Symbol, Set<Word>> prevResult,
            Word currentOption,
            Word currentlyDerived,
            Symbol currentNonTerminal,
            int currentSymbolIndex
    ) {

        Set<Word> availableDerivations = prevResult.get(currentNonTerminal);

        for (Word ad : availableDerivations) {

            Word derivation = new Word(currentlyDerived);

            derivation.concat(ad);

            if (derivation.length() >= 1) {
                currResult.get(nonTerminal).add(derivation.subWord(0, 1));
                continue;
            }

            int size = currentOption.size();
            if (currentSymbolIndex + 1 == size) {
                currResult.get(nonTerminal).add(derivation);
                continue;
            }

            for (int i = currentSymbolIndex + 1; i < size; i++) {

                Symbol symbol = currentOption.getAt(i);

                if (!nonTerminals.contains(symbol)) {
                    derivation.append(symbol);

                    if (derivation.length() == 1 || i == size - 1) {
                        currResult.get(nonTerminal).add(derivation);
                    }
                    continue;
                }

                iterateOnNonTerminalRecursive(nonTerminal,
                        nonTerminals,
                        currResult,
                        prevResult,
                        currentOption,
                        derivation,
                        symbol,
                        i);

            }
        }
    }
}
