package org.develnext.jphp.core.compiler.common.util;

import org.objectweb.asm.Opcodes;
import org.develnext.jphp.core.compiler.common.misc.StackItem;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.DoubleMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.StringMemory;
import php.runtime.Memory;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.*;
import org.develnext.jphp.core.tokenizer.token.expr.value.*;

final public class CompilerUtils {

    private CompilerUtils(){ }

    protected static Memory toCallMemory(CallExprToken call, Memory... arguments){
        return null; // TODO
    }

    public static Memory toMemory(ValueExprToken value, Memory... arguments){
        if (value instanceof IntegerExprToken){
            return LongMemory.valueOf(((IntegerExprToken) value).getValue());
        } else if (value instanceof DoubleExprToken){
            return DoubleMemory.valueOf(((DoubleExprToken) value).getValue());
        } else if (value instanceof StringExprToken && !((StringExprToken) value).isBinary()){
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

    public static Memory calcUnary(Environment env, TraceInfo trace, Memory o1, OperatorExprToken operator){
        if (operator.isBinary())
            throw new IllegalArgumentException("Operator is not unary");

        return operator.calc(env, trace, o1, null);
    }

    public static Memory calcBinary(Environment env, TraceInfo trace, Memory o1, Memory o2, OperatorExprToken operator, boolean right){
        if (!operator.isBinary())
            throw new IllegalArgumentException("Operator is not binary");

        if (right) {
            Memory o = o1;
            o1 = o2;
            o2 = o;
        }

        return operator.calc(env, trace, o1, o2);
    }

    public static int getOperatorOpcode(OperatorExprToken operator, StackItem.Type type){
        if (operator instanceof PlusExprToken){
            switch (type){
                case DOUBLE: return Opcodes.DADD;
                case FLOAT: return Opcodes.FADD;
                case LONG: return Opcodes.LADD;
                case BYTE:
                case SHORT:
                case INT: return Opcodes.IADD;
            }
        }

        if (operator instanceof MinusExprToken){
            switch (type){
                case DOUBLE: return Opcodes.DSUB;
                case FLOAT: return Opcodes.FSUB;
                case LONG: return Opcodes.LSUB;
                case BYTE:
                case SHORT:
                case INT: return Opcodes.ISUB;
            }
        }

        if (operator instanceof MulExprToken){
            switch (type){
                case DOUBLE: return Opcodes.DMUL;
                case FLOAT: return Opcodes.FMUL;
                case LONG: return Opcodes.LMUL;
                case BYTE:
                case SHORT:
                case INT: return Opcodes.IMUL;
            }
        }

        throw new IllegalArgumentException("Unknown operator " + operator.getWord() + " for type " + type.name());
    }

    public static boolean isOperatorAlwaysReturn(OperatorExprToken operator) {
        return operator instanceof KeyValueExprToken;

    }
}
