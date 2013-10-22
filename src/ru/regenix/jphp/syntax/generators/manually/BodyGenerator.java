package ru.regenix.jphp.syntax.generators.manually;

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

    @Override
    public BodyStmtToken getToken(Token current, ListIterator<Token> iterator) {
        if (isOpenedBrace(current, BraceExprToken.Kind.BLOCK)){
            List<ExprStmtToken> instructions = new ArrayList<ExprStmtToken>();
            while (iterator.hasNext()){
                current = nextToken(iterator);
                ExprStmtToken expr = analyzer.generator(ExprGenerator.class).getToken(current, iterator);
                if (expr == null)
                    break;

                instructions.add(expr);
            }

            if (instructions.isEmpty())
                return null;

            BodyStmtToken result = new BodyStmtToken(TokenMeta.of(instructions));
            result.setInstructions(instructions);
            return result;
        }
        return null;
    }

    @Override
    public boolean isAutomatic() {
        return false;
    }
}
