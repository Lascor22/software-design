package ru.itmo.sd.parser.tokenizer;

import ru.itmo.sd.parser.visitor.TokenVisitor;

public interface Token {
    void accept(TokenVisitor visitor);
}
