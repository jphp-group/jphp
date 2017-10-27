package org.develnext.jphp.core.compiler.jvm.statement;

import org.develnext.jphp.core.compiler.common.ASMExpression;
import org.develnext.jphp.core.compiler.common.misc.StackItem;
import org.develnext.jphp.core.compiler.common.util.CompilerUtils;
import org.develnext.jphp.core.compiler.jvm.Constants;
import org.develnext.jphp.core.compiler.jvm.JvmCompiler;
import org.develnext.jphp.core.compiler.jvm.misc.LocalVariable;
import org.develnext.jphp.core.compiler.jvm.statement.expr.*;
import org.develnext.jphp.core.compiler.jvm.statement.expr.operator.DynamicAccessCompiler;
import org.develnext.jphp.core.compiler.jvm.statement.expr.operator.InstanceOfCompiler;
import org.develnext.jphp.core.compiler.jvm.statement.expr.value.*;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.OpenEchoTagToken;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.token.expr.ClassExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.*;
import org.develnext.jphp.core.tokenizer.token.expr.value.*;
import org.develnext.jphp.core.tokenizer.token.expr.value.macro.*;
import org.develnext.jphp.core.tokenizer.token.stmt.*;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import php.runtime.Memory;
import php.runtime.OperatorUtils;
import php.runtime.annotation.Runtime;
import php.runtime.common.Association;
import php.runtime.common.Messages;
import php.runtime.common.StringUtils;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.CriticalException;
import php.runtime.ext.support.compile.CompileClass;
import php.runtime.ext.support.compile.CompileConstant;
import php.runtime.ext.support.compile.CompileFunction;
import php.runtime.invoke.InvokeHelper;
import php.runtime.invoke.ObjectInvokeHelper;
import php.runtime.invoke.cache.FunctionCallCache;
import php.runtime.invoke.cache.MethodCallCache;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.IObject;
import php.runtime.memory.*;
import php.runtime.memory.helper.UndefinedMemory;
import php.runtime.memory.support.MemoryUtils;
import php.runtime.reflection.*;
import php.runtime.reflection.support.Entity;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static org.objectweb.asm.Opcodes.*;

public class ExpressionStmtCompiler extends StmtCompiler {
    protected final MethodStmtCompiler method;
    protected final MethodStmtToken methodStatement;
    protected final ExprStmtToken expression;

    private MethodNode node;
    private InsnList code;

    private Stack<Integer> exprStackInit = new Stack<Integer>();

    private int lastLineNumber = -1;

    private Map<Class<? extends Token>, BaseStatementCompiler> compilers;
    private static final Map<Class<? extends Token>, Class<? extends BaseStatementCompiler>> compilerRules;

    static {
        compilerRules = new HashMap<Class<? extends Token>, Class<? extends BaseStatementCompiler>>();
        compilerRules.put(IfStmtToken.class, IfElseCompiler.class);
        compilerRules.put(SwitchStmtToken.class, SwitchCompiler.class);
        compilerRules.put(FunctionStmtToken.class, FunctionCompiler.class);
        compilerRules.put(EchoRawToken.class, EchoRawCompiler.class);
        compilerRules.put(EchoStmtToken.class, EchoCompiler.class);
        compilerRules.put(OpenEchoTagToken.class, OpenEchoTagCompiler.class);
        compilerRules.put(ReturnStmtToken.class, ReturnCompiler.class);
        compilerRules.put(BodyStmtToken.class, BodyCompiler.class);
        compilerRules.put(WhileStmtToken.class, WhileCompiler.class);
        compilerRules.put(DoStmtToken.class, DoCompiler.class);
        compilerRules.put(ForStmtToken.class, ForCompiler.class);
        compilerRules.put(ForeachStmtToken.class, ForeachCompiler.class);
        compilerRules.put(TryStmtToken.class, TryCatchCompiler.class);
        compilerRules.put(ThrowStmtToken.class, ThrowCompiler.class);

        compilerRules.put(BreakStmtToken.class, JumpCompiler.class);
        compilerRules.put(ContinueStmtToken.class, JumpCompiler.class);

        compilerRules.put(GotoStmtToken.class, GotoCompiler.class);
        compilerRules.put(LabelStmtToken.class, GotoLabelCompiler.class);

        compilerRules.put(GlobalStmtToken.class, GlobalDefinitionCompiler.class);
        compilerRules.put(StaticStmtToken.class, StaticDefinitionCompiler.class);

        compilerRules.put(JvmCompiler.ClassInitEnvironment.class, ClassInitEnvironmentCompiler.class);

        // values
        compilerRules.put(BooleanExprToken.class, BooleanValueCompiler.class);
        compilerRules.put(IntegerExprToken.class, IntValueCompiler.class);
        compilerRules.put(NullExprToken.class, NullValueCompiler.class);
        compilerRules.put(DoubleExprToken.class, DoubleValueCompiler.class);
        compilerRules.put(StringExprToken.class, StringValueCompiler.class);
        compilerRules.put(ShellExecExprToken.class, ShellExecValueCompiler.class);

        compilerRules.put(IncludeExprToken.class, ImportCompiler.class);
        compilerRules.put(IncludeOnceExprToken.class, ImportCompiler.class);
        compilerRules.put(RequireExprToken.class, ImportCompiler.class);
        compilerRules.put(RequireOnceExprToken.class, ImportCompiler.class);

        compilerRules.put(NewExprToken.class, NewValueCompiler.class);
        compilerRules.put(StringBuilderExprToken.class, StringBuilderValueCompiler.class);
        compilerRules.put(UnsetExprToken.class, UnsetCompiler.class);
        compilerRules.put(IssetExprToken.class, IssetCompiler.class);
        compilerRules.put(EmptyExprToken.class, EmptyCompiler.class);
        compilerRules.put(DieExprToken.class, DieCompiler.class);
        compilerRules.put(StaticExprToken.class, StaticValueCompiler.class);
        compilerRules.put(ClosureStmtToken.class, ClosureValueCompiler.class);
        compilerRules.put(GetVarExprToken.class, VarVarValueCompiler.class);
        compilerRules.put(SelfExprToken.class, SelfValueCompiler.class);
        compilerRules.put(StaticAccessExprToken.class, StaticAccessValueCompiler.class);
        compilerRules.put(StaticAccessIssetExprToken.class, StaticAccessValueCompiler.class);
        compilerRules.put(StaticAccessUnsetExprToken.class, StaticAccessValueCompiler.class);
        compilerRules.put(ListExprToken.class, ListCompiler.class);
        compilerRules.put(YieldExprToken.class, YieldValueCompiler.class);

        // operation
        compilerRules.put(InstanceofExprToken.class, InstanceOfCompiler.class);
        compilerRules.put(DynamicAccessAssignExprToken.class, DynamicAccessCompiler.class);
        compilerRules.put(DynamicAccessEmptyExprToken.class, DynamicAccessCompiler.class);
        compilerRules.put(DynamicAccessExprToken.class, DynamicAccessCompiler.class);
        compilerRules.put(DynamicAccessGetRefExprToken.class, DynamicAccessCompiler.class);
        compilerRules.put(DynamicAccessIssetExprToken.class, DynamicAccessCompiler.class);
        compilerRules.put(DynamicAccessUnsetExprToken.class, DynamicAccessCompiler.class);
        compilerRules.put(StaticAccessOperatorExprToken.class, StaticAccessValueAsOperatorCompiler.class);
    }

    public ExpressionStmtCompiler(MethodStmtCompiler method, ExprStmtToken expression) {
        super(method.getCompiler());
        this.method = method;
        this.expression = expression;
        this.node = method.node;
        this.code = method.node.instructions;
        this.methodStatement = method.statement != null ? method.statement : MethodStmtToken.of("", method.clazz.statement);
    }

    public ExpressionStmtCompiler(JvmCompiler compiler) {
        super(compiler);
        this.expression = null;
        ClassStmtCompiler classStmtCompiler = new ClassStmtCompiler(
                compiler, new ClassStmtToken(new TokenMeta("", 0, 0, 0, 0))
        );

        this.method = new MethodStmtCompiler(classStmtCompiler, (MethodStmtToken) null);
        this.methodStatement = method.statement != null ? method.statement : MethodStmtToken.of("", method.clazz.statement);

        this.node = method.node;
        this.code = method.node.instructions;
    }

    @SuppressWarnings("unchecked")
    public <T extends Token> T write(Class<? extends Token> clazz, T token) {
        BaseStatementCompiler<T> cmp = (BaseStatementCompiler<T>) getCompiler(clazz);
        if (cmp == null)
            throw new CriticalException("Cannot find compiler for " + clazz.getName());
        cmp.write(token);
        return token;
    }

    @SuppressWarnings("unchecked")
    public <T extends Token> T write(T token) {
        if (token == null)
            throw new IllegalArgumentException("Token cannot be null");

        return write(token.getClass(), token);
    }

    @SuppressWarnings("unchecked")
    public <T extends ValueExprToken> T writeValue(T token, boolean returnValue) {
        BaseStatementCompiler<T> cmp = (BaseStatementCompiler<T>) getCompiler(token.getClass());
        if (cmp == null)
            throw new CriticalException("Cannot find compiler for " + token.getClass().getName());

        if (cmp instanceof BaseExprCompiler)
            ((BaseExprCompiler) cmp).write(token, returnValue);
        else
            throw new CriticalException("Compiler is not for values");

        return token;
    }

    @SuppressWarnings("unchecked")
    public <T extends Token> BaseStatementCompiler<T> getCompiler(Class<T> clazz) {
        if (compilers == null) {
            compilers = new HashMap<Class<? extends Token>, BaseStatementCompiler>();
        }

        BaseStatementCompiler<T> r = compilers.get(clazz);
        if (r != null)
            return r;

        Class<? extends BaseStatementCompiler<T>> rule =
                (Class<? extends BaseStatementCompiler<T>>) compilerRules.get(clazz);

        if (rule == null)
            return null;

        try {
            r = rule.getConstructor(ExpressionStmtCompiler.class).newInstance(this);
            compilers.put(clazz, r);
            return r;
        } catch (Exception e) {
            throw new CriticalException(e);
        }
    }

    public MethodNode getMethodNode() {
        return node;
    }

    public MethodStmtCompiler getMethod() {
        return method;
    }

    public MethodStmtToken getMethodStatement() {
        return methodStatement;
    }

    public void makeVarStore(LocalVariable variable) {
        code.add(new VarInsnNode(ASTORE, variable.index));
    }

    public void makeVarLoad(LocalVariable variable) {
        code.add(new VarInsnNode(ALOAD, variable.index));
    }

    public void makeUnknown(AbstractInsnNode node) {
        code.add(node);
    }

    public LabelNode makeLabel() {
        LabelNode el;
        code.add(el = new LabelNode());
        return el;
    }

    protected Memory getMacros(ValueExprToken token) {
        if (token instanceof SelfExprToken) {
            if (method.clazz.entity.isTrait()) {
                throw new IllegalArgumentException("Cannot use this in Traits");
            } else {
                return new StringMemory(method.clazz.statement.getFulledName());
            }
        }

        return null;
    }

    protected void stackPush(ValueExprToken token, StackItem.Type type) {
        method.push(new StackItem(token, type));
    }

    public void stackPush(StackItem item) {
        method.push(item);
    }

    public void stackPush(Memory memory) {
        method.push(new StackItem(memory));
    }

    public void stackPush(ValueExprToken token, Memory memory) {
        method.push(new StackItem(token, memory));
    }

