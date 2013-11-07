package ru.regenix.jphp.compiler.jvm.entity;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.compiler.common.compile.*;
import ru.regenix.jphp.compiler.common.ASMExpression;
import ru.regenix.jphp.compiler.jvm.Constants;
import ru.regenix.jphp.compiler.jvm.JvmCompiler;
import ru.regenix.jphp.lexer.tokens.OpenEchoTagToken;
import ru.regenix.jphp.runtime.OperatorUtils;
import ru.regenix.jphp.runtime.memory.*;
import ru.regenix.jphp.runtime.type.HashTable;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.exceptions.CompileException;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.stmt.*;
import ru.regenix.jphp.lexer.tokens.stmt.EchoRawToken;
import ru.regenix.jphp.lexer.tokens.expr.NameToken;
import ru.regenix.jphp.lexer.tokens.expr.OperatorExprToken;
import ru.regenix.jphp.lexer.tokens.expr.ValueExprToken;
import ru.regenix.jphp.lexer.tokens.expr.operator.*;
import ru.regenix.jphp.lexer.tokens.expr.value.*;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.ListIterator;
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

    protected void stackPush(MethodEntity.StackItem.Type type){
        stack.push(null);
        method.push(type);
    }

    protected void stackPush(Memory.Type type){
        stackPush(MethodEntity.StackItem.Type.valueOf(type));
    }

    protected void stackPush(ValueExprToken token){
        if (token instanceof IntegerExprToken || token instanceof HexExprValue)
            stackPush(Memory.Type.INT);
        else if (token instanceof DoubleExprToken)
            stackPush(Memory.Type.DOUBLE);
        else if (token instanceof StringExprToken)
            stackPush(Memory.Type.STRING);
        else if (token instanceof BooleanExprToken)
            stackPush(Memory.Type.BOOL);
        else
            stackPush(Memory.Type.REFERENCE);

        stack.pop();
        stack.push(token);
    }

    protected MethodEntity.StackItem stackPop(){
        stack.pop();
        return method.pop();
    }

    protected ValueExprToken stackPopToken(){
        method.pop();
        return stack.pop();
    }

    protected MethodEntity.StackItem stackPeek(){
        return method.peek();
    }

    protected void setStackPeekAsImmutable(){
        method.peek().immutable = true;
    }

    protected ValueExprToken stackPeekToken(){
        return stack.peek();
    }

    protected boolean stackEmpty(){
        return method.getStackSize() == 0;
    }

    protected void writePushMemory(Memory memory){
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
                    type = Memory.Type.INT;
                } break;
                case DOUBLE: {
                    mv.visitLdcInsn(memory.toDouble());
                    type = Memory.Type.DOUBLE;
                } break;
                case STRING: {
                    mv.visitLdcInsn(memory.toString());
                    type = Memory.Type.STRING;
                } break;
            }
        }

        stackPush(type);
    }

    protected boolean writePopBoxing(boolean asImmutable){
        return writePopBoxing(stackPeek().type, asImmutable);
    }

    protected boolean writePopBoxing(){
        return writePopBoxing(false);
    }

    protected boolean writePopBoxing(Class<?> clazz, boolean asImmutable){
        return writePopBoxing(MethodEntity.StackItem.Type.valueOf(clazz), asImmutable);
    }

    protected boolean writePopBoxing(Class<?> clazz){
        return writePopBoxing(clazz, false);
    }

    protected boolean writePopBoxing(MethodEntity.StackItem.Type type){
        return writePopBoxing(type, false);
    }

    protected boolean writePopBoxing(MethodEntity.StackItem.Type type, boolean asImmutable){
        switch (type){
            case BOOL:
            case SHORT:
            case BYTE:
            case LONG:
            case INT: {
                writeSysStaticCall(LongMemory.class, "valueOf", Memory.class, type.toClass());
                setStackPeekAsImmutable();
            } return true;
            case FLOAT:
            case DOUBLE: {
                writeSysStaticCall(DoubleMemory.class, "valueOf", Memory.class, type.toClass());
                setStackPeekAsImmutable();
                return true;
            }
            case STRING: {
                writeSysStaticCall(StringMemory.class, "valueOf", Memory.class, String.class);
                setStackPeekAsImmutable();
                return true;
            }
            case REFERENCE: {
                if (asImmutable && !stackPeek().immutable){
                    writeSysDynamicCall(Memory.class, "toImmutable", Memory.class);
                    setStackPeekAsImmutable();
                }
            } break;
            case ARRAY: {
                writeSysStaticCall(ArrayMemory.class, "valueOf", Memory.class, HashTable.class);
                setStackPeekAsImmutable();
                return true;
            }
        }
        return false;
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
        stackPush(Memory.Type.REFERENCE);
        MethodEntity.LocalVariable variable = method.getLocalVariable("~env");
        mv.visitVarInsn(Opcodes.ALOAD, variable.index);
    }

    protected void writePushDup(MethodEntity.StackItem.Type type){
        stackPush(type);
        if (type.size() == 2)
            mv.visitInsn(Opcodes.DUP2);
        else
            mv.visitInsn(Opcodes.DUP);
    }

    protected void writePushDup(){
        writePushDup(method.peek().type);
    }

    protected void writePushNull(){
        writePushMemory(Memory.NULL);
    }

    protected void writePushNewObject(Class clazz){
        mv.visitTypeInsn(Opcodes.NEW, Type.getInternalName(clazz));
        stackPush(Memory.Type.REFERENCE);
        writePushDup();
        writeSysCall(clazz, Opcodes.INVOKESPECIAL, Constants.INIT_METHOD, void.class);
        stackPop();
    }

    protected void writePushStaticCall(Method method){
        writeSysStaticCall(
                method.getDeclaringClass(),
                method.getName(),
                method.getReturnType(),
                method.getParameterTypes()
        );
    }

    protected void writePushCompileFunction(CallExprToken function, CompileFunction compileFunction){
        Method method = compileFunction.find( function.getParameters().size() );
        if (method == null){
            method = compileFunction.find( function.getParameters().size() + 1 );
            if(method.getParameterTypes()[0] != Environment.class)
                method = null;
        }

        if (method == null){
            throw new CompileException(
                    Messages.ERR_FATAL_PASS_INCORRECT_ARGUMENTS_TO_FUNCTION.fetch(function.getName().getWord()),
                    function.getName().toTraceInfo(compiler.getContext())
            );
        }

        Class[] types = method.getParameterTypes();
        ListIterator<ExprStmtToken> iterator = function.getParameters().listIterator();

        for(int i = 0; i < types.length; i++){
            Class<?> argType = types[i];
            if (argType == Environment.class){
                writePushEnv();
            } else {
                writeExpression(iterator.next(), true, false);
                writePop(argType);
            }
        }
        writePushStaticCall(method);
        setStackPeekAsImmutable();
    }

    protected void writePushCall(CallExprToken function){
        Token name = function.getName();
        if (name instanceof NameToken){
            String realName = ((NameToken) name).getName();
            CompileFunction compileFunction = compiler.getScope().findFunction(realName);
            if (compileFunction != null){
                writePushCompileFunction(function, compileFunction);
            }
        }
    }

    protected void writeDefineVariables(Collection<VariableExprToken> values){
        for(VariableExprToken value : values)
            writeDefineVariable(value);
    }

    protected void writeDefineVariable(VariableExprToken value){
        MethodEntity.LocalVariable variable = method.getLocalVariable(value.getName());
        if (variable == null){
            Label label = writeLabel(mv, value.getMeta().getStartLine());
            variable = method.addLocalVariable(value.getName(), label, Memory.class);

            if (method.method.isReference(value)){
                variable.setReference(true);
            } else {
                variable.setReference(false);
            }

            if (variable.isReference()){
                writePushNewObject(ReferenceMemory.class);
            } else {
                writePushNull();
            }
            mv.visitVarInsn(Opcodes.ASTORE, variable.index);
            stackPop();
        }
    }

    protected void writePushVariable(VariableExprToken value){
        MethodEntity.LocalVariable variable = method.getLocalVariable(value.getName());
        if (variable.clazz == null)
            writePushNull();
        else {
            writeVarLoad(variable.index);
            if (!variable.isReference())
                setStackPeekAsImmutable();
        }
    }

    protected void writePushArray(ArrayExprToken array){
        if (array.getParameters().isEmpty()){
            writeSysStaticCall(ArrayMemory.class, "valueOf", Memory.class);
        } else {
            writePushNewObject(HashTable.class);
            for(ExprStmtToken param : array.getParameters()){
                writePushDup();
                writeExpression(param, true, false);
                writePopBoxing();
                writeSysDynamicCall(HashTable.class, "add", void.class, Memory.class);
            }
            writeSysStaticCall(ArrayMemory.class, "valueOf", Memory.class, HashTable.class);
        }
    }

    protected void writePush(ValueExprToken value){
        if (value instanceof IntegerExprToken){
            writePushInt((IntegerExprToken)value);
        } else if (value instanceof HexExprValue){
            writePushHex((HexExprValue)value);
        } else if (value instanceof BooleanExprToken){
            writePushBoolean((BooleanExprToken)value);
        } else if (value instanceof NullExprToken){
            writePushNull();
        } else if (value instanceof StringExprToken){
            writePushString((StringExprToken)value);
        } else if (value instanceof DoubleExprToken){
            writePushDouble((DoubleExprToken)value);
        } else if (value instanceof VariableExprToken){
            writePushVariable((VariableExprToken) value);
        } else if (value instanceof ArrayExprToken){
            writePushArray((ArrayExprToken)value);
        } else if (value instanceof CallExprToken){
            writePushCall((CallExprToken)value);
        }
    }

    protected void writeSysCall(Class clazz, int INVOKE_TYPE, String method, Class returnClazz, Class... paramClasses){
        Type[] args = new Type[paramClasses.length];
        if (INVOKE_TYPE == Opcodes.INVOKEVIRTUAL)
            stackPop(); // this

        for(int i = 0; i < args.length; i++){
            args[i] = Type.getType(paramClasses[i]);
            stackPop();
        }

        mv.visitMethodInsn(INVOKE_TYPE,
                Type.getInternalName(clazz),
                method,
                Type.getMethodDescriptor(Type.getType(returnClazz), args)
        );

        if (returnClazz != void.class){
            stackPush(Memory.Type.valueOf(returnClazz));
        }
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

    protected void writeVarStore(int index, boolean returned, boolean asImmutable){
        writePopBoxing();

        if (asImmutable && !stackPeek().immutable)
            writeSysDynamicCall(Memory.class, "toImmutable", Memory.class);

        if (returned){
            writePushDup();
        }

        mv.visitVarInsn(Opcodes.ASTORE, index);
        stackPop();
    }

    protected void writeVarStore(int index, boolean returned){
        writeVarStore(index, returned, false);
    }

    protected void writeVarLoad(int index){
        stackPush(Memory.Type.REFERENCE);
        mv.visitVarInsn(Opcodes.ALOAD, index);
    }

    protected void writeGetField(Class clazz, String name, Class fieldClass){
        stackPop();
        stackPush(Memory.Type.REFERENCE);
        mv.visitFieldInsn(Opcodes.GETFIELD, Type.getInternalName(clazz), name, Type.getDescriptor(fieldClass));
    }

    protected void writeGetStatic(Class clazz, String name, Class fieldClass){
        stackPush(Memory.Type.REFERENCE);
        mv.visitFieldInsn(Opcodes.GETSTATIC, Type.getInternalName(clazz), name, Type.getDescriptor(fieldClass));
    }

    protected void writeGetEnum(Enum enumInstance){
        writeGetStatic(enumInstance.getDeclaringClass(), enumInstance.name(), enumInstance.getDeclaringClass());
    }

    protected void writeVariableAssign(VariableExprToken variable, ValueExprToken R, MethodEntity.StackItem.Type Rt, boolean returnValue){
        MethodEntity.LocalVariable local = method.getLocalVariable(variable.getName());
        local.setClazz(Memory.class);

        // boolean returned = !stack.empty() || returnValue;
        if (local.isReference()){
            writePushVariable(variable);

            if (R == null){
                stackPush(Rt);
                writeSysStaticCall(Memory.class, "assignRight", void.class, stackPeek().type.toClass(), Memory.class);
            } else {
                writePush(R);
                writeSysDynamicCall(Memory.class, "assign", Memory.class, Rt.toClass());
            }

            if (returnValue)
                writeVarLoad(local.index);
        } else {
            writePush(R);
            if (R == null)
                stackPush(Rt);

            writeVarStore(local.index, returnValue, true);
        }
    }

    protected void writeScalarOperator(MethodEntity.StackItem.Type Lt, MethodEntity.StackItem.Type Rt,
                                       MethodEntity.StackItem.Type operatorType, String operatorName){
        writeSysDynamicCall(OperatorUtils.class, operatorName, operatorType.toClass(), Lt.toClass(), Rt.toClass());
    }

    protected void writePop(Class clazz){
        if (clazz == String.class)
            writePopString();
        else if (clazz == Boolean.TYPE)
            writePopBoolean();
        else if (clazz == Memory.class)
            writePopBoxing(true);
        else if (clazz == Double.TYPE)
            writePopDouble();
        else if (clazz == Float.TYPE)
            writePopFloat();
        else if (clazz == Long.TYPE)
            writePopLong();
        else if (clazz == Integer.TYPE || clazz == Byte.TYPE || clazz == Short.TYPE)
            writePopInteger();
        else
            throw new IllegalArgumentException("Cannot pop this class: " + clazz.getName());
    }

    protected void writePopInteger(){
        writePopLong();
        mv.visitInsn(Opcodes.L2I);
        stackPop();
        stackPush(MethodEntity.StackItem.Type.INT);
    }

    protected void writePopFloat(){
        writePopDouble();
        mv.visitInsn(Opcodes.D2F);
        stackPop();
        stackPush(MethodEntity.StackItem.Type.FLOAT);
    }

    protected void writePopLong(){
        switch (stackPeek().type){
            case LONG: break;
            case FLOAT: {
                mv.visitInsn(Opcodes.L2F);
                stackPop();
                stackPush(Memory.Type.INT);
            } break;
            case BYTE:
            case SHORT:
            case BOOL:
            case INT: {
                mv.visitInsn(Opcodes.L2I);
                stackPop();
                stackPush(Memory.Type.INT);
            } break;
            case DOUBLE: {
                mv.visitInsn(Opcodes.D2L);
                stackPop();
                stackPush(Memory.Type.INT);
            } break;
            case STRING: {
                writeSysStaticCall(StringMemory.class, "toNumeric", Memory.class, String.class);
                writeSysDynamicCall(Memory.class, "toLong", Long.TYPE);
            } break;
            default: {
                writeSysDynamicCall(Memory.class, "toLong", Long.TYPE);
            }
        }
    }

    protected void writePopDouble(){
        switch (stackPeek().type){
            case DOUBLE: break;
            case BYTE:
            case SHORT:
            case BOOL:
            case INT: {
                mv.visitInsn(Opcodes.I2D);
                stackPop();
                stackPush(Memory.Type.DOUBLE);
            } break;
            case LONG: {
                mv.visitInsn(Opcodes.L2D);
                stackPop();
                stackPush(Memory.Type.DOUBLE);
            } break;
            case STRING: {
                writeSysStaticCall(StringMemory.class, "toNumeric", Memory.class, String.class);
                writeSysDynamicCall(Memory.class, "toDouble", Double.TYPE);
            } break;
            default: {
                writeSysDynamicCall(Memory.class, "toDouble", Double.TYPE);
            }
        }
    }

    protected void writePopString(){
        MethodEntity.StackItem.Type peek = stackPeek().type;
        switch (peek){
            case STRING: break;
            default:
                if (peek.isConstant())
                    writeSysStaticCall(String.class, "valueOf", String.class, peek.toClass());
                else
                    writeSysDynamicCall(Memory.class, "toString", String.class);
        }
    }

    protected void writePopBoolean(){
        MethodEntity.StackItem.Type peek = stackPeek().type;
        switch (peek){
            case BOOL: break;
            case BYTE:
            case INT:
            case SHORT:
            case LONG: {
                Label fail = new Label();
                Label end = new Label();

                mv.visitJumpInsn(Opcodes.IFEQ, fail);
                mv.visitInsn(Opcodes.ICONST_1);
                mv.visitJumpInsn(Opcodes.GOTO, end);
                mv.visitLabel(fail);
                mv.visitInsn(Opcodes.ICONST_0);
                mv.visitLabel(end);

                if (peek == MethodEntity.StackItem.Type.LONG){
                    stackPop();
                    stackPush(MethodEntity.StackItem.Type.BOOL);
                }
            } break;
            case DOUBLE: {
                writeSysStaticCall(OperatorUtils.class, "toBoolean", Boolean.TYPE, Double.TYPE);
            } break;
            case STRING: {
                writeSysStaticCall(OperatorUtils.class, "toBoolean", Boolean.TYPE, String.class);
            } break;
            case REFERENCE: {
                writeSysDynamicCall(Memory.class, "toBoolean", Boolean.TYPE);
            } break;
        }
    }

    protected void writeArrayGet(ArrayGetExprToken operator, boolean returnValue){
        ValueExprToken L = stackPopToken();
        writePush(L);
        writePopBoxing();

        MethodEntity.LocalVariable local = null;
        if (L instanceof VariableExprToken){
            local = method.getLocalVariable(((VariableExprToken)L).getName());
        }

        int i = 0;
        for(ExprStmtToken param : operator.getParameters()){
            writeExpression(param, true, false);
            writeSysDynamicCall(Memory.class, "valueOfIndex", Memory.class, stackPeek().type.toClass());

            if (i == 0){
                if (local != null && !local.isReference()){
                    // only if it null
                    writeVarLoad(local.index); // ALOAD 0
                    writeSysDynamicCall(Memory.class, "isNull", Boolean.TYPE);

                    Label end = new Label();
                    mv.visitJumpInsn(Opcodes.IFEQ, end);
                    stackPop();

                    writePushDup();
                    writeSysStaticCall(Memory.class, "toArrayValue", Memory.class, Memory.class);
                    writeVarStore(local.index, false);
                    mv.visitLabel(end);
                }
            }
            i++;
        }
    }

    protected void writeOperator(OperatorExprToken operator, boolean returnValue){
        MethodEntity.StackItem.Type Rt = stackPeek().type;
        ValueExprToken R = stackPopToken();

        MethodEntity.StackItem.Type Lt = stackPeek().type;
        ValueExprToken L = stackPopToken();

        ValueExprToken tmp = tryApplyOperator(R, L, operator);
        if (tmp != null) {
            stackPush(tmp);
            return;
        }

        MethodEntity.LocalVariable variable = null;
        if (L instanceof VariableExprToken){
            variable = method.getLocalVariable(((VariableExprToken) L).getName());
        }

        if (operator instanceof AssignExprToken){
            if (L instanceof VariableExprToken){
                writeVariableAssign((VariableExprToken)L, R, Rt, returnValue);
                return;
            }
        }

        writePush(L);
        if (L == null)
            stackPush(Lt);

        writePush(R);
        if (R == null)
            stackPush(Rt);

        //Memory.Type Rt = stackPop();
        //Memory.Type Lt = stackPeek();
        //stackPush(Rt);

        MethodEntity.StackItem.Type operatorType = MethodEntity.StackItem.Type.REFERENCE;

        String name = null;

        if (operator instanceof PlusExprToken || operator instanceof AssignPlusExprToken){
            name = "plus";
        } else if (operator instanceof MinusExprToken || operator instanceof AssignMinusExprToken){
            name = "minus";
        } else if (operator instanceof MulExprToken || operator instanceof AssignMulExprToken){
            name = "mul";
        } else if (operator instanceof DivExprToken || operator instanceof AssignDivExprToken){
            name = "div";
        } else if (operator instanceof ModExprToken || operator instanceof AssignModExprToken){
            name = "mod";
        } else if (operator instanceof AssignExprToken){
            name = "assign";
        } else if (operator instanceof AssignRefExprToken){
            name = "assignRef";
        } else if (operator instanceof ConcatExprToken || operator instanceof AssignConcatExprToken){
            name = "concat";
            operatorType = MethodEntity.StackItem.Type.STRING;
        } else if (operator instanceof SmallerExprToken){
            name = "smaller";
            operatorType = MethodEntity.StackItem.Type.BOOL;
        } else if (operator instanceof SmallerOrEqualToken){
            name = "smallerEq";
            operatorType = MethodEntity.StackItem.Type.BOOL;
        } else if (operator instanceof GreaterExprToken){
            name = "greater";
            operatorType = MethodEntity.StackItem.Type.BOOL;
        } else if (operator instanceof GreaterOrEqualExprToken){
            name = "greaterEq";
            operatorType = MethodEntity.StackItem.Type.BOOL;
        } else if (operator instanceof EqualExprToken){
            name = "equal";
            operatorType = MethodEntity.StackItem.Type.BOOL;
        } else if (operator instanceof BooleanNotEqualExprToken){
            name = "notEqual";
            operatorType = MethodEntity.StackItem.Type.BOOL;
        }

        if (Rt.isConstant() && Lt.isConstant()){
            if (operator instanceof AssignOperatorExprToken)
                unexpectedToken(operator);

            writeScalarOperator(Lt, Rt, operatorType, name);
        } else if ((L != null && L.isConstant()) || Lt.isConstant()){
            if (operator instanceof AssignOperatorExprToken)
                unexpectedToken(operator);

            if ("plus".equals(name)
                    || "mul".equals(name)
                    || "equal".equals(name)
                    || "notEqual".equals(name)){
                writeSysDynamicCall(
                        Memory.class, name, operatorType == null ? void.class : operatorType.toClass(),
                        Lt.toClass(), Memory.class
                );
            } else
                writeSysStaticCall(
                        Memory.class,
                        name + "Right", operatorType == null ? void.class : operatorType.toClass(),
                        Lt.toClass(), Memory.class
                );
        } else {
            writeSysDynamicCall(
                    Memory.class, name, operatorType == null ? void.class : operatorType.toClass(), Rt.toClass()
            );

            if (operator instanceof AssignOperatorExprToken){
                if (returnValue)
                    writePushDup(operatorType);

                if (variable == null || variable.isReference()){
                    writeSysDynamicCall(Memory.class, "assign", Memory.class, Rt.toClass());
                } else {
                    writePopBoxing(operatorType);
                    mv.visitVarInsn(Opcodes.ASTORE, variable.index);
                    stackPop();
                }
            }
        }
    }

    protected void writeEchoRaw(EchoRawToken token){
        if (!token.getMeta().getWord().isEmpty()){
            writePushEnv();
            mv.visitLdcInsn(token.getMeta().getWord());
            stackPush(Memory.Type.STRING);
            writeSysDynamicCall(Environment.class, "echo", void.class, String.class);
        }
    }

    protected void writeOpenEchoTag(OpenEchoTagToken token){
        writePushEnv();
        writeExpression(token.getValue(), true, false);
        writePopString();
        writeSysDynamicCall(Environment.class, "echo", void.class, String.class);
    }

    protected void writeBody(BodyStmtToken body){
        if (body!= null){
            for(ExprStmtToken line : body.getInstructions()){
                writeExpression(line, false);
            }
        }
    }

    protected void writeIf(IfStmtToken token){
        //writeDefineVariables(token.getLocal());
        Label end = new Label();
        writeExpression(token.getCondition(), true);
        writePopBoolean();

        mv.visitJumpInsn(Opcodes.IFEQ, end);
        stackPop();
        writeBody(token.getBody());
        mv.visitLabel(end);
        mv.visitLineNumber(token.getMeta().getEndLine(), end);
    }

    protected void writeWhile(WhileStmtToken token){
        writeDefineVariables(token.getLocal());

        Label start = writeLabel(mv, token.getMeta().getStartLine());
        Label end = new Label();

        writeExpression(token.getCondition(), true);
        writePopBoolean();

        mv.visitJumpInsn(Opcodes.IFEQ, end);
        stackPop();

        writeBody(token.getBody());

        mv.visitJumpInsn(Opcodes.GOTO, start);
        mv.visitLabel(end);
        mv.visitLineNumber(token.getMeta().getEndLine(), end);
    }

    protected void writeReturn(ReturnStmtToken token){
        if (token.getValue() == null)
            writePushNull();
        else
            writeExpression(token.getValue(), true);

        if (stackEmpty())
            writePushNull();
        else
            writePopBoxing();

        mv.visitInsn(Opcodes.ARETURN);
    }

    protected void writeExpression(ExprStmtToken expression, boolean returnValue, boolean popAll){
        if (popAll){
            method.popAll();
        }

        if (expression.getTokens().size() == 1){
            Token token = expression.getTokens().get(0);
            if (!(token instanceof StmtToken)){
                expression = new ASMExpression(compiler.getContext(), expression, true).getResult();
            }
        } else
            expression = new ASMExpression(compiler.getContext(), expression, true).getResult();

        for(Token token : expression.getTokens()){
            if (token instanceof EchoRawToken){
                writeEchoRaw((EchoRawToken)token);
            } else  if (token instanceof OpenEchoTagToken){
                writeOpenEchoTag((OpenEchoTagToken)token);
            } else if (token instanceof ValueExprToken){
                stackPush((ValueExprToken) token);
            } else if (token instanceof ArrayGetExprToken){
                writeArrayGet((ArrayGetExprToken) token, returnValue);
            } else if (token instanceof OperatorExprToken){
                writeOperator((OperatorExprToken) token, returnValue);
            } else if (token instanceof ReturnStmtToken){
                writeReturn((ReturnStmtToken) token);
            } else if (token instanceof IfStmtToken){
                writeIf((IfStmtToken)token);
            } else if (token instanceof WhileStmtToken){
                writeWhile((WhileStmtToken)token);
            }
        }

        if (returnValue && !stack.empty() && stack.peek() != null){
            writePush(stackPopToken());
        } else if (!stack.empty()){
            if (stackPeekToken() instanceof CallExprToken)
                writePush(stackPopToken());
        }

        if (!returnValue)
            writePopAll();
    }

    protected void writePopAll(){
        while (!stack.empty()){
            ValueExprToken token = stackPeekToken();
            MethodEntity.StackItem.Type type = stackPop().type;

            if (token == null){
                switch (type.size()){
                    case 2: mv.visitInsn(Opcodes.POP2); break;
                    case 1: mv.visitInsn(Opcodes.POP); break;
                    default:
                        throw new IllegalArgumentException("Invalid of size StackItem: " + type.size());
                }
            }
        }
        for(int i = 0; i < method.getStackSize(); i++){
            if (stack.pop() == null)
                mv.visitInsn(Opcodes.POP);
        }
        method.popAll();
        stack.clear();
    }

    protected void writeExpression(ExprStmtToken expression, boolean returnValue){
        writeExpression(expression, returnValue, true);
    }

    @Override
    public void getResult() {
        mv = method.mv;
        writeDefineVariables(method.method.getLocal());
        writeExpression(expression, false);
        method.popAll();
    }
}
