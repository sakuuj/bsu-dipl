package by.bsu.fpmi.processor.service;

import by.bsu.fpmi.processor.model.CFG;
import by.bsu.fpmi.processor.model.Symbol;
import by.bsu.fpmi.processor.model.Word;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

class NormalizationServiceImplTests {

    private final NormalizationServiceImpl normalizationService = new NormalizationServiceImpl();

    @Test
    void shouldRetainGeneratingNonTerminals() {

        // given
        Symbol z = new Symbol("z");
        Symbol x = new Symbol("x");
        Symbol c = new Symbol("c");

        Symbol Q = new Symbol("Q");
        Symbol W = new Symbol("W");
        Symbol E = new Symbol("E");

        CFG actual = CFG.builder()
                .terminals(Set.of(z, x, c))
                .nonTerminals(Set.of(Q, W, E))
                .definingEquations(Map.of(
                        Q, Set.of(new Word().append(W).append(x), new Word().append(E)),
                        W, Set.of(new Word().append(W), new Word().append(x).append(z)),
                        E, Set.of(new Word().append(E))
                ))
                .build();

        CFG expected = CFG.builder()
                .terminals(Set.of(z, x, c))
                .nonTerminals(Set.of(Q, W))
                .definingEquations(Map.of(
                        Q, Set.of(new Word().append(W).append(x)),
                        W, Set.of(new Word().append(W), new Word().append(x).append(z))
                ))
                .build();

        // when
        normalizationService.retainGeneratingNonTerminals(actual);

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

        CFG actual = CFG.builder()
                .startSymbol(Q)
                .terminals(Set.of(z, x, c))
                .nonTerminals(Set.of(Q, W, E))
                .definingEquations(Map.of(
                        Q, Set.of(new Word().append(W).append(x)),
                        W, Set.of(new Word().append(W), new Word().append(x).append(z)),
                        E, Set.of(new Word().append(E))
                ))
                .build();

        CFG expected = CFG.builder()
                .startSymbol(Q)
                .terminals(Set.of(z, x, c))
                .nonTerminals(Set.of(Q, W))
                .definingEquations(Map.of(
                        Q, Set.of(new Word().append(W).append(x)),
                        W, Set.of(new Word().append(W), new Word().append(x).append(z))
                ))
                .build();

        // when
        normalizationService.retainReachableNonTerminals(actual);

        // then
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}
