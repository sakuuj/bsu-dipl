package by.bsu.fpmi.processor.mapper;

import by.bsu.fpmi.processor.dto.CFGRequest;
import by.bsu.fpmi.processor.exception.MalformedGrammarException;
import by.bsu.fpmi.processor.model.CFG;
import by.bsu.fpmi.processor.model.Symbol;
import by.bsu.fpmi.processor.model.Word;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class ToEntityCfgMapperImpl implements ToEntityCfgMapper {

    @Override
    public CFG toEntity(CFGRequest request) {

        Pattern whiteSpacePattern = Pattern.compile("\\s");

        LinkedHashSet<Symbol> nonTerminals = mapNonTerminals(request.nonTerminals(), whiteSpacePattern);

        Symbol startSymbol = mapStartSymbol(request.startSymbol(), nonTerminals);

        LinkedHashSet<Symbol> terminals = mapTerminals(request.terminals(), whiteSpacePattern);

        if (!Collections.disjoint(terminals, nonTerminals)) {
            throw new MalformedGrammarException("множества терминалов и нетерминалов не должны пересекаться");
        }

        Map<Symbol, Set<Word>> definingEquations = mapDefiningEquations(request.definingEquations(), nonTerminals, terminals);

        return CFG.builder()
                .terminals(terminals)
                .nonTerminals(nonTerminals)
                .definingEquations(definingEquations)
                .startSymbol(startSymbol)
                .build();
    }

    private static LinkedHashSet<Symbol> mapNonTerminals(List<String> nonTerminals, Pattern whiteSpacePattern) {

        return nonTerminals.stream()
                .map(String::strip)
                .peek(str -> {
                    if (str.isEmpty()) {
                        throw new MalformedGrammarException("пустые символы для нетерминалов запрещены");
                    }
                })
                .peek(s -> {
                    Matcher matcher = whiteSpacePattern.matcher(s);
                    if (matcher.find()) {
                        throw new MalformedGrammarException(
                                "нетерминалы не должны содержать пустых символов");
                    }
                })
                .map(Symbol::new)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private static LinkedHashSet<Symbol> mapTerminals(List<String> terminals, Pattern whiteSpacePattern) {

        LinkedHashSet<Symbol> mappedTerminals = terminals.stream()
                .map(String::strip)
                .peek(str -> {
                    if (str.isEmpty()) {
                        throw new MalformedGrammarException("пустые символы для терминалов запрещены");
                    }
                })
                .peek(s -> {
                    Matcher matcher = whiteSpacePattern.matcher(s);
                    if (matcher.find()) {
                        throw new MalformedGrammarException(
                                "терминалы не должны содержать пустых символов");
                    }
                })
                .map(Symbol::new)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        mappedTerminals.add(Symbol.EMPTY_SYMBOL);

        return mappedTerminals;
    }

    private static Symbol mapStartSymbol(String s, LinkedHashSet<Symbol> nonTerminals) {

        Symbol startSymbol = new Symbol(s.strip());

        if (!nonTerminals.contains(startSymbol)) {
            throw new MalformedGrammarException("стартовый нетерминал должен быть включен вo множество нетерминалов");
        }

        return startSymbol;
    }

    private static Map<Symbol, Set<Word>> mapDefiningEquations(List<String> reqDefiningEquations, Set<Symbol> reqNonTerminals, Set<Symbol> reqTerminals) {

        Map<Symbol, Set<Word>> definingEquations = new HashMap<>();
        reqNonTerminals.forEach(nt -> definingEquations.put(nt, new HashSet<>()));

        reqDefiningEquations.forEach(equation -> {
            String[] equationSplitByEquals = equation.split("=");
            if (equationSplitByEquals.length != 2) {
                throw new MalformedGrammarException("определяющие уравнения должны быть в виде: T = alpha_1 | ... | alpha_n, " +
                        "где T - нетерминал; alpha_1, ... alpha_n - строки состоящие из терминалов или нетерминалов, " +
                        "выводящиеся за один шаг из нетерминала T");
            }
            String nonTerminal = equationSplitByEquals[0].strip();

            boolean nonTerminalNotFound = reqNonTerminals.stream()
                    .noneMatch(reqNt -> reqNt.toString().equals(nonTerminal));

            if (nonTerminalNotFound) {
                throw new MalformedGrammarException("символ " + nonTerminal + " не содержится в исходном списке нетерминалов," +
                        " для него не может быть задано определяющее уравнение");
            }

            Collection<Word> transformationOptions = Arrays.stream(equationSplitByEquals[1].split("\\|"))
                    .map(String::strip)

                    .map(s -> parseStringOfTerminalsAndNonTerminals(
                            reqTerminals,
                            reqNonTerminals,
                            nonTerminal,
                            s)
                    ).collect(Collectors.toCollection(HashSet::new));

            definingEquations.get(new Symbol(nonTerminal)).addAll(transformationOptions);
        });

        return definingEquations;
    }

    private static Word parseStringOfTerminalsAndNonTerminals(
            Set<Symbol> symbolTerminals,
            Set<Symbol> symbolNonTerminals,
            String nonTerminalHavingStringAsDerivationOption,
            String stringToParse
    ) {
        Set<String> terminals = symbolTerminals.stream()
                .map(Objects::toString)
                .collect(Collectors.toSet());

        Set<String> nonTerminals = symbolNonTerminals.stream()
                .map(Object::toString)
                .collect(Collectors.toSet());

        Word word = new Word();

        int length = stringToParse.length();

        int intervalLength = 1;
        for (int i = 0; i < length && (length - intervalLength - i >= 0); ) {
            String subStr = stringToParse.substring(i, i + intervalLength);

            if (!terminals.contains(subStr) && !nonTerminals.contains(subStr)) {
                intervalLength++;
            } else {
                word.append(new Symbol(subStr));
                i += intervalLength;
                intervalLength = 1;
            }
        }

        if (intervalLength != 1) {
            throw new MalformedGrammarException("некорректная опция '" + stringToParse
                    + "' в определяющем уравнении нетерминала '"
                    + nonTerminalHavingStringAsDerivationOption + "'");
        }

        return word;
    }
}
