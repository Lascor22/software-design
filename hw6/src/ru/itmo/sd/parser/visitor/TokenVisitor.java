package ru.itmo.sd.parser.visitor;

import ru.itmo.sd.parser.tokenizer.BraceToken;
import ru.itmo.sd.parser.tokenizer.NumberToken;
import ru.itmo.sd.parser.tokenizer.OpToken;

public interface TokenVisitor {

    void visit(BraceToken token);

    void visit(NumberToken token);

    void visit(OpToken token);


}
