package by.bsu.fpmi.firstkservice.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@EqualsAndHashCode
public class Word implements Iterable<Symbol> {


    private ArrayList<Symbol> content;

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
        });

        return sb.toString();
    }

    public void append(Symbol symbol) {

        if (symbol == null) {
            throw new IllegalArgumentException("null argument");
        }

        if (Symbol.EMPTY_SYMBOL.equals(symbol)) {
            return;
        }

        if (content.size() > 1) {
            content.add(symbol);
            return;
        }

        if (content.get(0).equals(Symbol.EMPTY_SYMBOL)) {
            content.set(0, symbol);
            return;
        }

        content.add(symbol);
    }

    public void insertAt(int index, Word word) {

        if (word.length() == 0) {
            return;
        }

        int oldLength = length();

        if (index > oldLength || index < 0) {
            throw new IllegalArgumentException("Index " + index + " is out of bounds, size is " + content.size());
        }

        int wordLength = word.length();

        Symbol symbol = new Symbol("!");
        for (int i = 0; i < wordLength; i++) {

            append(symbol);
        }


        if (index == oldLength) {
            for (int j = index; j < index + wordLength; j++) {
                content.set(j, word.content.get(j - index));
            }

            return;
        }

        for (int i = 0, j = length() - 1; j >= index + wordLength; j--, i++) {
            content.set(j, content.get(oldLength - 1 - i));
        }

        for (int i = 0; i < wordLength; i++) {
            content.set(i + index, word.content.get(i));
        }
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
