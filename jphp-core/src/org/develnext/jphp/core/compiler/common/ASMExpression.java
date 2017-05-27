package org.develnext.jphp.core.compiler.common;

import php.runtime.common.Association;
import php.runtime.common.Messages;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.token.expr.BraceExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.ExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.*;
import org.develnext.jphp.core.tokenizer.token.expr.value.CallExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ASMExpression {
    protected Context context;
    protected Environment env;
    protected ExprStmtToken result;
    protected ExprStmtToken expr;
    protected Token prev;

    public ASMExpression(Environment env, Context context, ExprStmtToken expr){
        this.env = env;
        this.context = context;
        this.expr = expr;

        Stack<Token> stack  = new Stack<Token>();
        List<Token> result = new ArrayList<Token>();

        int i = 0, size = expr.getTokens().size();
        Token next = null;
        for(Token token : expr.getTokens()){
            next = i >= size - 1 ? null : expr.getTokens().get(i + 1);

            if (token instanceof OperatorExprToken) {
                OperatorExprToken operatorToken = (OperatorExprToken)token;
                if (operatorToken.isBinary()){
                    if (next == null)
                        unexpectedToken(operatorToken);
                    else if (next instanceof OperatorExprToken){
                        if (((OperatorExprToken)next).isBinary())
                            unexpectedToken(next);
                    }
                }

                boolean isRight = prev == null;
                OperatorExprToken operator = null;
                if (prev instanceof OperatorExprToken)
                    operator = (OperatorExprToken)prev;
                else if (prev instanceof CallExprToken && ((CallExprToken) prev).getName() instanceof OperatorExprToken)
                    operator = (OperatorExprToken) ((CallExprToken) prev).getName();

                if (operator != null){
                    isRight = operator.getOnlyAssociation() != Association.LEFT;
                } else if (prev instanceof BraceExprToken){
                    isRight = ((BraceExprToken) prev).isOpened();
                }

                if (isRight)
                    ((OperatorExprToken) token).setAssociation(Association.RIGHT);
                else
                    ((OperatorExprToken) token).setAssociation(Association.LEFT);
            }
            processToken(token, stack, result);
            prev = token instanceof ExprToken ? ((ExprToken)token).getLast() : token;
            i++;
        }

        if (!stack.empty())
            processOperator(stack, result, null);

        Stack<Token> checkStack = new Stack<Token>();
        i = 0;
        Token prev = null;

        for(Token el : result){
            if (el instanceof OperatorExprToken){
                OperatorExprToken operator = (OperatorExprToken)el;
                if (!operator.isValidAssociation())
                    unexpectedToken(operator);

                if (el instanceof IncExprToken || el instanceof DecExprToken){
                    if (prev != null && prev.getClass() == DynamicAccessExprToken.class){
                        DynamicAccessAssignExprToken newAssign
                                = new DynamicAccessAssignExprToken((DynamicAccessExprToken)prev);
                        newAssign.setAssignOperator(el);
                        result.set(i - 1, newAssign);
                        result.set(i, null);
                    } else if (prev != null && prev.getClass() == ArrayGetExprToken.class){
                        ArrayGetRefExprToken newAssign = new ArrayGetRefExprToken((ArrayGetExprToken)prev);
                        result.set(i - 1, newAssign);
                    }
                }

                if (operator.isBinary()){
                    if (checkStack.size() < 2)
                        unexpectedToken(operator);
                    checkStack.pop();
                    checkStack.pop();
                    checkStack.push(null);
                } else {
                    if (checkStack.empty())
                        unexpectedToken(operator);
                    checkStack.pop();
                    checkStack.push(null);
                }
            } else if (el instanceof CallExprToken){
                if (((CallExprToken) el).getName() instanceof OperatorExprToken){
                    OperatorExprToken operator = (OperatorExprToken) ((CallExprToken) el).getName();
                    if (operator.isBinary()){
                        unexpectedToken(operator);
                    } else {
                        if (checkStack.empty())
                            unexpectedToken(operator);
                        checkStack.pop();
                        checkStack.push(null);
                    }
                } else
                    checkStack.push(el);
            } else {
                checkStack.push(el);
            }

            i++;
            prev = el;
        }

        if (checkStack.size() > 1){
            for (Token el : checkStack)
                if (el != null)
                    unexpectedToken(el);
        }

        this.result = new ExprStmtToken(null, null, result);
    }

    protected void processOperator(Stack<Token> stack, List<Token> result, OperatorExprToken current){
        List<Token> list = new ArrayList<Token>();
        boolean isRightOperator = current != null && current.isRightSide();
        int prior = current == null ? -1 : current.getPriority();

        while (!stack.empty()){
            Token el = stack.peek();
            int elPrior = getPriority(el);
            if (el instanceof BraceExprToken)
                break;

            if (current != null
                    && current.getAssociation() == Association.RIGHT
                    && !current.isBinary()
                    && prev instanceof OperatorExprToken)
                break;

            boolean flush = current == null
                    || elPrior == 1
                    || (isRightOperator ? elPrior > prior : elPrior <= prior);
            if (flush){
                stack.pop();
                list.add(el);
            } else {
                break;
            }
        }

        result.addAll(list);
    }

    protected void processToken(Token token, Stack<Token> stack, List<Token> result){
        //int prior = getPriority(token);

        if (token instanceof CallExprToken) {
            CallExprToken call = (CallExprToken)token;
            if (call.getName() instanceof OperatorExprToken){
                processOperator(stack, result, (OperatorExprToken)call.getName());
            }
            result.add(token);
        } else if (token instanceof ValueExprToken){
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
            /*boolean done = !stack.empty();
            if (done){
                if (operator.isRightSide())
                    done = getPriority(stack.peek()) > prior;
                else
                    done = getPriority(stack.peek()) > prior;
            }

            if (done){
                if (prior == 1){
                    processOperator(stack, result, prior);
                    result.add(token);
                    return;
                }

                stack.push(token);
                return;
            }*/

            processOperator(stack, result, operator);
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
        env.error(
                token.toTraceInfo(context),
                ErrorType.E_PARSE,
                Messages.ERR_PARSE_UNEXPECTED_X,
                token.getWord()
        );
    }
}
