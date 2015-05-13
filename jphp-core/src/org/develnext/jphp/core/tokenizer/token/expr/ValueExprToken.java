package org.develnext.jphp.core.tokenizer.token.expr;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.token.expr.operator.*;
import org.develnext.jphp.core.tokenizer.token.expr.value.*;
import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;

abstract public class ValueExprToken extends ExprToken {
    public ValueExprToken(TokenMeta meta, TokenType type) {
        super(meta, type);
    }

    public boolean isConstant(){
        return false;
    }

    public Object toNumeric(){
        return 0L;
    }

    protected ValueExprToken operatorResult(Object value){
        if (value instanceof String)
            return new StringExprToken(TokenMeta.of(value.toString(), this), StringExprToken.Quote.SINGLE);
        else if (value instanceof Long)
            return new IntegerExprToken(TokenMeta.of(value.toString(), this));
        else if (value instanceof Double)
            return new DoubleExprToken(TokenMeta.of(value.toString(), this));
        else if (value instanceof Boolean)
            return new BooleanExprToken(TokenMeta.of((Boolean)value ? "true" : "false", this));
        else
            return null;
    }

    protected ValueExprToken plus(ValueExprToken value){
        Object o1 = this.toNumeric();
        Object o2 = value.toNumeric();

        if (o1 instanceof Long && o2 instanceof Long)
            return operatorResult((Long)o1 + (Long)o2);
        else
            return operatorResult(Double.valueOf(o1.toString()) + Double.valueOf(o2.toString()));
    }

    protected ValueExprToken minus(ValueExprToken value){
        Object o1 = this.toNumeric();
        Object o2 = value.toNumeric();

        if (o1 instanceof Long && o2 instanceof Long)
            return operatorResult((Long)o1 - (Long)o2);
        else
            return operatorResult(Double.valueOf(o1.toString()) - Double.valueOf(o2.toString()));
    }

    protected ValueExprToken mul(ValueExprToken value){
        Object o1 = this.toNumeric();
        Object o2 = value.toNumeric();

        if (o1 instanceof Long && o2 instanceof Long)
            return operatorResult((Long)o1 * (Long)o2);
        else
            return operatorResult(Double.valueOf(o1.toString()) * Double.valueOf(o2.toString()));
    }

    protected ValueExprToken pow(ValueExprToken value){
        Object o1 = this.toNumeric();
        Object o2 = value.toNumeric();

        return operatorResult(Math.pow(Double.valueOf(o1.toString()), Double.valueOf(o2.toString())));
    }

    protected ValueExprToken div(ValueExprToken value){
        Object o1 = this.toNumeric();
        Object o2 = value.toNumeric();

        double r = (Double.valueOf(o1.toString()) / Double.valueOf(o2.toString()));
        if (r == Math.floor(r) && !Double.isInfinite(r))
            return operatorResult((long)r);
        else
            return operatorResult(r);
    }

    protected ValueExprToken mod(ValueExprToken value){
        Object o1 = this.toNumeric();
        Object o2 = value.toNumeric();

        if (o1 instanceof Long && o2 instanceof Long)
            return operatorResult((Long)o1 % (Long)o2);
        else
            return operatorResult(Double.valueOf(o1.toString()) % Double.valueOf(o2.toString()));
    }

    protected ValueExprToken concat(ValueExprToken value){
        return operatorResult(this.toString() + value.toString());
    }

    public ValueExprToken operator(ValueExprToken value, OperatorExprToken operator){
        if (operator instanceof PlusExprToken)
            return plus(value);
        else if (operator instanceof MinusExprToken)
            return minus(value);
        else if (operator instanceof MulExprToken)
            return mul(value);
        else if (operator instanceof DivExprToken)
            return div(value);
        else if (operator instanceof PowExprToken)
            return pow(value);
        else if (operator instanceof ModExprToken)
            return mod(value);
        else if (operator instanceof ConcatExprToken)
            return concat(value);
        else
            return null;
    }

    public static boolean isConstable(Token token, boolean arrays){
        if (token instanceof NameToken)
            return true;

        if (token instanceof IntegerExprToken)
            return true;

        if (token instanceof DoubleExprToken)
            return true;

        if (token instanceof StringExprToken){
            if (((StringExprToken) token).getSegments().isEmpty())
                return true;
        }

        if (token instanceof StaticAccessExprToken) {
            StaticAccessExprToken sa = (StaticAccessExprToken) token;
            if (!sa.isGetStaticField()
                    && (sa.getClazz() instanceof NameToken || sa.getClazz() instanceof SelfExprToken)) {
                return true;
            }
        }

        if (arrays){
            if (token instanceof ArrayExprToken){
                ArrayExprToken array = (ArrayExprToken) token;

                for(ExprStmtToken e : array.getParameters()){
                    if (e.isSingle()){
                        if (!isConstable(e.getSingle(), true))
                            return false;
                    } else if (e.getTokens().size() == 3) {
                        if (e.getTokens().get(1) instanceof KeyValueExprToken){
                            if (isConstable(e.getSingle(), false) && isConstable(e.getLast(), true))
                                continue;
                        }
                        return false;
                    } else
                        return false;
                }
                return true;
            }
        }

        return false;
    }
}
