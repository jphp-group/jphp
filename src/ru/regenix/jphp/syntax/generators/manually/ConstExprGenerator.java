package ru.regenix.jphp.syntax.generators.manually;

import ru.regenix.jphp.lexer.tokens.expr.CommaToken;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.expr.ArrayExprToken;
import ru.regenix.jphp.lexer.tokens.expr.BraceExprToken;
import ru.regenix.jphp.lexer.tokens.expr.operator.ArrayKeyValueExprToken;
import ru.regenix.jphp.lexer.tokens.expr.value.*;
import ru.regenix.jphp.lexer.tokens.stmt.ExprStmtToken;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;
import ru.regenix.jphp.syntax.generators.ExprGenerator;
import ru.regenix.jphp.syntax.generators.Generator;

import java.util.ListIterator;

/**
 * Const Expressions
 *
 * Examples:
 *      0..9 - numbers,
 *      strings,
 *      true / false,
 *      double,
 *      static array - array("aa", 10, 20 => abc, 3 => 100)
 *      name - const,
 *      call static, simple Class::const
 */
public class ConstExprGenerator extends Generator<ExprStmtToken> {

    @SuppressWarnings("unchecked")
    public final static Class<? extends Token>[] allowed = new Class[]{
            BooleanExprToken.class,
            StringExprToken.class,
            IntegerExprToken.class,
            HexExprValue.class,
            DoubleExprToken.class,
            ArrayExprToken.class,
            ArrayKeyValueExprToken.class,
            CommaToken.class
    };

    public ConstExprGenerator(SyntaxAnalyzer analyzer) {
        super(analyzer);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ExprStmtToken getToken(Token current, ListIterator<Token> iterator) {
        ExprStmtToken expr = (ExprStmtToken)analyzer.generateToken(current, iterator, ExprGenerator.class);
        for (Token token : expr.getTokens()){
            if (isOpenedBrace(token, BraceExprToken.Kind.SIMPLE) ||
                    isClosedBrace(token, BraceExprToken.Kind.SIMPLE))
                continue;

            if (!isTokenClass(token, allowed))
                unexpectedToken(token);
        }
        return expr;
    }

    @Override
    public boolean isAutomatic() {
        return false;
    }
}
