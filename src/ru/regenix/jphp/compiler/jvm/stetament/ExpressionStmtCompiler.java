package ru.regenix.jphp.compiler.jvm.stetament;

import org.apache.commons.lang3.StringUtils;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import ru.regenix.jphp.annotation.Runtime;
import ru.regenix.jphp.common.Association;
import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.compiler.common.ASMExpression;
import ru.regenix.jphp.compiler.common.compile.CompileConstant;
import ru.regenix.jphp.compiler.common.compile.CompileFunction;
import ru.regenix.jphp.compiler.common.misc.StackItem;
import ru.regenix.jphp.compiler.common.util.CompilerUtils;
import ru.regenix.jphp.compiler.jvm.Constants;
import ru.regenix.jphp.compiler.jvm.JvmCompiler;
import ru.regenix.jphp.compiler.jvm.misc.JumpItem;
import ru.regenix.jphp.compiler.jvm.misc.LocalVariable;
import ru.regenix.jphp.compiler.jvm.misc.StackFrame;
import ru.regenix.jphp.exceptions.CompileException;
import ru.regenix.jphp.exceptions.FatalException;
import ru.regenix.jphp.runtime.OperatorUtils;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.invoke.InvokeHelper;
import ru.regenix.jphp.runtime.invoke.ObjectInvokeHelper;
import ru.regenix.jphp.runtime.lang.BaseException;
import ru.regenix.jphp.runtime.lang.ForeachIterator;
import ru.regenix.jphp.runtime.lang.IObject;
import ru.regenix.jphp.runtime.memory.*;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.memory.support.MemoryUtils;
import ru.regenix.jphp.runtime.reflection.ClassEntity;
import ru.regenix.jphp.runtime.reflection.ConstantEntity;
import ru.regenix.jphp.runtime.reflection.helper.ClosureEntity;
import ru.regenix.jphp.runtime.reflection.support.Entity;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.OpenEchoTagToken;
import ru.regenix.jphp.tokenizer.token.Token;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;
import ru.regenix.jphp.tokenizer.token.expr.ValueExprToken;
import ru.regenix.jphp.tokenizer.token.expr.operator.*;
import ru.regenix.jphp.tokenizer.token.expr.value.*;
import ru.regenix.jphp.tokenizer.token.expr.value.macro.*;
import ru.regenix.jphp.tokenizer.token.stmt.*;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

import static org.objectweb.asm.Opcodes.*;

public class ExpressionStmtCompiler extends StmtCompiler {
    protected final MethodStmtCompiler method;
    protected final ExprStmtToken expression;

    private MethodNode node;
    private InsnList code;
    private Stack<StackFrame> frames;

    private Stack<Integer> exprStackInit = new Stack<Integer>();

    private int lastLineNumber = -1;

    public ExpressionStmtCompiler(MethodStmtCompiler method, ExprStmtToken expression) {
        super(method.getCompiler());
        this.method = method;
        this.expression = expression;
        this.node = method.node;
        this.code = method.node.instructions;
        this.frames = new Stack<StackFrame>();
    }

    public ExpressionStmtCompiler(JvmCompiler compiler){
        super(compiler);
        this.expression = null;
        ClassStmtCompiler classStmtCompiler = new ClassStmtCompiler(
                compiler, new ClassStmtToken(new TokenMeta("", 0, 0, 0, 0))
        );

        this.method = new MethodStmtCompiler(classStmtCompiler, (MethodStmtToken) null);
        this.node = method.node;
        this.code = method.node.instructions;
        this.frames = new Stack<StackFrame>();
    }

    protected StackFrame addStackFrame(LabelNode start, LabelNode end){
        FrameNode node = new FrameNode(F_SAME, 0, null, 0, null);
        StackFrame frame = new StackFrame(node, start, end);
        frames.push(frame);
        code.add(node); // frame f_same
        code.add(start); // label start:

        return frame;
    }

    protected StackFrame addStackFrame(){
        return addStackFrame(new LabelNode(), new LabelNode());
    }

    protected StackFrame getStackFrame(){
        if (frames.empty()) return null;
        return frames.peek();
    }

    protected StackFrame removeStackFrame(){
        StackFrame frame = frames.pop();
        code.add(frame.end); // label end;
        return frame;
    }

    @SuppressWarnings("unchecked")
    protected void processVariable(LocalVariable variable){
        StackFrame frame = getStackFrame();
        if (frame != null && !frame.hasVariable(variable)){
            frame.addVariable(variable);
        }
    }

    protected void makeVarStore(LocalVariable variable){
        processVariable(variable);
        code.add(new VarInsnNode(ASTORE, variable.index));
    }

    protected void makeVarLoad(LocalVariable variable){
        processVariable(variable);
        code.add(new VarInsnNode(ALOAD, variable.index));
    }

    protected Memory getMacros(ValueExprToken token){
        if (token instanceof SelfExprToken){
            return new StringMemory(method.clazz.statement.getFulledName());
        }

        return null;
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
        if (o != null) {
            stackPush(o);
        } else if (token instanceof StaticAccessExprToken){
            StaticAccessExprToken access = (StaticAccessExprToken)token;
            if (access.getField() instanceof NameToken && access.getClazz() instanceof FulledNameToken){
                String constant = ((NameToken) access.getField()).getName();
                String clazz    = ((FulledNameToken) access.getClazz()).getName();

                ClassEntity entity = compiler.getModule().findClass(clazz);
                if (entity == null && clazz.equalsIgnoreCase(method.clazz.entity.getName()))
                    entity = method.clazz.entity;

                if (entity != null){
                    ConstantEntity c = entity.findConstant(constant);
                    if (c != null){
                        stackPush(c.getValue());
                        stackPeek().setLevel(-1);
                        return;
                    }
                }
            }
            stackPush(token, StackItem.Type.REFERENCE);
        } else {
            if (token instanceof VariableExprToken
                    && !method.statement.isUnstableVariable((VariableExprToken)token)){
                LocalVariable local = method.getLocalVariable(((VariableExprToken) token).getName());
                if (local != null && local.getValue() != null){
                    stackPush(token, local.getValue());
                    stackPeek().setLevel(-1);
                    return;
                }
            }
            stackPush(token, StackItem.Type.REFERENCE);
        }
        stackPeek().setLevel(-1);
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

    protected void setStackPeekAsImmutable(boolean value){
        method.peek().immutable = value;
    }

    protected void setStackPeekAsImmutable(){
        setStackPeekAsImmutable(true);
    }

    protected ValueExprToken stackPeekToken(){
        return method.peek().getToken();
    }

    protected boolean stackEmpty(boolean relative){
        if (relative)
            return method.getStackCount() == exprStackInit.peek();
        else
            return method.getStackCount() == 0;
    }

    protected boolean isStackPeekValue(boolean relative){
        if (stackEmpty(relative))
            return false;

        Token token = stackPeek().getToken();
        return (token == null || token instanceof ValueExprToken);
    }

    void writeLineNumber(Token token) {
        if (token.getMeta().getStartLine() > lastLineNumber){
            lastLineNumber = token.getMeta().getStartLine();
            code.add(new LineNumberNode(lastLineNumber, new LabelNode()));
        }
    }

    void writePushBooleanAsMemory(boolean value){
        if (value)
            code.add(new FieldInsnNode(
                    GETSTATIC, Type.getInternalName(Memory.class), "TRUE", Type.getDescriptor(Memory.class)
            ));
        else
            code.add(new FieldInsnNode(
                    GETSTATIC, Type.getInternalName(Memory.class), "FALSE", Type.getDescriptor(Memory.class)
            ));

        stackPush(Memory.Type.REFERENCE);
    }

    void writePushMemory(Memory memory){
        Memory.Type type = Memory.Type.REFERENCE;

        if (memory instanceof NullMemory){
            code.add(new FieldInsnNode(
                    GETSTATIC, Type.getInternalName(Memory.class), "NULL", Type.getDescriptor(Memory.class)
            ));
        } else if (memory instanceof FalseMemory){
            writePushConstBoolean(false);
            return;
            /*code.add(new FieldInsnNode(
                    GETSTATIC, Type.getInternalName(Memory.class), "FALSE", Type.getDescriptor(Memory.class)
            )); */
        } else if (memory instanceof TrueMemory){
            writePushConstBoolean(true);
            return;
            /*code.add(new FieldInsnNode(
                    GETSTATIC, Type.getInternalName(Memory.class), "TRUE", Type.getDescriptor(Memory.class)
            ));*/
        } else if (memory instanceof ReferenceMemory){
            code.add(new TypeInsnNode(NEW, Type.getInternalName(ReferenceMemory.class)));
            code.add(new InsnNode(DUP));
            code.add(new MethodInsnNode(INVOKESPECIAL, Type.getInternalName(ReferenceMemory.class), Constants.INIT_METHOD, "()V"));
        } else if (memory instanceof ArrayMemory) {
            writePushNewObject(ArrayMemory.class);
            ArrayMemory array = (ArrayMemory)memory;

            ForeachIterator foreachIterator = ((ArrayMemory)memory).foreachIterator(false, false);
            while (foreachIterator.next()){
                writePushDup();
                if (array.isList()) {
                    writePushMemory(foreachIterator.getValue());
                    writePopBoxing();
                    writeSysDynamicCall(ArrayMemory.class, "add", ReferenceMemory.class, Memory.class);
                } else {
                    Memory key = foreachIterator.getMemoryKey();
                    writePushMemory(key);
                    if (!key.isString())
                        writePopBoxing();

                    writePushMemory(foreachIterator.getValue());
                    writePopBoxing();

                    writeSysDynamicCall(ArrayMemory.class, "put", ReferenceMemory.class, Object.class, Memory.class);
                }

                writePopAll(1);
            }
            stackPop();
            stackPush(Memory.Type.ARRAY);
            setStackPeekAsImmutable();
            return;
        } else {
            switch (memory.type){
                case INT: {
                    code.add(new LdcInsnNode(memory.toLong()));
                    type = Memory.Type.INT;
                } break;
                case DOUBLE: {
                    code.add(new LdcInsnNode(memory.toDouble()));
                    type = Memory.Type.DOUBLE;
                } break;
                case STRING: {
                    code.add(new LdcInsnNode(memory.toString()));
                    type = Memory.Type.STRING;
                } break;
            }
        }

        stackPush(type);
        setStackPeekAsImmutable();
    }

    boolean writePopBoxing(boolean asImmutable){
        return writePopBoxing(stackPeek().type, asImmutable);
    }

    boolean writePopBoxing(){
        return writePopBoxing(false);
    }

    boolean writePopBoxing(Class<?> clazz, boolean asImmutable){
        return writePopBoxing(StackItem.Type.valueOf(clazz), asImmutable);
    }

    boolean writePopBoxing(Class<?> clazz){
        return writePopBoxing(clazz, false);
    }

    boolean writePopBoxing(StackItem.Type type){
        return writePopBoxing(type, false);
    }

    boolean writePopBoxing(StackItem.Type type, boolean asImmutable){
        switch (type){
            case BOOL:
                writeSysStaticCall(TrueMemory.class, "valueOf", Memory.class, type.toClass());
                setStackPeekAsImmutable();
                return true;
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
                if (asImmutable){
                    writePopImmutable();
                }
            } break;
        }
        return false;
    }

    void writePushInt(IntegerExprToken value){
        writePushMemory(new LongMemory(value.getValue()));
    }

    void writePushInt(long value){
        writePushMemory(LongMemory.valueOf(value));
    }

    void writePushSmallInt(int value){
        switch (value){
            case -1: code.add(new InsnNode(ICONST_M1)); break;
            case 0: code.add(new InsnNode(ICONST_0)); break;
            case 1: code.add(new InsnNode(ICONST_1)); break;
            case 2: code.add(new InsnNode(ICONST_2)); break;
            case 3: code.add(new InsnNode(ICONST_3)); break;
            case 4: code.add(new InsnNode(ICONST_4)); break;
            case 5: code.add(new InsnNode(ICONST_5)); break;
            default:
                code.add(new LdcInsnNode(value));
        }

        stackPush(Memory.Type.INT);
    }

    void writePushHex(HexExprValue value){
        writePushInt(new IntegerExprToken(TokenMeta.of(value.getValue() + "", value)));
    }

    void _writePushBoolean(boolean value){
        writePushMemory(value ? Memory.TRUE : Memory.FALSE);
    }

    void writePushScalarBoolean(boolean value){
        writePushSmallInt(value ? 1 : 0);
        stackPop();
        stackPush(value ? Memory.TRUE : Memory.FALSE);
    }

    void writePushBoolean(BooleanExprToken value){
        _writePushBoolean(value.getValue());
    }

