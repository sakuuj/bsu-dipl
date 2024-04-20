package by.bsu.fpmi.processor.parser;

import by.bsu.fpmi.processor.dto.CFGRequest;
import by.bsu.fpmi.processor.exception.MalformedGrammarException;
import by.bsu.fpmi.processor.model.OrderedCFG;
import by.bsu.fpmi.processor.model.Symbol;
import by.bsu.fpmi.processor.model.Word;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class CFGParser {
    public OrderedCFG fromRequest(CFGRequest request) {

        Pattern whiteSpacePattern = Pattern.compile("\\s");

        LinkedHashSet<Symbol> nonTerminals = request.getNonTerminals().stream()
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

        Symbol startSymbol = new Symbol(request.getStartSymbol().strip());

        if (!nonTerminals.contains(startSymbol)) {
            throw new MalformedGrammarException("стартовый нетерминал должен быть включен вo множество нетерминалов");
        }

        LinkedHashSet<Symbol> terminals = request.getTerminals().stream()
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

        if (!Collections.disjoint(terminals, nonTerminals)) {
            throw new MalformedGrammarException("множества терминалов и нетерминалов не должны пересекаться");
        }

        Map<Symbol, Set<Word>> nonTerminalsToTransformationOptions = getNonTerminalsToTransformationOptions(
               nonTerminals.stream().map(Object::toString).collect(Collectors.toSet()),
               terminals.stream().map(Object::toString).collect(Collectors.toSet()),
               request.getDefiningEquations()
        );

        return OrderedCFG.builder()
                .terminals(terminals)
                .nonTerminals(nonTerminals)
                .definingEquations(nonTerminalsToTransformationOptions)
                .startSymbol(new Symbol(request.getStartSymbol().strip()))
                .build();
    }

    private static Map<Symbol, Set<Word>> getNonTerminalsToTransformationOptions(Set<String> reqNonTerminals,
                                                                                 Set<String> reqTerminals,
                                                                                 List<String> reqDefiningEquations) {

        Map<Symbol, Set<Word>> nonTerminalsToTransformationOptions = new HashMap<>();
        reqNonTerminals.forEach(nt -> nonTerminalsToTransformationOptions.put(new Symbol(nt), new HashSet<>()));

        reqDefiningEquations.forEach(equation -> {
            String[] equationSplitByEquals = equation.split("=");
            if (equationSplitByEquals.length != 2) {
                throw new MalformedGrammarException("определяющие уравнения должны быть в виде: T = alpha_1 | ... | alpha_n, " +
                        "где T - нетерминал; alpha_1, ... alpha_n - строки состоящие из терминалов или нетерминалов, " +
                        "выводящиеся за один шаг из нетерминала T");
            }
            String nonTerminal = equationSplitByEquals[0].strip();

            if (!reqNonTerminals.contains(nonTerminal)) {
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

            nonTerminalsToTransformationOptions.get(new Symbol(nonTerminal)).addAll(transformationOptions);
        });

        return nonTerminalsToTransformationOptions;
    }

    public static Word parseStringOfTerminalsAndNonTerminals(Set<String> terminals,
                                                             Set<String> nonTerminals,
                                                             String nonTerminalHavingStringAsDerivationOption,
                                                             String stringToParse) {
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
