package org.develnext.jphp.core.syntax;

import php.runtime.common.LangMode;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import org.develnext.jphp.core.tokenizer.Tokenizer;
import org.develnext.jphp.core.tokenizer.token.Token;

import java.io.IOException;
import java.util.List;

abstract class AbstractSyntaxTestCase {

    protected Environment environment = new Environment();

    protected List<Token> getSyntaxTree(String code){
        Tokenizer tokenizer = null;
        try {
            tokenizer = new Tokenizer(new Context(code));
            environment.scope.setLangMode(LangMode.DEFAULT);
            SyntaxAnalyzer analyzer = new SyntaxAnalyzer(environment, tokenizer);

            return analyzer.getTree();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
