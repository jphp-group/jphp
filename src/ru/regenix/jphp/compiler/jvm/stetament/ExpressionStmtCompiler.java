package ru.regenix.jphp.compiler.jvm.stetament;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.compiler.common.ASMExpression;
import ru.regenix.jphp.compiler.common.compile.CompileConstant;
import ru.regenix.jphp.compiler.common.compile.CompileFunction;
import ru.regenix.jphp.compiler.jvm.Constants;
import ru.regenix.jphp.compiler.common.misc.LocalVariable;
import ru.regenix.jphp.compiler.common.misc.StackItem;
import ru.regenix.jphp.compiler.common.util.CompilerUtils;
import ru.regenix.jphp.exceptions.CompileException;
import ru.regenix.jphp.lexer.tokens.OpenEchoTagToken;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.value.NameToken;
import ru.regenix.jphp.lexer.tokens.expr.OperatorExprToken;
import ru.regenix.jphp.lexer.tokens.expr.ValueExprToken;
import ru.regenix.jphp.lexer.tokens.expr.operator.*;
import ru.regenix.jphp.lexer.tokens.expr.value.*;
import ru.regenix.jphp.lexer.tokens.stmt.*;
import ru.regenix.jphp.runtime.OperatorUtils;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.memory.*;
import ru.regenix.jphp.runtime.reflection.Entity;
import ru.regenix.jphp.runtime.type.HashTable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.ListIterator;

public class ExpressionStmtCompiler extends StmtCompiler {

    protected final MethodStmtCompiler method;
    protected final ExprStmtToken expression;

    private MethodVisitor mv;

    public ExpressionStmtCompiler(MethodStmtCompiler method, ExprStmtToken expression) {
        super(method.getCompiler());
        this.method = method;
        this.expression = expression;
    }

    protected void stackPush(ValueExprToken token, StackItem.Type type){
        method.push(new StackItem(token, type));
    }

    protected void stackPush(StackItem item){
        method.push(item);
    }

    protected void stackPush(Memory memory){
        method.push(new StackItem(memory));
    }

    protected void stackPush(ValueExprToken token, Memory memory){
        method.push(new StackItem(token, memory));
    }

    protected void stackPush(Memory.Type type){
        stackPush(null, StackItem.Type.valueOf(type));
    }

    protected void stackPush(ValueExprToken token){
        Memory o = CompilerUtils.toMemory(token);
        if (o != null){
            stackPush(o);
        } else {
            if (token instanceof VariableExprToken){
                LocalVariable local = method.getLocalVariable(((VariableExprToken) token).getName());
                if (local != null && local.getValue() != null){
                    stackPush(token, local.getValue());
                    return;
                }
            }
            stackPush(token, StackItem.Type.REFERENCE);
        }
    }

    protected StackItem stackPop(){
        return method.pop();
    }

    protected ValueExprToken stackPopToken(){
        return method.pop().getToken();
    }

    protected StackItem stackPeek(){
        return method.peek();
    }

    protected void setStackPeekAsImmutable(){
        method.peek().immutable = true;
    }

    protected ValueExprToken stackPeekToken(){
        return method.peek().getToken();
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
        setStackPeekAsImmutable();
    }

    protected boolean writePopBoxing(boolean asImmutable){
        return writePopBoxing(stackPeek().type, asImmutable);
    }

    protected boolean writePopBoxing(){
        return writePopBoxing(false);
    }

    protected boolean writePopBoxing(Class<?> clazz, boolean asImmutable){
        return writePopBoxing(StackItem.Type.valueOf(clazz), asImmutable);
    }

    protected boolean writePopBoxing(Class<?> clazz){
        return writePopBoxing(clazz, false);
    }

    protected boolean writePopBoxing(StackItem.Type type){
        return writePopBoxing(type, false);
    }

