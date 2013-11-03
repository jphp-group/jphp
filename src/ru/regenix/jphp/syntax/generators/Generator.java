package ru.regenix.jphp.syntax.generators;

import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.exceptions.ParseException;
import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.expr.BraceExprToken;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;

import java.util.ListIterator;

abstract public class Generator<T extends Token> {

    protected final SyntaxAnalyzer analyzer;

    public Generator(SyntaxAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    public boolean isAutomatic(){
        return true;
    }

    /**
     * @throws ParseException
     * @param token
     */
    protected void unexpectedToken(Token token){
        Object unexpected = token.getType();
        if (token.getType() == TokenType.T_J_CUSTOM)
            unexpected = token.getWord();

        analyzer.getContext().triggerError( new ParseException(
                Messages.ERR_PARSE_UNEXPECTED_X.fetch(unexpected),
                token.toTraceInfo(analyzer.getContext())
        ) );
    }

    protected void unexpectedToken(Token token, Object expected){
        Object unexpected = token.getType();
        if (token.getType() == TokenType.T_J_CUSTOM)
            unexpected = token.getWord();

        analyzer.getContext().triggerError(new ParseException(
                Messages.ERR_PARSE_UNEXPECTED_X_EXPECTED_Y.fetch(unexpected, expected),
                token.toTraceInfo(analyzer.getContext())
        ));
    }


    @SuppressWarnings("unchecked")
    protected <T extends Token> T nextAndExpected(ListIterator<Token> iterator, Class<T> clazz){
        checkUnexpectedEnd(iterator);

        Token next = iterator.next();
        if (!isTokenClass(next, clazz))
            unexpectedToken(next);

        return (T) next;
    }

    protected boolean isTokenClass(Token token, Class<? extends Token>... classes){
        if (token == null)
            return false;

        Class<? extends Token> current = token.getClass();
        for (Class<? extends Token> clazz : classes){
            if (clazz == current || current.isAssignableFrom(clazz))
                return true;
        }
        return false;
    }

    protected boolean isOpenedBrace(Token token, BraceExprToken.Kind kind){
        if (token instanceof BraceExprToken)
            return ((BraceExprToken) token).isOpened(kind);
        return false;
    }

    protected boolean isClosedBrace(Token token, BraceExprToken.Kind kind){
        if (token instanceof BraceExprToken)
            return ((BraceExprToken) token).isClosed(kind);
        return false;
    }

    protected void checkUnexpectedEnd(ListIterator<Token> iterator){
        if (!iterator.hasNext()){
            iterator.previous();
            Token current = iterator.next();

            analyzer.getContext().triggerError(new ParseException(
                    Messages.ERR_PARSE_UNEXPECTED_END_OF_FILE.fetch(),
                    current.toTraceInfo(analyzer.getContext())
            ));
        }
    }

    protected Token nextToken(ListIterator<Token> iterator){
        checkUnexpectedEnd(iterator);
        return iterator.next();
    }

    abstract public T getToken(Token current, ListIterator<Token> iterator);
}
