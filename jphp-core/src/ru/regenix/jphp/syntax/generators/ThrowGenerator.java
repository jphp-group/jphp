package ru.regenix.jphp.syntax.generators;

import php.runtime.common.Separator;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;
import ru.regenix.jphp.syntax.generators.manually.SimpleExprGenerator;
import ru.regenix.jphp.tokenizer.token.Token;
import ru.regenix.jphp.tokenizer.token.stmt.ExprStmtToken;
import ru.regenix.jphp.tokenizer.token.stmt.ThrowStmtToken;

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
