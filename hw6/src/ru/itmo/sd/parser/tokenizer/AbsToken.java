package ru.itmo.sd.parser.tokenizer;

import ru.itmo.sd.parser.visitor.TokenVisitor;

import java.lang.reflect.InvocationTargetException;

public abstract class AbsToken implements Token {
    protected String value;

    public AbsToken(String value) {
        this.value = value;
    }

    public void accept(TokenVisitor visitor) {
        try {
            visitor.getClass().getMethod("visit", this.getClass()).invoke(visitor, this);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
