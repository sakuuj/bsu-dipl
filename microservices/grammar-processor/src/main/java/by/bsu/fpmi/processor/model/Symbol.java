package by.bsu.fpmi.processor.model;

public record Symbol(String content) implements Comparable<Symbol> {

    public static final Symbol EMPTY_SYMBOL = new Symbol("_");
    public static final Symbol RESERVED_SYMBOL = new Symbol("__$");

    public Symbol(String content) {
        this.content = content.intern();
    }

    @Override
    public String toString() {
        return content;
    }

    @Override
    public int compareTo(Symbol o) {

        return content.compareTo(o.content);
    }
}
