package org.develnext.jphp.core.syntax.generators;

import org.develnext.jphp.core.common.Separator;
import org.develnext.jphp.core.syntax.SyntaxAnalyzer;
import org.develnext.jphp.core.syntax.generators.manually.SimpleExprGenerator;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ThrowStmtToken;

import java.util.ListIterator;

public class ThrowGenerator extends Generator<ThrowStmtToken> {

    public ThrowGenerator(SyntaxAnalyzer analyzer) {
        super(analyzer);
    }

    @Override
    public ThrowStmtToken getToken(Token current, ListIterator<Token> iterator) {
        if (current instanceof ThrowStmtToken){
            ThrowStmtToken result = (ThrowStmtToken)current;
            ExprStmtToken exception = analyzer.generator(SimpleExprGenerator.class).getToken(
                    nextToken(iterator), iterator, Separator.SEMICOLON, null
            );
            if (exception == null)
                unexpectedToken(iterator.previous());

            result.setException(exception);
            return result;
        }
        return null;
    }
}