    void writePushStringBuilder(StringBuilderExprToken value){
        writePushNewObject(StringBuilder.class);

        for(Token el : value.getExpression()){
            //writePushDup();
            if (el instanceof ValueExprToken){
                writePush((ValueExprToken)el, true, false);
            } else if (el instanceof ExprStmtToken){
                //unexpectedToken(el);
                writeExpression((ExprStmtToken)el, true, false, true);
            } else
                unexpectedToken(el);

            StackItem.Type peek = stackPeek().type;
            if (!peek.isConstant()) {
                writeSysDynamicCall(StringBuilder.class, "append", StringBuilder.class, Object.class);
            } else
                writeSysDynamicCall(StringBuilder.class, "append", StringBuilder.class, peek.toClass());
        }

        writeSysDynamicCall(StringBuilder.class, "toString", String.class);
    }

    void writePushString(StringExprToken value){
        writePushMemory(new StringMemory(value.getValue()));
    }

    void writePushString(String value){
        writePushMemory(new StringMemory(value));
    }

    void writePushDouble(DoubleExprToken value){
        writePushMemory(new DoubleMemory(value.getValue()));
    }

    void writePushConstString(String value){
        writePushString(value);
    }

    void writePushConstDouble(double value){
        code.add(new LdcInsnNode(value));
        stackPush(null, StackItem.Type.DOUBLE);
    }

    void writePushConstFloat(float value){
        code.add(new LdcInsnNode(value));
        stackPush(null, StackItem.Type.FLOAT);
    }

    void writePushConstLong(long value){
        code.add(new LdcInsnNode(value));
        stackPush(null, StackItem.Type.LONG);
    }

    void writePushConstInt(int value){
        writePushSmallInt(value);
    }

    void writePushConstShort(short value){
        writePushConstInt(value);
        stackPop();
        stackPush(null, StackItem.Type.SHORT);
    }

    void writePushConstByte(byte value){
        writePushConstInt(value);
        stackPop();
        stackPush(null, StackItem.Type.BYTE);
    }

    void writePushConstBoolean(boolean value){
        writePushConstInt(value ? 1 : 0);
        stackPop();
        stackPush(null, StackItem.Type.BOOL);
    }

    void writePushGetVar(GetVarExprToken getVar, boolean returnValue){
        assert method.statement.isDynamicLocal();

        Memory result = writeExpression(getVar.getName(), true, true, false);
        if (result != null && result.isString()){
            String name = result.toString();
            LocalVariable variable = method.getLocalVariable(name);
            if (variable != null){
                if (returnValue)
                    writeVarLoad(variable);
                return;
            }
        }

        writePushLocal();
        writeExpression(getVar.getName(), true, false);
        writePopBoxing();

        writeSysDynamicCall(ArrayMemory.class, "refOfIndex", Memory.class, Memory.class);
        if (!returnValue)
            writePopAll(1);
    }

    void writePushClosure(ClosureStmtToken closure, boolean returnValue){
        if (returnValue){
            ClosureEntity entity = method.compiler.getModule().findClosure( closure.getId() );
            boolean thisExists = closure.getFunction().isThisExists();
            if (closure.getFunction().getUses().isEmpty() && !thisExists
                    && closure.getFunction().getStaticLocal().isEmpty()){
                writePushEnv();
                writePushConstInt(compiler.getModule().getId());
                writePushConstInt((int)entity.getId());
                writeSysDynamicCall(Environment.class, "getSingletonClosure", Memory.class, Integer.TYPE, Integer.TYPE);
            } else {
                code.add(new TypeInsnNode(NEW, entity.getInternalName()));
                stackPush(Memory.Type.REFERENCE);
                writePushDup();

                writePushEnv();
                writePushConstInt(compiler.getModule().getId());
                writePushConstInt((int)entity.getId());
                writeSysDynamicCall(Environment.class, "__getClosure", ClassEntity.class, Integer.TYPE, Integer.TYPE);

                if (thisExists)
                    writePushThis();
                else
                    writePushNull();

                writePushUses(closure.getFunction().getUses());
                code.add(new MethodInsnNode(
                        INVOKESPECIAL, entity.getInternalName(), Constants.INIT_METHOD,
                        Type.getMethodDescriptor(
                                Type.getType(void.class),
                                Type.getType(ClassEntity.class), Type.getType(Memory.class), Type.getType(Memory[].class)
                        )
                ));
                stackPop();
                stackPop();
                stackPop();
                stackPop();

                writeSysStaticCall(ObjectMemory.class, "valueOf", Memory.class, IObject.class);
            }
            setStackPeekAsImmutable();
        }
    }

    void writePushSelf(boolean toLower){
        writePushString(toLower ?
                method.clazz.statement.getFulledName().toLowerCase()
                : method.clazz.statement.getFulledName()
        );
    }

    void writePushStatic(){
        writePushEnv();
        writeSysDynamicCall(Environment.class, "getLateStatic", String.class);
    }

    void writePushLocal(){
        writeVarLoad("~local");
    }

    void writePushEnv(){
        LocalVariable variable = method.getLocalVariable("~env");
        if (variable == null) {
            if (!method.statement.isStatic())
                writePushEnvFromSelf();
            else
                throw new RuntimeException("Cannot find `~end` variable");
            return;
        }

        stackPush(Memory.Type.REFERENCE);
        makeVarLoad(variable);
    }

    void writePushEnvFromSelf(){
        writeVarLoad("~this");
        writeSysDynamicCall(null, "getEnvironment", Environment.class);
    }

    void writePushDup(StackItem.Type type){
        stackPush(null, type);
        if (type.size() == 2)
            code.add(new InsnNode(DUP2));
        else
            code.add(new InsnNode(DUP));
    }

    void writePushDup(){
        StackItem item = method.peek();
        stackPush(item);

        if (item.type.size() == 2)
            code.add(new InsnNode(DUP2));
        else
            code.add(new InsnNode(DUP));
    }

    void writePushDupLowerCase(){
        writePushDup();
        writePopString();
        writeSysDynamicCall(String.class, "toLowerCase", String.class);
    }

    void writePushNull(){
        writePushMemory(Memory.NULL);
    }

    void writePushConstNull(){
        stackPush(Memory.Type.REFERENCE);
        code.add(new InsnNode(ACONST_NULL));
    }

    void writePushNewObject(Class clazz){
        code.add(new TypeInsnNode(NEW, Type.getInternalName(clazz)));
        stackPush(Memory.Type.REFERENCE);
        writePushDup();
        writeSysCall(clazz, INVOKESPECIAL, Constants.INIT_METHOD, void.class);
        stackPop();
    }

    void writePushStaticCall(Method method){
        writeSysStaticCall(
                method.getDeclaringClass(),
                method.getName(),
                method.getReturnType(),
                method.getParameterTypes()
        );
    }

    Memory writePushCompileFunction(CallExprToken function, CompileFunction compileFunction, boolean returnValue,
                                    boolean writeOpcode, PushCallStatistic statistic){
        CompileFunction.Method method = compileFunction.find( function.getParameters().size() );
        if (method == null){
            throw new CompileException(
                    Messages.ERR_FATAL_PASS_INCORRECT_ARGUMENTS_TO_FUNCTION.fetch(function.getName().getWord()),
                    function.getName().toTraceInfo(compiler.getContext())
            );
        }

        if (statistic != null)
            statistic.returnType = StackItem.Type.valueOf(method.resultType);

        Class[] types = method.parameterTypes;
        ListIterator<ExprStmtToken> iterator = function.getParameters().listIterator();

        Object[] arguments = new Object[types.length];
        int j = 0;
        boolean immutable = method.isImmutable;

        boolean init = false;
        for(int i = 0; i < method.parameterTypes.length; i++){
            Class<?> argType = method.parameterTypes[i];
            boolean isRef = method.references[i];

            if (method.isPresentAnnotationOfParam(i, Runtime.GetLocals.class)){
                if (!writeOpcode)
                    return null;

                immutable = false;
                writePushLocal();
            } else if (argType == Environment.class){
                if (immutable){
                    arguments[i] = compiler.getEnvironment();
                } else {
                    if (!writeOpcode)
                        return null;
                    writePushEnv();
                }
            } else if (argType == TraceInfo.class){
                if (immutable){
                    arguments[i] = function.toTraceInfo(compiler.getContext());
                } else {
                    if (!writeOpcode)
                        return null;

                    writePushTraceInfo(function);
                }
            } else {
                if (argType == Memory[].class){
                    Memory[] args = new Memory[function.getParameters().size() - j];
                    boolean arrCreated = false;
                    for(int t = 0; t < function.getParameters().size() - j; t++){
                        ExprStmtToken next = iterator.next();
                        if (immutable)
                            args[t] = writeExpression(next, true, true, false);

                        if (args[t] == null){
                            if (!writeOpcode)
                                return null;

                            if (!arrCreated) {
                                if (immutable){
                                    for(int n = 0; n < i /*j*/; n++) {
                                        if (arguments[n] instanceof TraceInfo){
                                            writePushTraceInfo(function);
                                        } else if (arguments[n] instanceof Environment){
                                            writePushEnv();
                                        } else {
                                            writePushMemory((Memory)arguments[n]);
                                            writePop(types[n], true, true);
                                            arguments[t] = null;
                                        }
                                    }
                                }

                                // create new array
                                writePushSmallInt(args.length);
                                code.add(new TypeInsnNode(ANEWARRAY, Type.getInternalName(Memory.class)));
                                stackPop();
                                stackPush(Memory.Type.REFERENCE);
                                arrCreated = true;
                            }
                            writePushDup();
                            writePushSmallInt(t);
                            writeExpression(
                                    next, true, false, writeOpcode
                            );

                            if (!isRef)
                                writePopBoxing(true);

                            code.add(new InsnNode(AASTORE));
                            stackPop(); stackPop(); stackPop();

                            immutable = false;
                        }
                    }

                    if (!immutable && !arrCreated){
                        code.add(new InsnNode(ACONST_NULL));
                        stackPush(Memory.Type.REFERENCE);
                    }

                    arguments[i/*j*/] = MemoryUtils.valueOf(args);
                } else {
                    ExprStmtToken next = iterator.next();
                    if (!immutable && !init){
                        init = true;
                        for(int k = 0; k < i/*j*/; k++){
                            if (arguments[k] != null){
                                if (arguments[k] instanceof TraceInfo){
                                    writePushTraceInfo(function);
                                } else if (arguments[k] instanceof Environment){
                                    writePushEnv();
                                } else {
                                    writePushMemory((Memory)arguments[k]);
                                    writePop(types[k], true, isRef);
                                    arguments[k] = null;
                                }
                            }
                        }
                    }

                    if (immutable)
                        arguments[i/*j*/] = writeExpression(next, true, true, false);

                    if (arguments[i/*j*/] == null){
                        if (!writeOpcode)
                            return null;

                        if (!init) {
                            for(int n = 0; n < i/*j*/; n++){
                                if (arguments[n] instanceof TraceInfo) {
                                    writePushTraceInfo(function);
                                } else if (arguments[n] instanceof Environment) {
                                    writePushEnv();
                                } else {
                                    writePushMemory((Memory)arguments[n]);
                                    writePop(types[n], true, false);
                                }
                                arguments[n] = null;
                            }

                            init = true;
                        }

                        if (isRef)
                            writeExpression(next, true, false, writeOpcode);
                        else {
                            Memory tmp = writeExpression(next, true, true, true);
                            if (tmp != null)
                                writePushMemory(tmp);
                        }

                        writePop(argType, true, !isRef);
                        immutable = false;
                    }
                    j++;
                }
            }
        }

        if (immutable){
            if (!returnValue)
                return null;

            Object[] typedArguments = new Object[arguments.length];
            for(int i = 0; i < arguments.length; i++){
                if (method.parameterTypes[i] != Memory.class && arguments[i] instanceof Memory)
                    typedArguments[i] = method.converters[i].run((Memory)arguments[i]);
                                        //MemoryUtils.toValue((Memory)arguments[i], types[i]);
                else
                    typedArguments[i] = arguments[i];
            }
            try {
                Object value = method.method.invoke(null, typedArguments);
                return MemoryUtils.valueOf(value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e.getCause());
            }
        }

        if (!writeOpcode)
            return null;

        this.method.entity.setImmutable(false);

        writeLineNumber(function);
        writePushStaticCall(method.method);
        if (returnValue){
            if (method.resultType == void.class)
                writePushNull();
            setStackPeekAsImmutable();
        }
        return null;
    }

    void writePushParameters(Collection<ExprStmtToken> parameters){
        if (parameters.isEmpty()){
            code.add(new InsnNode(ACONST_NULL));
            stackPush(Memory.Type.REFERENCE);
            return;
        }

        writePushSmallInt(parameters.size());
        code.add(new TypeInsnNode(ANEWARRAY, Type.getInternalName(Memory.class)));
        stackPop();
        stackPush(Memory.Type.REFERENCE);

        int i = 0;
        for(ExprStmtToken param : parameters){
            writePushDup();
            writePushSmallInt(i);
            writeExpression(param, true, false);
            writePopBoxing();

            code.add(new InsnNode(AASTORE));
            stackPop();
            stackPop();
            stackPop();
            i++;
        }
    }

