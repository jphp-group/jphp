package ru.regenix.jphp.compiler.common;

import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.env.Context;
import ru.regenix.jphp.exceptions.ParseException;
import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.expr.BraceExprToken;
import ru.regenix.jphp.lexer.tokens.expr.ExprToken;
import ru.regenix.jphp.lexer.tokens.expr.OperatorExprToken;
import ru.regenix.jphp.lexer.tokens.expr.ValueExprToken;
import ru.regenix.jphp.lexer.tokens.stmt.ExprStmtToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ASMExpression {
    protected Context context;
    protected ExprStmtToken result;
    protected ExprStmtToken expr;

    public ASMExpression(Context context, ExprStmtToken expr){
        this.context = context;
        this.expr = expr;

        Stack<Token> stack  = new Stack<Token>();
        List<Token> result = new ArrayList<Token>();

        for(Token token : expr.getTokens()){
            int prior = getPriority(token);

            if (token instanceof ValueExprToken){
                result.add(token);
            } else if (token instanceof BraceExprToken){
                BraceExprToken brace = (BraceExprToken)token;
                if (brace.isSimpleOpened()){
                    stack.push(brace);
                } else if (brace.isSimpleClosed()) {
                    if (stack.empty())
                        unexpectedToken(brace);

                    boolean done = false;
                    do {
                        Token el = stack.pop();
                        if (el instanceof BraceExprToken && ((BraceExprToken) el).isSimpleOpened()){
                            done = true;
                            break;
                        }
                        result.add(el);
                    } while (!stack.isEmpty());

                    if (!done)
                        unexpectedToken(brace);
                } else
                    unexpectedToken(brace);
            } else if (token instanceof OperatorExprToken){
                if (stack.empty() || getPriority(stack.peek()) > prior){
                    stack.push(token);
                    continue;
                }

                while (!stack.empty()){
                    Token el = stack.peek();
                    int elPrior = getPriority(el);
                    if (elPrior <= prior){
                        stack.pop();
                        result.add(el);
                    } else {
                        break;
                    }
                }
                stack.push(token);
            }
        }

        while (!stack.empty())
            result.add(stack.pop());

        this.result = new ExprStmtToken(result);
    }

    public ExprStmtToken getResult(){
        return result;
    }

    private int getPriority(Token token){
        if (token instanceof ExprToken)
            return  ((ExprToken)token).getPriority();
        else
            return 0;
    }

    /**
     * @throws ru.regenix.jphp.exceptions.ParseException
     * @param token
     */
    protected void unexpectedToken(Token token){
        Object unexpected = token.getType();
        if (token.getType() == TokenType.T_J_CUSTOM)
            unexpected = token.getWord();

        throw new ParseException(
                Messages.ERR_PARSE_UNEXPECTED_X.fetch(unexpected),
                token.toTraceInfo(context.getFile())
        );
    }
}
