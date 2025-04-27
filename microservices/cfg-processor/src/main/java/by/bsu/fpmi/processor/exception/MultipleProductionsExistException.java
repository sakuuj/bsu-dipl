package by.bsu.fpmi.processor.exception;

import by.bsu.fpmi.processor.model.Symbol;
import by.bsu.fpmi.processor.model.Word;
import lombok.Data;
import lombok.Getter;

@Getter
public class MultipleProductionsExistException extends RuntimeException {

    private final Symbol nonTerminal;
    private final Symbol terminal;
    private final Word firstProduction;
    private final Word secondProduction;

    public MultipleProductionsExistException(Symbol nonTerminal, Symbol terminal, Word firstProduction, Word secondProduction) {
        super("Как минимум две продукции существует для нетерминала '%s', терминала '%s': '%s' и '%s'".formatted(nonTerminal, terminal, firstProduction, secondProduction));
        this.terminal = terminal;
        this.nonTerminal = nonTerminal;
        this.firstProduction = firstProduction;
        this.secondProduction = secondProduction;
    }
}
