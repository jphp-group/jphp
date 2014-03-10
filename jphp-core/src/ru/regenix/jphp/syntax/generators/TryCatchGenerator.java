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
import ru.regenix.jphp.tokenizer.token.stmt.FinallyStmtToken;
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

    protected void processFinally(TryStmtToken result, Token current, ListIterator<Token> iterator){
        if (result.getFinally() != null)
            unexpectedToken(current);

        result.setFinally(
                analyzer.generator(BodyGenerator.class).getToken(nextToken(iterator), iterator)
        );
        if (!iterator.hasNext())
            return;

        current = iterator.next();
        if (current instanceof CatchStmtToken)
            processCatches(result, current, iterator);
        else
            iterator.previous();
    }

    protected void processCatches(TryStmtToken result, Token current, ListIterator<Token> iterator){
        List<CatchStmtToken> catches = result.getCatches();

        do {
            CatchStmtToken _catch = (CatchStmtToken) current;
            processCatch(_catch, iterator);
            catches.add(_catch);

            if (!iterator.hasNext())
                break;

            current = iterator.next();
            if (!(current instanceof CatchStmtToken)){
                if (current instanceof FinallyStmtToken){
                    processFinally(result, current, iterator);
                    break;
                }

                iterator.previous();
                break;
            }
        } while (true);
    }

    @Override
    public TryStmtToken getToken(Token current, ListIterator<Token> iterator) {
        if (current instanceof TryStmtToken){
            TryStmtToken result = (TryStmtToken)current;

            analyzer.addLocalScope(false);
            BodyStmtToken body = analyzer.generator(BodyGenerator.class).getToken(nextToken(iterator), iterator);
            result.setBody(body);

            Token next = nextToken(iterator);
            result.setCatches(new ArrayList<CatchStmtToken>());

            if (next instanceof CatchStmtToken){
                processCatches(result, next, iterator);
            } else if (next instanceof FinallyStmtToken){
                processFinally(result, next, iterator);
            } else
                unexpectedToken(next, TokenType.T_CATCH);

            result.setLocal(analyzer.removeLocalScope());
            return result;
        }
        return null;
    }
}
