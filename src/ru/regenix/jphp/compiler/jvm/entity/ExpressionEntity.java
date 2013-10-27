package ru.regenix.jphp.compiler.jvm.entity;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import ru.regenix.jphp.compiler.common.ASMExpression;
import ru.regenix.jphp.compiler.jvm.Constants;
import ru.regenix.jphp.compiler.jvm.JvmCompiler;
import ru.regenix.jphp.compiler.jvm.runtime.invokedynamic.CallableValue;
import ru.regenix.jphp.compiler.jvm.runtime.memory.*;
import ru.regenix.jphp.compiler.jvm.runtime.operator.Concat;
import ru.regenix.jphp.env.Environment;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.OperatorExprToken;
import ru.regenix.jphp.lexer.tokens.expr.ValueExprToken;
import ru.regenix.jphp.lexer.tokens.expr.operator.*;
import ru.regenix.jphp.lexer.tokens.expr.value.*;
import ru.regenix.jphp.lexer.tokens.stmt.ExprStmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.ReturnStmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.StmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.WhileStmtToken;

import java.util.List;
import java.util.Stack;

public class ExpressionEntity extends Entity {

    protected final MethodEntity method;
    protected final ExprStmtToken expression;

    private MethodVisitor mv;
    private Stack<ValueExprToken> stack = new Stack<ValueExprToken>();

    public ExpressionEntity(JvmCompiler compiler, MethodEntity method, ExprStmtToken expression) {
        super(compiler);
        this.method = method;
        this.expression = expression;
    }