    void writePushUses(Collection<ArgumentStmtToken> parameters){
        if (parameters.isEmpty()){
            code.add(new InsnNode(ACONST_NULL));
            stackPush(Memory.Type.REFERENCE);
            return;
        }

        writePushSmallInt(parameters.size());
        code.add(new TypeInsnNode(ANEWARRAY, Type.getInternalName(Memory.class)));
        stackPop();
        stackPush(Memory.Type.REFERENCE);

        int i = 0;
        for(ArgumentStmtToken param : parameters){
            writePushDup();
            writePushSmallInt(i);

            LocalVariable local = method.getLocalVariable(param.getName().getName());
            writeVarLoad(local);
            if (!param.isReference())
                writePopBoxing(true);

            code.add(new InsnNode(AASTORE));
            stackPop();
            stackPop();
            stackPop();
            i++;
        }
    }
    Memory writePushParentDynamicMethod(CallExprToken function, boolean returnValue, boolean writeOpcode,
                                  PushCallStatistic statistic){
        if (!writeOpcode)
            return null;

        StaticAccessExprToken dynamic = (StaticAccessExprToken)function.getName();
        writeLineNumber(function);

        writePushThis();
        if (dynamic.getField() != null){
            if (dynamic.getField() instanceof NameToken){
                String name = ((NameToken) dynamic.getField()).getName();
                writePushString(name);
                writePushString(name.toLowerCase());
            } else {
                writePush(dynamic.getField(), true, false);
                writePopString();
                writePushDupLowerCase();
            }
        } else {
            writeExpression(dynamic.getFieldExpr(), true, false);
            writePopString();
            writePushDupLowerCase();
        }

        writePushEnv();
        writePushTraceInfo(dynamic);
        writePushParameters(function.getParameters());

        writeSysStaticCall(
                ObjectInvokeHelper.class, "invokeParentMethod",
                Memory.class,
                Memory.class, String.class, String.class, Environment.class, TraceInfo.class, Memory[].class
        );

        if (!returnValue)
            writePopAll(1);

        if (statistic != null)
            statistic.returnType = StackItem.Type.REFERENCE;

        return null;
    }

    Memory writePushDynamicMethod(CallExprToken function, boolean returnValue, boolean writeOpcode,
                                  PushCallStatistic statistic){
        if (!writeOpcode)
            return null;

        DynamicAccessExprToken access = (DynamicAccessExprToken)function.getName();
        if (access instanceof DynamicAccessAssignExprToken)
            unexpectedToken(access);

        writeLineNumber(function);
        writeDynamicAccessPrepare(access, true);
        writePushParameters(function.getParameters());

        writeSysStaticCall(
                ObjectInvokeHelper.class, "invokeMethod",
                Memory.class,
                Memory.class, String.class, String.class, Environment.class, TraceInfo.class, Memory[].class
        );
        if (!returnValue)
            writePopAll(1);

        if (statistic != null)
            statistic.returnType = StackItem.Type.REFERENCE;

        return null;
    }

    Memory writePushStaticMethod(CallExprToken function, boolean returnValue, boolean writeOpcode,
                                 PushCallStatistic statistic){
        StaticAccessExprToken access = (StaticAccessExprToken)function.getName();
        if (!writeOpcode)
            return null;

        writeLineNumber(function);

        writePushEnv();
        writePushTraceInfo(function.getName());
        String methodName = null;

        ValueExprToken clazz = access.getClazz();
        if ((clazz instanceof NameToken || clazz instanceof SelfExprToken)
                && access.getField() instanceof NameToken){
            String className;
            if (clazz instanceof SelfExprToken)
                className = getMacros(clazz).toString();
            else
                className = ((NameToken)clazz).getName();

            methodName = ((NameToken) access.getField()).getName();

            writePushString(className.toLowerCase());
            writePushString(methodName.toLowerCase());

            writePushString(className);
            writePushString(methodName);

            writePushParameters(function.getParameters());
            writeSysStaticCall(
                    InvokeHelper.class, "callStatic", Memory.class,
                    Environment.class, TraceInfo.class,
                    String.class, String.class, // lower sign name
                    String.class, String.class, // origin names
                    Memory[].class
            );
        } else {
            if (clazz instanceof NameToken){
                writePushString(((NameToken) clazz).getName());
                writePushDupLowerCase();
            } else if (clazz instanceof SelfExprToken){
                writePushSelf(false);
                writePushSelf(true);
            } else if (clazz instanceof StaticExprToken){
                writePushStatic();
                writePushDupLowerCase();
            } else {
                writePush(clazz, true, false);
                writePopString();
                writePushDupLowerCase();
            }

            if (access.getField() != null){
                ValueExprToken field = access.getField();
                if (field instanceof NameToken){
                    writePushString(((NameToken) field).getName());
                    writePushString(((NameToken) field).getName().toLowerCase());
                } else {
                    writePush(access.getField(), true, false);
                    writePopString();
                    writePushDupLowerCase();
                }
            } else {
                writeExpression(access.getFieldExpr(), true, false);
                writePopString();
                writePushDupLowerCase();
            }

            writePushParameters(function.getParameters());
            writeSysStaticCall(
                    InvokeHelper.class, "callStaticDynamic", Memory.class,
                    Environment.class, TraceInfo.class,
                    String.class, String.class,
                    String.class, String.class,
                    Memory[].class
            );
        }
        if (statistic != null)
            statistic.returnType = StackItem.Type.REFERENCE;

        return null;
    }

    public static class PushCallStatistic {
        public StackItem.Type returnType = StackItem.Type.REFERENCE;
    }

    Memory writePushCall(CallExprToken function, boolean returnValue, boolean writeOpcode,
                         PushCallStatistic statistic){
        Token name = function.getName();
        if (name instanceof NameToken){
            String realName = ((NameToken) name).getName();
            CompileFunction compileFunction = compiler.getScope().findCompileFunction(realName);

            // try find system function, like max, sin, cos, etc.
            if (compileFunction == null
                    && name instanceof FulledNameToken
                    && compiler.getEnvironment().functionMap.get(realName.toLowerCase()) == null
                    && compiler.findFunction(realName) == null){
                String tryName = ((FulledNameToken) name).getLastName().getName();
                compileFunction = compiler.getScope().findCompileFunction(tryName);
            }

            if (compileFunction != null){
                return writePushCompileFunction(function, compileFunction, returnValue, writeOpcode, statistic);
            } else {
                if (!writeOpcode)
                    return null;
                method.entity.setImmutable(false);

                writePushEnv();
                writePushTraceInfo(function);
                writePushString(realName.toLowerCase());
                writePushString(realName);
                writePushParameters(function.getParameters());
                writeSysStaticCall(
                        InvokeHelper.class, "call", Memory.class,
                        Environment.class, TraceInfo.class, String.class, String.class, Memory[].class
                );
                if (!returnValue)
                    writePopAll(1);
            }
        } else if (name instanceof StaticAccessExprToken){
            method.entity.setImmutable(false);
            if (((StaticAccessExprToken) name).isAsParent())
                return writePushParentDynamicMethod(function, returnValue, writeOpcode, statistic);
            else
                return writePushStaticMethod(function, returnValue, writeOpcode, statistic);
        } else if (name instanceof DynamicAccessExprToken){
            method.entity.setImmutable(false);
            return writePushDynamicMethod(function, returnValue, writeOpcode, statistic);
        } else {
            if (!writeOpcode)
                return null;
            method.entity.setImmutable(false);
            writeLineNumber(function);

            writePush((ValueExprToken)function.getName(), true, false);
            writePopBoxing();
            writePushParameters(function.getParameters());

            writePushEnv();
            writePushTraceInfo(function);

            writeSysStaticCall(
                    InvokeHelper.class, "callAny", Memory.class,
                    Memory.class, Memory[].class, Environment.class, TraceInfo.class
            );
            if (!returnValue)
                writePopAll(1);
        }
        return null;
    }

    void writePushImport(ImportExprToken include, boolean returnValue){
        writePushEnv();

        writeExpression(include.getValue(), true, false);
        writePopString();

        writePushLocal();
        writePushTraceInfo(include);

        String name = "";
        if (include instanceof IncludeOnceExprToken)
            name = "includeOnce";
        else if (include instanceof IncludeExprToken)
            name = "include";
        else
            unexpectedToken(include);

        writeSysDynamicCall(Environment.class, name, Memory.class,
                String.class, ArrayMemory.class, TraceInfo.class
        );
        if (!returnValue)
            writePopAll(1);
    }

    void writePushDie(DieExprToken token, boolean returnValue) {
        writePushEnv();
        if (token.getValue() == null)
            writePushConstNull();
        else {
            writeExpression(token.getValue(), true, false, true);
            writePopBoxing(false);
        }

        writeSysDynamicCall(Environment.class, "die", void.class, Memory.class);
        if (returnValue)
            writePushConstBoolean(true);
    }

    void writePushIsset(IssetExprToken token, boolean returnValue){
        writePushParameters(token.getParameters());
        writeSysStaticCall(OperatorUtils.class, "isset", Boolean.TYPE, Memory[].class);

        if (!returnValue)
            writePopAll(1);
    }

    void writePushEmpty(EmptyExprToken token, boolean returnValue){
        writeExpression(token.getValue(), true, false);
        writeSysStaticCall(OperatorUtils.class, "empty", Boolean.TYPE, Memory.class);

        if (!returnValue)
            writePopAll(1);
    }

    void writePushUnset(UnsetExprToken token, boolean returnValue){
        for(ExprStmtToken param : token.getParameters()){
            if (param.isSingle() && param.getSingle() instanceof VariableExprToken){
                VariableExprToken variable = (VariableExprToken)param.getSingle();
                LocalVariable local = method.getLocalVariable(variable.getName());

                if (local.isReference()){
                    writeVarLoad(local);
                    writeSysDynamicCall(Memory.class, "unset", void.class);
                } else {
                    writePushNull();
                    writeVarAssign(local, false, false);
                }
                local.setValue(null);
            } else if (param.isSingle() && param.getSingle() instanceof GetVarExprToken){
                writePushGetVar((GetVarExprToken)param.getSingle(), true);
                writeSysDynamicCall(Memory.class, "unset", void.class);
            } else {
                writeExpression(param, false, false, true);
            }
        }

        if (returnValue)
            writePushNull();
    }

    void writePushNew(NewExprToken newToken, boolean returnValue){
        writeLineNumber(newToken);

        writePushEnv();
        if (newToken.isDynamic()){
            writePushVariable((VariableExprToken) newToken.getName());
            writePopString();
            writePushDupLowerCase();
        } else {
            if (newToken.getName() instanceof StaticExprToken){
                writePushStatic();
                writePushDupLowerCase();
            } else {
                FulledNameToken name = (FulledNameToken) newToken.getName();
                writePushString(name.getName());
                writePushString(name.getName().toLowerCase());
            }
        }
        writePushTraceInfo(newToken.getName());
        writePushParameters(newToken.getParameters());
        writeSysDynamicCall(
                Environment.class, "newObject",
                Memory.class,
                String.class, String.class, TraceInfo.class, Memory[].class
        );

        if (!returnValue)
            writePopAll(1);
    }

    protected void writeDefineVariables(Collection<VariableExprToken> values){
        for(VariableExprToken value : values)
            writeDefineVariable(value);
    }

    protected void writeUndefineVariables(Collection<VariableExprToken> values){
        LabelNode end = new LabelNode();
        for(VariableExprToken value : values)
            writeUndefineVariable(value, end);
    }

    protected void writeDefineGlobalVar(String name){
        writePushEnv();
        writePushConstString(name);
        writeSysDynamicCall(Environment.class, "getOrCreateGlobal", Memory.class, String.class);
        makeVarStore(method.getLocalVariable(name));
        stackPop();
    }

    protected void writePushThis(){
        if (method.clazz.isClosure()){
            writeVarLoad("~this");
            writeGetDynamic("self", Memory.class);
        } else {
            if (method.getLocalVariable("this") == null) {
                if (!method.statement.isStatic()){
                    LabelNode label = writeLabel(node);
                    LocalVariable local = method.addLocalVariable("this", label, Memory.class);
                    writeDefineThis(local);
                } else {
                    writePushNull();
                    return;
                }
            }

            writeVarLoad("this");
        }
    }

    protected void writeDefineThis(LocalVariable variable){
        if (method.clazz.isClosure()){
            writeVarLoad("~this");
            writeGetDynamic("self", Memory.class);
            makeVarStore(variable);
            stackPop();
        } else {
            LabelNode endLabel = new LabelNode();
            LabelNode elseLabel = new LabelNode();

            writeVarLoad("~this");
            writeSysDynamicCall(null, "isMock", Boolean.TYPE);

            code.add(new JumpInsnNode(IFEQ, elseLabel));
            stackPop();

            writePushNull();
            writeVarStore(variable, false, false);

            code.add(new JumpInsnNode(GOTO, endLabel));
            code.add(elseLabel);

            writeVarLoad("~this");
            writeSysStaticCall(ObjectMemory.class, "valueOf", Memory.class, IObject.class);
            makeVarStore(variable);
            stackPop();

            code.add(endLabel);
        }

        variable.pushLevel();
        variable.setValue(null);
        variable.setImmutable(false);
        variable.setReference(true);
    }

