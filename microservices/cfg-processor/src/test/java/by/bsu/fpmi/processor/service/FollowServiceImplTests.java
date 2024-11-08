package by.bsu.fpmi.processor.service;

import by.bsu.fpmi.processor.model.CFG;
import by.bsu.fpmi.processor.model.Symbol;
import by.bsu.fpmi.processor.model.Word;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

class FollowServiceImplTests {

    private final FollowServiceImpl followService = new FollowServiceImpl(new First1ServiceImpl());

    @Test
    void shouldFindFollow() {

        // given
        Symbol a = new Symbol("a");
        Symbol b = new Symbol("b");

        Symbol A = new Symbol("A");
        Symbol S = new Symbol("S");
        Symbol P = new Symbol("P");


        CFG cfg = CFG.builder()
                .terminals(new LinkedHashSet<>(Set.of(a, b, Symbol.EMPTY_SYMBOL)))
                .nonTerminals(new LinkedHashSet<>(Set.of(A, S, P)))
                .startSymbol(A)
                .definingEquations(Map.of(
                        A, Set.of(new Word().append(S)),
                        S, Set.of(new Word().append(S).append(P).append(b), new Word()),
                        P, Set.of(new Word().append(a).append(S))
                ))
                .build();

        Map<Symbol, Set<Symbol>> first1 = Map.of(
            A, Set.of(Symbol.EMPTY_SYMBOL, a),
            S, Set.of(Symbol.EMPTY_SYMBOL, a),
            P, Set.of(a)
        );

        Map<Symbol, Set<Symbol>> expected = Map.of(
                A, Set.of(Symbol.RESERVED_SYMBOL),
                P, Set.of(b),
                S, Set.of(a, b, Symbol.RESERVED_SYMBOL)
        );

        // when
        Map<Symbol, Set<Symbol>> actual = followService.follow(cfg, first1);

        // then
        Assertions.assertThat(actual).isEqualTo(expected);
    }

}