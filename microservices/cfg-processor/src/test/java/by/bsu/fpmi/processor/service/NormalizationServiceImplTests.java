package by.bsu.fpmi.processor.service;

import by.bsu.fpmi.processor.model.Grammar;
import by.bsu.fpmi.processor.model.Symbol;
import by.bsu.fpmi.processor.model.Word;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

class NormalizationServiceImplTests {

    @Test
    void shouldRetainGeneratingNonTerminals() {

        // given
        Symbol z = new Symbol("z");
        Symbol x = new Symbol("x");
        Symbol c = new Symbol("c");

        Symbol Q = new Symbol("Q");
        Symbol W = new Symbol("W");
        Symbol E = new Symbol("E");

        Grammar actual = Grammar.builder()
                .terminals(new LinkedHashSet<>(Set.of(z, x, c)))
                .nonTerminals(new LinkedHashSet<>(Set.of(Q, W, E)))
                .definingEquations(Map.of(
                        Q, Set.of(new Word().append(W).append(x), new Word().append(E)),
                        W, Set.of(new Word().append(W), new Word().append(x).append(z)),
                        E, Set.of(new Word().append(E))
                ))
                .build();

        Grammar expected = Grammar.builder()
                .terminals(new LinkedHashSet<>(Set.of(z, x, c)))
                .nonTerminals(new LinkedHashSet<>(Set.of(Q, W)))
                .definingEquations(Map.of(
                        Q, Set.of(new Word().append(W).append(x)),
                        W, Set.of(new Word().append(W), new Word().append(x).append(z))
                ))
                .build();

        // when
        NormalizationService.retainGeneratingNonTerminals(actual);

        // then
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldRetainReachableNonTerminals() {

        // given
        Symbol z = new Symbol("z");
        Symbol x = new Symbol("x");
        Symbol c = new Symbol("c");

        Symbol Q = new Symbol("Q");
        Symbol W = new Symbol("W");
        Symbol E = new Symbol("E");

        Grammar actual = Grammar.builder()
                .startSymbol(Q)
                .terminals(new LinkedHashSet<>(Set.of(z, x, c)))
                .nonTerminals(new LinkedHashSet<>(Set.of(Q, W, E)))
                .definingEquations(Map.of(
                        Q, Set.of(new Word().append(W).append(x)),
                        W, Set.of(new Word().append(W), new Word().append(x).append(z)),
                        E, Set.of(new Word().append(E))
                ))
                .build();

        Grammar expected = Grammar.builder()
                .startSymbol(Q)
                .terminals(new LinkedHashSet<>(Set.of(z, x, c)))
                .nonTerminals(new LinkedHashSet<>(Set.of(Q, W)))
                .definingEquations(Map.of(
                        Q, Set.of(new Word().append(W).append(x)),
                        W, Set.of(new Word().append(W), new Word().append(x).append(z))
                ))
                .build();

        // when
        NormalizationService.retainReachableNonTerminals(actual);

        // then
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}
