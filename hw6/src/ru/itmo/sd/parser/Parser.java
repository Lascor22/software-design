package ru.itmo.sd.parser;

import ru.itmo.sd.parser.tokenizer.Token;
import ru.itmo.sd.parser.tokenizer.tree.TreeBuilder;
import ru.itmo.sd.parser.visitor.EvaluateVisitor;
import ru.itmo.sd.parser.visitor.TranslatorVisitor;
import ru.itmo.sd.parser.tokenizer.Tokenizer;

public class Parser {

    public static void main(String... args) {
        String expression;
        if (args.length > 0) {
            expression = String.join("", args);
        } else {
            expression = "(23 + 10) * 5 - 3 * (32 + 5) * (10 - 4 * 5) + 8 / 2";
        }
        System.out.println(expression);

        Tokenizer tokenizer = new Tokenizer();
        tokenizer.parse(expression);

        Token root = new TreeBuilder(tokenizer.getTokens()).build();
        System.out.println(root);

        TranslatorVisitor translator = new TranslatorVisitor();
        root.accept(translator);
        System.out.println(translator.getCode());

        EvaluateVisitor evaluator = new EvaluateVisitor();
        root.accept(evaluator);
        System.out.println(evaluator.getValue());
    }

}
