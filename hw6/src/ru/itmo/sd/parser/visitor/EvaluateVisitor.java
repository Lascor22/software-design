package ru.itmo.sd.parser.visitor;

import ru.itmo.sd.parser.tokenizer.BraceToken;
import ru.itmo.sd.parser.tokenizer.NumberToken;
import ru.itmo.sd.parser.tokenizer.OpToken;

import java.util.Stack;

public class EvaluateVisitor implements TokenVisitor {
    private final Stack<Integer> stack = new Stack<>();

    @Override
    public void visit(BraceToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(NumberToken token) {
        stack.push(token.getNumber());
    }

    @Override
    public void visit(OpToken token) {
        token.LEFT.accept(this);
        token.RIGHT.accept(this);
        var right = stack.pop();
        var left = stack.pop();
        var result = switch (token.getOperation()) {
            case "+" -> left + right;
            case "-" -> left - right;
            case "*" -> left * right;
            case "/" -> left / right;
            default -> throw new IllegalArgumentException();
        };
        stack.push(result);
    }

    public Integer getValue() {
        return stack.peek();
    }
}
