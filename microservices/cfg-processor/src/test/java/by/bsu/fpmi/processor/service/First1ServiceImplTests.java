package by.bsu.fpmi.processor.service;


import by.bsu.fpmi.processor.model.CFG;
import by.bsu.fpmi.processor.model.Symbol;
import by.bsu.fpmi.processor.model.Word;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

class First1ServiceImplTests {

    private final First1ServiceImpl first1Service = new First1ServiceImpl();

    @Test
    void shouldFindFirst1Correctly() {

        // given
        Symbol z = new Symbol("z");
        Symbol x = new Symbol("x");
        Symbol c = new Symbol("c");

        Symbol Q = new Symbol("Q");
        Symbol W = new Symbol("W");
        Symbol E = new Symbol("E");

        // Q = Wx | E
        // W = W | xz
        // E = E | _ | Ez
        //
        // Q = x, _, z
        // W = x
        // E = _, z

        CFG cfg = CFG.builder()
                .terminals(new LinkedHashSet<>(Set.of(z, x, c)))
                .nonTerminals(new LinkedHashSet<>(Set.of(Q, W, E)))
                .definingEquations(Map.of(
                        Q, Set.of(new Word().append(W).append(x), new Word().append(E)),
                        W, Set.of(new Word().append(W), new Word().append(x).append(z)),
                        E, Set.of(new Word().append(E), new Word(), new Word().append(E).append(z))
                ))
                .build();

        Map<Symbol, Set<Symbol>> expected = Map.of(
                Q, Set.of(x, Symbol.EMPTY_SYMBOL, z),
                W, Set.of(x),
                E, Set.of(Symbol.EMPTY_SYMBOL, z)
        );

        // when
        Map<Symbol, Set<Symbol>> actual = first1Service.first1(cfg);

        // then
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldFindFirst1OverWord() {

        // given
        Symbol z = new Symbol("z");
        Symbol x = new Symbol("x");
        Symbol c = new Symbol("c");
        Symbol v = new Symbol("v");
        Symbol b = new Symbol("b");
        Symbol n = new Symbol("n");

        Symbol Q = new Symbol("Q");
        Symbol W = new Symbol("W");
        Symbol E = new Symbol("E");

        Set<Symbol> terminals = Set.of(z, x, c, v, b, n);

        Word word = new Word()
                .append(Q)
                .append(E)
                .append(W)
                .append(v);

        // Q = _, c
        // W = x, _, n
        // E = _, z

        Map<Symbol, Set<Symbol>> grammarFirst1 = Map.of(
                Q, Set.of(Symbol.EMPTY_SYMBOL, c),
                W, Set.of(x, Symbol.EMPTY_SYMBOL, n),
                E, Set.of(Symbol.EMPTY_SYMBOL, z)
        );

        Set<Symbol> expected = Set.of(c, x, z, n, v);

        // when
        Set<Symbol> actual = first1Service.first1OverWord(word, grammarFirst1, terminals);

        // then
        Assertions.assertThat(actual).isEqualTo(expected);
    }

}