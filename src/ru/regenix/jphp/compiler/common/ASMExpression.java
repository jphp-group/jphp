package ru.regenix.jphp.compiler.common;

import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.exceptions.ParseException;
import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.Token;
import ru.regenix.jphp.tokenizer.token.expr.BraceExprToken;
import ru.regenix.jphp.tokenizer.token.expr.ExprToken;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;
import ru.regenix.jphp.tokenizer.token.expr.ValueExprToken;
import ru.regenix.jphp.tokenizer.token.expr.operator.LogicOperatorExprToken;
import ru.regenix.jphp.tokenizer.token.stmt.ExprStmtToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ASMExpression {
    protected Context context;
    protected ExprStmtToken result;
    protected ExprStmtToken expr;
    /*protected Token prev;
    protected Token next;*/

    public ASMExpression(Context context, ExprStmtToken expr){
        this.context = context;
        this.expr = expr;

        Stack<Token> stack  = new Stack<Token>();
        List<Token> result = new ArrayList<Token>();

        int i = 0;
        for(Token token : expr.getTokens()){
            /*if (i + 1 < expr.getTokens().size())
                next = expr.getTokens().get(i + 1);
            else
                next = null;

            if (i > 0)
                prev = expr.getTokens().get(i - 1);
            else
                prev = null;*/

            processToken(token, stack, result);
            i++;
        }

        if (!stack.empty())
            processOperator(stack, result, Integer.MAX_VALUE);

        this.result = new ExprStmtToken(result);
    }

    protected void processOperator(Stack<Token> stack, List<Token> result, int prior){
        List<Token> list = new ArrayList<Token>();
        while (!stack.empty()){
            Token el = stack.peek();
            int elPrior = getPriority(el);
            if (elPrior <= prior){
                stack.pop();
                if (el instanceof LogicOperatorExprToken){
                    ExprStmtToken value = ((LogicOperatorExprToken)el).getRightValue();
                    /*if (recursive)
                        value = new ASMExpression(context, value).getResult();*/

                    ((LogicOperatorExprToken) el).setRightValue(value);
                }

                list.add(el);
            } else {
                break;
            }
        }

        result.addAll(list);
    }

    protected void processToken(Token token, Stack<Token> stack, List<Token> result){
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
            OperatorExprToken operator = (OperatorExprToken)token;
            if (!stack.empty() && getPriority(stack.peek()) > prior){
                if (prior == 1){
                    processOperator(stack, result, prior);
                    result.add(token);
                    return;
                }
                stack.push(token);
                return;
            }

            processOperator(stack, result, prior);
            stack.push(token);
        }
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
     * @param token
     */
    protected void unexpectedToken(Token token){
        Object unexpected = token.getType();
        if (token.getType() == TokenType.T_J_CUSTOM)
            unexpected = token.getWord();

        context.triggerError(new ParseException(
                Messages.ERR_PARSE_UNEXPECTED_X.fetch(unexpected),
                token.toTraceInfo(context)
        ));
    }
}