    protected boolean writePopBoxing(StackItem.Type type, boolean asImmutable){
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

    protected void writePushString(String value){
        writePushMemory(new StringMemory(value));
    }

    protected void writePushDouble(DoubleExprToken value){
        writePushMemory(new DoubleMemory(value.getValue()));
    }

    protected void writePushEnv(){
        stackPush(Memory.Type.REFERENCE);
        LocalVariable variable = method.getLocalVariable("~env");
        mv.visitVarInsn(Opcodes.ALOAD, variable.index);
    }

    protected void writePushDup(StackItem.Type type){
        stackPush(null, type);
        if (type.size() == 2)
            mv.visitInsn(Opcodes.DUP2);
        else
            mv.visitInsn(Opcodes.DUP);
    }

    protected void writePushDup(){
        StackItem item = method.peek();
        stackPush(item);

        if (item.type.size() == 2)
            mv.visitInsn(Opcodes.DUP2);
        else
            mv.visitInsn(Opcodes.DUP);
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

    protected Memory writePushCompileFunction(CallExprToken function, CompileFunction compileFunction, boolean returnValue){
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

        Memory[] arguments = new Memory[types.length];
        int j = 0;
        boolean immutable = compileFunction.isImmutable && method.getReturnType() != void.class;
        for(int i = 0; i < types.length; i++){
            Class<?> argType = types[i];
            if (argType == Environment.class){
                writePushEnv();
                immutable = false;
            } else {
                arguments[j] = writeExpression(iterator.next(), true, immutable && (j == 0 || arguments[j-1] != null));
                if (arguments[j] == null){
                    for(int k = 0; k < j - 1; k++){
                        if (arguments[k] != null){
                            writePushMemory(arguments[k]);
                            writePop(types[k]);
                            arguments[k] = null;
                        }
                    }
                    immutable = false;
                    writePop(argType);
                }
                j++;
            }
        }

        if (immutable){
            if (!returnValue)
                return null;

            Object[] typedArguments = new Object[arguments.length];
            for(int i = 0; i < arguments.length; i++){
                typedArguments[i] = MemoryUtils.toValue(arguments[i], types[i]);
            }
            try {
                Object value = method.invoke(null, typedArguments);
                return MemoryUtils.valueOf(value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e.getCause());
            }
        }

        writePushStaticCall(method);
        if (returnValue){
            if (method.getReturnType() == void.class)
                writePushNull();
            setStackPeekAsImmutable();
        }
        return null;
    }

    protected Memory writePushCall(CallExprToken function, boolean returnValue){
        Token name = function.getName();
        if (name instanceof NameToken){
            String realName = ((NameToken) name).getName();
            CompileFunction compileFunction = compiler.getScope().findCompileFunction(realName);
            if (compileFunction != null){
                return writePushCompileFunction(function, compileFunction, returnValue);
            }
        }
        return null;
    }

    protected void writeDefineVariables(Collection<VariableExprToken> values){
        for(VariableExprToken value : values)
            writeDefineVariable(value);
    }

    protected void writeUndefineVariables(Collection<VariableExprToken> values){
        for(VariableExprToken value : values)
            writeUndefineVariable(value);
    }

    protected void writeDefineVariable(VariableExprToken value){
        LocalVariable variable = method.getLocalVariable(value.getName());
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
        variable.pushLevel();
    }

    protected void writeUndefineVariable(VariableExprToken value){
        LocalVariable variable = method.getLocalVariable(value.getName());
        variable.popLevel();
    }

    protected void writePushVariable(VariableExprToken value){
        LocalVariable variable = method.getLocalVariable(value.getName());
        if (variable.getClazz() == null)
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

    protected void writePushTraceInfo(Token token){
        writeGetStatic("__FN", String.class);
        writePushMemory(LongMemory.valueOf(token.getMeta().getStartLine()));
        writePushMemory(LongMemory.valueOf(token.getMeta().getStartPosition()));
        writeSysStaticCall(TraceInfo.class, "valueOf", TraceInfo.class, String.class, Long.TYPE, Long.TYPE);
    }

    protected void writePushName(NameToken token){
        CompileConstant constant = compiler.getScope().findCompileConstant(token.getName());
        if (constant != null){
            writePushMemory(constant.value);
        } else {
            writePushEnv();
            writePushString(token.getName());
            writePushTraceInfo(token);
            writeSysDynamicCall(Environment.class, "getConstant", Memory.class, String.class, TraceInfo.class);
            setStackPeekAsImmutable();
        }
    }

    protected Memory tryWritePush(StackItem item){
        if (item.getToken() != null)
            return tryWritePush(item.getToken(), true);
        else if (item.getMemory() != null){
            return item.getMemory();
        } else
            stackPush(null, item.type);
        return null;
    }

    protected void writePush(StackItem item){
        Memory memory = tryWritePush(item);
        if (memory != null)
            writePushMemory(memory);
    }

    protected Memory tryWritePush(ValueExprToken value, boolean returnValue){
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
            return writePushCall((CallExprToken)value, returnValue);
        } else if (value instanceof NameToken){
            writePushName((NameToken)value);
        }
        return null;
    }

    protected void writePush(ValueExprToken value, boolean returnValue){
        Memory memory = tryWritePush(value, returnValue);
        if (memory != null)
            writePushMemory(memory);
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

    protected void writeGetStatic(Class clazz, String name, Class fieldClass){
        mv.visitFieldInsn(Opcodes.GETSTATIC, Type.getInternalName(clazz), name, Type.getDescriptor(fieldClass));
        stackPush(null, StackItem.Type.valueOf(fieldClass));
        setStackPeekAsImmutable();
    }

    protected void writeGetStatic(String name, Class fieldClass){
        mv.visitFieldInsn(
                Opcodes.GETSTATIC,
                method.clazz.entity.getName().replace('\\', '/'),
                name,
                Type.getDescriptor(fieldClass)
        );
        stackPush(null, StackItem.Type.valueOf(fieldClass));
        setStackPeekAsImmutable();
    }

    protected void writeGetEnum(Enum enumInstance){
        writeGetStatic(enumInstance.getDeclaringClass(), enumInstance.name(), enumInstance.getDeclaringClass());
    }

    protected void writeVariableAssign(VariableExprToken variable, StackItem R, boolean returnValue){
        LocalVariable local = method.getLocalVariable(variable.getName());

        Memory value = R == null ? null : R.getMemory();
        if (local.isReference()){
            writePushVariable(variable);
            if (R == null){
                writeSysStaticCall(Memory.class, "assignRight", void.class, stackPeek().type.toClass(), Memory.class);
            } else {
                writePush(R);
                writeSysDynamicCall(Memory.class, "assign", Memory.class, R.type.toClass());
            }
            if (returnValue)
                writeVarLoad(local.index);
        } else {
            Memory result = tryWritePush(R);
            if (result != null){
                writePushMemory(result);
                value = result;
            }
            writeVarStore(local.index, returnValue, true);
        }

        local.setValue(value);
    }

    protected void writeScalarOperator(StackItem.Type Lt, StackItem.Type Rt,
                                       StackItem.Type operatorType, String operatorName){
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
        stackPush(null, StackItem.Type.INT);
    }

    protected void writePopFloat(){
        writePopDouble();
        mv.visitInsn(Opcodes.D2F);
        stackPop();
        stackPush(null, StackItem.Type.FLOAT);
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
        StackItem.Type peek = stackPeek().type;
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
        StackItem.Type peek = stackPeek().type;
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

                if (peek == StackItem.Type.LONG){
                    stackPop();
                    stackPush(null, StackItem.Type.BOOL);
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
        writePush(L, true);
        writePopBoxing();

        LocalVariable local = null;
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

    protected void writeUnaryOperator(OperatorExprToken operator, boolean returnValue){

    }

    protected void writeOperator(OperatorExprToken operator, boolean returnValue){
        if (!operator.isBinary()){
            writeUnaryOperator(operator, returnValue);
            return;
        }

        if (stackEmpty())
            unexpectedToken(operator);

        StackItem o1 = stackPop();
        StackItem.Type Rt = o1.type;

        if (stackEmpty())
            unexpectedToken(operator);

        StackItem o2 = stackPeek();
        StackItem.Type Lt = o2.type;
        ValueExprToken L = stackPopToken();

        if (!(operator instanceof AssignExprToken || operator instanceof AssignOperatorExprToken))
        if (o1.getMemory() != null && o2.getMemory() != null){
            stackPush(CompilerUtils.calcBinary(o2.getMemory(), o1.getMemory(), operator));
            return;
        }

        LocalVariable variable = null;
        if (L instanceof VariableExprToken){
            variable = method.getLocalVariable(((VariableExprToken) L).getName());
        }

        if (operator instanceof AssignExprToken){
            if (L instanceof VariableExprToken){
                writeVariableAssign((VariableExprToken)L, o1, returnValue);
                return;
            }
        }

        Memory value1 = tryWritePush(o2); // LEFT
        Memory value2 = null;

        if (value1 != null)
            value2 = tryWritePush(o1); // RIGHT
        else
            writePush(o1);

        if (value1 != null && value2 != null){
            stackPush(value1);
            stackPush(value2);
            writeOperator(operator, returnValue);
            return;
        }

        if (value2 != null)
            writePushMemory(value2);

        //Memory.Type Rt = stackPop();
        //Memory.Type Lt = stackPeek();
        //stackPush(Rt);

        StackItem.Type operatorType = StackItem.Type.REFERENCE;

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
            operatorType = StackItem.Type.STRING;
        } else if (operator instanceof SmallerExprToken){
            name = "smaller";
            operatorType = StackItem.Type.BOOL;
        } else if (operator instanceof SmallerOrEqualToken){
            name = "smallerEq";
            operatorType = StackItem.Type.BOOL;
        } else if (operator instanceof GreaterExprToken){
            name = "greater";
            operatorType = StackItem.Type.BOOL;
        } else if (operator instanceof GreaterOrEqualExprToken){
            name = "greaterEq";
            operatorType = StackItem.Type.BOOL;
        } else if (operator instanceof EqualExprToken){
            name = "equal";
            operatorType = StackItem.Type.BOOL;
        } else if (operator instanceof BooleanNotEqualExprToken){
            name = "notEqual";
            operatorType = StackItem.Type.BOOL;
        }

        if (Rt.isConstant() && Lt.isConstant()){
            if (operator instanceof AssignOperatorExprToken)
                unexpectedToken(operator);

            writeScalarOperator(Lt, Rt, operatorType, name);
            setStackPeekAsImmutable();
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

            setStackPeekAsImmutable();
        } else {
            writeSysDynamicCall(
                    Memory.class, name, operatorType == null ? void.class : operatorType.toClass(), Rt.toClass()
            );
            setStackPeekAsImmutable();

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
                writeExpression(line, false, false);
            }
        }
    }

    protected void writeIf(IfStmtToken token){
        writeDefineVariables(token.getLocal());

        Label end = new Label();
        Label elseL = new Label();
        Memory memory = writeExpression(token.getCondition(), true, true);

        if (memory != null){
            if (memory.toBoolean()){
                writeBody(token.getBody());
            } else {
                writeBody(token.getElseBody());
            }
        } else {
            writePopBoolean();

            mv.visitJumpInsn(Opcodes.IFEQ, token.getElseBody() != null ? elseL : end);
            stackPop();

            writeBody(token.getBody());
            if (token.getElseBody() != null){
                mv.visitJumpInsn(Opcodes.GOTO, end);
                mv.visitLabel(elseL);
                writeBody(token.getElseBody());
            }

            mv.visitLabel(end);
            mv.visitLineNumber(token.getMeta().getEndLine(), end);
        }
        writeUndefineVariables(token.getLocal());
    }

    protected void writeWhile(WhileStmtToken token){
        writeDefineVariables(token.getLocal());

        Label start = writeLabel(mv, token.getMeta().getStartLine());
        Label end = new Label();

        writeExpression(token.getCondition(), true, false);
        writePopBoolean();

        mv.visitJumpInsn(Opcodes.IFEQ, end);
        stackPop();

        writeBody(token.getBody());

        mv.visitJumpInsn(Opcodes.GOTO, start);
        mv.visitLabel(end);
        mv.visitLineNumber(token.getMeta().getEndLine(), end);

        writeUndefineVariables(token.getLocal());
    }

    protected void writeReturn(ReturnStmtToken token){
        if (token.getValue() == null)
            writePushNull();
        else
            writeExpression(token.getValue(), true, false);

        if (stackEmpty())
            writePushNull();
        else
            writePopBoxing(true);

        mv.visitInsn(Opcodes.ARETURN);
        stackPop();
    }

    protected Memory writeExpression(ExprStmtToken expression, boolean returnValue, boolean returnMemory){
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

        if (returnMemory && returnValue && !stackEmpty() && stackPeek().isConstant())
            return stackPop().memory;

        if (returnValue && !stackEmpty() && stackPeek().isKnown()){
            writePush(stackPop());
        } else if (method.getStackSize() > 0){
            if (stackPeekToken() instanceof CallExprToken)
                writePush(stackPopToken(), returnValue);
        }

        if (!returnValue)
            writePopAll();

        return null;
    }

    protected void writePopAll(){
        while (method.getStackSize() > 0){
            ValueExprToken token = stackPeekToken();
            StackItem.Type type = stackPop().type;

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
            if (method.pop().getToken() == null)
                mv.visitInsn(Opcodes.POP);
        }
        method.popAll();
    }

    @Override
    public Entity compile() {
        mv = method.mv;
        writeDefineVariables(method.method.getLocal());
        writeExpression(expression, false, false);
        method.popAll();
        return null;
    }
}
