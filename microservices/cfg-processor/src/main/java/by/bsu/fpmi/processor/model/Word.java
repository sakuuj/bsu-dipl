package by.bsu.fpmi.processor.model;

import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@EqualsAndHashCode
public class Word implements Iterable<Symbol> {

    public static final Word EMPTY_WORD = new Word().append(Symbol.EMPTY_SYMBOL);

    private List<Symbol> content;

    public Word(Word word) {
        content = new ArrayList<>(word.content);
    }

    public Word() {
        content = new ArrayList<>(List.of(Symbol.EMPTY_SYMBOL));
    }

    @Override
    public Iterator<Symbol> iterator() {

        return content.iterator();
    }

    public void concat(Word word) {

        if (word.length() == 0) {
            return;
        }

        word.content.forEach(this::append);
    }

    public int size() {
        return content.size();
    }

    public int length() {
        if (content.size() == 1 && content.get(0).equals(Symbol.EMPTY_SYMBOL)) {
            return 0;
        }

        return content.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        content.forEach(s -> {
            sb.append(s.toString());
            sb.append(" ");
        });
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    public Word append(Symbol symbol) {

        if (symbol == null) {
            throw new IllegalArgumentException("null argument");
        }

        if (Symbol.EMPTY_SYMBOL.equals(symbol)) {
            return this;
        }

        if (content.size() > 1) {
            content.add(symbol);
            return this;
        }

        if (content.get(0).equals(Symbol.EMPTY_SYMBOL)) {
            content.set(0, symbol);
            return this;
        }

        content.add(symbol);
        return this;
    }

    public Word subWord(int startIncl, int endExcl) {

        Word word = new Word();
        if (endExcl - startIncl == 0) {
            return word;
        }

        if (endExcl - startIncl < 0) {
            throw new IndexOutOfBoundsException("End index: " + endExcl + "; start index: " + startIncl);
        }
        word.content = new ArrayList<>(content.subList(startIncl, endExcl));

        return word;
    }

    public Word subWord(int startIncl) {
        return subWord(startIncl, content.size());
    }

    public Symbol getAt(int i) {
        return content.get(i);
    }
}
