package ru.regenix.jphp.syntax.generators;

import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.stmt.EchoRawToken;
import ru.regenix.jphp.lexer.tokens.stmt.ExprStmtToken;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;

import java.util.ListIterator;

public class EchoRawGenerator extends Generator<ExprStmtToken> {
    public EchoRawGenerator(SyntaxAnalyzer analyzer) {
        super(analyzer);
    }

    @Override
    public ExprStmtToken getToken(Token current, ListIterator<Token> iterator) {
        if (current instanceof EchoRawToken){
            return new ExprStmtToken(current);
        }
        return null;
    }

    @Override
    public boolean isAutomatic() {
        return false;
    }
}
