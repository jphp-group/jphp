package ru.regenix.jphp.syntax.generators.manually;


import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.expr.*;
import ru.regenix.jphp.lexer.tokens.expr.operator.DynamicAccessExprToken;
import ru.regenix.jphp.lexer.tokens.expr.operator.StaticAccessExprToken;
import ru.regenix.jphp.lexer.tokens.expr.value.VariableExprToken;
import ru.regenix.jphp.lexer.tokens.stmt.ExprStmtToken;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;
import ru.regenix.jphp.syntax.generators.Generator;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class SimpleExprGenerator extends Generator<ExprStmtToken> {

    public SimpleExprGenerator(SyntaxAnalyzer analyzer) {
        super(analyzer);
    }

    protected CallExprToken processCall(Token name, ListIterator<Token> iterator){
        // TODO
    }

    protected DynamicCallExprToken processDynamicCall(ListIterator<Token> iterator){
        // TODO
    }

    protected StaticCallExprToken processStaticCall(ListIterator<Token> iterator){
        // TODO
    }

    @Override
    public ExprStmtToken getToken(Token current, ListIterator<Token> iterator) {
        List<Token> tokens = new ArrayList<Token>();
        Token previous = null;
        do {
            if (isOpenedBrace(current, BraceExprToken.Kind.SIMPLE)){
                if (previous != null
                        && (previous instanceof NameToken || previous instanceof VariableExprToken)){
                    tokens.add(processCall(previous, iterator));
                }
            } else if (current instanceof DynamicAccessExprToken){
                tokens.add(processDynamicCall(iterator));
            } else if (current instanceof StaticAccessExprToken){
                tokens.add(processStaticCall(iterator));
            } else {
                tokens.add(current);
            }

            previous = current;
            current  = nextToken(iterator);
        } while (iterator.hasNext());

        if (tokens.isEmpty())
            return null;

        return new ExprStmtToken(tokens);
    }

    @Override
    public boolean isAutomatic() {
        return false;
    }
}
