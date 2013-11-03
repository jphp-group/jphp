package ru.regenix.jphp.syntax;

import ru.regenix.jphp.env.Context;
import ru.regenix.jphp.env.Environment;
import ru.regenix.jphp.lexer.Tokenizer;
import ru.regenix.jphp.lexer.tokens.Token;

import java.io.File;
import java.util.List;

abstract class AbstractSyntaxTestCase {

    protected Environment environment = new Environment();

    protected List<Token> getSyntaxTree(String code){
        Tokenizer tokenizer = new Tokenizer(new Context(environment, code));
        SyntaxAnalyzer analyzer = new SyntaxAnalyzer(tokenizer);

        return analyzer.getTree();
    }
}
