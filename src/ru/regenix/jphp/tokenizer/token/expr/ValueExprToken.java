package ru.regenix.jphp.tokenizer.token.expr;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.operator.*;
import ru.regenix.jphp.tokenizer.token.expr.value.BooleanExprToken;
import ru.regenix.jphp.tokenizer.token.expr.value.DoubleExprToken;
import ru.regenix.jphp.tokenizer.token.expr.value.IntegerExprToken;
import ru.regenix.jphp.tokenizer.token.expr.value.StringExprToken;

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
        else if (operator instanceof ModExprToken)
            return mod(value);
        else if (operator instanceof ConcatExprToken)
            return concat(value);
        else
            return null;
    }
}
