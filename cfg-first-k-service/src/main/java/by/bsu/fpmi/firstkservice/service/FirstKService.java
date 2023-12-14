package by.bsu.fpmi.firstkservice.service;

import by.bsu.fpmi.firstkservice.dto.ContextFreeGrammarRequest;
import by.bsu.fpmi.firstkservice.mapper.ContextFreeGrammarMapper;
import by.bsu.fpmi.firstkservice.model.ContextFreeGrammar;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class FirstKService {

    public static final char EMPTY_SYMBOL = '_';

    private final ContextFreeGrammarMapper cfgMapper;

    private static String firstK(int k, String source) {
        if (k == 0) {
            return "";
        }

        if (source.length() < k) {
            return source;
        }

        return source.substring(0, k);
    }

    public Map<Character, Set<String>> findFirstK(int k, ContextFreeGrammarRequest cfgReq) {

        if (k > 7) {
            throw new IllegalArgumentException("k should be less than 8");
        }

        if (k < 0) {
            throw new IllegalArgumentException("k should be between 0 and 7");
        }
        cfgReq.getTerminals().add(EMPTY_SYMBOL);
        ContextFreeGrammar cfg = cfgMapper.fromRequest(cfgReq);
        Set<Character> nonTerminals = cfg.getNonTerminalsToTransformationOptions().keySet();

        Map<Character, Set<String>> prevNonTerminalLangs = new HashMap<>();
        Map<Character, Set<String>> currentNonTerminalLangs = new HashMap<>();
        Map<Character, Set<String>> resultNonTerminalLangs = new HashMap<>();
        nonTerminals.forEach(nt -> {
            currentNonTerminalLangs.put(nt, new HashSet<>());
            prevNonTerminalLangs.put(nt, new HashSet<>());
            resultNonTerminalLangs.put(nt, new HashSet<>());
        });

        do {
            currentNonTerminalLangs
                    .forEach(
                            (nonTerminal, lang) -> prevNonTerminalLangs.get(nonTerminal).addAll(lang)
                    );
            performIteration(prevNonTerminalLangs, currentNonTerminalLangs, k, cfg);
        } while (!currentNonTerminalLangs.equals(prevNonTerminalLangs));

        currentNonTerminalLangs.forEach((key, value) -> value.forEach(
                word -> {
                    String wordToAdd = word.replace("_", "");

                    resultNonTerminalLangs.get(key).add(wordToAdd.isEmpty() ? "_" : wordToAdd);
                }
        ));

        return resultNonTerminalLangs;
    }

    private static void performIteration(Map<Character, Set<String>> prevNonTerminalLangs,
                                         Map<Character, Set<String>> currentNonTerminalLangs,
                                         int k, ContextFreeGrammar cfg) {

        Set<Character> nonTerminals = cfg.getNonTerminalsToTransformationOptions().keySet();
        Map<Character, Set<String>> transformOptions = cfg.getNonTerminalsToTransformationOptions();

        for (Character nt : nonTerminals) {
            for (String option : transformOptions.get(nt)) {
                if (doesOptionContainsEmptyNonTerminal(option, cfg.getTerminals(), prevNonTerminalLangs)) {
                    continue;
                }

                StringBuilder sb = new StringBuilder();
                performIterationRecursiveHelper(option, nt, sb, 0,
                        prevNonTerminalLangs, currentNonTerminalLangs, k);
            }
        }
    }

    private static void performIterationRecursiveHelper(String option,
                                                        Character nonTerminal,
                                                        StringBuilder sb,
                                                        int nextIndex,
                                                        Map<Character, Set<String>> prevNonTerminalLangs,
                                                        Map<Character, Set<String>> currentNonTerminalLangs,
                                                        int k) {
        Set<Character> nonTerminals = prevNonTerminalLangs.keySet();

        if (nextIndex >= k && k != 0
                || nextIndex >= option.length()
                || countNonEmptySymbols(sb, EMPTY_SYMBOL) >= k && k != 0
                || k == 0 && nextIndex > 0) {
            if (k == 0) {
                currentNonTerminalLangs
                        .get(nonTerminal)
                        .add("_");
                return;
            }
            currentNonTerminalLangs
                    .get(nonTerminal)
                    .add(sb.toString());
            return;
        }

        for (int i = nextIndex; i < k && i < option.length()
                || i < 1; i++) {
            char symbol = option.charAt(i);

            if (!nonTerminals.contains(option.charAt(i))) {
                sb.append(symbol);

                if (countNonEmptySymbols(sb, EMPTY_SYMBOL) == k && k != 0
                        || (i + 1) == option.length()
                        || k == 0) {
                    if (k == 0) {
                        sb.replace(0, 1, "_");
                    }

                    currentNonTerminalLangs.get(nonTerminal).add(sb.toString());
                    return;
                }
                continue;
            }


            for (String word : prevNonTerminalLangs.get(symbol)) {
                int currentK = k != 0 ? k : 1;
                String firstKWord = firstK(currentK, word);

                int sbLength = sb.length();

                int endIndex;
                if (sbLength + firstKWord.length() <= currentK) {
                    endIndex = firstKWord.length();
                } else {
                    endIndex = currentK - sbLength;
                }
                sb.append(firstKWord, 0, endIndex);
                performIterationRecursiveHelper(option, nonTerminal, sb, i + 1,
                        prevNonTerminalLangs, currentNonTerminalLangs, k);
                sb.delete(sbLength, sb.length());

            }

            return;
        }
    }

    private static boolean doesOptionContainsEmptyNonTerminal(String option,
                                                              Set<Character> terminals,
                                                              Map<Character, Set<String>> lang) {
        for (int i = 0; i < option.length(); i++) {
            if (terminals.contains(option.charAt(i))) {
                continue;
            }

            if (lang.get(option.charAt(i)).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private static int countNonEmptySymbols(StringBuilder sb, char emptySymbol) {
        int counter = sb.length();
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == emptySymbol) {
                counter--;
            }
        }

        return counter;
    }
}
