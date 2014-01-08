package ru.regenix.jphp.tokenizer.token.expr;

import ru.regenix.jphp.common.Association;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

abstract public class OperatorExprToken extends ExprToken {

    protected Association association = Association.BOTH;

    public OperatorExprToken(TokenMeta meta, TokenType type) {
        super(meta, type);
    }

    public Association getOnlyAssociation(){
        return Association.BOTH;
    }

    public Association getAssociation() {
        return association;
    }

    public boolean isValidAssociation(){
        return getOnlyAssociation() == Association.BOTH || getOnlyAssociation() == getAssociation();
    }

    public void setAssociation(Association association) {
        this.association = association;
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

    public boolean isEnvironmentNeeded(){
        return false;
    }

    public boolean isTraceNeeded(){
        return false;
    }
}
