package by.bsu.fpmi.processor.parser;

import by.bsu.fpmi.processor.exception.MalformedGrammarException;
import by.bsu.fpmi.processor.model.Word;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class ParserTests {

    private final CFGParser cfgParser = new CFGParser();

    @ParameterizedTest
    @MethodSource
    public void shouldParseOneLengthSymbols(Set<String> nonTerminals,
                                            Set<String> terminals,
                                            String stringToParse,
                                            String nonTerminal,
                                            int expectedLength) {

        Word parsed = CFGParser.parseStringOfTerminalsAndNonTerminals(terminals, nonTerminals, nonTerminal, stringToParse);

        assertThat(parsed.length()).isEqualTo(expectedLength);
        assertThat(parsed.toString()).isEqualTo(stringToParse);
    }

    public static Stream<Arguments> shouldParseOneLengthSymbols() {
        return Stream.of(
                arguments(Set.of("A", "B", "C"),
                        Set.of("q", "w", "e"),
                        "qABwCew",
                        "A",
                        7),
                arguments(Set.of("A", "B", "C"),
                        Set.of("q", "w", "e"),
                        "ABwCewq",
                        "A",
                        7),
                arguments(Set.of("A", "B", "C"),
                        Set.of("q", "w", "e"),
                        "qweqweABC",
                        "A",
                        9)
        );
    }


    @ParameterizedTest
    @MethodSource
    public void shouldParseMultipleLengthSymbols(Set<String> nonTerminals,
                                            Set<String> terminals,
                                            String stringToParse,
                                            String nonTerminal,
                                            int expectedLength) {

        Word parsed = CFGParser.parseStringOfTerminalsAndNonTerminals(terminals, nonTerminals, nonTerminal, stringToParse);

        assertThat(parsed.length()).isEqualTo(expectedLength);
        assertThat(parsed.toString()).isEqualTo(stringToParse);
    }

    public static Stream<Arguments> shouldParseMultipleLengthSymbols() {
        return Stream.of(
                arguments(Set.of("A", "BA", "BCD", "D"),
                        Set.of("qxe", "www", "e"),
                        "ABCDBABADwwwqxee",
                        "A",
                        8),
                arguments(Set.of("Я", "Л", "Ц", "if", "else", "goto"),
                        Set.of("зз2", "щит", "лук"),
                        "ЯЛзз2ЦщитщитifgotoЛлукelseлук",
                        "A",
                        12),
                arguments(Set.of("Я", "Л", "Ц", "if", "else", "goto"),
                        Set.of("зз2", "щит", "лук"),
                        "gotoЯЛзз2ЦщитщитifgotoЛлукelseлук",
                        "A",
                        13)
        );

    }

    @ParameterizedTest
    @MethodSource
    public void shouldThrowExceptionOnMalformedInput(Set<String> nonTerminals,
                                            Set<String> terminals,
                                            String stringToParse,
                                            String nonTerminal) {

        assertThatThrownBy(() -> CFGParser.parseStringOfTerminalsAndNonTerminals(terminals, nonTerminals, nonTerminal, stringToParse))
                .isInstanceOf(MalformedGrammarException.class);

    }


    public static Stream<Arguments> shouldThrowExceptionOnMalformedInput() {

        return Stream.of(
                arguments(Set.of("A", "BA", "BCD", "D"),
                        Set.of("qxe", "www", "e"),
                        "ABCDBABADwwwqxeeq",
                        "A"),
                arguments(Set.of("Я", "Л", "Ц", "if", "else", "goto"),
                        Set.of("зз2", "щит", "лук"),
                        "ЯЛзз2ЦщитщитiffgotoЛлукelseлук",
                        "A")
        );
    }
}
