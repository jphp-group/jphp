package ru.regenix.jphp.compiler.common.util;

import ru.regenix.jphp.lexer.tokens.expr.OperatorExprToken;
import ru.regenix.jphp.lexer.tokens.expr.ValueExprToken;
import ru.regenix.jphp.lexer.tokens.expr.operator.*;
import ru.regenix.jphp.lexer.tokens.expr.value.*;
import ru.regenix.jphp.lexer.tokens.stmt.*;
import ru.regenix.jphp.runtime.memory.DoubleMemory;
import ru.regenix.jphp.runtime.memory.LongMemory;
import ru.regenix.jphp.runtime.memory.Memory;
import ru.regenix.jphp.runtime.memory.StringMemory;

final public class CompilerUtils {

    private CompilerUtils(){ }

    protected static Memory toCallMemory(CallExprToken call, Memory... arguments){
        return null; // TODO
    }

    public static Memory toMemory(ValueExprToken value, Memory... arguments){
        if (value instanceof IntegerExprToken){
            return LongMemory.valueOf(((IntegerExprToken) value).getValue());
        } else if (value instanceof HexExprValue){
            return LongMemory.valueOf(((HexExprValue) value).getValue());
        } else if (value instanceof DoubleExprToken){
            return DoubleMemory.valueOf(((DoubleExprToken) value).getValue());
        } else if (value instanceof StringExprToken){
            return StringMemory.valueOf(((StringExprToken) value).getValue());
        } else if (value instanceof BooleanExprToken){
            return ((BooleanExprToken) value).getValue() ? Memory.TRUE : Memory.FALSE;
        } else if (value instanceof NullExprToken){
            return Memory.NULL;
        } else if (value instanceof CallExprToken){
            return toCallMemory((CallExprToken)value, arguments);
        }

        return null;
    }

    public static Memory calcUnary(Memory o1, OperatorExprToken operator){
        if (operator.isBinary())
            throw new IllegalArgumentException("Operator is not unary");

        if (operator instanceof UnarMinusExprToken)
            return o1.negative();

        if (operator instanceof BooleanNotExprToken)
            return o1.not() ? Memory.TRUE : Memory.FALSE;

        throw new IllegalArgumentException("Unsupported operator: " + operator.getWord());
    }

    public static boolean isControlFlow(StmtToken token){
        if (token instanceof IfStmtToken)
            return true;
        if (token instanceof WhileStmtToken)
            return true;
        if (token instanceof DoStmtToken)
            return true;
        if (token instanceof ForStmtToken)
            return true;
        if (token instanceof ForeachStmtToken)
            return true;
        if (token instanceof TryStmtToken)
            return true;

        return false;
    }

    public static Memory calcBinary(Memory o1, Memory o2, OperatorExprToken operator){
        if (!operator.isBinary())
            throw new IllegalArgumentException("Operator is not binary");

        if (operator instanceof PlusExprToken)
            return o1.plus(o2);

        if (operator instanceof MinusExprToken)
            return o1.minus(o2);

        if (operator instanceof MulExprToken)
            return o1.mul(o2);

        if (operator instanceof DivExprToken)
            return o1.div(o2);

        if (operator instanceof ModExprToken)
            return o1.mod(o2);

        if (operator instanceof ConcatExprToken)
            return StringMemory.valueOf(o1.concat(o2));

        if (operator instanceof SmallerExprToken)
            return o1.smaller(o2) ? Memory.TRUE : Memory.FALSE;

        if (operator instanceof SmallerOrEqualToken)
            return o1.smallerEq(o2) ? Memory.TRUE : Memory.FALSE;

        if (operator instanceof GreaterExprToken)
            return o1.greater(o2) ? Memory.TRUE : Memory.FALSE;

        if (operator instanceof GreaterOrEqualExprToken)
            return o1.greaterEq(o2) ? Memory.TRUE : Memory.FALSE;

        if (operator instanceof EqualExprToken)
            return o1.equal(o2) ? Memory.TRUE : Memory.FALSE;

        if (operator instanceof BooleanNotEqualExprToken)
            return o1.notEqual(o2) ? Memory.TRUE : Memory.FALSE;

        if (operator instanceof BooleanAndExprToken || operator instanceof BooleanAnd2ExprToken)
            return o1.toBoolean() && o2.toBoolean() ? Memory.TRUE : Memory.FALSE;

        if (operator instanceof BooleanOrExprToken || operator instanceof BooleanOr2ExprToken)
            return o1.toBoolean() || o2.toBoolean() ? Memory.TRUE : Memory.FALSE;

        throw new IllegalArgumentException("Unsupported operator: " + operator.getWord());
    }
}
