package ru.regenix.jphp.compiler.common;

import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.exceptions.ParseException;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.Token;
import ru.regenix.jphp.tokenizer.token.expr.BraceExprToken;
import ru.regenix.jphp.tokenizer.token.expr.ExprToken;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;
import ru.regenix.jphp.tokenizer.token.expr.ValueExprToken;
import ru.regenix.jphp.tokenizer.token.expr.operator.LogicOperatorExprToken;
import ru.regenix.jphp.tokenizer.token.expr.value.CallExprToken;
import ru.regenix.jphp.tokenizer.token.expr.value.GetVarExprToken;
import ru.regenix.jphp.tokenizer.token.stmt.ExprStmtToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ASMExpression {
    protected Context context;
    protected ExprStmtToken result;
    protected ExprStmtToken expr;

    public ASMExpression(Context context, ExprStmtToken expr, boolean recursive){
        this.context = context;
        this.expr = expr;

        Stack<Token> stack  = new Stack<Token>();
        List<Token> result = new ArrayList<Token>();

        for(Token token : expr.getTokens()){
            processToken(token, stack, result, recursive);
        }

        if (!stack.empty())
            processOperator(stack, result, Integer.MAX_VALUE, recursive);

        this.result = new ExprStmtToken(result);
    }

    protected void processOperator(Stack<Token> stack, List<Token> result, int prior, boolean recursive){
        List<Token> list = new ArrayList<Token>();
        while (!stack.empty()){
            Token el = stack.peek();
            int elPrior = getPriority(el);
            if (elPrior <= prior){
                stack.pop();
                if (el instanceof LogicOperatorExprToken){
                    ExprStmtToken value = ((LogicOperatorExprToken)el).getRightValue();
                    if (recursive)
                        value = new ASMExpression(context, value, recursive).getResult();

                    ((LogicOperatorExprToken) el).setRightValue(value);
                }

                list.add(el);
            } else {
                break;
            }
        }

        result.addAll(list);
    }

    protected void processToken(Token token, Stack<Token> stack, List<Token> result, boolean recursive){
        int prior = getPriority(token);

        if (token instanceof ValueExprToken){
            if (recursive){
                Token el = getRecursiveToken((ValueExprToken)token);
                if (el != null){
                    result.add(el);
                    return;
                }
            }
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
                if (prior == 1){
                    processOperator(stack, result, prior, recursive);
                    result.add(token);
                    return;
                }
                stack.push(token);
                return;
            }

            processOperator(stack, result, prior, recursive);
            stack.push(token);
        }
    }

    public ASMExpression(Context context, ExprStmtToken expr) {
        this(context, expr, true);
    }

    public ExprStmtToken getResult(){
        return result;
    }

    private ValueExprToken getRecursiveToken(ValueExprToken token){
        if (token instanceof CallExprToken){
            CallExprToken call = (CallExprToken)token;
            List<ExprStmtToken> newParameters = new ArrayList<ExprStmtToken>(call.getParameters().size());
            for(ExprStmtToken param : call.getParameters()){
                newParameters.add(new ASMExpression(context, param, true).getResult());
            }
            call.setParameters(newParameters);
            return call;
        } else if (token instanceof GetVarExprToken){
            GetVarExprToken getVar = (GetVarExprToken)token;
            getVar.setName(new ASMExpression(context, getVar.getName(), true).getResult());
        }
        return null;
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
