package org.develnext.jphp.core.tokenizer.token.expr;

import php.runtime.common.Association;
import php.runtime.Memory;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;

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

    public String getCheckerCode() { return null; }

    public Class<?> getResultClass(){
        return Memory.class;
    }

    public boolean isSide(){
        return true;
    }

    public Memory calc(Environment env, TraceInfo trace, Memory o1, Memory o2){
        return null;
    }

    public boolean isEnvironmentNeeded(){
        return false;
    }

    public boolean isTraceNeeded(){
        return false;
    }

    public boolean isMutableArguments() { return false; }
}
