package org.develnext.jphp.core.syntax.generators.manually;

import org.develnext.jphp.core.tokenizer.token.ColonToken;
import org.develnext.jphp.core.tokenizer.token.SemicolonToken;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.BraceExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.YieldExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.BodyStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;
import org.develnext.jphp.core.syntax.SyntaxAnalyzer;
import org.develnext.jphp.core.syntax.generators.ExprGenerator;
import org.develnext.jphp.core.syntax.generators.Generator;

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
                   // nop break;
                } else {
                    instructions.add(expr);
                }
            }
        } else {
            ExprStmtToken expr = analyzer.generator(ExprGenerator.class).getToken(current, iterator);
            if (expr != null) {
                if (expr.getTokens().size() == 1 && expr.getTokens().get(0) instanceof SemicolonToken) {
                    // nop
                } else {
                    instructions.add(expr);
                }
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
    @SuppressWarnings("unchecked")
    public BodyStmtToken getToken(Token current, ListIterator<Token> iterator) {
        return getToken(current, iterator, (Class<? extends Token>[])null);
    }

    @Override
    public boolean isAutomatic() {
        return false;
    }
}
