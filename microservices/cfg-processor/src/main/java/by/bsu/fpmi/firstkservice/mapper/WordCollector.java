package by.bsu.fpmi.firstkservice.mapper;

import by.bsu.fpmi.firstkservice.model.Symbol;
import by.bsu.fpmi.firstkservice.model.Word;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class WordCollector implements Collector<Symbol, Word, Word> {

    @Override
    public Supplier<Word> supplier() {
        return Word::new;
    }

    @Override
    public BiConsumer<Word, Symbol> accumulator() {
        return Word::append;
    }

    @Override
    public BinaryOperator<Word> combiner() {
        return null;
    }

    @Override
    public Function<Word, Word> finisher() {
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of();
    }
}
