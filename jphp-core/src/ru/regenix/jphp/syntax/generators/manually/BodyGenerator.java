package ru.regenix.jphp.syntax.generators.manually;

import ru.regenix.jphp.tokenizer.token.ColonToken;
import ru.regenix.jphp.tokenizer.token.SemicolonToken;
import ru.regenix.jphp.tokenizer.token.Token;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.BraceExprToken;
import ru.regenix.jphp.tokenizer.token.stmt.BodyStmtToken;
import ru.regenix.jphp.tokenizer.token.stmt.ExprStmtToken;
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
        return getToken(current, iterator, absolute, true, endTokens);
    }

    @SuppressWarnings("unchecked")
    public BodyStmtToken getToken(Token current, ListIterator<Token> iterator, boolean absolute, boolean returnNull,
                                  Class<? extends Token>... endTokens) {
        boolean alternativeSyntax = false;
        List<ExprStmtToken> instructions = new ArrayList<ExprStmtToken>();
        if (isOpenedBrace(current, BraceExprToken.Kind.BLOCK)
                /*|| current instanceof SemicolonToken*/){
            //if (!returnNull)
                returnNull = false;
            while ((current = nextToken(iterator)) != null){
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
            else
                alternativeSyntax = true;

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
            if (expr != null) {
                if (expr.getTokens().size() == 1 && expr.getTokens().get(0) instanceof SemicolonToken) {
                    // nop
                } else
                    instructions.add(expr);
            }
        }

        if (instructions.isEmpty() && returnNull)
            return null;

        BodyStmtToken result = new BodyStmtToken(TokenMeta.of(instructions));
        result.setInstructions(instructions);
        result.setAlternativeSyntax(alternativeSyntax);
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
