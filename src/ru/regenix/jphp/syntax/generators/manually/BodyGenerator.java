package ru.regenix.jphp.syntax.generators.manually;

import ru.regenix.jphp.lexer.tokens.ColonToken;
import ru.regenix.jphp.lexer.tokens.SemicolonToken;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.BraceExprToken;
import ru.regenix.jphp.lexer.tokens.stmt.BodyStmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.EndStmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.ExprStmtToken;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;
import ru.regenix.jphp.syntax.generators.ExprGenerator;
import ru.regenix.jphp.syntax.generators.Generator;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class BodyGenerator extends Generator<BodyStmtToken> {

    public BodyGenerator(SyntaxAnalyzer analyzer) {
        super(analyzer);
    }

    @SuppressWarnings("unchecked")
    public BodyStmtToken getToken(Token current, ListIterator<Token> iterator,
                                  Class<? extends EndStmtToken> endToken) {
        List<ExprStmtToken> instructions = new ArrayList<ExprStmtToken>();
        if (isOpenedBrace(current, BraceExprToken.Kind.BLOCK)
                || current instanceof SemicolonToken){
            while (iterator.hasNext()){
                current = nextToken(iterator);
                ExprStmtToken expr = analyzer.generator(ExprGenerator.class).getToken(
                        current,
                        iterator,
                        current instanceof SemicolonToken ? EndStmtToken.class : null
                );
                if (expr == null)
                    break;

                instructions.add(expr);
            }
        } else if (current instanceof ColonToken){
            if (endToken == null)
                unexpectedToken(current);

            while (iterator.hasNext()){
                current = nextToken(iterator);
                ExprStmtToken expr = analyzer.generator(ExprGenerator.class)
                        .getToken(current, iterator, endToken);
                if (expr == null)
                    break;

                instructions.add(expr);
            }
        } else {
            ExprStmtToken expr = analyzer.generator(ExprGenerator.class).getToken(current, iterator);
            if (expr != null)
                instructions.add(expr);
        }

        if (instructions.isEmpty())
            return null;

        BodyStmtToken result = new BodyStmtToken(TokenMeta.of(instructions));
        result.setInstructions(instructions);
        return result;
    }

    @Override
    public BodyStmtToken getToken(Token current, ListIterator<Token> iterator) {
        return getToken(current, iterator, null);
    }

    @Override
    public boolean isAutomatic() {
        return false;
    }
}
