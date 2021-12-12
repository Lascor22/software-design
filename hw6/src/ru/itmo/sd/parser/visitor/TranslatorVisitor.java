package ru.itmo.sd.parser.visitor;

import ru.itmo.sd.parser.tokenizer.BraceToken;
import ru.itmo.sd.parser.tokenizer.NumberToken;
import ru.itmo.sd.parser.tokenizer.OpToken;

public class TranslatorVisitor implements TokenVisitor {
    private final StringBuilder buffer = new StringBuilder();

    @Override
    public void visit(BraceToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(NumberToken token) {
        buffer.append(token.getNumber()).append(" ");
    }

    @Override
    public void visit(OpToken token) {
        token.LEFT.accept(this);
        token.RIGHT.accept(this);
        buffer.append(token.OPERATION).append(" ");
    }

    public String getCode() {
        return buffer.toString();
    }
}
