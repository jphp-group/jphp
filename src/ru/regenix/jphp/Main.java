package ru.regenix.jphp;

import ru.regenix.jphp.env.Context;
import ru.regenix.jphp.env.Environment;
import ru.regenix.jphp.lexer.Tokenizer;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        Environment environment = new Environment();
        Context context = new Context(environment, null);

        Tokenizer tokenizer = new Tokenizer(context, "if(true){ function myfunc(){ } }");
        SyntaxAnalyzer analyzer = new SyntaxAnalyzer(tokenizer);

        List<Token> tree = analyzer.getTree();
        for(Token token : tree){
            System.out.println(token.toString());
        }
    }
}
