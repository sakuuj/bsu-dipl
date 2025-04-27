package by.bsu.fpmi.processor.service;


import by.bsu.fpmi.processor.model.Symbol;
import by.bsu.fpmi.processor.model.Word;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class First1ServiceTests {

    @MethodSource
    @ParameterizedTest
    void shouldFindFirst1Correctly(GrammarExample grammarExample) {

        // given, when
        Map<Symbol, Set<Symbol>> actual = First1Service.first1(grammarExample.grammar());

        // then
        assertThat(actual).isEqualTo(grammarExample.first1Set());
    }

    static List<GrammarExample> shouldFindFirst1Correctly() {

        return List.of(
                GrammarExamples.firstExample(),
                GrammarExamples.secondExample(),
                GrammarExamples.thirdExample()
        );
    }

    @Test
    void shouldFindFirst1OverWord() {

        // given
        Symbol z = new Symbol("z");
        Symbol x = new Symbol("x");
        Symbol c = new Symbol("c");
        Symbol v = new Symbol("v");
        Symbol n = new Symbol("n");

        Symbol Q = new Symbol("Q");
        Symbol W = new Symbol("W");
        Symbol E = new Symbol("E");

        Set<Symbol> nonTerminals = Set.of(Q, W, E);

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
        Set<Symbol> actual = First1Service.first1OverWord(word, grammarFirst1, nonTerminals);

        // then
        assertThat(actual).isEqualTo(expected);
    }

}