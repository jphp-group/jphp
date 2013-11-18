package ru.regenix.jphp.syntax;

import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.tokenizer.Tokenizer;
import ru.regenix.jphp.tokenizer.token.Token;

import java.util.List;

abstract class AbstractSyntaxTestCase {

    protected Environment environment = new Environment();

    protected List<Token> getSyntaxTree(String code){
        Tokenizer tokenizer = new Tokenizer(new Context(environment, code));
        SyntaxAnalyzer analyzer = new SyntaxAnalyzer(tokenizer);

        return analyzer.getTree();
    }
}