    protected void writeDefineVariable(VariableExprToken value){
        LocalVariable variable = method.getLocalVariable(value.getName());
        if (variable == null) {
            LabelNode label = writeLabel(node, value.getMeta().getStartLine());
            variable = method.addLocalVariable(value.getName(), label, Memory.class);

            if (method.statement.isReference(value) || compiler.getScope().superGlobals.contains(value.getName())) {
                variable.setReference(true);
            } else {
                variable.setReference(false);
            }

            if (variable.name.equals("this") && method.getLocalVariable("~this") != null){ // $this
                writeDefineThis(variable);
            } else if (compiler.getScope().superGlobals.contains(value.getName())){ // super-globals
                writeDefineGlobalVar(value.getName());
            } else if (method.statement.isDynamicLocal()){ // ref-local variables
                writePushLocal();
                writePushConstString(value.getName());
                writeSysDynamicCall(ArrayMemory.class, "refOfIndex", Memory.class, String.class);
                makeVarStore(variable);
                stackPop();

                variable.pushLevel();
                variable.setValue(null);
                variable.setReference(true);
            } else { // simple local variables
                if (variable.isReference()){
                    writePushNewObject(ReferenceMemory.class);
                } else {
                    writePushNull();
                }

                makeVarStore(variable);

                stackPop();
                variable.pushLevel();
            }
        } else
            variable.pushLevel();
    }

    protected void writeUndefineVariable(VariableExprToken value, LabelNode end){
        LocalVariable variable = method.getLocalVariable(value.getName());
        variable.popLevel();
    }

    void writePushVariable(VariableExprToken value){
        LocalVariable variable = method.getLocalVariable(value.getName());
        if (variable == null || variable.getClazz() == null)
            writePushNull();
        else {
            writeVarLoad(variable);
        }
    }

    Memory tryWritePushVariable(VariableExprToken value, boolean heavyObjects){
        if (method.statement.isUnstableVariable(value))
            return null;

        if (compiler.getScope().superGlobals.contains(value.getName()))
            return null;

        LocalVariable variable = method.getLocalVariable(value.getName());
        if (variable == null || variable.getClazz() == null) {
            return Memory.NULL;
        } else {
            if (method.statement.getPassedLocal().contains(value))
                return null;

            Memory mem = variable.getValue();
            if (mem != null) {
                if (!heavyObjects){
                    if (mem.isArray() || mem.toString().length() > 100){
                        return null;
                    }
                }
                return mem;
            }
        }
        return null;
    }

    Memory writePushArray(ArrayExprToken array, boolean returnMemory, boolean writeOpcode){
        if (array.getParameters().isEmpty()){
            if (returnMemory)
                return new ArrayMemory();
            else if (writeOpcode)
                writeSysStaticCall(ArrayMemory.class, "valueOf", Memory.class);
        } else {
            ArrayMemory ret = returnMemory ? new ArrayMemory() : null;

            if (ret == null)
                writePushNewObject(ArrayMemory.class);
            for(ExprStmtToken param : array.getParameters()){
                if (ret == null)
                    writePushDup();

                Memory result = writeExpression(param, true, returnMemory, ret == null);
                if (result != null) {
                    if (ret != null)
                        ret.add(result);
                    continue;
                } else {
                    if (!writeOpcode)
                        return null;
                    ret = null;
                }

                if (result == null && returnMemory) {
                    return writePushArray(array, false, writeOpcode);
                }

                writePopBoxing();
                writeSysDynamicCall(ArrayMemory.class, "add", ReferenceMemory.class, Memory.class);
                writePopAll(1);
            }
            if (ret != null)
                return ret;
        }
        setStackPeekAsImmutable();
        return null;
    }

    void writePushTraceInfo(Token token){
        writePushTraceInfo(token.getMeta().getStartLine(), token.getMeta().getStartPosition());
    }

    void writePushGetFromArray(int index, Class clazz){
        writePushSmallInt(index);
        code.add(new InsnNode(AALOAD));
        stackPop();
        stackPop();
        stackPush(null, StackItem.Type.valueOf(clazz));
    }

    void writePushTraceInfo(int line, int position){
        int index = method.clazz.addTraceInfo(line, position);
        writeGetStatic("$TRC", TraceInfo[].class);
        writePushGetFromArray(index, TraceInfo.class);
    }

    void writePushCreateTraceInfo(int line, int position){
        writeGetStatic("$FN", String.class);
        writePushMemory(LongMemory.valueOf(line));
        writePushMemory(LongMemory.valueOf(position));
        writeSysStaticCall(TraceInfo.class, "valueOf", TraceInfo.class, String.class, Long.TYPE, Long.TYPE);
    }

    Memory tryWritePushMacro(MacroToken macro, boolean writeOpcode){
        if (macro instanceof LineMacroToken){
            return LongMemory.valueOf(macro.getMeta().getStartLine() + 1);
        } else if (macro instanceof FileMacroToken){
            return new StringMemory(compiler.getSourceFile());
        } else if (macro instanceof DirMacroToken){
            return new StringMemory(new File(compiler.getSourceFile()).getParent());
        } else if (macro instanceof FunctionMacroToken){
            return new StringMemory(
                    method.clazz.getFunctionName().isEmpty()
                            ? method.clazz.isSystem() ? "" : method.getRealName()
                            : method.clazz.getFunctionName()
            );
        } else if (macro instanceof MethodMacroToken){
            return new StringMemory(method.clazz.isSystem()
                    ? ""
                    : method.clazz.entity.getName() + "::" + method.getRealName()
            );
        } else if (macro instanceof ClassMacroToken){
            return new StringMemory(method.clazz.isSystem() ? "" : method.clazz.entity.getName());
        } else if (macro instanceof NamespaceMacroToken){
            return new StringMemory(
                    compiler.getNamespace() == null || compiler.getNamespace().getName() == null
                            ? ""
                            : compiler.getNamespace().getName().getName());
        } else
            throw new IllegalArgumentException("Unsupported macro value: " + macro.getWord());
    }

    Memory writePushName(NameToken token, boolean returnMemory, boolean writeOpcode){
        CompileConstant constant = compiler.getScope().findCompileConstant(token.getName());
        if (constant != null){
            if (returnMemory)
                return constant.value;
            else if (writeOpcode)
                writePushMemory(constant.value);
        } else {
            ConstantEntity constantEntity = compiler.findConstant(token.getName());
            /*if (constantEntity == null)   // TODO: maybe it's not needed! we should search a namespaced constant in local context
                constantEntity = compiler.getScope().findUserConstant(token.getName()); */

            if (constantEntity != null){
                if (returnMemory)
                    return constantEntity.getValue();
                else if (writeOpcode){
                    writePushMemory(constantEntity.getValue());
                    setStackPeekAsImmutable();
                }
            } else {
                writePushEnv();
                writePushString(token.getName());
                writePushString(token.getName().toLowerCase());

               // writePushDupLowerCase();
                writePushTraceInfo(token);
                writeSysDynamicCall(Environment.class, "__getConstant",
                        Memory.class, String.class, String.class, TraceInfo.class);
                setStackPeekAsImmutable();
            }
        }
        return null;
    }

    Memory tryWritePushReference(StackItem item, boolean writeOpcode, boolean toStack, boolean heavyObjects){
        if (item.getToken() instanceof VariableExprToken){
            writePushVariable((VariableExprToken)item.getToken());
            return null;
        }

        return tryWritePush(item, writeOpcode, toStack, heavyObjects);
    }

    Memory tryWritePush(StackItem item, boolean writeOpcode, boolean toStack, boolean heavyObjects){
        if (item.getToken() != null)
            return tryWritePush(item.getToken(), true, writeOpcode, heavyObjects);
        else if (item.getMemory() != null){
            return item.getMemory();
        } else if (toStack)
            stackPush(null, item.type);
        return null;
    }

    StackItem.Type tryGetType(StackItem item){
        if (item.getToken() != null){
            return tryGetType(item.getToken());
        } else if (item.getMemory() != null) {
            return StackItem.Type.valueOf(item.getMemory().type);
        } else
            return item.type;
    }

    Memory tryWritePush(StackItem item){
        return tryWritePush(item, true, true, true);
    }

    void writePush(StackItem item){
        writePush(item, true, false);
    }

    void writePush(StackItem item, boolean tryGetMemory, boolean heavyObjects){
        if (tryGetMemory){
            Memory memory = tryWritePushReference(item, true, true, heavyObjects);
            if (memory != null)
                writePushMemory(memory);
        } else {
            tryWritePushReference(item, true, true, heavyObjects);
        }
    }

    void writePush(StackItem item, StackItem.Type castType){
        Memory memory = tryWritePush(item);
        if (memory != null) {
            switch (castType){
                case DOUBLE: writePushConstDouble(memory.toDouble()); return;
                case FLOAT: writePushConstFloat((float)memory.toDouble()); return;
                case LONG: writePushConstLong(memory.toLong()); return;
                case INT: writePushConstInt((int)memory.toLong()); return;
                case SHORT: writePushConstInt((short)memory.toLong()); return;
                case BYTE: writePushConstByte((byte)memory.toLong()); return;
                case BOOL: writePushConstBoolean(memory.toBoolean()); return;
                case STRING: writePushConstString(memory.toString()); return;
            }
            writePushMemory(memory);
        }
        writePop(castType.toClass(), true, true);
    }

    StackItem.Type tryGetType(ValueExprToken value){
        if (value instanceof IntegerExprToken || value instanceof HexExprValue)
            return StackItem.Type.LONG;
        else if (value instanceof DoubleExprToken)
            return StackItem.Type.DOUBLE;
        else if (value instanceof StringExprToken)
            return StackItem.Type.STRING;
        else if (value instanceof LineMacroToken)
            return StackItem.Type.LONG;
        else if (value instanceof MacroToken)
            return StackItem.Type.STRING;
        else if (value instanceof ArrayExprToken)
            return StackItem.Type.ARRAY;
        else if (value instanceof StringBuilderExprToken)
            return StackItem.Type.STRING;
        else if (value instanceof CallExprToken) {
            PushCallStatistic statistic = new PushCallStatistic();
            writePushCall((CallExprToken)value, true, false, statistic);
            return statistic.returnType;
        } else
            return StackItem.Type.REFERENCE;
    }

    Memory tryWritePush(ValueExprToken value, boolean returnValue, boolean writeOpcode, boolean heavyObjects){
        if (writeOpcode)
            if (value instanceof IntegerExprToken){
                writePushInt((IntegerExprToken)value);
            } else if (value instanceof HexExprValue){
                writePushHex((HexExprValue)value);
            } else if (value instanceof BooleanExprToken){
                writePushBoolean((BooleanExprToken) value);
            } else if (value instanceof NullExprToken){
                writePushNull();
            } else if (value instanceof StringExprToken){
                writePushString((StringExprToken)value);
            } else if (value instanceof DoubleExprToken){
                writePushDouble((DoubleExprToken)value);
            } else if (value instanceof ImportExprToken){
                writePushImport((ImportExprToken) value, returnValue);
            } else if (value instanceof NewExprToken){
                writePushNew((NewExprToken)value, returnValue);
            } else if (value instanceof StringBuilderExprToken){
                writePushStringBuilder((StringBuilderExprToken)value);
            } else if (value instanceof UnsetExprToken){
                writePushUnset((UnsetExprToken)value, returnValue);
                return null;
            } else if (value instanceof IssetExprToken){
                writePushIsset((IssetExprToken)value, returnValue);
                return null;
            } else if (value instanceof EmptyExprToken){
                writePushEmpty((EmptyExprToken)value, returnValue);
                return null;
            } else if (value instanceof DieExprToken){
                writePushDie((DieExprToken)value, returnValue);
                return null;
            } else if (value instanceof StaticExprToken){
                writePushStatic();
                return null;
            } else if (value instanceof ClosureStmtToken){
                writePushClosure((ClosureStmtToken)value, returnValue);
                return null;
            } else if (value instanceof GetVarExprToken){
                writePushGetVar((GetVarExprToken)value, returnValue);
                return null;
            } else if (value instanceof StaticAccessExprToken){
                writePushStaticAccess((StaticAccessExprToken)value, returnValue);
            }

        if (value instanceof NameToken){
            return writePushName((NameToken)value, returnValue, writeOpcode);
        } if (value instanceof ArrayExprToken){
            return writePushArray((ArrayExprToken)value, returnValue, writeOpcode);
        } else if (value instanceof CallExprToken) {
            return writePushCall((CallExprToken)value, returnValue, writeOpcode, null);
        } else if (value instanceof MacroToken){
            return tryWritePushMacro((MacroToken) value, writeOpcode);
        } else if (value instanceof VariableExprToken){
            if (writeOpcode)
                writePushVariable((VariableExprToken)value);

            if (returnValue)
                return tryWritePushVariable((VariableExprToken) value, heavyObjects);
        }

        return null;
    }

    void writePush(ValueExprToken value, boolean returnValue, boolean heavyObjects){
        if (value instanceof VariableExprToken){
            writePushVariable((VariableExprToken)value);
            return;
        }

        Memory memory = tryWritePush(value, returnValue, true, heavyObjects);
        if (memory != null)
            writePushMemory(memory);
    }

