package by.bsu.fpmi.grammar.processor.service;

import by.bsu.fpmi.grammar.processor.dto.ParsingMetadata;
import by.bsu.fpmi.grammar.processor.model.Symbol;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.Set;

class FollowServiceTests {

    @MethodSource
    @ParameterizedTest
    void shouldFindFollow(GrammarExample grammarExample) {

        // given, when
        Map<Symbol, Set<Symbol>> actual = FollowService.follow(grammarExample.grammar(), grammarExample.first1Set());

        // then
        Assertions.assertThat(actual).isEqualTo(grammarExample.followSet());
        var predictiveParsingTable = LL1ParserService.createPredictiveParsingTable(grammarExample.grammar(), grammarExample.first1Set(), grammarExample.followSet());
        predictiveParsingTable.entrySet().forEach(System.out::println);
        System.out.println("---");
        ParsingMetadata parsingMetadata = LL1ParserService.parseInputUsingTable(List.of(new Symbol("id"), new Symbol("+"), new Symbol("id"), new Symbol("*"), new Symbol("id"), Symbol.RESERVED_SYMBOL), grammarExample.grammar(), predictiveParsingTable);
        System.out.println(parsingMetadata);
    }

    static List<GrammarExample> shouldFindFollow() {

        return List.of(
                GrammarExamples.secondExample()
//                GrammarExamples.thirdExample()
        );
    }
}