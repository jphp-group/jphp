package ru.regenix.jphp.compiler.jvm.entity;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import ru.regenix.jphp.compiler.common.ASMExpression;
import ru.regenix.jphp.compiler.jvm.Constants;
import ru.regenix.jphp.compiler.jvm.JvmCompiler;
import ru.regenix.jphp.compiler.jvm.runtime._Memory;
import ru.regenix.jphp.compiler.jvm.runtime.invokedynamic.CallableValue;
import ru.regenix.jphp.compiler.jvm.runtime.memory.*;
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

import java.util.List;

public class ExpressionEntity extends Entity {

    protected final MethodEntity method;
    protected final ExprStmtToken expression;

    private MethodVisitor mv;

    public ExpressionEntity(JvmCompiler compiler, MethodEntity method, ExprStmtToken expression) {
        super(compiler);
        this.method = method;
        if (expression.getTokens().size() == 1){
            Token token = expression.getTokens().get(0);
            if (token instanceof StmtToken){
                this.expression = expression;
                return;
            }
        }

        this.expression = new ASMExpression(compiler.getContext(), expression, true).getResult();
    }

    protected void writePushMemory(Memory memory){
        method.push(1);
        if (memory instanceof NullMemory){
            mv.visitFieldInsn(
                    Opcodes.GETSTATIC,
                    Type.getInternalName(NullMemory.class),
                    "INSTANCE",
                    Type.getInternalName(NullMemory.class)
            );
        } else if (memory instanceof FalseMemory){
            mv.visitFieldInsn(
                    Opcodes.GETSTATIC,
                    Type.getInternalName(FalseMemory.class),
                    "INSTANCE",
                    Type.getInternalName(FalseMemory.class)
            );
        } else if (memory instanceof TrueMemory){
            mv.visitFieldInsn(
                    Opcodes.GETSTATIC,
                    Type.getInternalName(TrueMemory.class),
                    "INSTANCE",
                    Type.getInternalName(TrueMemory.class)
            );
        } else if (memory instanceof ReferenceMemory){
            mv.visitTypeInsn(Opcodes.NEW, Type.getInternalName(ReferenceMemory.class));
            mv.visitInsn(Opcodes.DUP);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, Type.getInternalName(ReferenceMemory.class), Constants.INIT_METHOD, "()V");
        } else {
            switch (memory.getType()){
                case INT: {
                    method.push(3);
                    mv.visitTypeInsn(Opcodes.NEW, Type.getInternalName(LongMemory.class));
                    mv.visitInsn(Opcodes.DUP);
                    mv.visitLdcInsn(memory.toLong());
                    mv.visitMethodInsn(Opcodes.INVOKESPECIAL, Type.getInternalName(LongMemory.class), Constants.INIT_METHOD, "(J)V");
                } break;
                case DOUBLE: {
                    method.push(3);
                    mv.visitTypeInsn(Opcodes.NEW, Type.getInternalName(DoubleMemory.class));
                    mv.visitInsn(Opcodes.DUP);
                    mv.visitLdcInsn(memory.toDouble());
                    mv.visitMethodInsn(Opcodes.INVOKESPECIAL, Type.getInternalName(DoubleMemory.class), Constants.INIT_METHOD, "(D)V");
                } break;
                case STRING: {
                    method.push(2);
                    mv.visitTypeInsn(Opcodes.NEW, Type.getInternalName(StringMemory.class));
                    mv.visitInsn(Opcodes.DUP);
                    mv.visitLdcInsn(memory.toString());
                    mv.visitMethodInsn(Opcodes.INVOKESPECIAL, Type.getInternalName(StringMemory.class), Constants.INIT_METHOD,
                            Type.getMethodDescriptor(Type.getType(void.class), Type.getType(String.class)));
                }
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
        method.push();
        MethodEntity.LocalVariable variable = method.getLocalVariable("~env");
        mv.visitVarInsn(Opcodes.ALOAD, variable.index);
    }

    protected void writePushDup(){
        method.push();
        mv.visitInsn(Opcodes.DUP);
    }

    protected void writePushArgs(List<ExprStmtToken> args){
        mv.visitLdcInsn(args.size());
        mv.visitTypeInsn(Opcodes.ANEWARRAY, Constants.MEMORY_CLASS);
        int i = 0;
        for(ExprStmtToken arg : args){
            writePushDup();
            mv.visitLdcInsn(i);
            writeExpression(arg);
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
                        + Type.getType(_Memory[].class)
                        + ")"
                        + Constants.MEMORY_CLASS
        );
    }

    protected void writePushNull(){
        writePushMemory(Memory.NULL);
    }

    protected void writePushVariable(VariableExprToken value){
        MethodEntity.LocalVariable variable = method.getLocalVariable(value.getName());
        if (variable == null){
            method.push();
            Label label = writeLabel(mv, value.getMeta().getStartLine());
            writePushMemory(new ReferenceMemory());
            variable = method.addLocalVariable(value.getName(), label);
            mv.visitVarInsn(Opcodes.ASTORE, variable.index);
            method.pop();
        }

        method.push();
        mv.visitVarInsn(Opcodes.ALOAD, variable.index);
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

    protected void writeSysDynamicCall(Class clazz, String method, Class returnClazz, Class... paramClasses){
        Type[] args = new Type[paramClasses.length];
        for(int i = 0; i < args.length; i++){
            args[i] = Type.getType(paramClasses[i]);
        }

        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                Type.getInternalName(clazz),
                method,
                Type.getMethodDescriptor(Type.getType(returnClazz), args)
        );
    }

    protected void writeOperator(OperatorExprToken operator){
        if (operator instanceof PlusExprToken){
            writeSysDynamicCall(Memory.class, "plus", Memory.class, Memory.class);
        } else if (operator instanceof MinusExprToken){
            writeSysDynamicCall(Memory.class, "minus", Memory.class, Memory.class);
        } else if (operator instanceof MulExprToken){
            writeSysDynamicCall(Memory.class, "mul", Memory.class, Memory.class);
        } else if (operator instanceof DivExprToken){
            writeSysDynamicCall(Memory.class, "div", Memory.class, Memory.class);
        } else if (operator instanceof ModExprToken){
            writeSysDynamicCall(Memory.class, "mod", Memory.class, Memory.class);
        } else if (operator instanceof ConcatExprToken){
            writeSysDynamicCall(Memory.class, "concat", Memory.class, Memory.class);
        } else if (operator instanceof AssignExprToken){
            writeSysDynamicCall(Memory.class, "assign", Memory.class, Memory.class);
        } else if (operator instanceof AssignRefExprToken){
            writeSysDynamicCall(Memory.class, "assignRef", Memory.class, Memory.class);
        }
    }

    protected void writeReturn(ReturnStmtToken token){
        writeExpression(new ASMExpression(compiler.getContext(), token.getValue()).getResult());
        mv.visitInsn(Opcodes.ARETURN);
    }

    protected void writeExpression(ExprStmtToken expression){
        for(Token token : expression.getTokens()){
            if (token instanceof ValueExprToken){
                writePush((ValueExprToken)token);
            } else if (token instanceof OperatorExprToken){
                writeOperator((OperatorExprToken)token);
            } else if (token instanceof ReturnStmtToken){
                writeReturn((ReturnStmtToken)token);
            }
        }
    }

    @Override
    public void getResult() {
        mv = method.mv;
        writeExpression(expression);
    }
}