    public void stackPush(Memory.Type type) {
        stackPush(null, StackItem.Type.valueOf(type));
    }

    public void stackPush(ValueExprToken token) {
        Memory o = CompilerUtils.toMemory(token);

        if (o != null) {
            stackPush(o);
        } else if (token instanceof StaticAccessExprToken) {
            StaticAccessExprToken access = (StaticAccessExprToken) token;
            if (access.getField() instanceof NameToken) {
                String constant = ((NameToken) access.getField()).getName();
                String clazz = null;
                ClassEntity entity = null;
                if (access.getClazz() instanceof ParentExprToken) {
                    entity = method.clazz.entity.getParent();
                    if (entity != null)
                        clazz = entity.getName();
                } else if (access.getClazz() instanceof FulledNameToken) {
                    clazz = ((FulledNameToken) access.getClazz()).getName();
                    entity = compiler.getModule().findClass(clazz);
                }

                if (entity == null && method.clazz.entity != null && method.clazz.entity.getName().equalsIgnoreCase(clazz))
                    entity = method.clazz.entity;

                if (entity != null) {
                    ConstantEntity c = entity.findConstant(constant);

                    if (c != null && c.getValue() != null && c.isPublic()) {
                        stackPush(c.getValue());
                        stackPeek().setLevel(-1);
                        return;
                    }
                }
            }
            stackPush(token, StackItem.Type.REFERENCE);
        } else {
            if (token instanceof VariableExprToken
                    && !methodStatement.isUnstableVariable((VariableExprToken) token)) {
                LocalVariable local = method.getLocalVariable(((VariableExprToken) token).getName());
                if (local != null && local.getValue() != null) {
                    stackPush(token, local.getValue());
                    stackPeek().setLevel(-1);
                    return;
                }
            }
            stackPush(token, StackItem.Type.REFERENCE);
        }

        stackPeek().setLevel(-1);
    }

    public StackItem stackPop() {
        return method.pop();
    }

    public ValueExprToken stackPopToken() {
        return method.pop().getToken();
    }

    public StackItem stackPeek() {
        return method.peek();
    }

    public void setStackPeekAsImmutable(boolean value) {
        method.peek().immutable = value;
    }

    public void setStackPeekAsImmutable() {
        setStackPeekAsImmutable(true);
    }

    public ValueExprToken stackPeekToken() {
        return method.peek().getToken();
    }

    public boolean stackEmpty(boolean relative) {
        if (relative)
            return method.getStackCount() == exprStackInit.peek();
        else
            return method.getStackCount() == 0;
    }

    public void writeLineNumber(Token token) {
        if (token.getMeta().getStartLine() > lastLineNumber) {
            lastLineNumber = token.getMeta().getStartLine();
            code.add(new LineNumberNode(lastLineNumber, new LabelNode()));
        }
    }

    public void writeWarning(Token token, String message) {
        writePushEnv();
        writePushTraceInfo(token);
        writePushConstString(message);
        writePushConstNull();
        writeSysDynamicCall(Environment.class, "warning", void.class, TraceInfo.class, String.class, Object[].class);
    }

