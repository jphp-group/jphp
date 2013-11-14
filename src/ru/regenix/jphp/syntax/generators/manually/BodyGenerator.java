package ru.regenix.jphp.syntax.generators.manually;

import ru.regenix.jphp.lexer.tokens.ColonToken;
import ru.regenix.jphp.lexer.tokens.SemicolonToken;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.BraceExprToken;
import ru.regenix.jphp.lexer.tokens.stmt.BodyStmtToken;
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

    public BodyStmtToken getToken(Token current, ListIterator<Token> iterator,
                                  Class<? extends Token>... endTokens) {
        return getToken(current, iterator, false, endTokens);
    }

    @SuppressWarnings("unchecked")
    public BodyStmtToken getToken(Token current, ListIterator<Token> iterator, boolean absolute,
                                  Class<? extends Token>... endTokens) {
        List<ExprStmtToken> instructions = new ArrayList<ExprStmtToken>();
        if (isOpenedBrace(current, BraceExprToken.Kind.BLOCK)
                || current instanceof SemicolonToken){
            while (iterator.hasNext()){
                current = nextToken(iterator);
                ExprStmtToken expr = analyzer.generator(ExprGenerator.class).getToken(
                        current,
                        iterator,
                        BraceExprToken.class
                );
                if (expr == null){
                    break;
                }

                instructions.add(expr);
            }
        } else if (current instanceof ColonToken || (absolute && endTokens != null)){
            if (endTokens == null)
                unexpectedToken(current);

            if (!(current instanceof ColonToken))
                iterator.previous();

            while (iterator.hasNext()){
                current = nextToken(iterator);
                ExprStmtToken expr = analyzer.generator(ExprGenerator.class)
                        .getToken(current, iterator, endTokens);

                if (expr == null) {
                    iterator.previous();
                    break;
                } else if (expr.getTokens().size() == 1 && expr.getTokens().get(0) instanceof SemicolonToken){
                    break;
                }

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
