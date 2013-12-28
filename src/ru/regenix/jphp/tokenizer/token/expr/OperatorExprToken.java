package ru.regenix.jphp.tokenizer.token.expr;

import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

abstract public class OperatorExprToken extends ExprToken {

    public OperatorExprToken(TokenMeta meta, TokenType type) {
        super(meta, type);
    }

    public boolean isBinary(){
        return true;
    }

    public boolean isRightSide(){
        return false;
    }

    public String getCode(){
        return null;
    }

    public Class<?> getResultClass(){
        return Memory.class;
    }

    public boolean isSide(){
        return true;
    }

    public Memory calc(Memory o1, Memory o2){
        return null;
    }
}