    public void writePushBooleanAsMemory(boolean value) {
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

    public void writePushMemory(Memory memory) {
        Memory.Type type = Memory.Type.REFERENCE;

        if (memory instanceof UndefinedMemory) {
            code.add(new FieldInsnNode(
                    GETSTATIC, Type.getInternalName(Memory.class), "UNDEFINED", Type.getDescriptor(Memory.class)
            ));
        } else if (memory instanceof NullMemory) {
            code.add(new FieldInsnNode(
                    GETSTATIC, Type.getInternalName(Memory.class), "NULL", Type.getDescriptor(Memory.class)
            ));
        } else if (memory instanceof FalseMemory) {
            writePushConstBoolean(false);
            return;
        } else if (memory instanceof TrueMemory) {
            writePushConstBoolean(true);
            return;
        } else if (memory instanceof KeyValueMemory) {
            writePushMemory(((KeyValueMemory) memory).key);
            writePopBoxing();

            writePushMemory(((KeyValueMemory) memory).getValue());
            writePopBoxing();

            Object arrayKey = ((KeyValueMemory) memory).getArrayKey();

            if (arrayKey instanceof String) {
                writePushConstString((String) arrayKey);
                writeSysStaticCall(KeyValueMemory.class, "valueOf", Memory.class, Memory.class, Memory.class, String.class);
            } else if (arrayKey instanceof LongMemory) {
                writePushConstantMemory((Memory) arrayKey);
                writeSysStaticCall(KeyValueMemory.class, "valueOf", Memory.class, Memory.class, Memory.class, Memory.class);
            } else {
                writeSysStaticCall(KeyValueMemory.class, "valueOf", Memory.class, Memory.class, Memory.class);
            }

            setStackPeekAsImmutable();
            return;
        } else if (memory instanceof ReferenceMemory) {
            code.add(new TypeInsnNode(NEW, Type.getInternalName(ReferenceMemory.class)));
            code.add(new InsnNode(DUP));
            code.add(new MethodInsnNode(INVOKESPECIAL, Type.getInternalName(ReferenceMemory.class), Constants.INIT_METHOD, "()V", false));
        } else if (memory instanceof ArrayMemory) {
            ArrayMemory array = (ArrayMemory) memory;

            writePushConstInt(array.size());

            if (!array.isList()) {
                writeSysStaticCall(ArrayMemory.class, "createHashed", ArrayMemory.class, int.class);
            } else {
                writeSysStaticCall(ArrayMemory.class, "createListed", ArrayMemory.class, int.class);
            }

            ForeachIterator foreachIterator = ((ArrayMemory) memory).foreachIterator(false, false);
            while (foreachIterator.next()) {
                writePushDup();
                if (array.isList()) {
                    writePushMemory(foreachIterator.getValue());
                    writePopBoxing();
                    writeSysDynamicCall(ArrayMemory.class, "add", ReferenceMemory.class, Memory.class);
                } else {
                    Memory key = foreachIterator.getMemoryKey();
                    writePushMemory(key);

                    if (!key.isString()) {
                        writePopBoxing();
                    }

                    writePushMemory(foreachIterator.getValue());
                    writePopBoxing();

                    writeSysDynamicCall(ArrayMemory.class, "put", ReferenceMemory.class, Object.class, Memory.class);
                }

                writePopAll(1);
            }
            /*stackPop();
            stackPush(memory);*/

            setStackPeekAsImmutable();
            return;
        } else {
            switch (memory.type) {
                case INT: {
                    code.add(new LdcInsnNode(memory.toLong()));
                    type = Memory.Type.INT;
                }
                break;
                case DOUBLE: {
                    code.add(new LdcInsnNode(memory.toDouble()));
                    type = Memory.Type.DOUBLE;
                }
                break;
                case STRING: {
                    code.add(new LdcInsnNode(memory.toString()));
                    type = Memory.Type.STRING;
                }
                break;
            }
        }

        stackPush(type);
        setStackPeekAsImmutable();
    }

    public boolean writePopBoxing(boolean asImmutable) {
        return writePopBoxing(stackPeek().type, asImmutable);
    }

    public boolean writePopBoxing(boolean asImmutable, boolean useConstants) {
        return writePopBoxing(stackPeek().type, asImmutable, useConstants);
    }

    public boolean writePopBoxing() {
        return writePopBoxing(false);
    }

    public boolean writePopBoxing(Class<?> clazz, boolean asImmutable) {
        return writePopBoxing(StackItem.Type.valueOf(clazz), asImmutable);
    }

    public boolean writePopBoxing(Class<?> clazz) {
        return writePopBoxing(clazz, false);
    }

    public boolean writePopBoxing(StackItem.Type type) {
        return writePopBoxing(type, false);
    }

    public boolean writePopBoxing(StackItem.Type type, boolean asImmutable) {
        return writePopBoxing(type, asImmutable, true);
    }

    public boolean writePopBoxing(StackItem.Type type, boolean asImmutable, boolean useConstants) {
        switch (type) {
            case BOOL:
                /*if (useConstants && code.getLast() instanceof InsnNode) {
                    InsnNode node = (InsnNode) code.getLast();

                    StackItem stackPeek = stackPeek();

                    if (stackPeek.type == StackItem.Type.BOOL) {
                        switch (node.getOpcode()) {
                            case ICONST_0:
                            case ICONST_1:
                                code.remove(node);

                                stackPop();
                                writePushBooleanAsMemory(node.getOpcode() != ICONST_0);
                                break;
                            default:
                                writeSysStaticCall(TrueMemory.class, "valueOf", Memory.class, type.toClass());
                        }

                        break;
                    }
                }*/

                writeSysStaticCall(TrueMemory.class, "valueOf", Memory.class, type.toClass());
                setStackPeekAsImmutable();
                return true;
            case SHORT:
            case BYTE:
            case LONG:
            case INT: {
                if (useConstants && code.getLast() instanceof LdcInsnNode) {
                    LdcInsnNode node = (LdcInsnNode) code.getLast();
                    code.remove(node);

                    stackPop();

                    writePushConstantMemory(LongMemory.valueOf((Long) node.cst));
                } else {
                    writeSysStaticCall(LongMemory.class, "valueOf", Memory.class, type.toClass());
                }

                setStackPeekAsImmutable();
            }
            return true;
            case FLOAT:
            case DOUBLE: {
                if (useConstants && code.getLast() instanceof LdcInsnNode) {
                    LdcInsnNode node = (LdcInsnNode) code.getLast();
                    code.remove(node);

                    stackPop();

                    if (type == StackItem.Type.FLOAT) {
                        writePushConstantMemory(DoubleMemory.valueOf((Float) node.cst));
                    } else {
                        writePushConstantMemory(DoubleMemory.valueOf((Double) node.cst));
                    }
                } else {
                    writeSysStaticCall(DoubleMemory.class, "valueOf", Memory.class, type.toClass());
                }

                setStackPeekAsImmutable();
                return true;
            }
            case STRING: {
                if (useConstants && code.getLast() instanceof LdcInsnNode) {
                    LdcInsnNode node = (LdcInsnNode) code.getLast();
                    code.remove(node);

                    stackPop();

                    writePushConstantMemory(StringMemory.valueOf(node.cst.toString()));
                } else {
                    writeSysStaticCall(StringMemory.class, "valueOf", Memory.class, String.class);
                }

                setStackPeekAsImmutable();
                return true;
            }
            case REFERENCE: {
                if (asImmutable) {
                    writePopImmutable();
                }
            }
            break;
        }
        return false;
    }

    public void writePushSmallInt(int value) {
        switch (value) {
            case -1:
                code.add(new InsnNode(ICONST_M1));
                break;
            case 0:
                code.add(new InsnNode(ICONST_0));
                break;
            case 1:
                code.add(new InsnNode(ICONST_1));
                break;
            case 2:
                code.add(new InsnNode(ICONST_2));
                break;
            case 3:
                code.add(new InsnNode(ICONST_3));
                break;
            case 4:
                code.add(new InsnNode(ICONST_4));
                break;
            case 5:
                code.add(new InsnNode(ICONST_5));
                break;
            default:
                code.add(new LdcInsnNode(value));
        }

        stackPush(Memory.Type.INT);
    }

    public void writePushScalarBoolean(boolean value) {
        writePushSmallInt(value ? 1 : 0);
        stackPop();
        stackPush(value ? Memory.TRUE : Memory.FALSE);
    }

    public void writePushString(String value) {
        writePushMemory(new StringMemory(value));
    }

    public void writePushConstString(String value) {
        writePushString(value);
    }

    public void writePushConstDouble(double value) {
        code.add(new LdcInsnNode(value));
        stackPush(null, StackItem.Type.DOUBLE);
    }

    public void writePushConstFloat(float value) {
        code.add(new LdcInsnNode(value));
        stackPush(null, StackItem.Type.FLOAT);
    }

    public void writePushConstLong(long value) {
        code.add(new LdcInsnNode(value));
        stackPush(null, StackItem.Type.LONG);
    }

    public void writePushConstInt(int value) {
        writePushSmallInt(value);
    }

    public void writePushConstShort(short value) {
        writePushConstInt(value);
        stackPop();
        stackPush(null, StackItem.Type.SHORT);
    }

    public void writePushConstByte(byte value) {
        writePushConstInt(value);
        stackPop();
        stackPush(null, StackItem.Type.BYTE);
    }

    public void writePushConstBoolean(boolean value) {
        writePushConstInt(value ? 1 : 0);
        stackPop();
        stackPush(null, StackItem.Type.BOOL);
    }

    public void writePushSelf(boolean withLower) {
        if (method.clazz.entity.isTrait()) {
            if (method.getLocalVariable("~class_name") != null) {
                writeVarLoad("~class_name");
            } else {
                writePushEnv();
                writeSysDynamicCall(Environment.class, "__getMacroClass", Memory.class);
            }
            writePopString();
            if (withLower)
                writePushDupLowerCase();
        } else {
            writePushConstString(method.clazz.statement.getFulledName());
            if (withLower)
                writePushConstString(method.clazz.statement.getFulledName().toLowerCase());
        }
    }

    public void writePushStatic() {
        writePushEnv();
        writeSysDynamicCall(Environment.class, "getLateStatic", String.class);
    }

    public void writePushParent(Token token) {
        writePushEnv();
        writePushTraceInfo(token);

        if (method.getLocalVariable("~class_name") != null) {
            writeVarLoad("~class_name");
            writeSysDynamicCall(Environment.class, "__getParent", String.class, TraceInfo.class, String.class);
        } else {
            writeSysDynamicCall(Environment.class, "__getParent", String.class, TraceInfo.class);
        }
    }

    public void writePushLocal() {
        writeVarLoad("~local");
    }

    public void writePushEnv() {
        method.entity.setUsesStackTrace(true);

        LocalVariable variable = method.getLocalVariable("~env");
        if (variable == null) {
            if (!methodStatement.isStatic())
                writePushEnvFromSelf();
            else
                throw new RuntimeException("Cannot find `~env` variable");
            return;
        }

        stackPush(Memory.Type.REFERENCE);
        makeVarLoad(variable);
    }

    public void writePushEnvFromSelf() {
        method.entity.setUsesStackTrace(true);

        writeVarLoad("~this");
        writeSysDynamicCall(null, "getEnvironment", Environment.class);
    }

    public void writePushDup(StackItem.Type type) {
        stackPush(null, type);
        if (type.size() == 2)
            code.add(new InsnNode(DUP2));
        else
            code.add(new InsnNode(DUP));
    }

    public void writePushDup() {
        StackItem item = method.peek();
        stackPush(item);

        if (item.type.size() == 2)
            code.add(new InsnNode(DUP2));
        else
            code.add(new InsnNode(DUP));
    }

    public void writePushDupLowerCase() {
        writePushDup();
        writePopString();
        writeSysDynamicCall(String.class, "toLowerCase", String.class);
    }

    public void writePushNull() {
        writePushMemory(Memory.NULL);
    }

    public void writePushVoid() {
        writePushMemory(Memory.UNDEFINED);
    }

    public void writePushConstNull() {
        stackPush(Memory.Type.REFERENCE);
        code.add(new InsnNode(ACONST_NULL));
    }

    public void writePushNewObject(Class clazz) {
        code.add(new TypeInsnNode(NEW, Type.getInternalName(clazz)));
        stackPush(Memory.Type.REFERENCE);
        writePushDup();

        writeSysCall(clazz, INVOKESPECIAL, Constants.INIT_METHOD, void.class);
        stackPop();
    }

    public void writePushNewObject(String internalName, Class... paramClasses) {
        code.add(new TypeInsnNode(NEW, internalName));
        stackPush(Memory.Type.REFERENCE);
        writePushDup();
        writeSysCall(internalName, INVOKESPECIAL, Constants.INIT_METHOD, void.class, paramClasses);
        stackPop();
    }

    public void writePushStaticCall(Method method) {
        writeSysStaticCall(
                method.getDeclaringClass(),
                method.getName(),
                method.getReturnType(),
                method.getParameterTypes()
        );
    }

    Memory writePushCompileFunction(CallExprToken function, CompileFunction compileFunction, boolean returnValue,
                                    boolean writeOpcode, PushCallStatistic statistic) {
        CompileFunction.Method method = compileFunction.find(function.getParameters().size());
        if (method == null) {
            if (!writeOpcode)
                return Memory.NULL;

            writeWarning(function, Messages.ERR_EXPECT_LEAST_PARAMS.fetch(
                    compileFunction.name, compileFunction.getMinArgs(), function.getParameters().size()
            ));
            if (returnValue)
                writePushNull();

            return null;
        } else if (!method.isVarArg() && method.argsCount < function.getParameters().size()) {
            if (!writeOpcode)
                return Memory.NULL;

            writeWarning(function, Messages.ERR_EXPECT_EXACTLY_PARAMS.fetch(
                    compileFunction.name, method.argsCount, function.getParameters().size()
            ));
            if (returnValue)
                writePushNull();

            return null;
        }

        if (statistic != null)
            statistic.returnType = StackItem.Type.valueOf(method.resultType);

        Class[] types = method.parameterTypes;
        ListIterator<ExprStmtToken> iterator = function.getParameters().listIterator();

        Object[] arguments = new Object[types.length];
        int j = 0;
        boolean immutable = method.isImmutable;

        boolean init = false;
        for (int i = 0; i < method.parameterTypes.length; i++) {
            Class<?> argType = method.parameterTypes[i];
            boolean isRef = method.references[i];
            boolean isMutable = method.isPresentAnnotationOfParam(i, Runtime.MutableValue.class);

            if (method.isPresentAnnotationOfParam(i, Runtime.GetLocals.class)) {
                if (!writeOpcode)
                    return null;

                immutable = false;
                writePushLocal();
            } else if (argType == Environment.class) {
                if (immutable) {
                    arguments[i] = compiler.getEnvironment();
                } else {
                    if (!writeOpcode)
                        return null;
                    writePushEnv();
                }
            } else if (argType == TraceInfo.class) {
                if (immutable) {
                    arguments[i] = function.toTraceInfo(compiler.getContext());
                } else {
                    if (!writeOpcode)
                        return null;

                    writePushTraceInfo(function);
                }
            } else {
                if (argType == Memory[].class) {
                    Memory[] args = new Memory[function.getParameters().size() - j];
                    boolean arrCreated = false;
                    for (int t = 0; t < function.getParameters().size() - j; t++) {
                        ExprStmtToken next = iterator.next();
                        if (immutable)
                            args[t] = writeExpression(next, true, true, false);

                        if (args[t] == null) {
                            if (!writeOpcode)
                                return null;

                            if (!arrCreated) {
                                if (immutable) {
                                    for (int n = 0; n < i /*j*/; n++) {
                                        if (arguments[n] instanceof TraceInfo) {
                                            writePushTraceInfo(function);
                                        } else if (arguments[n] instanceof Environment) {
                                            writePushEnv();
                                        } else {
                                            writePushMemory((Memory) arguments[n]);
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

                            //if (!isRef)  BUGFIX - array is memory[] and so we need to convert value to memory
                            writePopBoxing(!isRef);

                            code.add(new InsnNode(AASTORE));
                            stackPop();
                            stackPop();
                            stackPop();

                            immutable = false;
                        }
                    }

                    if (!immutable && !arrCreated) {
                        code.add(new InsnNode(ACONST_NULL));
                        stackPush(Memory.Type.REFERENCE);
                    }

                    arguments[i/*j*/] = MemoryUtils.valueOf(args);
                } else {
                    ExprStmtToken next = iterator.next();
                    if (!immutable && !init) {
                        init = true;
                        for (int k = 0; k < i/*j*/; k++) {
                            if (arguments[k] != null) {
                                if (arguments[k] instanceof TraceInfo) {
                                    writePushTraceInfo(function);
                                } else if (arguments[k] instanceof Environment) {
                                    writePushEnv();
                                } else {
                                    writePushMemory((Memory) arguments[k]);
                                    writePop(types[k], true, isRef);
                                    arguments[k] = null;
                                }
                            }
                        }
                    }

                    if (immutable)
                        arguments[i/*j*/] = writeExpression(next, true, true, false);

                    if (arguments[i/*j*/] == null) {
                        if (!writeOpcode)
                            return null;

                        if (!init) {
                            for (int n = 0; n < i/*j*/; n++) {
                                if (arguments[n] instanceof TraceInfo) {
                                    writePushTraceInfo(function);
                                } else if (arguments[n] instanceof Environment) {
                                    writePushEnv();
                                } else {
                                    writePushMemory((Memory) arguments[n]);
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

                        writePop(argType, true, isMutable && !isRef);
                        if (!isMutable && !isRef && stackPeek().type.isReference()) {
                            writeSysDynamicCall(Memory.class, "toValue", Memory.class);
                        }

                        immutable = false;
                    }
                    j++;
                }
            }
        }

        if (immutable) {
            if (!returnValue)
                return null;

            Object[] typedArguments = new Object[arguments.length];
            for (int i = 0; i < arguments.length; i++) {
                if (method.parameterTypes[i] != Memory.class && arguments[i] instanceof Memory)
                    typedArguments[i] = method.converters[i].run((Memory) arguments[i]);
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
        if (returnValue) {
            if (method.resultType == void.class)
                writePushNull();
            setStackPeekAsImmutable();
        }
        return null;
    }

    public void writePushParameters(Collection<Memory> memories) {
        writePushSmallInt(memories.size());
        code.add(new TypeInsnNode(ANEWARRAY, Type.getInternalName(Memory.class)));
        stackPop();
        stackPush(Memory.Type.REFERENCE);

        int i = 0;
        for (Memory param : memories) {
            writePushDup();
            writePushSmallInt(i);

            writePushMemory(param);
            writePopBoxing(true, false);

            code.add(new InsnNode(AASTORE));
            stackPop();
            stackPop();
            stackPop();
            i++;
        }
    }

    public void writePushParameters(Collection<ExprStmtToken> parameters, Memory... additional) {
        writePushParameters(parameters, true, additional);
    }

    public void writePushParameters(Collection<ExprStmtToken> parameters, boolean useConstants, Memory... additional) {
        if (parameters.isEmpty()) {
            code.add(new InsnNode(ACONST_NULL));
            stackPush(Memory.Type.REFERENCE);
            return;
        }

        if (useConstants && additional == null || additional.length == 0) {
            List<Memory> constantParameters = new ArrayList<Memory>();

            for (ExprStmtToken param : parameters) {
                Memory paramMemory = writeExpression(param, true, true, false);
                if (paramMemory == null || paramMemory.isArray()) { // skip arrays.
                    break;
                }

                constantParameters.add(paramMemory);
            }

            if (constantParameters.size() == parameters.size()) {
                writePushConstantMemoryArray(constantParameters);
                return;
            }
        }

        writePushSmallInt(parameters.size() + (additional == null ? 0 : additional.length));
        code.add(new TypeInsnNode(ANEWARRAY, Type.getInternalName(Memory.class)));
        stackPop();
        stackPush(Memory.Type.REFERENCE);

        int i = 0;
        for (ExprStmtToken param : parameters) {
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

        if (additional != null) {
            for (Memory m : additional) {
                writePushDup();
                writePushSmallInt(i);
                writePushMemory(m);
                writePopBoxing();

                code.add(new InsnNode(AASTORE));
                stackPop();
                stackPop();
                stackPop();
                i++;
            }
        }
    }

    Memory writePushParentDynamicMethod(CallExprToken function, boolean returnValue, boolean writeOpcode,
                                        PushCallStatistic statistic) {
        if (!writeOpcode)
            return null;

        StaticAccessExprToken dynamic = (StaticAccessExprToken) function.getName();
        writeLineNumber(function);

        writePushThis();
        if (dynamic.getField() != null) {
            if (dynamic.getField() instanceof NameToken) {
                String name = ((NameToken) dynamic.getField()).getName();
                writePushConstString(name);
                writePushConstString(name.toLowerCase());
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
                                  PushCallStatistic statistic) {
        if (!writeOpcode)
            return null;

        DynamicAccessExprToken access = (DynamicAccessExprToken) function.getName();
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

    boolean writePushFastStaticMethod(CallExprToken function, boolean returnValue) {
        StaticAccessExprToken access = (StaticAccessExprToken) function.getName();

        CompileClass compileClass = compiler.getEnvironment().scope.findCompileClass(access.getClazz().getWord());
        if (compileClass == null)
            return false;

        ClassEntity classEntity = compiler.getEnvironment().fetchClass(compileClass.getNativeClass());
        MethodEntity methodEntity = classEntity.findMethod(access.getField().getWord().toLowerCase());
        if (methodEntity != null && methodEntity.getNativeMethod() != null
                && methodEntity.getNativeMethod().isAnnotationPresent(Runtime.FastMethod.class)) {
            int cnt = methodEntity.getRequiredParamCount();

            if (cnt > function.getParameters().size()) {
                writeWarning(function, Messages.ERR_EXPECT_EXACTLY_PARAMS.fetch(
                        methodEntity.getClazzName() + "::" + methodEntity.getName(),
                        cnt, function.getParameters().size()
                ));
                if (returnValue)
                    writePushNull();
                return true;
            }

            List<Memory> additional = new ArrayList<Memory>();
            for (ParameterEntity param : methodEntity.getParameters()) {
                if (param.getDefaultValue() != null)
                    additional.add(param.getDefaultValue());
                else if (!additional.isEmpty())
                    throw new IllegalStateException("Arguments with default values must be located at the end");
            }

            writePushEnv();
            writePushParameters(function.getParameters(), additional.toArray(new Memory[0]));
            writeSysStaticCall(
                    compileClass.getNativeClass(),
                    methodEntity.getName(),
                    Memory.class,
                    Environment.class, Memory[].class
            );
            if (!returnValue)
                writePopAll(1);
            return true;
        }
        return false;
    }

    Memory writePushStaticMethod(CallExprToken function, boolean returnValue, boolean writeOpcode,
                                 PushCallStatistic statistic) {
        StaticAccessExprToken access = (StaticAccessExprToken) function.getName();

        if (!writeOpcode)
            return null;

        writeLineNumber(function);

        if (writePushFastStaticMethod(function, returnValue))
            return null;

        writePushEnv();
        writePushTraceInfo(function.getName());
        String methodName = null;

        ValueExprToken clazz = access.getClazz();
        if ((clazz instanceof NameToken || (clazz instanceof SelfExprToken && !method.clazz.entity.isTrait()))
                && access.getField() instanceof NameToken) {
            String className;
            if (clazz instanceof SelfExprToken)
                className = getMacros(clazz).toString();
            else
                className = ((NameToken) clazz).getName();

            writePushString(className.toLowerCase());

            methodName = ((NameToken) access.getField()).getName();
            writePushString(methodName.toLowerCase());

            writePushString(className);
            writePushString(methodName);

            writePushParameters(function.getParameters());

            if (method.clazz.statement.isTrait()) {
                writePushConstNull();
                writePushConstInt(0);
            } else {
                int cacheIndex = method.clazz.getAndIncCallMethCount();
                writeGetStatic("$CALL_METH_CACHE", MethodCallCache.class);
                writePushConstInt(cacheIndex);
            }

            writeSysStaticCall(
                    InvokeHelper.class, "callStatic", Memory.class,
                    Environment.class, TraceInfo.class,
                    String.class, String.class, // lower sign name
                    String.class, String.class, // origin names
                    Memory[].class,
                    MethodCallCache.class, Integer.TYPE
            );
        } else {
            if (clazz instanceof NameToken) {
                writePushString(((NameToken) clazz).getName());
                writePushDupLowerCase();
            } else if (clazz instanceof SelfExprToken) {
                writePushSelf(true);
            } else if (clazz instanceof StaticExprToken) {
                writePushStatic();
                writePushDupLowerCase();
            } else {
                writePush(clazz, true, false);
                writePopString();
                writePushDupLowerCase();
            }

            if (access.getField() != null) {
                ValueExprToken field = access.getField();
                if (field instanceof NameToken) {
                    writePushString(((NameToken) field).getName());
                    writePushString(((NameToken) field).getName().toLowerCase());
                } else if (field instanceof ClassExprToken) {
                    unexpectedToken(field);
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

            if (clazz instanceof StaticExprToken || method.clazz.statement.isTrait()
                    || clazz instanceof VariableExprToken) {
                writePushConstNull();
                writePushConstInt(0);
            } else {
                int cacheIndex = method.clazz.getAndIncCallMethCount();
                writeGetStatic("$CALL_METH_CACHE", MethodCallCache.class);
                writePushConstInt(cacheIndex);
            }

            writeSysStaticCall(
                    InvokeHelper.class, "callStaticDynamic", Memory.class,
                    Environment.class, TraceInfo.class,
                    String.class, String.class,
                    String.class, String.class,
                    Memory[].class,
                    MethodCallCache.class, Integer.TYPE
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
                         PushCallStatistic statistic) {
        Token name = function.getName();
        if (name instanceof NameToken) {
            String realName = ((NameToken) name).getName();
            CompileFunction compileFunction = compiler.getScope().findCompileFunction(realName);

            // try find system function, like max, sin, cos, etc.
            if (compileFunction == null
                    && name instanceof FulledNameToken
                    && compiler.getEnvironment().fetchFunction(realName) == null
                    && compiler.findFunction(realName) == null) {
                String tryName = ((FulledNameToken) name).getLastName().getName();
                compileFunction = compiler.getScope().findCompileFunction(tryName);
            }

            if (compileFunction != null) {
                return writePushCompileFunction(function, compileFunction, returnValue, writeOpcode, statistic);
            } else {
                if (!writeOpcode)
                    return null;
                method.entity.setImmutable(false);
                int index = method.clazz.getAndIncCallFuncCount();

                writePushEnv();
                writePushTraceInfo(function);
                writePushString(realName.toLowerCase());
                writePushString(realName);
                writePushParameters(function.getParameters());

                writeGetStatic("$CALL_FUNC_CACHE", FunctionCallCache.class);
                writePushConstInt(index);

                writeSysStaticCall(
                        InvokeHelper.class, "call", Memory.class,
                        Environment.class, TraceInfo.class, String.class, String.class, Memory[].class,
                        FunctionCallCache.class, Integer.TYPE
                );
                if (!returnValue)
                    writePopAll(1);
            }
        } else if (name instanceof StaticAccessExprToken) {
            method.entity.setImmutable(false);
            if (((StaticAccessExprToken) name).isAsParent())
                return writePushParentDynamicMethod(function, returnValue, writeOpcode, statistic);
            else
                return writePushStaticMethod(function, returnValue, writeOpcode, statistic);
        } else if (name instanceof DynamicAccessExprToken) {
            method.entity.setImmutable(false);
            return writePushDynamicMethod(function, returnValue, writeOpcode, statistic);
        } else {
            if (!writeOpcode)
                return null;
            method.entity.setImmutable(false);
            writeLineNumber(function);

            writePush((ValueExprToken) function.getName(), true, false);
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

    public void writeDefineVariables(Collection<VariableExprToken> values) {
        for (VariableExprToken value : values)
            writeDefineVariable(value);
    }

    public void writeUndefineVariables(Collection<VariableExprToken> values) {
        LabelNode end = new LabelNode();
        for (VariableExprToken value : values)
            writeUndefineVariable(value, end);
    }

    public void writeDefineGlobalVar(String name) {
        writePushEnv();
        writePushConstString(name);
        writeSysDynamicCall(Environment.class, "getOrCreateGlobal", Memory.class, String.class);
        makeVarStore(method.getLocalVariable(name));
        stackPop();
    }

    public void writePushThis() {
        if (method.clazz.isClosure() || method.getGeneratorEntity() != null) {
            writeVarLoad("~this");
            writeGetDynamic("self", Memory.class);
        } else {
            if (method.getLocalVariable("this") == null) {
                if (!methodStatement.isStatic()) {
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

    protected void writeDefineThis(LocalVariable variable) {
        if (method.clazz.isClosure() || method.getGeneratorEntity() != null) {
            writeVarLoad("~this");
            writeGetDynamic("self", Memory.class);
            makeVarStore(variable);
            stackPop();
        } else {
            LabelNode endLabel = new LabelNode();
            LabelNode elseLabel = new LabelNode();

            if (methodStatement.isStatic()) {
                writePushMemory(Memory.UNDEFINED);
                writeVarStore(variable, false, false);
            } else {
                writeVarLoad("~this");
                writeSysDynamicCall(IObject.class, "isMock", Boolean.TYPE);

                code.add(new JumpInsnNode(IFEQ, elseLabel));
                stackPop();

                if (variable.isReference()) {
                    writePushMemory(Memory.UNDEFINED);
                    writeSysStaticCall(ReferenceMemory.class, "valueOf", Memory.class, Memory.class);
                } else {
                    writePushMemory(Memory.UNDEFINED);
                }
                writeVarStore(variable, false, false);

                code.add(new JumpInsnNode(GOTO, endLabel));
                code.add(elseLabel);

                writeVarLoad("~this");
                writeSysStaticCall(ObjectMemory.class, "valueOf", Memory.class, IObject.class);
                makeVarStore(variable);
                stackPop();

                code.add(endLabel);
            }
        }

        variable.pushLevel();
        variable.setValue(null);
        variable.setImmutable(false);
        variable.setReference(true);
    }

    protected void writeDefineVariable(VariableExprToken value) {
        if (methodStatement.isUnusedVariable(value))
            return;

        LocalVariable variable = method.getLocalVariable(value.getName());
        if (variable == null) {
            LabelNode label = writeLabel(node, value.getMeta().getStartLine());
            variable = method.addLocalVariable(value.getName(), label, Memory.class);

            if (methodStatement.isReference(value) || compiler.getScope().superGlobals.contains(value.getName())) {
                variable.setReference(true);
            } else {
                variable.setReference(false);
            }

            if (variable.name.equals("this") && method.getLocalVariable("~this") != null) { // $this
                writeDefineThis(variable);
            } else if (compiler.getScope().superGlobals.contains(value.getName())) { // super-globals
                writeDefineGlobalVar(value.getName());
            } else if (methodStatement.isDynamicLocal()) { // ref-local variables
                writePushLocal();
                writePushConstString(value.getName());
                writeSysDynamicCall(Memory.class, "refOfIndex", Memory.class, String.class);
                makeVarStore(variable);
                stackPop();

                variable.pushLevel();
                variable.setValue(null);
                variable.setReference(true);
            } else { // simple local variables
                if (variable.isReference()) {
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

    protected void writeUndefineVariable(VariableExprToken value, LabelNode end) {
        LocalVariable variable = method.getLocalVariable(value.getName());
        if (variable != null)
            variable.popLevel();
    }

    public void writePushVariable(VariableExprToken value) {
        LocalVariable variable = method.getLocalVariable(value.getName());
        if (variable == null || variable.getClazz() == null)
            writePushNull();
        else {
            writeVarLoad(variable);
        }
    }

    public Memory tryWritePushVariable(VariableExprToken value, boolean heavyObjects) {
        if (methodStatement.isUnstableVariable(value))
            return null;

        if (compiler.getScope().superGlobals.contains(value.getName()))
            return null;

        LocalVariable variable = method.getLocalVariable(value.getName());
        if (variable == null || variable.getClazz() == null || methodStatement.isUnusedVariable(value)) {
            return Memory.NULL;
        } else {
            if (methodStatement.variable(value).isPassed())
                return null;

            Memory mem = variable.getValue();
            if (mem != null) {
                if (!heavyObjects) {
                    if (mem.isArray() || mem.toString().length() > 100) {
                        return null;
                    }
                }
                return mem;
            }
        }
        return null;
    }

    public Memory writePushArray(ArrayExprToken array, boolean returnMemory, boolean writeOpcode) {
        if (array.getParameters().isEmpty()) {
            if (returnMemory)
                return new ArrayMemory();
            else if (writeOpcode)
                writeSysStaticCall(ArrayMemory.class, "valueOf", Memory.class);
        } else {
            ArrayMemory ret = returnMemory ? new ArrayMemory() : null;

            if (ret == null) {
                boolean map = false;

                for (ExprStmtToken token : array.getParameters()) {
                    for (Token sub : token.getTokens()) {
                        if (sub instanceof KeyValueExprToken) {
                            map = true;
                            break;
                        }
                    }

                    if (map) break;
                }

                writePushConstInt(array.getParameters().size());
                if (map) {
                    writeSysStaticCall(ArrayMemory.class, "createHashed", ArrayMemory.class, int.class);
                } else {
                    writeSysStaticCall(ArrayMemory.class, "createListed", ArrayMemory.class, int.class);
                }
            }

            for (ExprStmtToken param : array.getParameters()) {
                if (ret == null)
                    writePushDup();

                Memory result = writeExpression(param, true, true, ret == null);

                if (result != null) {
                    if (ret != null) {
                        ret.add(result);
                        continue;
                    } else {
                        writePushMemory(result);
                    }
                } else {
                    if (!writeOpcode) {
                        return null;
                    }

                    ret = null;
                }

                if (result == null && returnMemory) {
                    return writePushArray(array, false, writeOpcode);
                }

                writePopBoxing();
                writePopImmutable();
                writeSysDynamicCall(ArrayMemory.class, "add", ReferenceMemory.class, Memory.class);
                writePopAll(1);
            }

            if (ret != null)
                return ret;
        }
        setStackPeekAsImmutable();
        return null;
    }

    public int writePushTraceInfo(Token token) {
        return writePushTraceInfo(token.getMeta().getStartLine(), token.getMeta().getStartPosition());
    }

    void writePushGetFromArray(int index, Class clazz) {
        writePushSmallInt(index);
        code.add(new InsnNode(AALOAD));
        stackPop();
        stackPop();
        stackPush(null, StackItem.Type.valueOf(clazz));
    }

    void writePushGetArrayLength() {
        code.add(new InsnNode(ARRAYLENGTH));
        stackPop();
        stackPush(null, StackItem.Type.INT);
    }

    int createTraceInfo(Token token) {
        return method.clazz.addTraceInfo(token);
    }

    int writePushTraceInfo(int line, int position) {
        int index = method.clazz.addTraceInfo(line, position);
        writePushTraceInfo(index);
        return index;
    }

    void writePushTraceInfo(int traceIndex) {
        writeGetStatic("$TRC", TraceInfo[].class);
        writePushGetFromArray(traceIndex, TraceInfo.class);
    }

    void writePushCreateTraceInfo(int line, int position) {
        writeGetStatic("$FN", String.class);
        writePushMemory(LongMemory.valueOf(line));
        writePushMemory(LongMemory.valueOf(position));
        writeSysStaticCall(TraceInfo.class, "valueOf", TraceInfo.class, String.class, Long.TYPE, Long.TYPE);
    }

    int writePushConstantMemory(Memory memory) {
        int index = method.clazz.addMemoryConstant(memory);

        writeGetStatic("$MEM", Memory[].class);
        writePushGetFromArray(index, Memory.class);

        return index;
    }

    int writePushConstantMemoryArray(Collection<Memory> memories) {
        int index = method.clazz.addMemoryArray(memories);

        writeGetStatic("$AMEM", Memory[][].class);
        writePushGetFromArray(index, Memory[].class);

        return index;
    }

    Memory tryWritePushMacro(MacroToken macro, boolean writeOpcode) {
        if (macro instanceof LineMacroToken) {
            return LongMemory.valueOf(macro.getMeta().getStartLine() + 1);
        } else if (macro instanceof FileMacroToken) {
            return new StringMemory(compiler.getSourceFile());
        } else if (macro instanceof DirMacroToken) {
            String sourceFile = compiler.getSourceFile();
            String parent = new File(sourceFile).getParent();

            // Fix issue #198.
            if (sourceFile.startsWith(parent + "//") && parent.endsWith(":")) {
                parent += "//";
            }

            return new StringMemory(parent);
        } else if (macro instanceof FunctionMacroToken) {
            if (method.clazz == null) {
                return Memory.CONST_EMPTY_STRING;
            }

            if (method.clazz.isClosure()) {
                return new StringMemory("{closure}");
            }

            if (StringUtils.isEmpty(method.clazz.getFunctionName()))
                return method.clazz.isSystem()
                        ? Memory.CONST_EMPTY_STRING
                        : method.getRealName() == null
                        ? Memory.CONST_EMPTY_STRING : new StringMemory(method.getRealName());
            else {
                return method.clazz.getFunctionName() == null
                        ? Memory.CONST_EMPTY_STRING
                        : new StringMemory(method.clazz.getFunctionName());
            }

        } else if (macro instanceof MethodMacroToken) {
            if (method.clazz == null) {
                return Memory.CONST_EMPTY_STRING;
            }

            if (method.clazz.isClosure()) {
                return new StringMemory("{closure}");
            }

            if (method.clazz.isSystem())
                return method.clazz.getFunctionName() != null
                        ? new StringMemory(method.clazz.getFunctionName()) : Memory.NULL;
            else
                return new StringMemory(
                        method.clazz.entity.getName()
                                + (method.getRealName() == null ? "" : "::" + method.getRealName())
                );

        } else if (macro instanceof ClassMacroToken) {
            if (method.clazz.entity.isTrait()) {
                if (writeOpcode) {
                    writePushEnv();
                    writeSysDynamicCall(Environment.class, "__getMacroClass", Memory.class);
                }
                return null;
            } else {
                if (method.clazz.getClassContext() != null) {
                    return new StringMemory(method.clazz.getClassContext().getFulledName());
                } else {
                    return new StringMemory(method.clazz.isSystem() ? "" : method.clazz.entity.getName());
                }
            }
        } else if (macro instanceof NamespaceMacroToken) {
            return new StringMemory(
                    compiler.getNamespace() == null || compiler.getNamespace().getName() == null
                            ? ""
                            : compiler.getNamespace().getName().getName()
            );
        } else if (macro instanceof TraitMacroToken) {
            if (method.clazz.entity.isTrait())
                return new StringMemory(method.clazz.entity.getName());
            else
                return Memory.CONST_EMPTY_STRING;
        } else
            throw new IllegalArgumentException("Unsupported macro value: " + macro.getWord());
    }

    Memory writePushName(NameToken token, boolean returnMemory, boolean writeOpcode) {
        CompileConstant constant = compiler.getScope().findCompileConstant(token.getName());
        if (constant != null) {
            if (returnMemory)
                return constant.value;
            else if (writeOpcode)
                writePushMemory(constant.value);
        } else {
            ConstantEntity constantEntity = compiler.findConstant(token.getName());
            /*if (constantEntity == null)   // TODO: maybe it's not needed! we should search a namespaced constant in local context
                constantEntity = compiler.getScope().findUserConstant(token.getName()); */

            if (constantEntity != null) {
                if (returnMemory)
                    return constantEntity.getValue();
                else if (writeOpcode) {
                    writePushMemory(constantEntity.getValue());
                    setStackPeekAsImmutable();
                }
            } else {
                if (!writeOpcode)
                    return null;
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

    Memory tryWritePushReference(StackItem item, boolean writeOpcode, boolean toStack, boolean heavyObjects) {
        if (item.getToken() instanceof VariableExprToken) {
            writePushVariable((VariableExprToken) item.getToken());
            return null;
        }

        return tryWritePush(item, writeOpcode, toStack, heavyObjects);
    }

    Memory tryWritePush(StackItem item, boolean writeOpcode, boolean toStack, boolean heavyObjects) {
        if (item.getToken() != null)
            return tryWritePush(item.getToken(), true, writeOpcode, heavyObjects);
        else if (item.getMemory() != null) {
            return item.getMemory();
        } else if (toStack)
            stackPush(null, item.type);
        return null;
    }

    StackItem.Type tryGetType(StackItem item) {
        if (item.getToken() != null) {
            return tryGetType(item.getToken());
        } else if (item.getMemory() != null) {
            return StackItem.Type.valueOf(item.getMemory().type);
        } else
            return item.type;
    }

    public boolean tryIsImmutable(StackItem item) {
        if (item.getToken() != null) {
            return tryIsImmutable(item.getToken());
        } else if (item.getMemory() != null)
            return true;
        else
            return item.type.isConstant();
    }

    public Memory tryWritePush(StackItem item) {
        return tryWritePush(item, true, true, true);
    }

    public void writePush(StackItem item) {
        writePush(item, true, false);
    }

    public void writePush(StackItem item, boolean tryGetMemory, boolean heavyObjects) {
        if (tryGetMemory) {
            Memory memory = tryWritePushReference(item, true, true, heavyObjects);
            if (memory != null)
                writePushMemory(memory);
        } else {
            tryWritePushReference(item, true, true, heavyObjects);
        }
    }

    public void writePush(StackItem item, StackItem.Type castType) {
        Memory memory = tryWritePush(item);
        if (memory != null) {
            switch (castType) {
                case DOUBLE:
                    writePushConstDouble(memory.toDouble());
                    return;
                case FLOAT:
                    writePushConstFloat((float) memory.toDouble());
                    return;
                case LONG:
                    writePushConstLong(memory.toLong());
                    return;
                case INT:
                    writePushConstInt((int) memory.toLong());
                    return;
                case SHORT:
                    writePushConstInt((short) memory.toLong());
                    return;
                case BYTE:
                    writePushConstByte((byte) memory.toLong());
                    return;
                case BOOL:
                    writePushConstBoolean(memory.toBoolean());
                    return;
                case STRING:
                    writePushConstString(memory.toString());
                    return;
            }
            writePushMemory(memory);
        }
        writePop(castType.toClass(), true, true);
    }

    boolean tryIsImmutable(ValueExprToken value) {
        if (value instanceof IntegerExprToken)
            return true;
        else if (value instanceof DoubleExprToken)
            return true;
        else if (value instanceof StringExprToken)
            return true;
        else if (value instanceof LineMacroToken)
            return true;
        else if (value instanceof MacroToken)
            return true;
        else if (value instanceof ArrayExprToken)
            return true;
        else if (value instanceof StringBuilderExprToken)
            return true;
        else if (value instanceof NameToken)
            return true;
        else if (value instanceof CallExprToken) {
            PushCallStatistic statistic = new PushCallStatistic();
            writePushCall((CallExprToken) value, true, false, statistic);
            return statistic.returnType.isConstant();
        }

        return false;
    }

    StackItem.Type tryGetType(ValueExprToken value) {
        if (value instanceof IntegerExprToken)
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
            writePushCall((CallExprToken) value, true, false, statistic);
            return statistic.returnType;
        } else if (value instanceof NameToken) {
            Memory tmpMemory = writePushName((NameToken) value, true, false);
            return tmpMemory == null ? StackItem.Type.REFERENCE : StackItem.Type.valueOf(tmpMemory.type);
        } else
            return StackItem.Type.REFERENCE;
    }

    Memory tryWritePush(ValueExprToken value, boolean returnValue, boolean writeOpcode, boolean heavyObjects) {
        if (writeOpcode) {
            BaseStatementCompiler compiler = getCompiler(value.getClass());
            if (compiler != null) {
                if (compiler instanceof BaseExprCompiler) {
                    ((BaseExprCompiler) compiler).write(value, returnValue);
                } else
                    throw new IllegalStateException("Compiler must be extended by " + BaseExprCompiler.class.getName());

                return null;
            }
        }

        if (value instanceof NameToken) {
            return writePushName((NameToken) value, returnValue, writeOpcode);
        }
        if (value instanceof ArrayExprToken) {
            return writePushArray((ArrayExprToken) value, returnValue, writeOpcode);
        } else if (value instanceof CallExprToken) {
            return writePushCall((CallExprToken) value, returnValue, writeOpcode, null);
        } else if (value instanceof MacroToken) {
            return tryWritePushMacro((MacroToken) value, writeOpcode);
        } else if (value instanceof VariableExprToken) {
            if (writeOpcode)
                writePushVariable((VariableExprToken) value);

            /*if (returnValue)
                return tryWritePushVariable((VariableExprToken) value, heavyObjects);  */
        }

        return null;
    }

    public void writePush(ValueExprToken value, boolean returnValue, boolean heavyObjects) {
        if (value instanceof VariableExprToken) {
            writePushVariable((VariableExprToken) value);
            return;
        }

        Memory memory = tryWritePush(value, returnValue, true, heavyObjects);
        if (memory != null)
            writePushMemory(memory);
    }

    @SuppressWarnings("unchecked")
    boolean methodExists(Class clazz, String method, Class... paramClasses) {
        try {
            clazz.getDeclaredMethod(method, paramClasses);
            return true;
        } catch (java.lang.NoSuchMethodException e) {
            return false;
        }
    }

    void writeSysCall(String internalClassName, int INVOKE_TYPE, String method, Class returnClazz, Class... paramClasses) {
        Type[] args = new Type[paramClasses.length];
        if (INVOKE_TYPE == INVOKEVIRTUAL || INVOKE_TYPE == INVOKEINTERFACE)
            stackPop(); // this

        for (int i = 0; i < args.length; i++) {
            args[i] = Type.getType(paramClasses[i]);
            stackPop();
        }

        code.add(new MethodInsnNode(
                INVOKE_TYPE, internalClassName, method, Type.getMethodDescriptor(Type.getType(returnClazz), args),
                false
        ));

        if (returnClazz != void.class) {
            stackPush(null, StackItem.Type.valueOf(returnClazz));
        }
    }

    @SuppressWarnings("unchecked")
    void writeSysCall(Class clazz, int INVOKE_TYPE, String method, Class returnClazz, Class... paramClasses) {
        if (INVOKE_TYPE != INVOKESPECIAL && clazz != null) {
            if (compiler.getScope().isDebugMode()) {
                if (!methodExists(clazz, method, paramClasses)) {
                    throw new NoSuchMethodException(clazz, method, paramClasses);
                }
            }
        }

        Type[] args = new Type[paramClasses.length];
        if (INVOKE_TYPE == INVOKEVIRTUAL || INVOKE_TYPE == INVOKEINTERFACE)
            stackPop(); // this

        for (int i = 0; i < args.length; i++) {
            args[i] = Type.getType(paramClasses[i]);
            stackPop();
        }

        String owner = clazz == null ? this.method.clazz.node.name : Type.getInternalName(clazz);
        if (clazz == null && this.method.clazz.entity.isTrait())
            throw new CriticalException("[Compiler Error] Cannot use current classname in Trait");

        code.add(new MethodInsnNode(
                INVOKE_TYPE, owner, method, Type.getMethodDescriptor(Type.getType(returnClazz), args),
                clazz != null && clazz.isInterface()
        ));

        if (returnClazz != void.class) {
            stackPush(null, StackItem.Type.valueOf(returnClazz));
        }
    }

    public void writeTickTrigger(Token token) {
        writeTickTrigger(token.toTraceInfo(getCompiler().getContext()));
    }

    public void writeTickTrigger(TraceInfo trace) {
        if (compiler.getScope().isDebugMode() && method.getLocalVariable("~local") != null) {
            int line = trace.getStartLine();

            if (method.registerTickTrigger(line)) {
                writePushEnv();
                writePushTraceInfo(trace.getStartLine(), trace.getStartPosition());
                writePushLocal();
                writeSysDynamicCall(Environment.class, "__tick", void.class, TraceInfo.class, ArrayMemory.class);
            }
        }
    }

    public void writeSysDynamicCall(Class clazz, String method, Class returnClazz, Class... paramClasses)
            throws NoSuchMethodException {
        writeSysCall(
                clazz, clazz != null && clazz.isInterface() ? INVOKEINTERFACE : INVOKEVIRTUAL,
                method, returnClazz, paramClasses
        );
    }

    public void writeSysStaticCall(Class clazz, String method, Class returnClazz, Class... paramClasses)
            throws NoSuchMethodException {
        writeSysCall(clazz, INVOKESTATIC, method, returnClazz, paramClasses);
    }

    public void writePopImmutable() {
        if (!stackPeek().immutable) {
            writeSysDynamicCall(Memory.class, "toImmutable", Memory.class);
            setStackPeekAsImmutable();
        }
    }

    public void writeVarStore(LocalVariable variable, boolean returned, boolean asImmutable) {
        writePopBoxing();

        if (asImmutable)
            writePopImmutable();

        if (returned) {
            writePushDup();
        }

        makeVarStore(variable);
        stackPop();
    }

    public void checkAssignableVar(VariableExprToken var) {
        if (method.clazz.isClosure() || !method.clazz.isSystem()) {
            if (var.getName().equals("this"))
                compiler.getEnvironment().error(var.toTraceInfo(compiler.getContext()), "Cannot re-assign $this");
        }
    }

    public void writeVarAssign(LocalVariable variable, VariableExprToken token, boolean returned, boolean asImmutable) {
        if (token != null)
            checkAssignableVar(token);

        writePopBoxing(asImmutable);

        if (variable.isReference()) {
            writeVarLoad(variable);
            writeSysStaticCall(Memory.class, "assignRight", Memory.class, Memory.class, Memory.class);
            if (!returned)
                writePopAll(1);
        } else {
            if (returned) {
                writePushDup();
            }

            makeVarStore(variable);
            stackPop();
        }
    }

    public void writeVarStore(LocalVariable variable, boolean returned) {
        writeVarStore(variable, returned, false);
    }

    public void writeVarLoad(LocalVariable variable) {
        stackPush(Memory.Type.valueOf(variable.getClazz()));
        makeVarLoad(variable);
    }

    public void writeVarLoad(String name) {
        LocalVariable local = method.getLocalVariable(name);
        if (local == null)
            throw new IllegalArgumentException("Variable '" + name + "' is not registered");

        writeVarLoad(local);
    }

    public void writePutStatic(Class clazz, String name, Class fieldClass) {
        code.add(new FieldInsnNode(PUTSTATIC, Type.getInternalName(clazz), name, Type.getDescriptor(fieldClass)));
        stackPop();
    }

    public void writePutStatic(String name, Class fieldClass) {
        code.add(new FieldInsnNode(
                PUTSTATIC,
                method.clazz.node.name,
                name,
                Type.getDescriptor(fieldClass)
        ));
        stackPop();
    }

    public void writePutDynamic(String name, Class fieldClass) {
        code.add(new FieldInsnNode(
                PUTFIELD,
                method.clazz.node.name,
                name,
                Type.getDescriptor(fieldClass)
        ));
        stackPop();
    }

    public void writeGetStatic(Class clazz, String name, Class fieldClass) {
        code.add(new FieldInsnNode(GETSTATIC, Type.getInternalName(clazz), name, Type.getDescriptor(fieldClass)));
        stackPush(null, StackItem.Type.valueOf(fieldClass));
        setStackPeekAsImmutable();
    }

    public void writeGetStatic(String name, Class fieldClass) {
        code.add(new FieldInsnNode(
                GETSTATIC,
                method.clazz.node.name,
                name,
                Type.getDescriptor(fieldClass)
        ));
        stackPush(null, StackItem.Type.valueOf(fieldClass));
        setStackPeekAsImmutable();
    }

    public void writeGetDynamic(String name, Class fieldClass) {
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

    void writeGetEnum(Enum enumInstance) {
        writeGetStatic(enumInstance.getDeclaringClass(), enumInstance.name(), enumInstance.getDeclaringClass());
    }

    void writeVariableAssign(VariableExprToken variable, StackItem R, AssignExprToken operator, boolean returnValue) {
        LocalVariable local = method.getLocalVariable(variable.getName());
        checkAssignableVar(variable);

        Memory value = R.getMemory();

        writeLineNumber(variable);

        if (local.isReference()) {
            String name = "assign";
            if (operator.isAsReference())
                name = "assignRef";
            if (!R.isKnown()) {
                stackPush(R);
                writePopBoxing();
                writePopImmutable();
                writePushVariable(variable);
                writeSysStaticCall(Memory.class, name + "Right", Memory.class, stackPeek().type.toClass(), Memory.class);
            } else {
                writePushVariable(variable);
                Memory tmp = tryWritePush(R);
                if (tmp != null) {
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
            if (result != null) {
                writePushMemory(result);
                value = result;
            }
            writePopBoxing();
            writeVarStore(local, returnValue, true);
        }

        /*if (!methodStatement.variable(variable).isPassed())
            local.setValue(value);

        if (methodStatement.isDynamicLocal())   */
        local.setValue(null);
    }

    void writeScalarOperator(StackItem L, StackItem.Type Lt,
                             StackItem R, StackItem.Type Rt,
                             OperatorExprToken operator,
                             Class operatorResult, String operatorName) {
        boolean isInvert = !R.isKnown();
        if (!R.isKnown() && !L.isKnown() && R.getLevel() > L.getLevel())
            isInvert = false;

        if (operator instanceof ConcatExprToken) {
            if (isInvert) {
                stackPush(R);
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
                || operator instanceof MulExprToken) {
            if (operator instanceof MinusExprToken && isInvert) {
                // nothing
            } else if (Lt.isLikeNumber() && Rt.isLikeNumber()) {
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

        if (isInvert) {
            stackPush(R);
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

    public void writePop(Class clazz, boolean boxing, boolean asImmutable) {
        if (clazz == String.class)
            writePopString();
        else if (clazz == Character.TYPE)
            writePopChar();
        else if (clazz == Boolean.TYPE)
            writePopBoolean();
        else if (clazz == Memory.class) {
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

    public void writePopInteger() {
        writePopLong();
        code.add(new InsnNode(L2I));
        stackPop();
        stackPush(null, StackItem.Type.INT);
    }

    public void writePopFloat() {
        writePopDouble();
        code.add(new InsnNode(D2F));
        stackPop();
        stackPush(null, StackItem.Type.FLOAT);
    }

    public void writePopLong() {
        switch (stackPeek().type) {
            case LONG:
                break;
            case FLOAT: {
                code.add(new InsnNode(L2F));
                stackPop();
                stackPush(Memory.Type.INT);
            }
            break;
            case BYTE:
            case SHORT:
            case BOOL:
            case CHAR:
            case INT: {
                code.add(new InsnNode(I2L));
                stackPop();
                stackPush(Memory.Type.INT);
            }
            break;
            case DOUBLE: {
                code.add(new InsnNode(D2L));
                stackPop();
                stackPush(Memory.Type.INT);
            }
            break;
            case STRING: {
                writeSysStaticCall(StringMemory.class, "toNumeric", Memory.class, String.class);
                writeSysDynamicCall(Memory.class, "toLong", Long.TYPE);
            }
            break;
            default: {
                writeSysDynamicCall(Memory.class, "toLong", Long.TYPE);
            }
        }
    }

    public void writePopDouble() {
        switch (stackPeek().type) {
            case DOUBLE:
                break;
            case BYTE:
            case SHORT:
            case BOOL:
            case CHAR:
            case INT: {
                code.add(new InsnNode(I2D));
                stackPop();
                stackPush(Memory.Type.DOUBLE);
            }
            break;
            case LONG: {
                code.add(new InsnNode(L2D));
                stackPop();
                stackPush(Memory.Type.DOUBLE);
            }
            break;
            case STRING: {
                writeSysStaticCall(StringMemory.class, "toNumeric", Memory.class, String.class);
                writeSysDynamicCall(Memory.class, "toDouble", Double.TYPE);
            }
            break;
            default: {
                writeSysDynamicCall(Memory.class, "toDouble", Double.TYPE);
            }
        }
    }

    public void writePopString() {
        StackItem.Type peek = stackPeek().type;
        switch (peek) {
            case STRING:
                break;
            default:
                if (peek.isConstant()) {
                    if (peek == StackItem.Type.BOOL)
                        writeSysStaticCall(Memory.class, "boolToString", String.class, peek.toClass());
                    else
                        writeSysStaticCall(String.class, "valueOf", String.class, peek.toClass());
                } else
                    writeSysDynamicCall(Memory.class, "toString", String.class);
        }
    }

    public void writePopChar() {
        StackItem.Type peek = stackPeek().type;
        if (peek == StackItem.Type.CHAR)
            return;

        if (peek.isConstant()) {
            writeSysStaticCall(OperatorUtils.class, "toChar", Character.TYPE, peek.toClass());
        } else {
            writePopBoxing();
            writeSysDynamicCall(Memory.class, "toChar", Character.TYPE);
        }
    }

    public void writePopBooleanAsObject() {
        StackItem.Type peek = stackPeek().type;
        writeSysStaticCall(TrueMemory.class, "valueOf", Memory.class, peek.toClass());
        setStackPeekAsImmutable();
    }

    public void writePopBoolean() {
        StackItem.Type peek = stackPeek().type;
        switch (peek) {
            case BOOL:
                break;
            case BYTE:
            case INT:
            case SHORT:
            case CHAR:
            case LONG: {
                writeSysStaticCall(OperatorUtils.class, "toBoolean", Boolean.TYPE, peek.toClass());
            }
            break;
            case DOUBLE: {
                writeSysStaticCall(OperatorUtils.class, "toBoolean", Boolean.TYPE, Double.TYPE);
            }
            break;
            case STRING: {
                writeSysStaticCall(OperatorUtils.class, "toBoolean", Boolean.TYPE, String.class);
            }
            break;
            case REFERENCE: {
                writeSysDynamicCall(Memory.class, "toBoolean", Boolean.TYPE);
            }
            break;
        }
    }

    public void writeDynamicAccessInfo(DynamicAccessExprToken dynamic, boolean addLowerName) {
        if (dynamic.getField() != null) {
            if (dynamic.getField() instanceof NameToken) {
                String name = ((NameToken) dynamic.getField()).getName();
                writePushString(name);
                if (addLowerName)
                    writePushString(name.toLowerCase());
            } else {
                writePush(dynamic.getField(), true, false);
                writePopString();
                if (addLowerName) {
                    writePushDupLowerCase();
                }
            }
        } else {
            writeExpression(dynamic.getFieldExpr(), true, false);
            writePopString();
            if (addLowerName) {
                writePushDupLowerCase();
            }
        }
        writePushEnv();
        writePushTraceInfo(dynamic);
    }

    public void writeDynamicAccessPrepare(DynamicAccessExprToken dynamic, boolean addLowerName) {
        if (stackEmpty(true))
            unexpectedToken(dynamic);

        StackItem o = stackPop();
        writePush(o);

        if (stackPeek().isConstant())
            unexpectedToken(dynamic);
        writePopBoxing();

        if (dynamic instanceof DynamicAccessAssignExprToken) {
            if (((DynamicAccessAssignExprToken) dynamic).getValue() != null) {
                writeExpression(((DynamicAccessAssignExprToken) dynamic).getValue(), true, false);
                writePopBoxing(true);
            }
        }

        writeDynamicAccessInfo(dynamic, addLowerName);
    }

    void writeArrayGet(ArrayGetExprToken operator, boolean returnValue) {
        StackItem o = stackPeek();
        ValueExprToken L = null;
        if (o.getToken() != null) {
            stackPop();
            writePush(L = o.getToken(), true, false);
            writePopBoxing();
        }

        String methodName = operator instanceof ArrayGetRefExprToken ? "refOfIndex" : "valueOfIndex";
        boolean isShortcut = operator instanceof ArrayGetRefExprToken && ((ArrayGetRefExprToken) operator).isShortcut();
        int i = 0;
        int size = operator.getParameters().size();

        int traceIndex = createTraceInfo(operator);
        for (ExprStmtToken param : operator.getParameters()) {
            writePushTraceInfo(traceIndex); // push dup trace
            writeExpression(param, true, false);
            if (operator instanceof ArrayGetUnsetExprToken && i == size - 1) {
                writePopBoxing();
                writeSysDynamicCall(Memory.class, "unsetOfIndex", void.class, TraceInfo.class, stackPeek().type.toClass());
                if (returnValue)
                    writePushNull();
            } else if (operator instanceof ArrayGetIssetExprToken && i == size - 1) {
                writePopBoxing();
                writeSysDynamicCall(Memory.class, "issetOfIndex", Memory.class, TraceInfo.class, stackPeek().type.toClass());
            } else if (operator instanceof ArrayGetEmptyExprToken && i == size - 1) {
                writePopBoxing();
                writeSysDynamicCall(Memory.class, "emptyOfIndex", Memory.class, TraceInfo.class, stackPeek().type.toClass());
            } else {
                // TODO: Remove.
                // PHP CMP: $hack = &$arr[0];
                // boolean isMemory = stackPeek().type.toClass() == Memory.class;
                //if (isMemory)
                /*if (compiler.isPhpMode()){
                    if (i == size - 1 && isShortcut){
                        writePopBoxing();
                        writeSysDynamicCall(Memory.class, methodName + "AsShortcut", Memory.class, TraceInfo.class, Memory.class);
                        continue;
                    }
                }*/

                writeSysDynamicCall(Memory.class, methodName, Memory.class, TraceInfo.class, stackPeek().type.toClass());
                i++;
            }
        }
    }

    Memory writeUnaryOperator(OperatorExprToken operator, boolean returnValue, boolean writeOpcode) {
        if (stackEmpty(true))
            unexpectedToken(operator);

        StackItem o = stackPop();
        ValueExprToken L = o.getToken();

        Memory mem = tryWritePush(o, false, false, true);
        StackItem.Type type = tryGetType(o);

        if (mem != null) {
            Memory result = CompilerUtils.calcUnary(getCompiler().getEnvironment(), operator.toTraceInfo(getCompiler().getContext()), mem, operator);

            if (operator instanceof ValueIfElseToken) {
                ValueIfElseToken valueIfElseToken = (ValueIfElseToken) operator;
                ExprStmtToken ret = valueIfElseToken.getValue();
                if (mem.toBoolean()) {
                    if (ret == null)
                        result = mem;
                    else
                        result = writeExpression(ret, true, true, false);
                } else {
                    result = writeExpression(valueIfElseToken.getAlternative(), true, true, false);
                }
            } else if (operator instanceof ArrayGetExprToken && !(operator instanceof ArrayGetRefExprToken)) {
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

            if (result != null) {
                stackPush(result);
                setStackPeekAsImmutable();
                return result;
            }
        }

        if (!writeOpcode)
            return null;

        writeLineNumber(operator);

        String name = operator.getCode();
        Class operatorResult = operator.getResultClass();

        LocalVariable variable = null;
        if (L instanceof VariableExprToken) {
            variable = method.getLocalVariable(((VariableExprToken) L).getName());
            if (operator instanceof ArrayPushExprToken || operator instanceof ArrayGetRefExprToken)
                variable.setValue(null);
        }

        if (operator instanceof IncExprToken || operator instanceof DecExprToken) {
            if (variable == null || variable.isReference()) {
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

                if (operator.getAssociation() == Association.LEFT && returnValue) {
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
        } else if (operator instanceof AmpersandRefToken) {
            writePush(o, false, false);
            setStackPeekAsImmutable();
            Token token = o.getToken();
            if (token instanceof VariableExprToken) {
                LocalVariable local = method.getLocalVariable(((VariableExprToken) token).getName());
                local.setValue(null);
            }

        } else if (operator instanceof SilentToken) {
            writePushEnv();
            writeSysDynamicCall(Environment.class, "__pushSilent", void.class);

            writePush(o);

            writePushEnv();
            writeSysDynamicCall(Environment.class, "__popSilent", void.class);

        } else if (operator instanceof ValueIfElseToken) {
            writePush(o);
            ValueIfElseToken valueIfElseToken = (ValueIfElseToken) operator;

            LabelNode end = new LabelNode();
            LabelNode elseL = new LabelNode();

            if (valueIfElseToken.getValue() == null) {
                StackItem.Type dup = stackPeek().type;

                writePushDup();

                if (operator instanceof ValueNullCoalesceIfElseToken) {
                    writePopBoxing();
                    writeSysDynamicCall(Memory.class, "isNotNull", Boolean.TYPE);
                } else {
                    writePopBoolean();
                }

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
            writeArrayGet((ArrayGetExprToken) operator, returnValue);
        } else if (operator instanceof CallOperatorToken) {
            stackPush(o);

            CallOperatorToken call = (CallOperatorToken) operator;

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

            if (operator.isEnvironmentNeeded() && operator.isTraceNeeded()) {
                writePushEnv();
                writePushTraceInfo(operator);
                writeSysDynamicCall(Memory.class, name, operatorResult, Environment.class, TraceInfo.class);
            } else if (operator.isEnvironmentNeeded()) {
                writePushEnv();
                writeSysDynamicCall(Memory.class, name, operatorResult, Environment.class);
            } else if (operator.isTraceNeeded()) {
                writePushTraceInfo(operator);
                writeSysDynamicCall(Memory.class, name, operatorResult, TraceInfo.class);
            } else
                writeSysDynamicCall(Memory.class, name, operatorResult);

            if (!returnValue) {
                writePopAll(1);
            }
        }

        return null;
    }

    Memory writeLogicOperator(LogicOperatorExprToken operator, boolean returnValue, boolean writeOpcode) {
        if (stackEmpty(true))
            unexpectedToken(operator);

        if (!writeOpcode) {
            StackItem top = stackPeek();
            Memory one = tryWritePush(top, false, false, true);
            if (one == null)
                return null;

            Memory two = writeExpression(operator.getRightValue(), true, true, false);
            if (two == null)
                return null;

            stackPop();

            Memory r = operator.calc(getCompiler().getEnvironment(), operator.toTraceInfo(getCompiler().getContext()), one, two);

            stackPush(r);
            return r;
        }

        writeLineNumber(operator);

        StackItem o = stackPop();

        writePush(o);
        writePopBoolean();

        LabelNode end = new LabelNode();
        LabelNode next = new LabelNode();

        if (operator instanceof BooleanOrExprToken || operator instanceof BooleanOr2ExprToken) {
            code.add(new JumpInsnNode(IFEQ, next));
            stackPop();
            if (returnValue) {
                writePushBooleanAsMemory(true);
                stackPop();
            }
        } else if (operator instanceof BooleanAndExprToken || operator instanceof BooleanAnd2ExprToken) {
            code.add(new JumpInsnNode(IFNE, next));
            stackPop();
            if (returnValue) {
                writePushBooleanAsMemory(false);
                stackPop();
            }
        }

        code.add(new JumpInsnNode(GOTO, end));

        code.add(next);
        writeExpression(operator.getRightValue(), returnValue, false);
        if (returnValue) {
            if (operator.getLast() instanceof ValueIfElseToken)
                writePopBoxing();
            else
                writePopBooleanAsObject();
        }

        code.add(end);
        return null;
    }

    Memory writeOperator(OperatorExprToken operator, boolean returnValue, boolean writeOpcode) {
        if (writeOpcode) {
            BaseExprCompiler compiler = (BaseExprCompiler) getCompiler(operator.getClass());
            if (compiler != null) {
                if (writeOpcode) {
                    writeLineNumber(operator);
                }

                compiler.write(operator, returnValue);
                return null;
            }
        }

        if (operator instanceof LogicOperatorExprToken) {
            return writeLogicOperator((LogicOperatorExprToken) operator, returnValue, writeOpcode);
        }

        if (!operator.isBinary()) {
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
            if (o1.getMemory() != null && o2.getMemory() != null) {
                Memory result;
                stackPush(result = CompilerUtils.calcBinary(
                        getCompiler().getEnvironment(), operator.toTraceInfo(getCompiler().getContext()), o2.getMemory(), o1.getMemory(), operator, false
                ));
                return result;
            }

        LocalVariable variable = null;
        if (L instanceof VariableExprToken) {
            variable = method.getLocalVariable(((VariableExprToken) L).getName());
        }

        if (operator instanceof AssignExprToken) {
            if (L instanceof VariableExprToken) {
                if (!writeOpcode)
                    return null;
                writeVariableAssign((VariableExprToken) L, o1, (AssignExprToken) operator, returnValue);
                return null;
            }
        }

        Memory value1 = operator instanceof AssignableOperatorToken
                ? null
                : tryWritePush(o2, false, false, true); // LEFT

        Memory value2 = tryWritePush(o1, false, false, true); // RIGHT
        if (value1 != null && value2 != null) {
            stackPush(value1);
            stackPush(value2);
            return writeOperator(operator, returnValue, writeOpcode);
        }
        if (!returnValue && CompilerUtils.isOperatorAlwaysReturn(operator)) {
            unexpectedToken(operator);
        }

        if (!writeOpcode) {
            stackPush(o2);
            stackPush(o1);
            return null;
        }

        if (writeOpcode) {
            writeLineNumber(operator);
        }

        StackItem.Type Lt = tryGetType(o2);
        StackItem.Type Rt = tryGetType(o1);

        String name = operator.getCode();
        Class operatorResult = operator.getResultClass();

        boolean isInvert = false;
        boolean sideOperator = operator.isSide();

        if (variable != null && !variable.isReference()) {
            if (operator instanceof AssignOperatorExprToken) {
                name = ((AssignOperatorExprToken) operator).getOperatorCode();

                if (operator instanceof AssignConcatExprToken)
                    operatorResult = String.class;
                if (operator instanceof AssignPlusExprToken || operator instanceof AssignMulExprToken)
                    sideOperator = false;
            }
        }

        if (operator instanceof AssignableOperatorToken && tryIsImmutable(o2))
            unexpectedToken(operator);

        if (Lt.isConstant() && Rt.isConstant()) {
            if (operator instanceof AssignableOperatorToken)
                unexpectedToken(operator);

            writeScalarOperator(o2, Lt, o1, Rt, operator, operatorResult, name);
        } else {
            isInvert = !o1.isKnown();
            if (!o1.isKnown() && !o2.isKnown() && o1.getLevel() > o2.getLevel())
                isInvert = false;

            if (Lt.isConstant() && !isInvert) {
                writePush(o2);
                if (methodExists(OperatorUtils.class, name, Lt.toClass(), Rt.toClass())) {
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

                    if (Rt.isReference()) {
                        writePopBoxing(false);
                    } else if (Rt.isLikeInt()) { // fix bug call int operator.
                        writePopLong();
                        Rt = StackItem.Type.LONG;
                    }

                    if (!o1.immutable && !operator.isMutableArguments())
                        writePopImmutable();
                }

                if (name != null) {
                    writeSysDynamicCall(Memory.class, name, operatorResult, isInvert ? Lt.toClass() : Rt.toClass());
                }

                if (operator.getCheckerCode() != null) {
                    writePopBoxing();
                    writePushEnv();
                    writePushTraceInfo(operator);
                    writeSysDynamicCall(Memory.class, operator.getCheckerCode(), Memory.class, Environment.class, TraceInfo.class);
                }
            }
            setStackPeekAsImmutable();

            if (operator instanceof AssignOperatorExprToken) {
                if (variable == null || variable.isReference()) {
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

    void writeDebugMessage(String message) {
        writePushEnv();
        writePushConstString(message);
        writeSysDynamicCall(Environment.class, "echo", void.class, String.class);
    }

    public void writeConditional(ExprStmtToken condition, LabelNode successLabel) {
        writeExpression(condition, true, false);
        writePopBoolean();
        code.add(new JumpInsnNode(IFEQ, successLabel));
        stackPop();
    }

    public Memory writeExpression(ExprStmtToken expression, boolean returnValue, boolean returnMemory) {
        return writeExpression(expression, returnValue, returnMemory, true);
    }

    public Memory tryCalculateExpression(ExprStmtToken expression) {
        return writeExpression(expression, true, true, false);
    }

    @SuppressWarnings("unchecked")
    public Memory writeExpression(ExprStmtToken expression, boolean returnValue, boolean returnMemory, boolean writeOpcode) {
        int initStackSize = method.getStackCount();
        exprStackInit.push(initStackSize);

        if (!expression.isStmtList()) {
            if (expression.getAsmExpr() == null) {
                throw new CriticalException("Invalid expression token without asm expr, on line " + expression.getMeta().getStartLine() + ", expr = " + expression.getWord());
            }

            expression = expression.getAsmExpr(); // new ASMExpression(compiler.getEnvironment(), compiler.getContext(), expression).getResult();
        }

        List<Token> tokens = expression.getTokens();
        int operatorCount = 0;
        for (Token token : tokens) {
            if (token instanceof OperatorExprToken)
                operatorCount++;
        }

        boolean invalid = false;
        for (Token token : tokens) {
            if (token == null) continue;

            writeTickTrigger(token);

            if (writeOpcode) {
                if (token instanceof StmtToken) {
                    if (!(token instanceof ReturnStmtToken))
                        method.entity.setImmutable(false);
                }

                BaseStatementCompiler cmp = getCompiler(token.getClass());
                if (cmp != null && !(cmp instanceof BaseExprCompiler)) {
                    cmp.write(token);
                    continue;
                }
            }

            if (token instanceof ValueExprToken) {  // mixed, calls, numbers, strings, vars, etc.
                if (token instanceof CallExprToken && ((CallExprToken) token).getName() instanceof OperatorExprToken) {
                    if (writeOpcode) {
                        writePush((ValueExprToken) token, true, true);
                        method.entity.setImmutable(false);
                    } else
                        break;
                } else
                    stackPush((ValueExprToken) token);
            } else if (token instanceof OperatorExprToken) { // + - * / % && || or ! and == > < etc.
                operatorCount--;
                if (operatorCount >= 0) {
                    Memory result;
                    if (operatorCount == 0) {
                        result = writeOperator((OperatorExprToken) token, returnValue, writeOpcode);
                    } else
                        result = writeOperator((OperatorExprToken) token, true, writeOpcode);

                    if (!writeOpcode && result == null) {
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

        if (!invalid) {
            if (returnValue && !stackEmpty(false) && stackPeek().isKnown()) {
                if (returnMemory)
                    result = tryWritePush(stackPop(), writeOpcode, returnValue, true);
                else
                    writePush(stackPop());
            } else if (method.getStackCount() > 0) {
                if (stackPeekToken() instanceof CallableExprToken) {
                    if (returnMemory)
                        result = tryWritePush(stackPopToken(), returnValue, writeOpcode, true);
                    else
                        writePush(stackPopToken(), returnValue, true);
                } else if (stackPeek().isConstant()) {
                    stackPop();
                }
            }
        }

        if (!returnValue && writeOpcode) {
            writePopAll(method.getStackCount() - initStackSize);
        } else if (!writeOpcode) {
            int count = method.getStackCount() - initStackSize;
            for (int i = 0; i < count; i++) {
                stackPop();
            }
        }

        exprStackInit.pop();

        return result;
    }

    void makePop(StackItem.Type type) {
        switch (type.size()) {
            case 2:
                code.add(new InsnNode(POP2));
                break;
            case 1:
                code.add(new InsnNode(POP));
                break;
            default:
                throw new IllegalArgumentException("Invalid of size StackItem: " + type.size());
        }
    }

    public void writePopAll(int count) {
        int i = 0;
        while (method.getStackCount() > 0 && i < count) {
            i++;
            StackItem o = stackPop();
            ValueExprToken token = o.getToken();
            StackItem.Type type = o.type;

            if (token == null) {
                switch (type.size()) {
                    case 2:
                        code.add(new InsnNode(POP2));
                        break;
                    case 1:
                        code.add(new InsnNode(POP));
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid of size StackItem: " + type.size());
                }
            } else/* if (o.isInvalidForOperations())*/
                unexpectedToken(token);
        }
    }

    @Override
    public Entity compile() {
        writeDefineVariables(methodStatement.getLocal());
        writeExpression(expression, false, false);
        method.popAll();
        return null;
    }

    public static class NoSuchMethodException extends RuntimeException {
        public NoSuchMethodException(Class clazz, String method, Class... parameters) {
            super("No such method " + clazz.getName() + "." + method + "(" + StringUtils.join(parameters, ", ") + ")");
        }
    }

    public static class UnsupportedTokenException extends RuntimeException {
        protected final Token token;

        public UnsupportedTokenException(Token token) {
            this.token = token;
        }
    }
}
