package ru.regenix.jphp;

import ru.regenix.jphp.env.Context;
import ru.regenix.jphp.env.Environment;
import ru.regenix.jphp.lexer.Tokenizer;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;

public class Main {

    public static void main(String[] args) {
        Environment environment = new Environment();
        Context context = new Context(environment, null);

        Tokenizer tokenizer = new Tokenizer(context, "0 ==10; `cmd`;");
        SyntaxAnalyzer analyzer = new SyntaxAnalyzer(tokenizer);

        for(Token token : analyzer.getTree()){
            System.out.println(token.toString());
        }
    }
}
