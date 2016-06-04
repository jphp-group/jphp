package org.develnext.jphp.core.syntax.generators;

import org.develnext.jphp.core.tokenizer.token.CommentToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.NameToken;
import php.runtime.common.Messages;
import php.runtime.exceptions.ParseException;
import php.runtime.exceptions.support.ErrorType;
import org.develnext.jphp.core.syntax.SyntaxAnalyzer;
import org.develnext.jphp.core.tokenizer.token.BreakToken;
import org.develnext.jphp.core.tokenizer.token.SemicolonToken;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.token.expr.BraceExprToken;

import java.util.ListIterator;

abstract public class Generator<T extends Token> {

    protected final SyntaxAnalyzer analyzer;

    public Generator(SyntaxAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    public boolean isAutomatic(){
        return true;
    }

    public boolean isSingleton(){
        return true;
    }

    protected void unexpectedToken(ListIterator<Token> iterator){
        unexpectedToken(iterator.previous());
    }
    /**
     * @throws ParseException
     * @param token
     */
    protected void unexpectedToken(Token token){
        analyzer.getEnvironment().error(
                token.toTraceInfo(analyzer.getContext()), ErrorType.E_PARSE,
                Messages.ERR_PARSE_UNEXPECTED_X.fetch(token.getWord())
        );
    }

    protected void unexpectedToken(Token token, Object expected){
        unexpectedToken(token, expected, true);
    }

    protected void unexpectedToken(Token token, Object expected, boolean quotes){
        analyzer.getEnvironment().error(
                token.toTraceInfo(analyzer.getContext()), ErrorType.E_PARSE,
                quotes
                        ? Messages.ERR_PARSE_UNEXPECTED_X_EXPECTED_Y.fetch(token.getWord(), expected)
                        : Messages.ERR_PARSE_UNEXPECTED_X_EXPECTED_Y_NO_QUOTES.fetch(token.getWord(), expected)
        );
    }


    @SuppressWarnings("unchecked")
    protected <K extends Token> K nextAndExpected(ListIterator<Token> iterator, Class<K> clazz){
        Token next = nextToken(iterator);

        if (!isTokenClass(next, clazz))
            unexpectedToken(next);

        return (K) next;
    }

    @SuppressWarnings("unchecked")
    protected <K extends Token> K nextAndExpectedSensitive(ListIterator<Token> iterator, Class<K> clazz){
        Token next = nextTokenSensitive(iterator);

        if (!isTokenClass(next, clazz))
            unexpectedToken(next);

        return (K) next;
    }

    @SuppressWarnings("unchecked")
    protected boolean isTokenClass(Token token, Class<? extends Token>... classes){
        if (token == null)
            return false;
        if (classes == null)
            return false;

        Class<? extends Token> current = token.getClass();
        for (Class<? extends Token> clazz : classes){
            if (clazz != null && (clazz == current || clazz.isAssignableFrom(current)))
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

    protected boolean isBreak(Token token){
        return token instanceof SemicolonToken || token instanceof BreakToken;
    }

    protected boolean isClosedBrace(Token token){
        if (token instanceof BraceExprToken)
            return ((BraceExprToken) token).isClosed();
        return false;
    }

    protected void unexpectedEnd(Token token){
        analyzer.getEnvironment().error(
                token.toTraceInfo(analyzer.getContext()), ErrorType.E_PARSE,
                Messages.ERR_PARSE_UNEXPECTED_END_OF_FILE
        );
    }

    protected void checkUnexpectedEnd(ListIterator<Token> iterator){
        if (!iterator.hasNext()){
            iterator.previous();
            Token current = iterator.next();

            analyzer.getEnvironment().error(
                    current.toTraceInfo(analyzer.getContext()), ErrorType.E_PARSE,
                    Messages.ERR_PARSE_UNEXPECTED_END_OF_FILE
            );
        }
    }

    protected Token makeSensitive(Token token) {
        if (token instanceof NameToken) {
            return token;
        }

        if (token.isNamedToken()) {
            return new NameToken(token.getMeta());
        }

        return token;
    }

    /**
     * Processes named token and returns them as named tokens.
     */
    @SafeVarargs
    protected final Token nextTokenSensitive(ListIterator<Token> iterator, Class<? extends Token>... excludes) {
        Token token = nextToken(iterator);

        if (token instanceof NameToken) {
            return token;
        }

        if (excludes != null && isTokenClass(token, excludes)) {
            return token;
        }

        if (token.isNamedToken()) {
            return new NameToken(token.getMeta());
        }

        return token;
    }

    protected Token nextToken(ListIterator<Token> iterator){
        checkUnexpectedEnd(iterator);
        Token tk = iterator.next();
        if (tk instanceof CommentToken)
            return nextToken(iterator);
        return tk;
    }

    protected Token nextTokenAndPrev(ListIterator<Token> iterator){
        checkUnexpectedEnd(iterator);
        Token result = iterator.next();
        if (result instanceof CommentToken) {
            return nextTokenAndPrev(iterator);
        }
        iterator.previous();
        return result;
    }

    abstract public T getToken(Token current, ListIterator<Token> iterator);
}
