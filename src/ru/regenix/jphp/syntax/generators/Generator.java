package ru.regenix.jphp.syntax.generators;

import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.exceptions.ParseException;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;

import java.util.ListIterator;

abstract public class Generator<T extends Token> {

    protected final SyntaxAnalyzer analyzer;

    public Generator(SyntaxAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    protected void checkUnexpectedEnd(Token current, ListIterator<Token> iterator){
        if (!iterator.hasNext())
            throw new ParseException(
                    Messages.ERR_PARSE_UNEXPECTED_END_OF_FILE.fetch(),
                    current.getMeta().toTraceInfo(analyzer.getFile())
            );
    }

    abstract public T getToken(Token current, ListIterator<Token> iterator);
}
