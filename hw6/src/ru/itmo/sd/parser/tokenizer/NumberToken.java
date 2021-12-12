package ru.itmo.sd.parser.tokenizer;


public class NumberToken extends AbsToken {

    public NumberToken(String value) {
        super(value);
    }

    public int getNumber() {
        return Integer.parseInt(value);
    }

    @Override
    public String toString() {
        return "Num(" + this.value + ")";
    }
}
