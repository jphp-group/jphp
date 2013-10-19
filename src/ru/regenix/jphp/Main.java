package ru.regenix.jphp;

import ru.regenix.jphp.env.Context;
import ru.regenix.jphp.env.Environment;
import ru.regenix.jphp.lexer.Tokenizer;
import ru.regenix.jphp.lexer.tokens.Token;

public class Main {

    public static void main(String[] args) {

        Environment environment = new Environment();
        Context context = new Context(environment, null);

        Tokenizer tokenizer = new Tokenizer(context, "0 == 10; `cmd`;");

        Token token;
        while ((token = tokenizer.nextToken()) != null){
            System.out.println(token.toString());
        }
    }
}
