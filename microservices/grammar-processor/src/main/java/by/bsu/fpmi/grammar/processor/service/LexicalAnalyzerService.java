package by.bsu.fpmi.grammar.processor.service;

import by.bsu.fpmi.grammar.processor.exception.MalformedGrammarException;
import by.bsu.fpmi.grammar.processor.model.Symbol;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class LexicalAnalyzerService {

    public static List<Symbol> tokenizeText(String text, Set<Symbol> nonTerminals, Set<Symbol> terminals) {

        if (text.isBlank()) {
            return List.of(Symbol.EMPTY_SYMBOL);
        }

        String[] splitText = text.strip().split("\\s+");

        ArrayList<Symbol> result = Arrays.stream(splitText)
                .map(Symbol::new)
                .peek(symbol -> {
                    
                    if (symbol.equals(Symbol.EMPTY_SYMBOL)) {
                        return;
                    }

                    if (!terminals.contains(symbol) && !nonTerminals.contains(symbol)) {
                        throw new MalformedGrammarException("Указанный символ '%s' не найден в списке символов грамматики".formatted(symbol));
                    }
                })
                .collect(Collectors.toCollection(ArrayList::new));

        return result;
    }
}
