package ru.regenix.jphp;

import ru.regenix.jphp.lexer.Tokenizer;
import ru.regenix.jphp.lexer.tokens.Token;

public class Main {

    public static void main(String[] args) {
        Tokenizer tokenizer = new Tokenizer("$a = \"I\\m working\"; `cmd`;");

        Token token;
        while ((token = tokenizer.nextToken()) != null){
            System.out.println(token.toString());
        }
    }
}