    @SuppressWarnings("unchecked")
    boolean methodExists(Class clazz, String method, Class... paramClasses){
        try {
            clazz.getDeclaredMethod(method, paramClasses);
            return true;
        } catch (java.lang.NoSuchMethodException e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    void writeSysCall(Class clazz, int INVOKE_TYPE, String method, Class returnClazz, Class... paramClasses) {
        if (INVOKE_TYPE != INVOKESPECIAL && clazz != null){
            if (compiler.getScope().isDebugMode())
            if (!methodExists(clazz, method, paramClasses))
                throw new NoSuchMethodException(clazz, method, paramClasses);
        }

        Type[] args = new Type[paramClasses.length];
        if (INVOKE_TYPE == INVOKEVIRTUAL || INVOKE_TYPE == INVOKEINTERFACE)
            stackPop(); // this

        for(int i = 0; i < args.length; i++){
            args[i] = Type.getType(paramClasses[i]);
            stackPop();
        }

        String owner = clazz == null ? this.method.clazz.node.name : Type.getInternalName(clazz);
        code.add(new MethodInsnNode(
                INVOKE_TYPE, owner, method, Type.getMethodDescriptor(Type.getType(returnClazz), args)
        ));

        if (returnClazz != void.class){
            stackPush(null, StackItem.Type.valueOf(returnClazz));
        }
    }

    void writeSysDynamicCall(Class clazz, String method, Class returnClazz, Class... paramClasses)
            throws NoSuchMethodException {
        writeSysCall(
                clazz, clazz != null && clazz.isInterface() ? INVOKEINTERFACE : INVOKEVIRTUAL,
                method, returnClazz, paramClasses
        );
    }

    void writeSysStaticCall(Class clazz, String method, Class returnClazz, Class... paramClasses)
            throws NoSuchMethodException {
        writeSysCall(clazz, INVOKESTATIC, method, returnClazz, paramClasses);
    }

    void writePopImmutable(){
        if (!stackPeek().immutable){
            writeSysDynamicCall(Memory.class, "toImmutable", Memory.class);
            setStackPeekAsImmutable();
        }
    }

    void writeVarStore(LocalVariable variable, boolean returned, boolean asImmutable){
        writePopBoxing();

        if (asImmutable)
            writePopImmutable();

        if (returned){
            writePushDup();
        }

        makeVarStore(variable);
        stackPop();
    }

    void writeVarAssign(LocalVariable variable, boolean returned, boolean asImmutable){
        writePopBoxing(asImmutable);

        if (variable.isReference()){
            writeVarLoad(variable);
            writeSysStaticCall(Memory.class, "assignRight", Memory.class, Memory.class, Memory.class);
            if (!returned)
                writePopAll(1);
        } else {
            if (returned){
                writePushDup();
            }

            makeVarStore(variable);
            stackPop();
        }
    }

    void writeVarStore(LocalVariable variable, boolean returned){
        writeVarStore(variable, returned, false);
    }

    void writeVarLoad(LocalVariable variable){
        stackPush(Memory.Type.REFERENCE);
        makeVarLoad(variable);
    }

    void writeVarLoad(String name){
        LocalVariable local = method.getLocalVariable(name);
        if (local == null)
            throw new IllegalArgumentException("Variable '" + name + "' is not registered");

        writeVarLoad(local);
    }

    void writePutStatic(Class clazz, String name, Class fieldClass){
        code.add(new FieldInsnNode(PUTSTATIC, Type.getInternalName(clazz), name, Type.getDescriptor(fieldClass)));
        stackPop();
    }

    void writePutStatic(String name, Class fieldClass){
        code.add(new FieldInsnNode(
                PUTSTATIC,
                method.clazz.node.name,
                name,
                Type.getDescriptor(fieldClass)
        ));
        stackPop();
    }

    void writePutDynamic(String name, Class fieldClass){
        code.add(new FieldInsnNode(
                PUTFIELD,
                method.clazz.node.name,
                name,
                Type.getDescriptor(fieldClass)
        ));
        stackPop();
    }

    void writeGetStatic(Class clazz, String name, Class fieldClass){
        code.add(new FieldInsnNode(GETSTATIC, Type.getInternalName(clazz), name, Type.getDescriptor(fieldClass)));
        stackPush(null, StackItem.Type.valueOf(fieldClass));
        setStackPeekAsImmutable();
    }

    void writeGetStatic(String name, Class fieldClass){
        code.add(new FieldInsnNode(
                GETSTATIC,
                method.clazz.node.name,
                name,
                Type.getDescriptor(fieldClass)
        ));
        stackPush(null, StackItem.Type.valueOf(fieldClass));
        setStackPeekAsImmutable();
    }

    void writeGetDynamic(String name, Class fieldClass){
        stackPop();
        code.add(new FieldInsnNode(
                GETFIELD,
                method.clazz.node.name,
                name,
                Type.getDescriptor(fieldClass)
        ));
        stackPush(null, StackItem.Type.valueOf(fieldClass));
        setStackPeekAsImmutable();
    }

    void writeGetEnum(Enum enumInstance){
        writeGetStatic(enumInstance.getDeclaringClass(), enumInstance.name(), enumInstance.getDeclaringClass());
    }

    void writeVariableAssign(VariableExprToken variable, StackItem R, AssignExprToken operator, boolean returnValue){
        LocalVariable local = method.getLocalVariable(variable.getName());

        Memory value = R.getMemory();

        if (local.isReference()){
            String name = "assign";
            if (operator.isAsReference())
                name = "assignRef";
            if (!R.isKnown()){
                stackPush(R);
                writePopBoxing();
                writePopImmutable();
                writePushVariable(variable);
                writeSysStaticCall(Memory.class, name + "Right", Memory.class, stackPeek().type.toClass(), Memory.class);
            } else {
                writePushVariable(variable);
                Memory tmp = tryWritePush(R);
                if (tmp != null){
                    value = tmp;
                    writePushMemory(value);
                }
                writePopBoxing();
                writePopImmutable();
                writeSysDynamicCall(Memory.class, name, Memory.class, stackPeek().type.toClass());
            }
            if (!returnValue)
                writePopAll(1);
        } else {
            Memory result = tryWritePush(R);
            if (result != null){
                writePushMemory(result);
                value = result;
            }
            writePopBoxing();
            writeVarStore(local, returnValue, true);
        }

        if (!method.statement.getPassedLocal().contains(variable))
            local.setValue(value);

        if (method.statement.isDynamicLocal())
            local.setValue(null);
    }

    void writeScalarOperator(StackItem L, StackItem.Type Lt,
                             StackItem R, StackItem.Type Rt,
                             OperatorExprToken operator,
                             Class operatorResult, String operatorName){
        boolean isInvert = !R.isKnown();

        if (operator instanceof ConcatExprToken){
            if (isInvert){
                writePopString();
                writePush(L, StackItem.Type.STRING);
                writeSysStaticCall(OperatorUtils.class, "concatRight", String.class, String.class, String.class);
            } else {
                writePush(L, StackItem.Type.STRING);
                writePush(R, StackItem.Type.STRING);
                writeSysDynamicCall(String.class, "concat", String.class, String.class);
            }
            return;
        } else if (operator instanceof PlusExprToken || operator instanceof MinusExprToken
            || operator instanceof MulExprToken){
            if (operator instanceof MinusExprToken && isInvert){
                // nothing
            } else if (Lt.isLikeNumber() && Rt.isLikeNumber()){
                StackItem.Type cast = StackItem.Type.LONG;
                if (Lt.isLikeDouble() || Rt.isLikeDouble())
                    cast = StackItem.Type.DOUBLE;

                writePush(L, cast);
                writePush(R, cast);

                code.add(new InsnNode(CompilerUtils.getOperatorOpcode(operator, cast)));

                stackPop();
                stackPop();
                stackPush(null, cast);
                return;
            }
        }

        if (isInvert){
            writePopBoxing();
            writePush(L);
            if (operator.isSide())
                operatorName += "Right";

            writeSysDynamicCall(Memory.class, operatorName, operatorResult, stackPeek().type.toClass());
        } else {
            writePush(L);
            writePopBoxing();
            writePush(R);
            writeSysDynamicCall(Memory.class, operatorName, operatorResult, stackPeek().type.toClass());
        }
    }

    void writePop(Class clazz, boolean boxing, boolean asImmutable){
        if (clazz == String.class)
            writePopString();
        else if (clazz == Character.TYPE)
            writePopChar();
        else if (clazz == Boolean.TYPE)
            writePopBoolean();
        else if (clazz == Memory.class){
            if (boxing)
                writePopBoxing(asImmutable);
        } else if (clazz == Double.TYPE)
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

    void writePopInteger(){
        writePopLong();
        code.add(new InsnNode(L2I));
        stackPop();
        stackPush(null, StackItem.Type.INT);
    }

    void writePopFloat(){
        writePopDouble();
        code.add(new InsnNode(D2F));
        stackPop();
        stackPush(null, StackItem.Type.FLOAT);
    }

    void writePopLong(){
        switch (stackPeek().type){
            case LONG: break;
            case FLOAT: {
                code.add(new InsnNode(L2F));
                stackPop();
                stackPush(Memory.Type.INT);
            } break;
            case BYTE:
            case SHORT:
            case BOOL:
            case CHAR:
            case INT: {
                code.add(new InsnNode(L2I));
                stackPop();
                stackPush(Memory.Type.INT);
            } break;
            case DOUBLE: {
                code.add(new InsnNode(D2L));
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

    void writePopDouble(){
        switch (stackPeek().type){
            case DOUBLE: break;
            case BYTE:
            case SHORT:
            case BOOL:
            case CHAR:
            case INT: {
                code.add(new InsnNode(I2D));
                stackPop();
                stackPush(Memory.Type.DOUBLE);
            } break;
            case LONG: {
                code.add(new InsnNode(L2D));
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

    void writePopString(){
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

    void writePopChar(){
        StackItem.Type peek = stackPeek().type;
        if (peek == StackItem.Type.CHAR)
            return;

        if (peek.isConstant()){
            writeSysStaticCall(OperatorUtils.class, "toChar", Character.TYPE, peek.toClass());
        } else {
            writePopBoxing();
            writeSysDynamicCall(Memory.class, "toChar", Character.TYPE);
        }
    }

    void writePopBoolean(){
        StackItem.Type peek = stackPeek().type;
        switch (peek){
            case BOOL: break;
            case BYTE:
            case INT:
            case SHORT:
            case CHAR:
            case LONG: {
                writeSysStaticCall(OperatorUtils.class, "toBoolean", Boolean.TYPE, peek.toClass());
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

    void writeStaticAccess(StaticAccessExprToken token){
        ValueExprToken value = token.getClazz();
        writePushEnv();
        writePush(value, true, false);
        writePushTraceInfo(token);
        writeSysDynamicCall(
                Environment.class, "getClass", Class.class, Environment.class, String.class, TraceInfo.class
        );
    }

    void writeDynamicAccessInfo(DynamicAccessExprToken dynamic, boolean addLowerName){
        if (dynamic.getField() != null){
            if (dynamic.getField() instanceof NameToken){
                String name = ((NameToken) dynamic.getField()).getName();
                writePushString(name);
                if (addLowerName)
                    writePushString(name.toLowerCase());
            } else {
                writePush(dynamic.getField(), true, false);
                writePopString();
                if (addLowerName){
                    writePushDupLowerCase();
                }
            }
        } else {
            writeExpression(dynamic.getFieldExpr(), true, false);
            writePopString();
            if (addLowerName){
                writePushDupLowerCase();
            }
        }
        writePushEnv();
        writePushTraceInfo(dynamic);
    }

    void writeDynamicAccessPrepare(DynamicAccessExprToken dynamic, boolean addLowerName){
        if (stackEmpty(true))
            unexpectedToken(dynamic);

        StackItem o = stackPop();
        writePush(o);

        if (stackPeek().isConstant())
            unexpectedToken(dynamic);
        writePopBoxing();

        if (dynamic instanceof DynamicAccessAssignExprToken){
            writeExpression(((DynamicAccessAssignExprToken) dynamic).getValue(), true, false);
            writePopBoxing(true);
        }

        writeDynamicAccessInfo(dynamic, addLowerName);
    }

    void writeInstanceOf(InstanceofExprToken instanceOf, boolean returnValue){
        if (stackEmpty(true))
            unexpectedToken(instanceOf);

        StackItem o = stackPop();
        writePush(o);

        if (stackPeek().isConstant())
            unexpectedToken(instanceOf);

        writePopBoxing();
        writePushEnv();
        writePushConstString(instanceOf.getWhat().getName());
        writePushConstString(instanceOf.getWhat().getName().toLowerCase());

        writeSysDynamicCall(Memory.class, "instanceOf",
                Boolean.TYPE, Environment.class, String.class, String.class);

        if (!returnValue)
            writePopAll(1);
    }

    void writePushStaticAccess(StaticAccessExprToken token, boolean returnValue) {
        boolean isConstant = token.getField() instanceof NameToken;
        if (token.getField() == null)
            unexpectedToken(token.getFieldExpr().getSingle());

        ValueExprToken clazz = token.getClazz();
        if (clazz instanceof NameToken){
            writePushConstString(((NameToken)clazz).getName());
            writePushConstString(((NameToken)clazz).getName().toLowerCase());
        } else {
            writePush(clazz, true, false);
            writePopString();
            writePushDupLowerCase();
        }

        if (isConstant){
            writePushConstString(((NameToken)token.getField()).getName());
            writePushEnv();
            writePushTraceInfo(token);

            writeSysStaticCall(ObjectInvokeHelper.class, "getConstant",
                    Memory.class, String.class, String.class, String.class, Environment.class, TraceInfo.class
            );
        } else {
            if (!(token.getField() instanceof VariableExprToken))
                unexpectedToken(token.getField());

            writePushConstString(((VariableExprToken)token.getField()).getName());
            writePushEnv();
            writePushTraceInfo(token);

            writeSysStaticCall(ObjectInvokeHelper.class, "getStaticProperty",
                    Memory.class, String.class, String.class, String.class, Environment.class, TraceInfo.class
            );
        }

        if (!returnValue)
            writePopAll(1);
    }

    void writePushDynamicAccess(DynamicAccessExprToken dynamic, boolean returnValue) {
        writeDynamicAccessPrepare(dynamic, false);

        if (dynamic instanceof DynamicAccessAssignExprToken){
            OperatorExprToken operator = (OperatorExprToken) ((DynamicAccessAssignExprToken) dynamic).getAssignOperator();
            writeSysStaticCall(ObjectInvokeHelper.class,
                    operator.getCode() + "Property", Memory.class,
                    Memory.class, Memory.class, String.class, Environment.class, TraceInfo.class
            );
        } else if (dynamic instanceof DynamicAccessUnsetExprToken){
            writeSysStaticCall(ObjectInvokeHelper.class,
                    "unsetProperty", void.class,
                    Memory.class, String.class, Environment.class, TraceInfo.class
            );
            if (returnValue)
                writePushNull();
        } else if (dynamic instanceof DynamicAccessEmptyExprToken){
            writeSysStaticCall(ObjectInvokeHelper.class,
                    "emptyProperty", Memory.class,
                    Memory.class, String.class, Environment.class, TraceInfo.class
            );
            if (!returnValue)
                writePopAll(1);
        } else if (dynamic instanceof DynamicAccessIssetExprToken){
            writeSysStaticCall(ObjectInvokeHelper.class,
                    "issetProperty", Memory.class,
                    Memory.class, String.class, Environment.class, TraceInfo.class
            );
            if (!returnValue)
                writePopAll(1);
        } else {
            writeSysStaticCall(ObjectInvokeHelper.class,
                    "getProperty", Memory.class,
                    Memory.class, String.class, Environment.class, TraceInfo.class
            );
        }

        if (!returnValue)
            writePopAll(1);
    }

    void writeArrayGet(ArrayGetExprToken operator, boolean returnValue){
        StackItem o = stackPeek();
        ValueExprToken L = null;
        if (o.getToken() != null){
            stackPop();
            writePush(L = o.getToken(), true, false);
            writePopBoxing();
        }

        String methodName = operator instanceof ArrayGetRefExprToken ? "refOfIndex" : "valueOfIndex";
        int i = 0;
        int size = operator.getParameters().size();

        for(ExprStmtToken param : operator.getParameters()){
            writeExpression(param, true, false);
            if (operator instanceof ArrayGetUnsetExprToken && i == size - 1){
                writePopBoxing();
                methodName = "unsetOfIndex";
                writeSysDynamicCall(Memory.class, methodName, void.class, stackPeek().type.toClass());
                if (returnValue)
                    writePushNull();
            } else {
                writeSysDynamicCall(Memory.class, methodName, Memory.class, stackPeek().type.toClass());
                i++;
            }
        }
    }

    Memory writeUnaryOperator(OperatorExprToken operator, boolean returnValue, boolean writeOpcode){
        if (stackEmpty(true))
            unexpectedToken(operator);

        StackItem o = stackPop();
        ValueExprToken L = o.getToken();

        Memory mem = tryWritePush(o, false, false, true);
        StackItem.Type type = tryGetType(o);

        if (mem != null){
            Memory result = CompilerUtils.calcUnary(mem, operator);

            if (operator instanceof ValueIfElseToken){
                ValueIfElseToken valueIfElseToken = (ValueIfElseToken)operator;
                ExprStmtToken ret = valueIfElseToken.getValue();
                if (mem.toBoolean()){
                    if (ret == null)
                        result = mem;
                    else
                        result = writeExpression(ret, true, true, false);
                } else {
                    result = writeExpression(valueIfElseToken.getAlternative(), true, true, false);
                }
            } else if (operator instanceof ArrayGetExprToken && !(operator instanceof ArrayGetRefExprToken)){
                // TODO: check!!!
                /*Memory array = mem;
                ArrayGetExprToken arrayGet = (ArrayGetExprToken)operator;
                for(ExprStmtToken expr : arrayGet.getParameters()){
                    Memory key = writeExpression(expr, true, true, false);
                    if (key == null)
                        break;
                    result = array = array.valueOfIndex(key).toImmutable();
                }*/
            }

            if (result != null){
                stackPush(result);
                setStackPeekAsImmutable();
                return result;
            }
        }

        if (!writeOpcode)
            return null;

        String name = operator.getCode();
        Class operatorResult = operator.getResultClass();

        LocalVariable variable = null;
        if (L instanceof VariableExprToken){
            variable = method.getLocalVariable(((VariableExprToken) L).getName());
            if (operator instanceof ArrayPushExprToken || operator instanceof ArrayGetRefExprToken)
                variable.setValue(null);
        }

        if (operator instanceof IncExprToken || operator instanceof DecExprToken){
            if (variable == null || variable.isReference()){
                if (operator.getAssociation() == Association.LEFT && returnValue) {
                    writePush(o);
                    if (stackPeek().type.isConstant())
                        unexpectedToken(operator);

                    writePushDup();
                    writePopImmutable();
                    code.add(new InsnNode(SWAP));
                    writePushDup();
                } else {
                    writePush(o);
                    if (stackPeek().type.isConstant())
                        unexpectedToken(operator);

                    writePushDup();
                }
                writeSysDynamicCall(Memory.class, name, operatorResult);
                writeSysDynamicCall(Memory.class, "assign", Memory.class, operatorResult);
                if (!returnValue || operator.getAssociation() == Association.LEFT) {
                    writePopAll(1);
                }
            } else {
                writePush(o);
                if (stackPeek().type.isConstant())
                    unexpectedToken(operator);

                if (operator.getAssociation() == Association.LEFT && returnValue){
                    writeVarLoad(variable);
                }

                writeSysDynamicCall(Memory.class, name, operatorResult);
                variable.setValue(null); // TODO for constant values
                if (operator.getAssociation() == Association.RIGHT)
                    writeVarStore(variable, returnValue);
                else {
                    writeVarStore(variable, false);
                }
            }
        } else if (operator instanceof AmpersandRefToken){
            writePush(o, false, false);
            setStackPeekAsImmutable();
            Token token = o.getToken();
            if (token instanceof VariableExprToken){
                LocalVariable local = method.getLocalVariable(((VariableExprToken) token).getName());
                local.setValue(null);
            }

        } else if (operator instanceof SilentToken) {
            if (!o.isKnown())
                unexpectedToken(operator);

            writePushEnv();
            writeSysDynamicCall(Environment.class, "__pushSilent", void.class);

            writePush(o);

            writePushEnv();
            writeSysDynamicCall(Environment.class, "__popSilent", void.class);

        } else if (operator instanceof ValueIfElseToken){
            writePush(o);
            ValueIfElseToken valueIfElseToken = (ValueIfElseToken)operator;

            LabelNode end = new LabelNode();
            LabelNode elseL = new LabelNode();

            if (valueIfElseToken.getValue() == null){
                StackItem.Type dup = stackPeek().type;

                writePushDup();
                writePopBoolean();
                code.add(new JumpInsnNode(Opcodes.IFEQ, elseL));
                stackPop();

                writePopBoxing();
                stackPop();

                code.add(new JumpInsnNode(Opcodes.GOTO, end));
                code.add(elseL);
                makePop(dup); // remove duplicate of condition value , IMPORTANT!!!

                writeExpression(valueIfElseToken.getAlternative(), true, false);
                writePopBoxing();

                code.add(end);
            } else {
                writePopBoolean();

                code.add(new JumpInsnNode(Opcodes.IFEQ, elseL));
                stackPop();
                writeExpression(valueIfElseToken.getValue(), true, false);

                writePopBoxing();
                stackPop();

                code.add(new JumpInsnNode(Opcodes.GOTO, end)); // goto end

                // else
                code.add(elseL);
                writeExpression(valueIfElseToken.getAlternative(), true, false);
                writePopBoxing();
                code.add(end);
            }

            setStackPeekAsImmutable(false);
        } else if (operator instanceof ArrayGetExprToken) {
            stackPush(o);
            writeArrayGet((ArrayGetExprToken)operator, returnValue);
        } else if (operator instanceof CallOperatorToken) {
            stackPush(o);

            CallOperatorToken call = (CallOperatorToken)operator;

            writePushParameters(call.getParameters());
            writePushEnv();
            writePushTraceInfo(operator);

            writeSysStaticCall(
                    InvokeHelper.class, "callAny", Memory.class,
                    Memory.class, Memory[].class, Environment.class, TraceInfo.class
            );
            if (!returnValue)
                writePopAll(1);
        } else {
                writePush(o);
                writePopBoxing();

                if (operator.isEnvironmentNeeded()) {
                    writePushEnv();
                    writeSysDynamicCall(Memory.class, name, operatorResult, Environment.class);
                } else
                    writeSysDynamicCall(Memory.class, name, operatorResult);

                if (!returnValue){
                    writePopAll(1);
                }
        }

        return null;
    }

    Memory writeLogicOperator(LogicOperatorExprToken operator, boolean returnValue, boolean writeOpcode){
        if (stackEmpty(true))
            unexpectedToken(operator);

        if (!writeOpcode)
            return null;

        StackItem o = stackPop();

        writePush(o);
        writePopBoolean();

        LabelNode end = new LabelNode();
        LabelNode next = new LabelNode();

        if (operator instanceof BooleanOrExprToken || operator instanceof BooleanOr2ExprToken){
            code.add(new JumpInsnNode(IFEQ, next));
            stackPop();
            if (returnValue){
                writePushBooleanAsMemory(true);
                stackPop();
            }
        } else if (operator instanceof BooleanAndExprToken || operator instanceof BooleanAnd2ExprToken){
            code.add(new JumpInsnNode(IFNE, next));
            stackPop();
            if (returnValue){
                writePushBooleanAsMemory(false);
                stackPop();
            }
        }

        code.add(new JumpInsnNode(GOTO, end));

        code.add(next);
        writeExpression(operator.getRightValue(), returnValue, false);
        if (returnValue)
            writePopBoxing();

        code.add(end);
        return null;
    }

    Memory writeOperator(OperatorExprToken operator, boolean returnValue, boolean writeOpcode){
        if (operator instanceof InstanceofExprToken) {
            if (writeOpcode)
                writeInstanceOf((InstanceofExprToken)operator, returnValue);

            return null;
        }

        if (operator instanceof DynamicAccessExprToken){
            if (writeOpcode)
                writePushDynamicAccess((DynamicAccessExprToken) operator, returnValue);
            return null;
        }

        if (operator instanceof LogicOperatorExprToken){
            return writeLogicOperator((LogicOperatorExprToken)operator, returnValue, writeOpcode);
        }

        if (!operator.isBinary()){
            return writeUnaryOperator(operator, returnValue, writeOpcode);
        }

        if (stackEmpty(true))
            unexpectedToken(operator);

        StackItem o1 = stackPop();
        if (o1.isInvalidForOperations())
            unexpectedToken(operator);

        if (stackEmpty(true))
            unexpectedToken(operator);

        StackItem o2 = stackPeek();
        ValueExprToken L = stackPopToken();

        if (o2.isInvalidForOperations())
            unexpectedToken(operator);

        if (!(operator instanceof AssignExprToken || operator instanceof AssignOperatorExprToken))
        if (o1.getMemory() != null && o2.getMemory() != null){
            Memory result;
            stackPush(result = CompilerUtils.calcBinary(o2.getMemory(), o1.getMemory(), operator, false));
            return result;
        }

        LocalVariable variable = null;
        if (L instanceof VariableExprToken){
            variable = method.getLocalVariable(((VariableExprToken) L).getName());
        }

        if (operator instanceof AssignExprToken){
            if (L instanceof VariableExprToken){
                if (!writeOpcode)
                    return null;
                writeVariableAssign((VariableExprToken)L, o1, (AssignExprToken)operator, returnValue);
                return null;
            }
        }

        Memory value1 = operator instanceof AssignableOperatorToken
                            ? null
                            : tryWritePush(o2, false, false, true); // LEFT

        Memory value2 = tryWritePush(o1, false, false, true); // RIGHT
        if (value1 != null && value2 != null){
            stackPush(value1);
            stackPush(value2);
            return writeOperator(operator, returnValue, writeOpcode);
        }
        if (!returnValue && CompilerUtils.isOperatorAlwaysReturn(operator)){
            unexpectedToken(operator);
        }

        if (!writeOpcode){
            stackPush(o2);
            stackPush(o1);
            return null;
        }

        StackItem.Type Lt = tryGetType(o2);
        StackItem.Type Rt = tryGetType(o1);

        String name = operator.getCode();
        Class operatorResult = operator.getResultClass();

        boolean isInvert = false;
        boolean sideOperator = operator.isSide();

        if (variable != null && !variable.isReference()){
            if (operator instanceof AssignOperatorExprToken){
                name = ((AssignOperatorExprToken)operator).getOperatorCode();
                if (operator instanceof AssignConcatExprToken)
                    operatorResult = String.class;
                if (operator instanceof AssignPlusExprToken || operator instanceof AssignMulExprToken)
                    sideOperator = false;
            }
        }

        if (Lt.isConstant() && Rt.isConstant()){
            if (operator instanceof AssignOperatorExprToken)
                unexpectedToken(operator);

            writeScalarOperator(o2, Lt, o1, Rt, operator, operatorResult, name);
        } else {
            isInvert = !o1.isKnown();
            if (!o1.isKnown() && !o2.isKnown() && o1.getLevel() > o2.getLevel())
                isInvert = false;

            if (Lt.isConstant() && !isInvert){
                writePush(o2);
                if (methodExists(OperatorUtils.class, name, Lt.toClass(), Rt.toClass())){
                    writePush(o1);
                    writeSysStaticCall(OperatorUtils.class, name, operatorResult, Lt.toClass(), Rt.toClass());
                } else {
                    writePopBoxing();
                    writePush(o1);
                    writeSysDynamicCall(Memory.class, name, operatorResult, Rt.toClass());
                }
            } else {
                if (isInvert) {
                    stackPush(o1);
                    if (o2.isKnown())
                        writePopBoxing(false);

                    writePush(o2);

                    if (!o2.isKnown() && !o2.type.isReference()) {
                        writeSysStaticCall(OperatorUtils.class, name, operatorResult, Lt.toClass(), Rt.toClass());
                        name = null;
                    } else if (sideOperator) {
                        name += "Right";
                    }

                    /*if (cloneValue){
                        writeSysDynamicCall(Memory.class, name, operatorResult, isInvert ? Lt.toClass() : Rt.toClass());
                        writePush(o2);
                        name = null;
                    }*/
                } else {
                    writePush(o2);
                    writePopBoxing(false);

                    /*if (cloneValue)
                        writePushDup();*/

                    writePush(o1);
                    writePopImmutable();
                }
                if (name != null)
                    writeSysDynamicCall(Memory.class, name, operatorResult, isInvert ? Lt.toClass() : Rt.toClass());
            }
            setStackPeekAsImmutable();

            if (operator instanceof AssignOperatorExprToken){
                if (variable == null || variable.isReference()){
                    /*if (isInvert)
                        writeSysStaticCall(Memory.class, "assignRight", Memory.class, Rt.toClass(), Memory.class);
                    else
                        writeSysDynamicCall(Memory.class, "assign", Memory.class, stackPeek().type.toClass());
                    */
                    if (!returnValue)
                        writePopAll(1);
                } else {
                    if (returnValue)
                        writePushDup(StackItem.Type.valueOf(operatorResult));

                    writePopBoxing(operatorResult);
                    makeVarStore(variable);
                    variable.setValue(!stackPeek().isConstant() ? null : stackPeek().getMemory());
                    stackPop();
                }
            }
        }
        return null;
    }

    void writeEchoRaw(EchoRawToken token){
        if (!token.getMeta().getWord().isEmpty()){
            writePushEnv();
            writePushString(token.getMeta().getWord());
            writeSysDynamicCall(Environment.class, "echo", void.class, String.class);
        }
    }

    void writeEcho(EchoStmtToken token){
        for(ExprStmtToken argument : token.getArguments()){
            writePushEnv();
            writeExpression(argument, true, false);
            writePopString();
            writeSysDynamicCall(Environment.class, "echo", void.class, String.class);
        }
    }

    void writeOpenEchoTag(OpenEchoTagToken token){
        writePushEnv();
        writeExpression(token.getValue(), true, false);
        writePopString();
        writeSysDynamicCall(Environment.class, "echo", void.class, String.class);
    }

    void writeBody(BodyStmtToken body){
        if (body!= null){
            for(ExprStmtToken line : body.getInstructions()){
                writeExpression(line, false, false);
            }
        }
    }

    void writeJump(JumpStmtToken token){
        int level = token.getLevel();
        JumpItem jump = method.getJump(level);

        if (jump == null){
            throw new CompileException(
                    level == 1
                        ? Messages.ERR_COMPILE_CANNOT_JUMP.fetch()
                        : Messages.ERR_COMPILE_CANNOT_JUMP_TO_LEVEL.fetch(level),
                    token.toTraceInfo(getCompiler().getContext())
            );
        }

        if (token instanceof ContinueStmtToken){
            code.add(new JumpInsnNode(GOTO, jump.continueLabel));
        } else if (token instanceof BreakStmtToken){
            code.add(new JumpInsnNode(GOTO, jump.breakLabel));
        }
    }

    void writeIf(IfStmtToken token){
        writeDefineVariables(token.getLocal());

        LabelNode end = new LabelNode();
        LabelNode elseL = new LabelNode();
        Memory memory = writeExpression(token.getCondition(), true, true);

        if (memory != null){
            if (memory.toBoolean()){
                writeBody(token.getBody());
            } else {
                writeBody(token.getElseBody());
            }
        } else {
            writePopBoolean();
            code.add(new JumpInsnNode(IFEQ, token.getElseBody() != null ? elseL : end));
            stackPop();

            writeBody(token.getBody());
            if (token.getElseBody() != null){
                code.add(new JumpInsnNode(GOTO, end));
                code.add(elseL);
                writeBody(token.getElseBody());
            }

            code.add(end);
            code.add(new LineNumberNode(token.getMeta().getEndLine(), end));
        }
        writeUndefineVariables(token.getLocal());
    }

    void writeSwitch(SwitchStmtToken token){
        writeDefineVariables(token.getLocal());

        LabelNode l = new LabelNode();
        LabelNode end = new LabelNode();

        code.add(l);
        LocalVariable switchValue = method.addLocalVariable(
                "~switch~" + method.nextStatementIndex(Memory.class), l, Memory.class
        );
        switchValue.setEndLabel(end);


        LabelNode[][] jumps = new LabelNode[token.getCases().size() + 1][2];
        int i = 0;
        for(CaseStmtToken one : token.getCases()){
            jumps[i] = new LabelNode[]{ new LabelNode(), new LabelNode() }; // checkLabel, bodyLabel
            if (i == jumps.length - 1)
                jumps[i] = new LabelNode[]{ end, end };

            i++;
        }
        jumps[jumps.length - 1] = new LabelNode[]{end, end};


        method.pushJump(end, end);
        writeExpression(token.getValue(), true, false);
        writePopBoxing();

        writeVarStore(switchValue, false, false);

        i = 0;
        for(CaseStmtToken one : token.getCases()){
            code.add(jumps[i][0]); // conditional

            if (one.getConditional() != null){
                writeVarLoad(switchValue);
                writeExpression(one.getConditional(), true, false);
                writeSysDynamicCall(Memory.class, "equal", Boolean.TYPE, stackPeek().type.toClass());
                code.add(new JumpInsnNode(IFEQ, jumps[i + 1][0]));
                stackPop();
            }

            code.add(new JumpInsnNode(GOTO, jumps[i][1])); // if is done...
            i++;
        }

        i = 0;
        for(CaseStmtToken one : token.getCases()){
            code.add(jumps[i][1]);
            writeBody(one.getBody());
            i++;
        }

        method.popJump();
        code.add(end);

        code.add(new LineNumberNode(token.getMeta().getEndLine(), end));
        method.prevStatementIndex(Memory.class);
        writeUndefineVariables(token.getLocal());
    }

    void writeForeach(ForeachStmtToken token){
        writeDefineVariables(token.getLocal());

        LabelNode start = new LabelNode();
        LabelNode end = new LabelNode();

        LabelNode l = new LabelNode();
        code.add(l);

        writePushEnv();
        writePushTraceInfo(token);
        writeExpression(token.getIterator(), true, false, true);
        writePopBoxing();
        writePushConstBoolean(token.isValueReference());
        writePushConstBoolean(token.isKeyReference());
        writeSysDynamicCall(Environment.class, "getIterator", ForeachIterator.class, TraceInfo.class, Memory.class, Boolean.TYPE, Boolean.TYPE);

        LocalVariable foreachVariable = method.addLocalVariable(
                "~foreach~" + method.nextStatementIndex(ForeachIterator.class), l, ForeachIterator.class
        );
        foreachVariable.setEndLabel(end);

        writeVarStore(foreachVariable, false, false);

        method.pushJump(end, start);

        code.add(start);
        writeVarLoad(foreachVariable);

        writeSysDynamicCall(ForeachIterator.class, "next", Boolean.TYPE);
        code.add(new JumpInsnNode(IFEQ, end));
        stackPop();

        // $key
        if (token.getKey() != null) {
            LocalVariable key = method.getLocalVariable(token.getKey().getName());

            writeVarLoad(foreachVariable);
            writeSysDynamicCall(ForeachIterator.class, "getMemoryKey", Memory.class);
            if (token.isKeyReference()) {
                throw new FatalException(
                        "Key element cannot be a reference",
                        token.getKey().toTraceInfo(compiler.getContext())
                );
                // writeVarStore(key, false, false);
            } else
                writeVarAssign(key, false, false);
        }

        // $var
        //LocalVariable variable = method.getLocalVariable(token.getValue().getName());
        Token last = token.getValue().getLast();
        if (last instanceof DynamicAccessExprToken){
            DynamicAccessExprToken setter = (DynamicAccessExprToken)last;

            ExprStmtToken value = new ExprStmtToken(token.getValue().getTokens());
            value.getTokens().remove(value.getTokens().size() - 1);
            writeExpression(value, true, false);

            writeVarLoad(foreachVariable);
            writeSysDynamicCall(ForeachIterator.class, "getValue", Memory.class);
            if (!token.isValueReference())
                writePopImmutable();

            writeDynamicAccessInfo(setter, false);

            writeSysStaticCall(ObjectInvokeHelper.class,
                    "assignProperty", Memory.class,
                    Memory.class, Memory.class, String.class, Environment.class, TraceInfo.class
            );
        } else {
            writeVarLoad(foreachVariable);
            writeSysDynamicCall(ForeachIterator.class, "getValue", Memory.class);
            if (!token.isValueReference())
                writePopImmutable();

            writeExpression(token.getValue(), true, false);
            if (stackPeek().immutable)
                unexpectedToken(token.getValue().getLast());

            writeSysStaticCall(Memory.class,
                    token.isValueReference() ? "assignRefRight" : "assignRight", Memory.class, Memory.class, Memory.class
            );
        }
        writePopAll(1);
                     /*
        if (token.isValueReference())
            writeVarStore(variable, false, false);
        else
            writeVarAssign(variable, false, true); */

        // body
        writeBody(token.getBody());

        code.add(new JumpInsnNode(GOTO, start));
        code.add(end);

        method.popJump();
        writeUndefineVariables(token.getLocal());
        method.prevStatementIndex(ForeachIterator.class);
    }

    void writeFor(ForStmtToken token){
        writeDefineVariables(token.getInitLocal());
        for(ExprStmtToken expr : token.getInitExpr()){
            writeExpression(expr, false, false);
        }
        writeUndefineVariables(token.getInitLocal());

        writeDefineVariables(token.getLocal());
        for(VariableExprToken variable : token.getIterationLocal()){
            // TODO optimize this for Dynamic Values of variables
            LocalVariable local = method.getLocalVariable(variable.getName());
            local.setValue(null);
        }

        LabelNode start = writeLabel(node, token.getMeta().getStartLine());
        LabelNode iter = new LabelNode();
        LabelNode end = new LabelNode();

        writeExpression(token.getCondition(), true, false);
        writePopBoolean();

        code.add(new JumpInsnNode(IFEQ, end));
        stackPop();

        method.pushJump(end, iter);
        writeBody(token.getBody());
        method.popJump();

        code.add(iter);
        for(ExprStmtToken expr : token.getIterationExpr()){
            writeExpression(expr, false, false);
        }
        code.add(new JumpInsnNode(GOTO, start));
        code.add(end);
        code.add(new LineNumberNode(token.getMeta().getEndLine(), end));
        writeUndefineVariables(token.getLocal());
    }

    void writeWhile(WhileStmtToken token){
        writeDefineVariables(token.getLocal());

        LabelNode start = writeLabel(node, token.getMeta().getStartLine());
        LabelNode end = new LabelNode();

        writeConditional(token.getCondition(), end);

        method.pushJump(end, start);
        writeBody(token.getBody());
        method.popJump();

        code.add(new JumpInsnNode(GOTO, start));
        code.add(end);
        code.add(new LineNumberNode(token.getMeta().getEndLine(), end));

        writeUndefineVariables(token.getLocal());
    }

    void writeDo(DoStmtToken token){
        writeDefineVariables(token.getLocal());

        LabelNode start = writeLabel(node, token.getMeta().getStartLine());
        LabelNode end = new LabelNode();

        method.pushJump(end, start);
        writeBody(token.getBody());
        method.popJump();

        writeConditional(token.getCondition(), end);

        code.add(new JumpInsnNode(GOTO, start));
        code.add(end);
        code.add(new LineNumberNode(token.getMeta().getEndLine(), end));

        writeUndefineVariables(token.getLocal());
    }

    void writeReturn(ReturnStmtToken token){
        //addStackFrame();

        Memory result = Memory.NULL;
        boolean isImmutable = method.entity.isImmutable();
        if (token.getValue() != null)
            result = writeExpression(token.getValue(), true, true);

        if (result != null) {
            if (isImmutable) {
                if (method.entity.getResult() == null)
                    method.entity.setResult(result);
            }
            writePushMemory(result);
        } else {
            method.entity.setImmutable(false);
        }

        if (stackEmpty(false))
            writePushNull();
        else
            writePopBoxing(false);

        if (method.entity.isReturnReference()){
            writePushDup();
            writePushEnv();
            writePushTraceInfo(token);
            writeSysStaticCall(
                    InvokeHelper.class,
                    "checkReturnReference",
                    void.class,
                    Memory.class, Environment.class, TraceInfo.class
            );
        }

        code.add(new InsnNode(ARETURN));
        //removeStackFrame();
        stackPop();
    }

    void writeThrow(ThrowStmtToken throwStmt){
        writePushEnv();
        writePushTraceInfo(throwStmt.getException());
        writeExpression(throwStmt.getException(), true, false, true);
        writePopBoxing();
        writeSysDynamicCall(Environment.class, "__throwException", void.class, TraceInfo.class, Memory.class);
    }

    @SuppressWarnings("unchecked")
    void writeTryCatch(TryStmtToken tryCatch){
        if (tryCatch.getBody() == null || tryCatch.getBody().getInstructions().isEmpty())
            return;

        writeDefineVariables(tryCatch.getLocal());
        LabelNode tryStart = writeLabel(node, tryCatch.getMeta().getStartLine());
        LabelNode tryEnd   = new LabelNode();
        LabelNode catchStart = new LabelNode();
        LabelNode catchEnd = new LabelNode();

        method.node.tryCatchBlocks.add(
                new TryCatchBlockNode(tryStart, tryEnd, catchStart, Type.getInternalName(BaseException.class))
        );

        writeBody(tryCatch.getBody());
        code.add(tryEnd);
        code.add(new JumpInsnNode(GOTO, catchEnd));
        code.add(catchStart);

        LocalVariable exception = method.addLocalVariable(
                "~catch~" + method.nextStatementIndex(BaseException.class), catchStart, BaseException.class
        );
        exception.setEndLabel(catchEnd);
        makeVarStore(exception);


        LabelNode nextCatch = null;
        int i = 0, size = tryCatch.getCatches().size();
        LocalVariable local = null;
        LabelNode catchFail = new LabelNode();
        for(CatchStmtToken _catch : tryCatch.getCatches()) {
            if (nextCatch != null) {
                code.add(nextCatch);
            }
            if (i == size - 1) {
                nextCatch = catchFail;
            } else {
                nextCatch = new LabelNode();
            }

            local = method.getLocalVariable(_catch.getVariable().getName());

            writePushEnv();
            writeVarLoad(exception);
            writePushConstString(_catch.getException().toName());
            writePushConstString(_catch.getException().toName().toLowerCase());
            writeSysDynamicCall(
                    Environment.class, "__throwCatch", Memory.class, BaseException.class, String.class, String.class
            );

            writeVarAssign(local, true, false);
            writePopBoolean();
            code.add(new JumpInsnNode(IFEQ, nextCatch));
            stackPop();

            writeBody(_catch.getBody());
            code.add(new JumpInsnNode(GOTO, catchEnd));
            i++;
        }
        code.add(catchFail);

        makeVarLoad(exception);
        code.add(new InsnNode(ATHROW));

        code.add(catchEnd);

        writeUndefineVariables(tryCatch.getLocal());
        method.prevStatementIndex(BaseException.class);
    }

    void writeGlobal(GlobalStmtToken global){
        for(VariableExprToken variable : global.getVariables()){
            LocalVariable local = method.getLocalVariable(variable.getName());
            assert local != null;

            writePushEnv();
            writePushConstString(local.name);
            writeSysDynamicCall(Environment.class, "getOrCreateGlobal", Memory.class, String.class);
            writeVarStore(local, false, false);
        }
    }

    void writeStatic(StaticStmtToken static_){
        LocalVariable local = method.getLocalVariable(static_.getVariable().getName());
        assert local != null;

        LabelNode end = new LabelNode();
        boolean isClosure = method.clazz.isClosure();
        String name = isClosure ? local.name : local.name + "\0" + method.getMethodId();

        if (isClosure)
            writeVarLoad("~this");
        else
            writePushEnv();

        writePushConstString(name);
        writeSysDynamicCall(isClosure ? null : Environment.class, "getStatic", Memory.class, String.class);
        writePushDup();

        code.add(new JumpInsnNode(IFNONNULL, end));
        stackPop();

            writePopAll(1);
            if (isClosure)
                writeVarLoad("~this");
            else
                writePushEnv();

            writePushConstString(name);
            if (static_.getInitValue() != null){
                writeExpression(static_.getInitValue(), true, false, true);
            } else {
                writePushNull();
            }
            writePopBoxing(true);
            writeSysDynamicCall(isClosure ? null : Environment.class, "getOrCreateStatic", Memory.class, String.class, Memory.class);

        code.add(end);
        writeVarStore(local, false, false);
    }

    void writeConditional(ExprStmtToken condition, LabelNode successLabel){
        writeExpression(condition, true, false);
        writePopBoolean();
        code.add(new JumpInsnNode(IFEQ, successLabel));
        stackPop();
    }

    void writeFunction(FunctionStmtToken function){
        writePushEnv();
        writePushTraceInfo(function);
        writePushConstInt(compiler.getModule().getId());
        writePushConstInt(function.getId());

        writeSysDynamicCall(
                Environment.class, "__defineFunction", void.class, TraceInfo.class, Integer.TYPE, Integer.TYPE
        );
    }

    public Memory writeExpression(ExprStmtToken expression, boolean returnValue, boolean returnMemory){
        return writeExpression(expression, returnValue, returnMemory, true);
    }

    public Memory writeExpression(ExprStmtToken expression, boolean returnValue, boolean returnMemory, boolean writeOpcode){
        int initStackSize = method.getStackCount();
        exprStackInit.push(initStackSize);

        if (!expression.isStmtList())
            expression = new ASMExpression(compiler.getEnvironment(), compiler.getContext(), expression).getResult();

        List<Token> tokens = expression.getTokens();
        int operatorCount = 0;
        for(Token token : tokens){
            if (token instanceof OperatorExprToken)
                operatorCount++;
        }

        boolean invalid = false;
        for(Token token : tokens){
            if (writeOpcode){
                if (token instanceof StmtToken){
                    if (!(token instanceof ReturnStmtToken))
                        method.entity.setImmutable(false);
                }

                if (token instanceof FunctionStmtToken){
                    writeFunction((FunctionStmtToken)token); continue;
                } else if (token instanceof EchoRawToken){   // <? ... ?>
                    writeEchoRaw((EchoRawToken) token); continue;
                } else if (token instanceof EchoStmtToken){ // echo ...
                    writeEcho((EchoStmtToken) token); continue;
                } else  if (token instanceof OpenEchoTagToken){ // <?= ... ?>
                    writeOpenEchoTag((OpenEchoTagToken) token); continue;
                } else if (token instanceof ReturnStmtToken){ // return ...
                    writeReturn((ReturnStmtToken) token); continue;
                } else if (token instanceof BodyStmtToken){ // { .. }
                    writeBody((BodyStmtToken) token); continue;
                } else if (token instanceof IfStmtToken){ // if [else]
                    writeIf((IfStmtToken) token); continue;
                } else if (token instanceof SwitchStmtToken){  // switch ...
                    writeSwitch((SwitchStmtToken) token); continue;
                } else if (token instanceof WhileStmtToken){  // while { .. }
                    writeWhile((WhileStmtToken) token); continue;
                } else if (token instanceof DoStmtToken){ // do { ... } while( ... );
                    writeDo((DoStmtToken) token); continue;
                } else if (token instanceof ForStmtToken){ // for(...;...;...){ ... }
                    writeFor((ForStmtToken) token); continue;
                } else if (token instanceof ForeachStmtToken){
                    writeForeach((ForeachStmtToken) token); continue;
                } else if (token instanceof TryStmtToken){
                    writeTryCatch((TryStmtToken)token); continue;
                } else if (token instanceof ThrowStmtToken){
                    writeThrow((ThrowStmtToken)token); continue;
                } else if (token instanceof JumpStmtToken){  // break, continue
                    writeJump((JumpStmtToken)token); continue;
                } else if (token instanceof GlobalStmtToken){
                    writeGlobal((GlobalStmtToken) token); continue;
                } else if (token instanceof StaticStmtToken){
                    writeStatic((StaticStmtToken) token); continue;
                }
            }
                if (token instanceof ValueExprToken){  // mixed, calls, numbers, strings, vars, etc.
                    if (token instanceof CallExprToken && ((CallExprToken)token).getName() instanceof OperatorExprToken){
                        if (writeOpcode) {
                            writePush((ValueExprToken) token, true, true);
                            method.entity.setImmutable(false);
                        } else
                            break;
                    } else
                        stackPush((ValueExprToken) token);
                } else if (token instanceof OperatorExprToken){ // + - * / % && || or ! and == > < etc.
                    operatorCount--;
                    if (operatorCount >= 0) {
                        Memory result;
                        if (operatorCount == 0) {
                            result = writeOperator((OperatorExprToken) token, returnValue, writeOpcode);
                        } else
                            result = writeOperator((OperatorExprToken) token, true, writeOpcode);

                        if (!writeOpcode && result == null){
                            invalid = true;
                            break;
                        }

                        if (result == null)
                            method.entity.setImmutable(false);
                    }
                } else
                    break;

        }


        Memory result = null;
        if (!invalid && returnMemory && returnValue && !stackEmpty(false) && stackPeek().isConstant()) {
            result = stackPop().memory;
            invalid = true;
        }

        if (!invalid){
            if (returnValue && !stackEmpty(false) && stackPeek().isKnown()){
                if (returnMemory)
                    result = tryWritePush(stackPop(), writeOpcode, returnValue, true);
                else
                    writePush(stackPop());
            } else if (method.getStackCount() > 0){
                if (stackPeekToken() instanceof CallableExprToken) {
                    if (returnMemory)
                        result = tryWritePush(stackPopToken(), returnValue, writeOpcode, true);
                    else
                        writePush(stackPopToken(), returnValue, true);
                }
            }
        }

        if (!returnValue && writeOpcode){
            writePopAll(method.getStackCount() - initStackSize);
        } else if (!writeOpcode) {
            int count = method.getStackCount() - initStackSize;
            for(int i = 0; i < count; i++){
                stackPop();
            }
        }

        exprStackInit.pop();
        return result;
    }

    void makePop(StackItem.Type type){
        switch (type.size()){
            case 2: code.add(new InsnNode(POP2)); break;
            case 1: code.add(new InsnNode(POP)); break;
            default:
                throw new IllegalArgumentException("Invalid of size StackItem: " + type.size());
        }
    }

    void writePopAll(int count){
        int i = 0;
        while (method.getStackCount() > 0 && i < count){
            i++;
            StackItem o = stackPop();
            ValueExprToken token = o.getToken();
            StackItem.Type type = o.type;

            if (token == null){
                switch (type.size()){
                    case 2: code.add(new InsnNode(POP2)); break;
                    case 1: code.add(new InsnNode(POP)); break;
                    default:
                        throw new IllegalArgumentException("Invalid of size StackItem: " + type.size());
                }
            } else/* if (o.isInvalidForOperations())*/
                unexpectedToken(token);
        }
    }

    @Override
    public Entity compile() {
        writeDefineVariables(method.statement.getLocal());
        writeExpression(expression, false, false);
        method.popAll();
        return null;
    }

    public static class NoSuchMethodException extends RuntimeException {
        public NoSuchMethodException(Class clazz, String method, Class... parameters){
            super("No such method " + clazz.getName() + "." + method + "(" + StringUtils.join(parameters, ", ") + ")");
        }
    }
}
