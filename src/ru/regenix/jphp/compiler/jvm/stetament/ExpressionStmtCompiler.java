package ru.regenix.jphp.compiler.jvm.stetament;

import org.apache.commons.lang3.StringUtils;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
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
import ru.regenix.jphp.runtime.OperatorUtils;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.invoke.DynamicInvoke;
import ru.regenix.jphp.runtime.invoke.ObjectHelper;
import ru.regenix.jphp.runtime.memory.*;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.memory.support.MemoryUtils;
import ru.regenix.jphp.runtime.reflection.ConstantEntity;
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
            return new StringMemory(method.clazz.clazz.getFulledName());
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
        return method.getStackCount() == 0;
    }

    void writePushMemory(Memory memory){
        Memory.Type type = Memory.Type.REFERENCE;

        if (memory instanceof NullMemory){
            code.add(new FieldInsnNode(
                    GETSTATIC, Type.getInternalName(Memory.class), "NULL", Type.getDescriptor(Memory.class)
            ));
        } else if (memory instanceof FalseMemory){
            code.add(new FieldInsnNode(
                    GETSTATIC, Type.getInternalName(Memory.class), "FALSE", Type.getDescriptor(Memory.class)
            ));
        } else if (memory instanceof TrueMemory){
            code.add(new FieldInsnNode(
                    GETSTATIC, Type.getInternalName(Memory.class), "TRUE", Type.getDescriptor(Memory.class)
            ));
        } else if (memory instanceof ReferenceMemory){
            code.add(new TypeInsnNode(NEW, Type.getInternalName(ReferenceMemory.class)));
            code.add(new InsnNode(DUP));
            code.add(new MethodInsnNode(INVOKESPECIAL, Type.getInternalName(ReferenceMemory.class), Constants.INIT_METHOD, "()V"));
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

    void writePushBoolean(boolean value){
        writePushMemory(value ? Memory.TRUE : Memory.FALSE);
    }

    void writePushScalarBoolean(boolean value){
        writePushSmallInt(value ? 1 : 0);
        stackPop();
        stackPush(value ? Memory.TRUE : Memory.FALSE);
    }

    void writePushBoolean(BooleanExprToken value){
        writePushBoolean(value.getValue());
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

    void writePushSelf(boolean toLower){
        writePushString(toLower ?
                method.clazz.clazz.getFulledName().toLowerCase()
                : method.clazz.clazz.getFulledName()
        );
    }

    void writePushStatic(){
        writeVarLoad("~static");
    }

    void writePushLocal(){
        writeVarLoad("~local");
    }

    void writePushEnv(){
        stackPush(Memory.Type.REFERENCE);
        LocalVariable variable = method.getLocalVariable("~env");

        makeVarLoad(variable);
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

    void writePushNull(){
        writePushMemory(Memory.NULL);
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
        Method method = compileFunction.find( function.getParameters().size() );
        if (method == null){
            method = compileFunction.find( function.getParameters().size() + 1 );
            if(method != null && method.getParameterTypes()[0] != Environment.class)
                method = null;
        }

        if (method == null){
            throw new CompileException(
                    Messages.ERR_FATAL_PASS_INCORRECT_ARGUMENTS_TO_FUNCTION.fetch(function.getName().getWord()),
                    function.getName().toTraceInfo(compiler.getContext())
            );
        }

        if (statistic != null)
            statistic.returnType = StackItem.Type.valueOf(method.getReturnType());

        Class[] types = method.getParameterTypes();
        ListIterator<ExprStmtToken> iterator = function.getParameters().listIterator();

        Memory[] arguments = new Memory[types.length];
        int j = 0;
        boolean immutable = compileFunction.isImmutable && method.getReturnType() != void.class;

        for(int i = 0; i < types.length; i++){
            Class<?> argType = types[i];
            if (argType == Environment.class){
                if (!writeOpcode)
                    return null;

                writePushEnv();
                immutable = false;
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
                                    for(int n = 0; n < j; n++){
                                        writePushMemory(arguments[n]);
                                        writePop(types[n]);
                                        arguments[t] = null;
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

                    arguments[j] = MemoryUtils.valueOf(args);
                } else {
                    arguments[j] = writeExpression(
                            iterator.next(), true, immutable && (j == 0 || arguments[j-1] != null), writeOpcode
                    );
                    if (arguments[j] == null){
                        if (!writeOpcode)
                            return null;
                        for(int k = 0; k < j - 1; k++){
                            if (arguments[k] != null){
                                writePushMemory(arguments[k]);
                                if (types[k] == Memory.class)
                                    writePopBoxing(true);
                                else
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

        if (!writeOpcode)
            return null;

        writePushStaticCall(method);
        if (returnValue){
            if (method.getReturnType() == void.class)
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

    Memory writePushDynamicMethod(CallExprToken function, boolean returnValue, boolean writeOpcode,
                                  PushCallStatistic statistic){
        if (!writeOpcode)
            return null;

        DynamicAccessExprToken access = (DynamicAccessExprToken)function.getName();
        if (access instanceof DynamicAccessAssignExprToken)
            unexpectedToken(access);

        writeDynamicAccessPrepare(access, true);
        writePushParameters(function.getParameters());

        writeSysStaticCall(
                ObjectHelper.class, "invokeMethod",
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

        writePushEnv();
        writePushStatic();
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

            writePushString(className.toLowerCase() + "#" + methodName.toLowerCase());
            writePushString(className);
            writePushString(methodName);

            writePushParameters(function.getParameters());
            writeSysStaticCall(
                    DynamicInvoke.class, "callStatic", Memory.class,
                    Environment.class, String.class, TraceInfo.class,
                    String.class, // lower sign name
                    String.class, String.class, // origin names
                    Memory[].class
            );
        } else {
            if (clazz instanceof NameToken){
                writePushString(((NameToken) clazz).getName());
                writePushDup();
                writeSysDynamicCall(String.class, "toLowerCase", String.class);
            } else if (clazz instanceof SelfExprToken){
                writePushSelf(false);
                writePushSelf(true);
            } else {
                writePush(clazz, true);
                writePopString();
                writePushDup();
                writeSysDynamicCall(String.class, "toLowerCase", String.class);
            }

            if (access.getField() != null){
                ValueExprToken field = access.getField();
                if (field instanceof NameToken){
                    writePushString(((NameToken) field).getName().toLowerCase());
                } else {
                    writePush(access.getField(), true);
                    writePopString();
                    writePushDup();
                    writeSysDynamicCall(String.class, "toLowerCase", String.class);
                }
            } else {
                writeExpression(access.getFieldExpr(), true, false);
                writePopString();
                writePushDup();
                writeSysDynamicCall(String.class, "toLowerCase", String.class);
            }

            writePushParameters(function.getParameters());
            writeSysStaticCall(
                    DynamicInvoke.class, "callStaticDynamic", Memory.class,
                    Environment.class,
                    String.class, TraceInfo.class,
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
                    && compiler.getAnalyzer().findFunction(realName) == null){
                String tryName = ((FulledNameToken) name).getLastName().getName();
                compileFunction = compiler.getScope().findCompileFunction(tryName);
            }

            if (compileFunction != null){
                return writePushCompileFunction(function, compileFunction, returnValue, writeOpcode, statistic);
            } else {
                writePushEnv();
                writePushTraceInfo(function);
                writePushString(realName.toLowerCase());
                writePushString(realName);
                writePushParameters(function.getParameters());
                writeSysStaticCall(
                        DynamicInvoke.class, "call", Memory.class,
                        Environment.class, TraceInfo.class, String.class, String.class, Memory[].class
                );
                if (!returnValue)
                    writePopAll(1);
            }
        } else if (name instanceof StaticAccessExprToken){
            return writePushStaticMethod(function, returnValue, writeOpcode, statistic);
        } else if (name instanceof DynamicAccessExprToken){
            return writePushDynamicMethod(function, returnValue, writeOpcode, statistic);
        } else {
            writePushEnv();
            writePushStatic();
            writePushTraceInfo(function);

            writePush((ValueExprToken)function.getName(), true);
            writePopBoxing();

            writePushParameters(function.getParameters());
            writeSysStaticCall(
                    DynamicInvoke.class, "callAny", Memory.class,
                    Environment.class, String.class, TraceInfo.class, Memory.class, Memory[].class
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

        writePushStatic();
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
                String.class, String.class, ArrayMemory.class, TraceInfo.class
        );
        if (!returnValue)
            writePopAll(1);
    }

    void writePushNew(NewExprToken newToken, boolean returnValue){
        writePushEnv();
        if (newToken.isDynamic()){
            writePushVariable((VariableExprToken) newToken.getName());
            writePopString();
            writePushDup();
            writeSysDynamicCall(String.class, "toLowerCase", String.class);
        } else {
            FulledNameToken name = (FulledNameToken) newToken.getName();
            writePushString(name.getName());
            writePushString(name.getName().toLowerCase());
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
        for(VariableExprToken value : values)
            writeUndefineVariable(value);
    }

    protected void writeDefineVariable(VariableExprToken value){
        LocalVariable variable = method.getLocalVariable(value.getName());
        if (variable == null){
            LabelNode label = writeLabel(node, value.getMeta().getStartLine());
            variable = method.addLocalVariable(value.getName(), label, Memory.class);

            if (method.method.isReference(value)){
                variable.setReference(true);
            } else {
                variable.setReference(false);
            }

            if (method.method.isDynamicLocal()){
                writePushLocal();
                writePushString(value.getName());
                writeSysDynamicCall(ArrayMemory.class, "refOfIndex", Memory.class, String.class);
                makeVarStore(variable);
                stackPop();

                variable.pushLevel();
                variable.setValue(null);
                variable.setReference(true);
            } else {
                if (variable.isReference()){
                    writePushNewObject(ReferenceMemory.class);
                } else {
                    writePushNull();
                }

                makeVarStore(variable);

                stackPop();
                variable.pushLevel();
            }
        }

    }

    protected void writeUndefineVariable(VariableExprToken value){
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

    void writePushArray(ArrayExprToken array){
        if (array.getParameters().isEmpty()){
            writeSysStaticCall(ArrayMemory.class, "valueOf", Memory.class);
        } else {
            writePushNewObject(ArrayMemory.class);
            for(ExprStmtToken param : array.getParameters()){
                writePushDup();
                writeExpression(param, true, false);
                writePopBoxing();
                writeSysDynamicCall(ArrayMemory.class, "add", void.class, Memory.class);
            }
        }
        setStackPeekAsImmutable();
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
        writeGetStatic("__TRACE", TraceInfo[].class);
        writePushGetFromArray(index, TraceInfo.class);
    }

    void writePushCreateTraceInfo(int line, int position){
        writeGetStatic("__FN", String.class);
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
                writePushTraceInfo(token);
                writeSysDynamicCall(Environment.class, "getConstant", Memory.class, String.class, TraceInfo.class);
                setStackPeekAsImmutable();
            }
        }
        return null;
    }

    Memory tryWritePush(StackItem item, boolean writeOpcode, boolean toStack){
        if (item.getToken() != null)
            return tryWritePush(item.getToken(), true, writeOpcode);
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
        return tryWritePush(item, true, true);
    }

    void writePush(StackItem item){
        Memory memory = tryWritePush(item);
        if (memory != null)
            writePushMemory(memory);
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
        writePop(castType.toClass());
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
        else if (value instanceof CallExprToken) {
            PushCallStatistic statistic = new PushCallStatistic();
            writePushCall((CallExprToken)value, true, false, statistic);
            return statistic.returnType;
        } else
            return StackItem.Type.REFERENCE;
    }

    Memory tryWritePush(ValueExprToken value, boolean returnValue, boolean writeOpcode){
        if (writeOpcode)
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
            } else if (value instanceof ImportExprToken){
                writePushImport((ImportExprToken) value, returnValue);
            } else if (value instanceof NewExprToken){
                writePushNew((NewExprToken)value, returnValue);
            }

        if (value instanceof NameToken){
            return writePushName((NameToken)value, returnValue, writeOpcode);
        } else if (value instanceof ArrayExprToken) {
            return null;
        } else if (value instanceof CallExprToken) {
            return writePushCall((CallExprToken)value, returnValue, writeOpcode, null);
        } else if (value instanceof MacroToken){
            return tryWritePushMacro((MacroToken) value, writeOpcode);
        }

        return null;
    }

    void writePush(ValueExprToken value, boolean returnValue){
        Memory memory = tryWritePush(value, returnValue, true);
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
        if (INVOKE_TYPE != INVOKESPECIAL){
            try {
                clazz.getDeclaredMethod(method, paramClasses);
            } catch (java.lang.NoSuchMethodException e) {
                throw new NoSuchMethodException(clazz, method, paramClasses);
            }
        }

        Type[] args = new Type[paramClasses.length];
        if (INVOKE_TYPE == INVOKEVIRTUAL || INVOKE_TYPE == INVOKEINTERFACE)
            stackPop(); // this

        for(int i = 0; i < args.length; i++){
            args[i] = Type.getType(paramClasses[i]);
            stackPop();
        }

        code.add(new MethodInsnNode(
                INVOKE_TYPE, Type.getInternalName(clazz), method, Type.getMethodDescriptor(Type.getType(returnClazz), args)
        ));

        if (returnClazz != void.class){
            stackPush(null, StackItem.Type.valueOf(returnClazz));
        }
    }

    void writeSysDynamicCall(Class clazz, String method, Class returnClazz, Class... paramClasses)
            throws NoSuchMethodException {
        writeSysCall(
                clazz, clazz.isInterface() ? INVOKEINTERFACE : INVOKEVIRTUAL,
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
                method.clazz.entity.getName().replace('\\', Constants.NAME_DELIMITER),
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
                method.clazz.entity.getName().replace('\\', Constants.NAME_DELIMITER),
                name,
                Type.getDescriptor(fieldClass)
        ));
        stackPush(null, StackItem.Type.valueOf(fieldClass));
        setStackPeekAsImmutable();
    }

    void writeGetEnum(Enum enumInstance){
        writeGetStatic(enumInstance.getDeclaringClass(), enumInstance.name(), enumInstance.getDeclaringClass());
    }

    void writeVariableAssign(VariableExprToken variable, StackItem R, boolean returnValue){
        LocalVariable local = method.getLocalVariable(variable.getName());

        Memory value = R.getMemory();
        if (local.isReference()){
            if (!R.isKnown()){
                stackPush(R);
                writePopBoxing();
                writePopImmutable();
                writePushVariable(variable);
                writeSysStaticCall(Memory.class, "assignRight", void.class, stackPeek().type.toClass(), Memory.class);
            } else {
                writePushVariable(variable);
                Memory tmp = tryWritePush(R);
                if (tmp != null){
                    value = tmp;
                    writePushMemory(value);
                }
                writePopBoxing();
                writePopImmutable();
                writeSysDynamicCall(Memory.class, "assign", Memory.class, stackPeek().type.toClass());
            }
            if (returnValue)
                writeVarLoad(local);
        } else {
            Memory result = tryWritePush(R);
            if (result != null){
                writePushMemory(result);
                value = result;
            }
            writePopBoxing();
            writeVarStore(local, returnValue, true);
        }

        local.setValue(value);
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
            if (CompilerUtils.isSideOperator(operator))
                operatorName += "Right";

            writeSysDynamicCall(Memory.class, operatorName, operatorResult, stackPeek().type.toClass());
        } else {
            writePush(L);
            writePopBoxing();
            writePush(R);
            writeSysDynamicCall(Memory.class, operatorName, operatorResult, stackPeek().type.toClass());
        }
    }

    void writePop(Class clazz){
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

    void writePopBoolean(){
        StackItem.Type peek = stackPeek().type;
        switch (peek){
            case BOOL: break;
            case BYTE:
            case INT:
            case SHORT:
            case LONG: {
                LabelNode fail = new LabelNode();
                LabelNode end = new LabelNode();

                code.add(new JumpInsnNode(IFEQ, fail));
                code.add(new InsnNode(ICONST_1));
                code.add(new JumpInsnNode(GOTO, end));
                code.add(fail);
                code.add(new InsnNode(ICONST_0));
                code.add(end);

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

    void writeStaticAccess(StaticAccessExprToken token){
        ValueExprToken value = token.getClazz();
        writePushEnv();
        writePush(value, true);
        writePushTraceInfo(token);
        writeSysDynamicCall(
                Environment.class, "getClass", Class.class, Environment.class, String.class, TraceInfo.class
        );
    }

    void writeDynamicAccessPrepare(DynamicAccessExprToken dynamic, boolean addLowerName){
        if (stackEmpty())
            unexpectedToken(dynamic);

        StackItem o = stackPop();
        writePush(o);

        if (stackPeek().isConstant())
            unexpectedToken(dynamic);
        writePopBoxing();

        if (dynamic instanceof DynamicAccessAssignExprToken){
            writeExpression(((DynamicAccessAssignExprToken) dynamic).getValue(), true, false);
            writePopBoxing();
        }

        if (dynamic.getField() != null){
            if (dynamic.getField() instanceof NameToken){
                String name = ((NameToken) dynamic.getField()).getName();
                writePushString(name);
                if (addLowerName)
                    writePushString(name.toLowerCase());
            } else {
                writePush(dynamic.getField(), true);
                writePopString();
                if (addLowerName){
                    writePushDup();
                    writeSysDynamicCall(String.class, "toLowerCase", String.class);
                }
            }
        } else {
            writeExpression(dynamic.getFieldExpr(), true, false);
            writePopString();
            if (addLowerName){
                writePushDup();
                writeSysDynamicCall(String.class, "toLowerCase", String.class);
            }
        }
        writePushEnv();
        writePushTraceInfo(dynamic);
    }

    void writeDynamicAccess(DynamicAccessExprToken dynamic, boolean returnValue){
        writeDynamicAccessPrepare(dynamic, false);

        if (dynamic instanceof DynamicAccessAssignExprToken){
            writeSysStaticCall(ObjectHelper.class,
                    "setProperty", Memory.class,
                    Memory.class, Memory.class, String.class, Environment.class, TraceInfo.class
            );
        } else {
            writeSysStaticCall(ObjectHelper.class,
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
            writePush(L = o.getToken(), true);
            writePopBoxing();
        }

        String methodName = operator instanceof ArrayGetRefExprToken ? "refOfIndex" : "valueOfIndex";
        for(ExprStmtToken param : operator.getParameters()){
            writeExpression(param, true, false);
            writeSysDynamicCall(Memory.class, methodName, Memory.class, stackPeek().type.toClass());
        }
    }

    void writeUnaryOperator(OperatorExprToken operator, boolean returnValue){
        if (stackEmpty())
            unexpectedToken(operator);

        StackItem o = stackPop();
        ValueExprToken L = o.getToken();

        if (o.getMemory() != null){
            Memory result = CompilerUtils.calcUnary(o.getMemory(), operator);
            if (result != null){
                stackPush(result);
                return;
            }
        }

        String name = CompilerUtils.getOperatorCode(operator);
        Class operatorResult = CompilerUtils.getOperatorResult(operator);

        LocalVariable variable = null;
        if (L instanceof VariableExprToken){
            variable = method.getLocalVariable(((VariableExprToken) L).getName());
        }

        if (operator instanceof IncExprToken || operator instanceof DecExprToken){
            writePush(o);

            if (variable == null || variable.isReference()){
                writePushDup();
                writeSysDynamicCall(Memory.class, name, operatorResult);
                writeSysDynamicCall(Memory.class, "assign", Memory.class, operatorResult);
                if (!returnValue){
                    writePopAll(1);
                }
            } else {
                writeSysDynamicCall(Memory.class, name, operatorResult);
                variable.setValue(null); // TODO for constant values
                writeVarStore(variable, returnValue);
            }
        } else if (operator instanceof SilentToken) {
            if (!o.isKnown())
                unexpectedToken(operator);

            writePushEnv();
            writePushDup();

            writeSysDynamicCall(Environment.class, "beginSilent", Integer.TYPE);

            writePush(o);
            writePopBoxing();

            writeSysDynamicCall(Environment.class, "endSilent", Memory.class, Integer.TYPE, Memory.class);
        } else {
                writePush(o);

                writeSysDynamicCall(Memory.class, name, operatorResult);
                if (!returnValue){
                    writePopAll(1);
                }
        }
    }

    void writeLogicOperator(LogicOperatorExprToken operator, boolean returnValue){
        if (stackEmpty())
            unexpectedToken(operator);

        StackItem o = stackPop();

        writePush(o);
        writePopBoolean();

        LabelNode end = new LabelNode();
        LabelNode next = new LabelNode();

        if (operator instanceof BooleanOrExprToken || operator instanceof BooleanOr2ExprToken){
            code.add(new JumpInsnNode(IFEQ, next));
            stackPop();
            if (returnValue){
                writePushBoolean(true);
                stackPop();
            }
        } else if (operator instanceof BooleanAndExprToken || operator instanceof BooleanAnd2ExprToken){
            code.add(new JumpInsnNode(IFNE, next));
            stackPop();
            if (returnValue){
                writePushBoolean(false);
                stackPop();
            }
        }

        code.add(new JumpInsnNode(GOTO, end));

        code.add(next);
        writeExpression(operator.getRightValue(), returnValue, false);
        if (returnValue)
            writePopBoxing();

        code.add(end);
    }

    void writeOperator(OperatorExprToken operator, boolean returnValue){
        if (operator instanceof DynamicAccessExprToken){
            writeDynamicAccess((DynamicAccessExprToken)operator, returnValue);
            return;
        }

        if (!operator.isBinary()){
            writeUnaryOperator(operator, returnValue);
            return;
        }

        if (operator instanceof LogicOperatorExprToken){
            writeLogicOperator((LogicOperatorExprToken)operator, returnValue);
            return;
        }

        if (stackEmpty())
            unexpectedToken(operator);

        StackItem o1 = stackPop();

        if (stackEmpty())
            unexpectedToken(operator);

        StackItem o2 = stackPeek();
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

        Memory value1 = tryWritePush(o2, false, false); // LEFT
        Memory value2 = tryWritePush(o1, false, false); // RIGHT
        if (value1 != null && value2 != null){
            stackPush(value1);
            stackPush(value2);
            writeOperator(operator, returnValue);
            return;
        }

        StackItem.Type Lt = tryGetType(o2);
        StackItem.Type Rt = tryGetType(o1);

        String name = CompilerUtils.getOperatorCode(operator);
        Class operatorResult = CompilerUtils.getOperatorResult(operator);
        if (Lt.isConstant() && Rt.isConstant()){
            if (operator instanceof AssignOperatorExprToken)
                unexpectedToken(operator);

            writeScalarOperator(o2, Lt, o1, Rt, operator, operatorResult, name);
        } else {
            boolean isInvert = !o1.isKnown();

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
                if (isInvert){
                    stackPush(o1);
                    writePopBoxing();
                    writePush(o2);
                    if (CompilerUtils.isSideOperator(operator))
                        name += "Right";
                } else {
                    writePush(o2);
                    writePopBoxing();

                    writePush(o1);
                }
                writeSysDynamicCall(Memory.class, name, operatorResult, isInvert ? Lt.toClass() : Rt.toClass());
            }
            setStackPeekAsImmutable();

            if (operator instanceof AssignOperatorExprToken){
                if (returnValue)
                    writePushDup(StackItem.Type.valueOf(operatorResult));

                if (variable == null || variable.isReference()){
                    writeSysDynamicCall(Memory.class, "assign", Memory.class, Rt.toClass());
                } else {
                    writePopBoxing(operatorResult);
                    makeVarStore(variable);
                    variable.setValue(!stackPeek().isConstant() ? null : stackPeek().getMemory());
                    stackPop();
                }
            }
        }
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

        LabelNode end = new LabelNode();

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

        i = 0;
        for(CaseStmtToken one : token.getCases()){
            code.add(jumps[i][0]); // conditional

            if (one.getConditional() != null){
                writePushDup();
                writeExpression(one.getConditional(), true, false);
                writeSysDynamicCall(Memory.class, "equal", Boolean.TYPE, stackPeek().type.toClass());
                code.add(new JumpInsnNode(IFEQ, jumps[i + 1][0]));
            }

            writePopAll(1);
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
        writeUndefineVariables(token.getLocal());
    }

    void writeFor(ForStmtToken token){
        writeDefineVariables(token.getInitLocal());
        writeExpression(token.getInitExpr(), false, false);
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
        writeExpression(token.getIterationExpr(), false, false);
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

        if (token.getValue() == null)
            writePushNull();
        else
            writeExpression(token.getValue(), true, false);

        if (stackEmpty())
            writePushNull();
        else
            writePopBoxing(true);

        if (method.entity.isReturnReference()){
            writePushDup();
            writePushEnv();
            writePushTraceInfo(token);
            writeSysStaticCall(
                    DynamicInvoke.class,
                    "checkReturnReference",
                    void.class,
                    Memory.class, Environment.class, TraceInfo.class
            );
        }

        code.add(new InsnNode(ARETURN));
        //removeStackFrame();
        stackPop();
    }

    void writeConditional(ExprStmtToken condition, LabelNode successLabel){
        writeExpression(condition, true, false);
        writePopBoolean();
        code.add(new JumpInsnNode(IFEQ, successLabel));
        stackPop();
    }

    public Memory writeExpression(ExprStmtToken expression, boolean returnValue, boolean returnMemory){
        return writeExpression(expression, returnValue, returnMemory, true);
    }

    public Memory writeExpression(ExprStmtToken expression, boolean returnValue, boolean returnMemory, boolean writeOpcode){
        int initStackSize = method.getStackCount();

        if (expression.getTokens().size() == 1){
            Token token = expression.getTokens().get(0);
            if (!(token instanceof StmtToken)){
                expression = new ASMExpression(compiler.getContext(), expression, true).getResult();
            }
        } else
            expression = new ASMExpression(compiler.getContext(), expression, true).getResult();

        List<Token> tokens = expression.getTokens();
        int operatorCount = 0;
        for(Token token : tokens){
            if (token instanceof OperatorExprToken)
                operatorCount++;
        }

        for(Token token : tokens){
            if (writeOpcode){
                if (token instanceof EchoRawToken){   // <? ... ?>
                    writeEchoRaw((EchoRawToken)token);
                } else if (token instanceof EchoStmtToken){ // echo ...
                    writeEcho((EchoStmtToken) token);
                } else  if (token instanceof OpenEchoTagToken){ // <?= ... ?>
                    writeOpenEchoTag((OpenEchoTagToken) token);
                } else if (token instanceof ArrayGetExprToken){  // ..[x][y][z]
                    writeArrayGet((ArrayGetExprToken) token, returnValue);
                } if (token instanceof ReturnStmtToken){ // return ...
                    writeReturn((ReturnStmtToken) token);
                } else if (token instanceof IfStmtToken){ // if [else]
                    writeIf((IfStmtToken) token);
                } else if (token instanceof SwitchStmtToken){  // switch ...
                    writeSwitch((SwitchStmtToken) token);
                } else if (token instanceof WhileStmtToken){  // while { .. }
                    writeWhile((WhileStmtToken) token);
                } else if (token instanceof DoStmtToken){ // do { ... } while( ... );
                    writeDo((DoStmtToken) token);
                } else if (token instanceof ForStmtToken){ // for(...;...;...){ ... }
                    writeFor((ForStmtToken) token);
                } else if (token instanceof JumpStmtToken){  // break, continue
                    writeJump((JumpStmtToken)token);
                }
            }

            if (token instanceof ValueExprToken){  // mixed, calls, numbers, strings, vars, etc.
                if (token instanceof CallExprToken && ((CallExprToken)token).getName() instanceof OperatorExprToken){
                    if (writeOpcode) {
                        writePush((ValueExprToken) token, true);
                    } else
                        break;
                } else
                    stackPush((ValueExprToken) token);
            } else if (token instanceof OperatorExprToken){ // + - * / % && || or ! and == > < etc.
                operatorCount--;
                if (operatorCount == 0)
                    writeOperator((OperatorExprToken) token, returnValue);
                else
                    writeOperator((OperatorExprToken) token, true);
            } else
                break;
        }

        if (returnMemory && returnValue && !stackEmpty() && stackPeek().isConstant())
            return stackPop().memory;

        Memory result = null;
        if (returnValue && !stackEmpty() && stackPeek().isKnown()){
            if (returnMemory)
                result = tryWritePush(stackPop(), writeOpcode, returnValue);
            else
                writePush(stackPop());
        } else if (method.getStackCount() > 0){
            if (stackPeekToken() instanceof CallExprToken || stackPeekToken() instanceof ImportExprToken) {
                if (returnMemory)
                    result = tryWritePush(stackPopToken(), returnValue, writeOpcode);
                else
                    writePush(stackPopToken(), returnValue);
            }
        }

        if (!returnValue && writeOpcode){
            writePopAll(method.getStackCount() - initStackSize);
        } else if (!writeOpcode)
            for(int i = 0; i < method.getStackCount() - initStackSize; i++){
                stackPop();
            }

        return result;
    }

    void writePopAll(int count){
        int i = 0;
        while (method.getStackCount() > 0 && i < count){
            i++;
            ValueExprToken token = stackPeekToken();
            StackItem.Type type = stackPop().type;

            if (token == null){
                switch (type.size()){
                    case 2: code.add(new InsnNode(POP2)); break;
                    case 1: code.add(new InsnNode(POP)); break;
                    default:
                        throw new IllegalArgumentException("Invalid of size StackItem: " + type.size());
                }
            }

        }
    }

    @Override
    public Entity compile() {
        writeDefineVariables(method.method.getLocal());
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
