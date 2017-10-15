package org.develnext.jphp.core.syntax.generators;

import org.develnext.jphp.core.syntax.SyntaxAnalyzer;
import org.develnext.jphp.core.syntax.generators.manually.BodyGenerator;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.token.expr.BraceExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.OrExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.FulledNameToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.NameToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.VariableExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.BodyStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.CatchStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.FinallyStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.TryStmtToken;

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

        List<FulledNameToken> exceptions = new ArrayList<>();

        do {
            next = nextToken(iterator);
            if (!(next instanceof NameToken)) {
                if (exceptions.isEmpty()) {
                    unexpectedToken(next, TokenType.T_STRING);
                } else {
                    iterator.previous();
                    break;
                }
            }

            FulledNameToken exception = analyzer.getRealName((NameToken) next);
            exceptions.add(exception);

            next = nextToken(iterator);

            if (!(next instanceof OrExprToken)) {
                iterator.previous();
                break;
            }
        } while (true);

        result.setExceptions(exceptions);

        next = nextToken(iterator);
        if (!(next instanceof VariableExprToken))
            unexpectedToken(next, TokenType.T_VARIABLE);

        VariableExprToken variable = (VariableExprToken)next;
        result.setVariable(variable);

        if (analyzer.getFunction() != null){
            analyzer.getFunction().variable(variable).setUnstable(true);
        }
        analyzer.getScope().addVariable(variable);

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

            analyzer.addScope(false);
            BodyStmtToken body = analyzer.generator(BodyGenerator.class).getToken(nextToken(iterator), iterator);
            result.setBody(body);

            Token next = nextToken(iterator);
            result.setCatches(new ArrayList<CatchStmtToken>());

            if (next instanceof CatchStmtToken){
                processCatches(result, next, iterator);
            } else if (next instanceof FinallyStmtToken) {
                analyzer.getScope().setLevelForGoto(true);
                processFinally(result, next, iterator);
                analyzer.getScope().setLevelForGoto(false);
            } else
                unexpectedToken(next, TokenType.T_CATCH);

            result.setLocal(analyzer.removeScope().getVariables());
            return result;
        }
        return null;
    }
}
