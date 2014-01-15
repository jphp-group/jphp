package ru.regenix.jphp.syntax;

import php.runtime.env.Context;
import php.runtime.env.Environment;
import ru.regenix.jphp.tokenizer.Tokenizer;
import ru.regenix.jphp.tokenizer.token.Token;

import java.io.IOException;
import java.util.List;

abstract class AbstractSyntaxTestCase {

    protected Environment environment = new Environment();

    protected List<Token> getSyntaxTree(String code){
        Tokenizer tokenizer = null;
        try {
            tokenizer = new Tokenizer(new Context(code));
            SyntaxAnalyzer analyzer = new SyntaxAnalyzer(environment, tokenizer);

            return analyzer.getTree();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
