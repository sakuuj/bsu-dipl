package by.bsu.fpmi.processor.service;

import by.bsu.fpmi.processor.model.CFG;
import by.bsu.fpmi.processor.model.Symbol;
import by.bsu.fpmi.processor.model.Word;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final First1Service first1Service;

    @Override
    public Map<Symbol, Set<Symbol>> follow(CFG cfg, Map<Symbol, Set<Symbol>> first1) {

        Set<Symbol> nonTerminals = cfg.getNonTerminals();
        Set<Symbol> terminals = new HashSet<>(cfg.getTerminals());
        terminals.add(Symbol.RESERVED_SYMBOL);

        Map<Symbol, Set<Word>> definingEquations = cfg.getDefiningEquations();

        Symbol startSymbol = cfg.getStartSymbol();

        Queue<Word> queue = new ArrayDeque<>();
        Map<Symbol, Set<Symbol>> result = new HashMap<>();

        queue.add(new Word().append(startSymbol).append(Symbol.RESERVED_SYMBOL));
        while (!queue.isEmpty()) {

            Word current = queue.remove();

            Symbol nonTerm = current.getAt(0);
            Symbol lastTerm = current.getAt(1);

            result.computeIfAbsent(nonTerm, k -> new HashSet<>());
            result.get(nonTerm).add(lastTerm);


            Set<Word> options = definingEquations.get(nonTerm);
            for (Word option : options) {

                Word word = new Word(option);
                word.append(lastTerm);

                int size = word.size();
                for (int i = 0; i < size; i++) {

                    Symbol s = word.getAt(i);
                    if (!nonTerminals.contains(s)) {
                        continue;
                    }

                    Word wordTail = word.subWord(i + 1);
                    Set<Symbol> symbolFirst1 = first1Service.first1OverWord(wordTail, first1, terminals);

                    symbolFirst1.forEach(sf1 -> {
                        Word wordToConsider = new Word().append(s).append(sf1);

                        result.computeIfAbsent(s, k -> new HashSet<>());

                        if (!result.get(s).contains(sf1)) {
                            queue.add(wordToConsider);
                        }
                    });
                }
            }
        }

        return result;
    }
}
