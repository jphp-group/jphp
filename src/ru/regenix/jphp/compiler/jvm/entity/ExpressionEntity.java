package ru.regenix.jphp.compiler.jvm.entity;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import ru.regenix.jphp.compiler.common.ASMExpression;
import ru.regenix.jphp.compiler.jvm.Constants;
import ru.regenix.jphp.compiler.jvm.JvmCompiler;
import ru.regenix.jphp.compiler.jvm.runtime.invokedynamic.CallableValue;
import ru.regenix.jphp.compiler.jvm.runtime.Memory;
import ru.regenix.jphp.env.Environment;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.OperatorExprToken;
import ru.regenix.jphp.lexer.tokens.expr.ValueExprToken;
import ru.regenix.jphp.lexer.tokens.expr.operator.PlusExprToken;
import ru.regenix.jphp.lexer.tokens.expr.value.*;
import ru.regenix.jphp.lexer.tokens.stmt.ExprStmtToken;

import java.util.List;
import java.util.Stack;

public class ExpressionEntity extends Entity {

    private enum ValueType {
        BOOL, INT, LONG, FLOAT, DOUBLE, STRING, MEMORY;

        public static boolean isScalar(ValueType type){
            return type != MEMORY;
        }
    }

    protected final MethodEntity method;
    protected final ExprStmtToken expression;

    private MethodVisitor mv;
    private Stack<ValueType> types;


    public ExpressionEntity(JvmCompiler compiler, MethodEntity method, ExprStmtToken expression) {
        super(compiler);
        this.method = method;
        this.expression = new ASMExpression(compiler.getContext(), expression, true).getResult();
        this.types = new Stack<ValueType>();
    }

    protected String toOpcodeClass(ValueType type){
        if (type == null)
            return "V"; // void;

        switch (type){
            case BOOL: return "Z";
            case INT: return "I";
            case LONG: return "J";
            case FLOAT: return "F";
            case DOUBLE: return "D";
            case MEMORY: return "L" + Constants.MEMORY_CLASS + ";";
        }

        return null;
    }

    protected ValueType pop(){
        ValueType type = types.pop();
        switch (type){
            case BOOL:
            case INT:
            case FLOAT:
            case MEMORY:
            case STRING: method.pop(); break;
            default:
                method.pop(2);
        }

        return type;
    }

    protected void writePushInt(long value){
        if (value > 0 && value < Integer.MAX_VALUE){
            method.push();
            types.push(ValueType.INT);
            int lval = (int)value;
            switch (lval){
                case 0: mv.visitInsn(Opcodes.ICONST_0); break;
                case 1: mv.visitInsn(Opcodes.ICONST_1); break;
                case 2: mv.visitInsn(Opcodes.ICONST_2); break;
                case 3: mv.visitInsn(Opcodes.ICONST_3); break;
                case 4: mv.visitInsn(Opcodes.ICONST_4); break;
                case 5: mv.visitInsn(Opcodes.ICONST_5); break;
                default: {
                    if (value <= Byte.MAX_VALUE){
                        mv.visitIntInsn(Opcodes.BIPUSH, lval);
                    } else if (value <= Short.MAX_VALUE){
                        mv.visitIntInsn(Opcodes.SIPUSH, lval);
                    } else
                        mv.visitLdcInsn(lval);
                }
            }
        } else {
            if (value > Integer.MIN_VALUE){
                method.push();
                types.push(ValueType.INT);
                mv.visitLdcInsn((int)value);
            } else {
                method.push(2);
                types.push(ValueType.LONG);
                mv.visitLdcInsn(value);
            }
        }
    }

    protected void writePushInt(IntegerExprToken value){
        writePushInt(value.getValue());
    }

    protected void writePushHex(HexExprValue value){
        writePushInt(new IntegerExprToken(TokenMeta.of(value.getValue() + "", value)));
    }

    protected void writePushBoolean(BooleanExprToken value){
        method.push();
        types.push(ValueType.BOOL);
        if (value.getValue())
            mv.visitInsn(Opcodes.ICONST_1);
        else
            mv.visitInsn(Opcodes.ICONST_0);
    }

    protected void writePushString(StringExprToken value){
        method.push();
        types.push(ValueType.STRING);
        mv.visitLdcInsn(value.getValue());
    }

