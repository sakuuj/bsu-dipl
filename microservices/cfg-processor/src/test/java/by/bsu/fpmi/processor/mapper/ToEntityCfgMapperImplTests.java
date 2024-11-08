package by.bsu.fpmi.processor.mapper;

import by.bsu.fpmi.processor.exception.MalformedGrammarException;
import by.bsu.fpmi.processor.model.Word;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ToEntityCfgMapperImplTests {

    private final ToEntityCfgMapperImpl cfgParser = new ToEntityCfgMapperImpl();

    private final Method[] declaredMethods = ReflectionUtils.getDeclaredMethods(ToEntityCfgMapperImpl.class);
    private final Method parseStringOfTerminalsAndNonTerminals = Arrays.stream(declaredMethods)
            .filter(m -> m.getName().equals("parseStringOfTerminalsAndNonTerminals"))
            .findFirst()
            .orElseThrow(IllegalStateException::new);

    @ParameterizedTest
    @MethodSource
    void shouldParseOneLengthSymbols(Set<String> nonTerminals,
                                     Set<String> terminals,
                                     String stringToParse,
                                     String nonTerminal,
                                     int expectedLength) {


        Word parsed = (Word) ReflectionUtils.invokeMethod(
                parseStringOfTerminalsAndNonTerminals,
                cfgParser,
                terminals, nonTerminals, nonTerminal, stringToParse
        );

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
    void shouldParseMultipleLengthSymbols(Set<String> nonTerminals,
                                          Set<String> terminals,
                                          String stringToParse,
                                          String nonTerminal,
                                          int expectedLength) {

        Word parsed = (Word) ReflectionUtils.invokeMethod(
                parseStringOfTerminalsAndNonTerminals,
                cfgParser,
                terminals, nonTerminals, nonTerminal, stringToParse
        );

        assertThat(parsed.length()).isEqualTo(expectedLength);
        assertThat(parsed.toString()).isEqualTo(stringToParse);
    }

    static Stream<Arguments> shouldParseMultipleLengthSymbols() {
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
    void shouldThrowExceptionOnMalformedInput(Set<String> nonTerminals,
                                              Set<String> terminals,
                                              String stringToParse,
                                              String nonTerminal) {

        assertThatThrownBy(() -> ReflectionUtils.invokeMethod(
                parseStringOfTerminalsAndNonTerminals,
                cfgParser,
                terminals, nonTerminals, nonTerminal, stringToParse
        ))
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
