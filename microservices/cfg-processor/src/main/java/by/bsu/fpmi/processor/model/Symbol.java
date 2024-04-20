package by.bsu.fpmi.processor.model;

public record Symbol(String content) implements Comparable<Symbol> {
    public static final Symbol EMPTY_SYMBOL = new Symbol("_");

    public Symbol(String content) {
        this.content = content.intern();
    }

    @Override
    public String toString() {
        return content;
    }

    @Override
    public int compareTo(Symbol o) {

        int leftCodePointCount = content.codePointCount(0, content.length());
        int rightCodePointCount = o.content.codePointCount(0, o.content.length());

        if (leftCodePointCount < rightCodePointCount) {
            return -1;
        } else if (leftCodePointCount > rightCodePointCount) {
            return 1;
        }

        for (int i = 0; i < leftCodePointCount; i++) {

            int nextCodePointLeftIndex = content.offsetByCodePoints(0, i);
            int nextCodePointRightIndex = o.content.offsetByCodePoints(0, i);

            int codePointLeft = content.codePointAt(nextCodePointLeftIndex);
            int codePointAtRight = o.content.codePointAt(nextCodePointRightIndex);
            if (codePointLeft == codePointAtRight) {
                continue;
            }

            if (codePointLeft < codePointAtRight) {
                return -1;
            }
            return 1;
        }

        return 0;
    }
}
