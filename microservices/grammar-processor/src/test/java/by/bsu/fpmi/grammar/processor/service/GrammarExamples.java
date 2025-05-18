package by.bsu.fpmi.grammar.processor.service;

import by.bsu.fpmi.grammar.processor.model.Grammar;
import by.bsu.fpmi.grammar.processor.model.Symbol;
import by.bsu.fpmi.grammar.processor.model.Word;
import lombok.experimental.UtilityClass;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;


@UtilityClass
public class GrammarExamples {

    public static GrammarExample firstExample() {
        // Q = Wx | E
        // W = W | xz
        // E = E | _ | Ez
        // R = EEEEEc | EEEc
        //
        // Q = x, _, z
        // W = x
        // E = _, z
        // R = z, c

        Symbol z = new Symbol("z");
        Symbol x = new Symbol("x");
        Symbol c = new Symbol("c");

        Symbol Q = new Symbol("Q");
        Symbol W = new Symbol("W");
        Symbol R = new Symbol("R");
        Symbol E = new Symbol("E");

        Grammar grammar = Grammar.builder()
                .terminals(new LinkedHashSet<>(Set.of(z, x, c)))
                .nonTerminals(new LinkedHashSet<>(Set.of(Q, W, E)))
                .definingEquations(Map.of(
                        Q, Set.of(new Word().append(W).append(x), new Word().append(E)),
                        W, Set.of(new Word().append(W), new Word().append(x).append(z)),
                        E, Set.of(new Word().append(E), new Word(), new Word().append(E).append(z)),
                        R, Set.of(
                                new Word().append(E).append(E).append(E).append(E).append(E).append(c),
                                new Word().append(E).append(E).append(E).append(c)
                        )
                ))
                .startSymbol(Q)
                .build();

        Map<Symbol, Set<Symbol>> first1Set = Map.of(
                Q, Set.of(x, Symbol.EMPTY_SYMBOL, z),
                W, Set.of(x),
                E, Set.of(Symbol.EMPTY_SYMBOL, z)
        );
        Map<Symbol, Set<Symbol>> followSet = null;

        return GrammarExample.builder()
                .grammar(grammar)
                .first1Set(first1Set)
                .followSet(followSet)
                .build();
    }

    public static GrammarExample secondExample() {

        // E = T E'
        // E' = + T E' | _
        // T = F T'
        // T' = * F T' | _
        // F = ( E ) | id
        //
        // F = T = E = (, id
        // E' = +, _
        // T' = *, _
        Symbol E = new Symbol("E");
        Symbol EHyphen = new Symbol("E'");
        Symbol THyphen = new Symbol("T'");
        Symbol T = new Symbol("T");
        Symbol F = new Symbol("F");

        Symbol plus = new Symbol("+");
        Symbol id = new Symbol("id");
        Symbol star = new Symbol("*");
        Symbol leftParenthesis = new Symbol("(");
        Symbol rightParenthesis = new Symbol(")");

        Grammar grammar = Grammar.builder()
                .terminals(new LinkedHashSet<>(Set.of(plus, star, leftParenthesis, rightParenthesis, id)))
                .nonTerminals(new LinkedHashSet<>(Set.of(E, EHyphen, T, THyphen, F)))
                .definingEquations(Map.of(
                        E, Set.of(new Word().append(T).append(EHyphen)),
                        EHyphen, Set.of(new Word().append(plus).append(T).append(EHyphen), new Word().append(Symbol.EMPTY_SYMBOL)),
                        T, Set.of(new Word().append(F).append(THyphen)),
                        THyphen, Set.of(new Word().append(star).append(F).append(THyphen), new Word().append(Symbol.EMPTY_SYMBOL)),
                        F, Set.of(new Word().append(leftParenthesis).append(E).append(rightParenthesis), new Word().append(id))
                ))
                .startSymbol(E)
                .build();
        Map<Symbol, Set<Symbol>> first1Set = Map.of(
                F, Set.of(leftParenthesis, id),
                T, Set.of(leftParenthesis, id),
                E, Set.of(leftParenthesis, id),
                EHyphen, Set.of(plus, Symbol.EMPTY_SYMBOL),
                THyphen, Set.of(star, Symbol.EMPTY_SYMBOL)
        );
        Map<Symbol, Set<Symbol>> followSet = Map.of(
                E, Set.of(rightParenthesis, Symbol.RESERVED_SYMBOL),
                EHyphen, Set.of(rightParenthesis, Symbol.RESERVED_SYMBOL),
                T, Set.of(plus, rightParenthesis, Symbol.RESERVED_SYMBOL),
                THyphen, Set.of(plus, rightParenthesis, Symbol.RESERVED_SYMBOL),
                F, Set.of(plus, star, rightParenthesis, Symbol.RESERVED_SYMBOL)
        );

        return GrammarExample.builder()
                .grammar(grammar)
                .first1Set(first1Set)
                .followSet(followSet)
                .build();
    }

    public static GrammarExample thirdExample() {

        Symbol a = new Symbol("a");
        Symbol b = new Symbol("b");

        Symbol A = new Symbol("A");
        Symbol S = new Symbol("S");
        Symbol P = new Symbol("P");

        Grammar grammar = Grammar.builder()
                .terminals(new LinkedHashSet<>(Set.of(a, b, Symbol.EMPTY_SYMBOL)))
                .nonTerminals(new LinkedHashSet<>(Set.of(A, S, P)))
                .startSymbol(A)
                .definingEquations(Map.of(
                        A, Set.of(new Word().append(S)),
                        S, Set.of(new Word().append(S).append(P).append(b), new Word()),
                        P, Set.of(new Word().append(a).append(S))
                ))
                .build();

        Map<Symbol, Set<Symbol>> first1Set = Map.of(
                A, Set.of(Symbol.EMPTY_SYMBOL, a),
                S, Set.of(Symbol.EMPTY_SYMBOL, a),
                P, Set.of(a)
        );
        Map<Symbol, Set<Symbol>> followSet = Map.of(
                A, Set.of(Symbol.RESERVED_SYMBOL),
                P, Set.of(b),
                S, Set.of(a, b, Symbol.RESERVED_SYMBOL)
        );

        return GrammarExample.builder()
                .grammar(grammar)
                .first1Set(first1Set)
                .followSet(followSet)
                .build();
    }
}