    protected void writePushMemory(Memory memory){
        int size = 1;
        Memory.Type type = Memory.Type.REFERENCE;

        if (memory instanceof NullMemory){
            mv.visitFieldInsn(
                    Opcodes.GETSTATIC,
                    Type.getInternalName(Memory.class),
                    "NULL",
                    Type.getDescriptor(Memory.class)
            );
        } else if (memory instanceof FalseMemory){
            mv.visitFieldInsn(
                    Opcodes.GETSTATIC,
                    Type.getInternalName(Memory.class),
                    "FALSE",
                    Type.getDescriptor(Memory.class)
            );
        } else if (memory instanceof TrueMemory){
            mv.visitFieldInsn(
                    Opcodes.GETSTATIC,
                    Type.getInternalName(Memory.class),
                    "TRUE",
                    Type.getDescriptor(Memory.class)
            );
        } else if (memory instanceof ReferenceMemory){
            mv.visitTypeInsn(Opcodes.NEW, Type.getInternalName(ReferenceMemory.class));
            mv.visitInsn(Opcodes.DUP);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, Type.getInternalName(ReferenceMemory.class), Constants.INIT_METHOD, "()V");
        } else {
            switch (memory.type){
                case INT: {
                    mv.visitLdcInsn(memory.toLong());
                    method.push(2, Memory.Type.INT);
                    return;
                    /*if (memory.toLong() <= 5 && memory.toLong() >= 0){
                        mv.visitFieldInsn(
                                Opcodes.GETSTATIC,
                                Type.getInternalName(Memory.class),
                                "CONST_INT_" + memory.toString(),
                                Type.getDescriptor(Memory.class)
                        );
                    } else {
                        size += 3;
                        mv.visitTypeInsn(Opcodes.NEW, Type.getInternalName(LongMemory.class));
                        mv.visitInsn(Opcodes.DUP);
                        mv.visitLdcInsn(memory.toLong());
                        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, Type.getInternalName(LongMemory.class), Constants.INIT_METHOD, "(J)V");
                    } */
                } //break;
                case DOUBLE: {
                    mv.visitLdcInsn(memory.toDouble());
                    method.push(2, Memory.Type.DOUBLE);
                    return;
                }
                case STRING: {
                    mv.visitLdcInsn(memory.toString());
                    method.push(Memory.Type.STRING);
                    return;
                    /*size += 2;
                    mv.visitTypeInsn(Opcodes.NEW, Type.getInternalName(StringMemory.class));
                    mv.visitInsn(Opcodes.DUP);
                    mv.visitLdcInsn(memory.toString());
                    mv.visitMethodInsn(Opcodes.INVOKESPECIAL, Type.getInternalName(StringMemory.class), Constants.INIT_METHOD,
                            Type.getMethodDescriptor(Type.getType(void.class), Type.getType(String.class))); */
                }
            }
        }
        method.push(size, type);
    }

    protected void writePushBoxing(Memory.Type type){
        switch (type){
            case INT: {
                writeSysStaticCall(LongMemory.class, "valueOf", Memory.class, Long.TYPE);
            } break;
            case DOUBLE: {
                writeSysStaticCall(DoubleMemory.class, "valueOf", Memory.class, Double.TYPE);
            } break;
            case STRING: {
                writeSysStaticCall(StringMemory.class, "valueOf", Memory.class, String.class);
            }
        }
    }

    protected void writePushInt(IntegerExprToken value){
        writePushMemory(new LongMemory(value.getValue()));
    }

    protected void writePushHex(HexExprValue value){
        writePushInt(new IntegerExprToken(TokenMeta.of(value.getValue() + "", value)));
    }

    protected void writePushBoolean(boolean value){
        writePushMemory(value ? Memory.TRUE : Memory.FALSE);
    }

    protected void writePushBoolean(BooleanExprToken value){
        writePushBoolean(value.getValue());
    }

    protected void writePushString(StringExprToken value){
        writePushMemory(new StringMemory(value.getValue()));
    }

    protected void writePushDouble(DoubleExprToken value){
        writePushMemory(new DoubleMemory(value.getValue()));
    }

    protected void writePushEnv(){
        method.push(Memory.Type.REFERENCE);
        MethodEntity.LocalVariable variable = method.getLocalVariable("~env");
        mv.visitVarInsn(Opcodes.ALOAD, variable.index);
    }

    protected void writePushDup(){
        method.push(Memory.Type.NULL);
        mv.visitInsn(Opcodes.DUP);
    }

    protected void writePushArgs(List<ExprStmtToken> args){
        mv.visitLdcInsn(args.size());
        mv.visitTypeInsn(Opcodes.ANEWARRAY, Constants.MEMORY_CLASS);
        int i = 0;
        for(ExprStmtToken arg : args){
            writePushDup();
            mv.visitLdcInsn(i);
            writeExpression(arg, true);
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
        writePushMemory(Memory.NULL);
    }

    protected void writeDefineVariables(List<VariableExprToken> values){
        for(VariableExprToken value : values)
            writeDefineVariable(value);
    }

    protected void writeDefineVariable(VariableExprToken value){
        MethodEntity.LocalVariable variable = method.getLocalVariable(value.getName());
        if (variable == null){
            Label label = writeLabel(mv, value.getMeta().getStartLine());
            //writePushMemory(new ReferenceMemory());

            writePushNull();
            variable = method.addLocalVariable(value.getName(), label, Memory.class);

            mv.visitVarInsn(Opcodes.ASTORE, variable.index);
            method.pop();
        }
    }

    protected void writePushVariable(VariableExprToken value){
        MethodEntity.LocalVariable variable = method.getLocalVariable(value.getName());
        if (variable.clazz == null)
            writePushNull();
        else {
            method.push(Memory.Type.REFERENCE);
            mv.visitVarInsn(Opcodes.ALOAD, variable.index);
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

    protected void writeSysCall(Class clazz, int INVOKE_TYPE, String method, Class returnClazz, Class... paramClasses){
        Type[] args = new Type[paramClasses.length];
        for(int i = 0; i < args.length; i++){
            args[i] = Type.getType(paramClasses[i]);
        }

        mv.visitMethodInsn(INVOKE_TYPE,
                Type.getInternalName(clazz),
                method,
                Type.getMethodDescriptor(Type.getType(returnClazz), args)
        );
    }


    protected void writeSysDynamicCall(Class clazz, String method, Class returnClazz, Class... paramClasses){
        writeSysCall(clazz, Opcodes.INVOKEVIRTUAL, method, returnClazz, paramClasses);
    }

    protected void writeSysStaticCall(Class clazz, String method, Class returnClazz, Class... paramClasses){
        writeSysCall(clazz, Opcodes.INVOKESTATIC, method, returnClazz, paramClasses);
    }

    protected ValueExprToken tryApplyOperator(ValueExprToken R, ValueExprToken L, OperatorExprToken operator){
        if (R == null || L == null)
            return null;

        if (R.isConstant() && L.isConstant()){
            return L.operator(R, operator);
        }
        return null;
    }

    protected void writeVariableAssign(VariableExprToken variable, Memory.Type valueType, boolean returnValue){
        MethodEntity.LocalVariable local = method.getLocalVariable(variable.getName());
        local.setClazz(Memory.class);
        writePushBoxing(valueType);
        mv.visitVarInsn(Opcodes.ASTORE, local.index);

        if (!stack.empty() || returnValue){
            mv.visitVarInsn(Opcodes.ALOAD, local.index);
            method.push(Memory.Type.REFERENCE);
            stack.push(null);
        }
    }

    protected void writeOperator(OperatorExprToken operator, boolean returnValue){
        ValueExprToken R = stack.pop();
        ValueExprToken L = stack.pop();

        ValueExprToken tmp = tryApplyOperator(R, L, operator);
        if (tmp != null) {
            stack.push(tmp);
            return;
        }

        if (operator instanceof AssignExprToken){
            writePush(R);
            Memory.Type Rt = method.pop();
            if (L instanceof VariableExprToken){
                writeVariableAssign((VariableExprToken)L, Rt, returnValue);
                return;
            }
        }

        writePush(L);
        writePush(R);

        Memory.Type Rt = method.pop();
        Memory.Type Lt = method.pop();

        String name = null;
        Class returnClazz = Memory.class;

        if (operator instanceof PlusExprToken){
            name = "plus";
        } else if (operator instanceof MinusExprToken){
            name = "minus";
        } else if (operator instanceof MulExprToken){
            name = "mul";
        } else if (operator instanceof DivExprToken){
            name = "div";
        } else if (operator instanceof ModExprToken){
            name = "mod";
        } else if (operator instanceof ConcatExprToken){
            name = "concat";
            returnClazz = String.class;
        } else if (operator instanceof SmallerExprToken){
            name = "smaller";
        } else if (operator instanceof AssignExprToken){
            name = "assign";
        } else if (operator instanceof AssignRefExprToken){
            name = "assignRef";
        }

        if (L.isConstant()){
            writeSysStaticCall(Memory.class, name + "Right", Memory.class, Lt.toClass(), Memory.class);
        } else {
            writeSysDynamicCall(Memory.class, name, Memory.class, Rt.toClass());
        }

        if (returnClazz == Memory.class)
            method.push(Memory.Type.REFERENCE);
        else if (returnClazz == String.class)
            method.push(Memory.Type.STRING);
        else if (returnClazz == Long.class)
            method.push(Memory.Type.INT);
        else if (returnClazz == Double.class)
            method.push(Memory.Type.DOUBLE);
        else if (returnClazz == Boolean.class)
            method.push(Memory.Type.BOOL);

        stack.push(null);
    }

    protected void writeReturn(ReturnStmtToken token){
        if (token.getValue() == null)
            writePushNull();
        else
            writeExpression(token.getValue(), true);

        mv.visitInsn(Opcodes.ARETURN);
    }

    protected void writeStackPopAll(){
        for(int i = 0; i < method.getStackSize() / 2; i++){
            mv.visitInsn(Opcodes.POP2);
        }
        if (method.getStackSize() % 2 != 0)
            mv.visitInsn(Opcodes.POP);
    }

    protected void writeWhile(WhileStmtToken token){
        writeDefineVariables(token.getLocal());

        Label start = writeLabel(mv, token.getMeta().getStartLine());
        Label end = new Label();

        writeExpression(token.getCondition(), true);
        writeSysDynamicCall(Memory.class, "toBoolean", Boolean.TYPE);
        mv.visitJumpInsn(Opcodes.IFEQ, end);

        if (token.getBody() != null){
            for(ExprStmtToken line : token.getBody().getInstructions()){
                writeExpression(line, false);
                //writeStackPopAll();
            }
        }

        mv.visitJumpInsn(Opcodes.GOTO, start);
        mv.visitLabel(end);
        mv.visitLineNumber(token.getMeta().getEndLine(), end);
    }

    protected void writeExpression(ExprStmtToken expression, boolean returnValue){
        method.popAll();

        if (expression.getTokens().size() == 1){
            Token token = expression.getTokens().get(0);
            if (!(token instanceof StmtToken)){
                expression = new ASMExpression(compiler.getContext(), expression, true).getResult();
            }
        } else
            expression = new ASMExpression(compiler.getContext(), expression, true).getResult();

        for(Token token : expression.getTokens()){
            if (token instanceof ValueExprToken){
                stack.push((ValueExprToken)token);
            } else if (token instanceof OperatorExprToken){
                writeOperator((OperatorExprToken)token, returnValue);
            } else if (token instanceof ReturnStmtToken){
                writeReturn((ReturnStmtToken)token);
            } else if (token instanceof WhileStmtToken){
                writeWhile((WhileStmtToken)token);
            }
        }

        if (returnValue && !stack.empty()){
            writePush(stack.pop());
        }
    }

    @Override
    public void getResult() {
        mv = method.mv;
        writeDefineVariables(method.method.getLocal());
        writeExpression(expression, false);
        method.popAll();
    }
}
