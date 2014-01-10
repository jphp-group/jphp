package ru.regenix.jphp.syntax.generators;


import ru.regenix.jphp.common.Separator;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;
import ru.regenix.jphp.syntax.generators.manually.SimpleExprGenerator;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.Token;
import ru.regenix.jphp.tokenizer.token.expr.CommaToken;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;
import ru.regenix.jphp.tokenizer.token.expr.ValueExprToken;
import ru.regenix.jphp.tokenizer.token.expr.operator.AssignExprToken;
import ru.regenix.jphp.tokenizer.token.expr.value.FulledNameToken;
import ru.regenix.jphp.tokenizer.token.expr.value.NameToken;
import ru.regenix.jphp.tokenizer.token.stmt.ConstStmtToken;
import ru.regenix.jphp.tokenizer.token.stmt.ExprStmtToken;

import java.util.ListIterator;

public class ConstGenerator extends Generator<ConstStmtToken> {

    @SuppressWarnings("unchecked")
    public final static Class<? extends Token>[] valueTokens = new Class[]{
            ValueExprToken.class, OperatorExprToken.class
    };

    public ConstGenerator(SyntaxAnalyzer analyzer) {
        super(analyzer);
    }

    /*@SuppressWarnings("unchecked")
    protected void processBody(ConstStmtToken result, ListIterator<Token> iterator){
        Token current = nextToken(iterator);
        if (!(current instanceof AssignExprToken))
            unexpectedToken(current, "=");

        ExprStmtToken value = analyzer.generator(SimpleExprGenerator.class).getToken(nextToken(iterator), iterator);
        if (value == null)
            unexpectedToken(nextToken(iterator));

        result.setValue(value);
    }*/

    @Override
    public ConstStmtToken getToken(Token current, ListIterator<Token> iterator) {
        if (current instanceof ConstStmtToken){
            ConstStmtToken result = (ConstStmtToken)current;

            Token prev = null;
            if (analyzer.getClazz() == null)
                result.setNamespace(analyzer.getNamespace());

            while (true){
                Token next = nextToken(iterator);
                if (next instanceof NameToken){
                    if (next instanceof FulledNameToken)
                        unexpectedToken(next, TokenType.T_STRING);

                    Token token = nextToken(iterator);
                    if (!(token instanceof AssignExprToken))
                        unexpectedToken(token, "=");

                    ExprStmtToken value = analyzer.generator(SimpleExprGenerator.class)
                        .getToken(nextToken(iterator), iterator, Separator.COMMA_OR_SEMICOLON, null);
                    if (!isBreak(iterator.previous())){
                        iterator.next();
                    }
                    if (value == null)
                        unexpectedToken(iterator.previous());

                    result.add((NameToken)next, value);
                } else if (next instanceof CommaToken){
                    if (prev instanceof CommaToken)
                        unexpectedToken(next);
                    prev = next;
                } else if (isBreak(next)){
                    break;
                } else
                    unexpectedToken(next, TokenType.T_STRING);
            }
            return result;
        }
        return null;
    }
}