    protected void writePushDouble(DoubleExprToken value){
        method.push();
        types.push(ValueType.DOUBLE);
        if (value.isZero()){
            types.push(ValueType.FLOAT);
            mv.visitInsn(Opcodes.FCONST_0);
        } else if (value.isOne()){
            types.push(ValueType.FLOAT);
            mv.visitInsn(Opcodes.FCONST_1);
        } else if (value.isFloat()){
            types.push(ValueType.FLOAT);
            mv.visitLdcInsn((float)value.getValue());
        } else {
            types.push(ValueType.DOUBLE);
            method.push();
            mv.visitLdcInsn(value.getValue());
        }
    }

    protected void writePushEnv(){
        method.push();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
    }

    protected void writePushDup(){
        method.push();
        mv.visitInsn(Opcodes.DUP);
    }

    protected void writePushArgs(List<ExprStmtToken> args){
        writePushInt(args.size());
        mv.visitTypeInsn(Opcodes.ANEWARRAY, Constants.MEMORY_CLASS);
        int i = 0;
        for(ExprStmtToken arg : args){
            writePushDup();
            writePushInt(i);

            mv.visitTypeInsn(Opcodes.NEW, Constants.MEMORY_CLASS);
            writePushDup();
            writeExpression(arg);

            mv.visitMethodInsn(
               Opcodes.INVOKESPECIAL,
               Constants.MEMORY_CLASS,
               Constants.INIT_METHOD,
               "(" + toOpcodeClass(types.pop()) + ")V"
            );

            mv.visitInsn(Opcodes.AASTORE);
            i += 1;
        }
    }

    protected void writePushCall(CallExprToken value){
        writePushEnv();
        writePushArgs(value.getParameters());
        mv.visitMethodInsn(
                Opcodes.INVOKEVIRTUAL,
                Constants.toClassName(CallableValue.class),
                "call",
                "("
                        + Type.getType(Environment.class)
                        + Type.getType(Memory[].class)
                        + ")"
                        + Constants.MEMORY_CLASS
        );
    }

    protected void writePushNull(){
        method.push();
        mv.visitLdcInsn(Memory.NULL);
    }

    protected void writePushVariable(VariableExprToken value){
        method.push();
        MethodEntity.LocalVariable variable = method.getLocalVariable(value.getName());
        if (variable == null){
            writePushNull();
        } else {
            mv.visitVarInsn(Opcodes.ILOAD, variable.index);
        }
    }

    protected void writePush(ValueExprToken value){
        if (value instanceof IntegerExprToken){
            writePushInt((IntegerExprToken)value);
        } else if (value instanceof HexExprValue){
            writePushHex((HexExprValue)value);
        } else if (value instanceof BooleanExprToken){
            writePushBoolean((BooleanExprToken)value);
        } else if (value instanceof StringExprToken){
            writePushString((StringExprToken)value);
        } else if (value instanceof DoubleExprToken){
            writePushDouble((DoubleExprToken)value);
        } else if (value instanceof CallExprToken){
            writePushCall((CallExprToken)value);
        } else if (value instanceof VariableExprToken){
            writePushVariable((VariableExprToken)value);
        }
    }

    protected void writeOperatorPlus(){
        ValueType v1 = pop();
        ValueType v2 = pop();

        if (ValueType.isScalar(v1) && ValueType.isScalar(v2)){
            if (v1 == ValueType.INT && v2 == ValueType.INT){
                mv.visitInsn(Opcodes.IADD);
            } else if (v1 == ValueType.LONG && v2 == ValueType.LONG){
                mv.visitInsn(Opcodes.LADD);
            } else if (v1 == ValueType.FLOAT && v2 == ValueType.FLOAT){
                mv.visitInsn(Opcodes.FADD);
            } else if (v1 == ValueType.DOUBLE && v2 == ValueType.DOUBLE){
                mv.visitInsn(Opcodes.DADD);
            }
        }
        ------------>
    }

    protected void writeOperator(OperatorExprToken operator){
        if (operator instanceof PlusExprToken){
            writeOperatorPlus();
        }
    }

    protected void writeExpression(ExprStmtToken expression){
        for(Token token : expression.getTokens()){
            if (token instanceof ValueExprToken){
                writePush((ValueExprToken)token);
            } else if (token instanceof OperatorExprToken){
                writeOperator((OperatorExprToken)token);
            }
        }
    }

    @Override
    public void getResult() {
        mv = method.mv;
        writeExpression(expression);
    }
}
