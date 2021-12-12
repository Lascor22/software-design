package ru.itmo.sd.parser.tokenizer;

public class OpToken extends AbsToken {
    public final Token LEFT, RIGHT;
    public final String OPERATION;

    public OpToken(String value) {
        super(value);
        this.LEFT = this.RIGHT = null;
        this.OPERATION = value;
    }

    public OpToken(String value, Token left, Token right) {
        super(value);
        this.LEFT = left;
        this.RIGHT = right;
        this.OPERATION = value;
    }

    public String getOperation() {
        return OPERATION;
    }

    private static final String[][] LEVELS = {
            {"+", "-"},
            {"*", "/"}
    };

    public static int getMaxPriority() {
        return LEVELS.length - 1;
    }

    public static int getPriorityLevel(OpToken operation) {
        final String value = operation.value;
        for (int i = 0; i <= getMaxPriority(); i++) {
            for (String op : LEVELS[i]) {
                if (op.equals(value)) {
                    return i;
                }
            }
        }
        return getMaxPriority() + 1;
    }

    @Override
    public String toString() {
        return "(" + LEFT + " " + OPERATION + " " + RIGHT + ")";
    }
}
