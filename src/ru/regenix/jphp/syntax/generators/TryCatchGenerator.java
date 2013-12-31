package ru.regenix.jphp.syntax.generators;

import ru.regenix.jphp.syntax.SyntaxAnalyzer;
import ru.regenix.jphp.syntax.generators.manually.BodyGenerator;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.Token;
import ru.regenix.jphp.tokenizer.token.expr.BraceExprToken;
import ru.regenix.jphp.tokenizer.token.expr.value.FulledNameToken;
import ru.regenix.jphp.tokenizer.token.expr.value.NameToken;
import ru.regenix.jphp.tokenizer.token.expr.value.VariableExprToken;
import ru.regenix.jphp.tokenizer.token.stmt.BodyStmtToken;
import ru.regenix.jphp.tokenizer.token.stmt.CatchStmtToken;
import ru.regenix.jphp.tokenizer.token.stmt.TryStmtToken;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class TryCatchGenerator extends Generator<TryStmtToken> {

    public TryCatchGenerator(SyntaxAnalyzer analyzer) {
        super(analyzer);
    }

    public void processCatch(CatchStmtToken result, ListIterator<Token> iterator){
        Token next = nextToken(iterator);
        if (!isOpenedBrace(next, BraceExprToken.Kind.SIMPLE))
            unexpectedToken(next, "(");

        next = nextToken(iterator);
        if (!(next instanceof NameToken))
            unexpectedToken(next, TokenType.T_STRING);

        FulledNameToken exception = analyzer.getRealName((NameToken)next);
        result.setException(exception);

        next = nextToken(iterator);
        if (!(next instanceof VariableExprToken))
            unexpectedToken(next, TokenType.T_VARIABLE);

        VariableExprToken variable = (VariableExprToken)next;
        result.setVariable(variable);

        if (analyzer.getFunction() != null){
            analyzer.getFunction().getUnstableLocal().add(variable);
        }
        analyzer.getLocalScope().add(variable);

        next = nextToken(iterator);
        if (!isClosedBrace(next, BraceExprToken.Kind.SIMPLE))
            unexpectedToken(next, ")");

        BodyStmtToken body = analyzer.generator(BodyGenerator.class).getToken(nextToken(iterator), iterator);
        result.setBody(body);
    }

    @Override
    public TryStmtToken getToken(Token current, ListIterator<Token> iterator) {
        if (current instanceof TryStmtToken){
            TryStmtToken result = (TryStmtToken)current;

            analyzer.addLocalScope(false);
            BodyStmtToken body = analyzer.generator(BodyGenerator.class).getToken(nextToken(iterator), iterator);
            result.setBody(body);

            List<CatchStmtToken> catches = new ArrayList<CatchStmtToken>();
            Token next = nextToken(iterator);
            if (next instanceof CatchStmtToken){
                do {
                    CatchStmtToken _catch = (CatchStmtToken) next;
                    processCatch(_catch, iterator);
                    catches.add(_catch);

                    if (!iterator.hasNext())
                        break;

                    next = iterator.next();
                    if (!(next instanceof CatchStmtToken)){
                        iterator.previous();
                        break;
                    }
                } while (true);
            } else
                unexpectedToken(next, TokenType.T_CATCH);

            result.setCatches(catches);
            result.setLocal(analyzer.removeLocalScope());
            return result;
        }
        return null;
    }
}
