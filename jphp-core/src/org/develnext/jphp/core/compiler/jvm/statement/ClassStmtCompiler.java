package org.develnext.jphp.core.compiler.jvm.statement;

import org.develnext.jphp.core.compiler.jvm.Constants;
import org.develnext.jphp.core.compiler.jvm.JPHPClassWriter;
import org.develnext.jphp.core.compiler.jvm.JvmCompiler;
import org.develnext.jphp.core.compiler.jvm.misc.LocalVariable;
import org.develnext.jphp.core.compiler.jvm.node.ClassNodeImpl;
import org.develnext.jphp.core.compiler.jvm.node.MethodNodeImpl;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.FulledNameToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.NameToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.SelfExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.StaticAccessExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ClassStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ClassVarStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ConstStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.MethodStmtToken;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import php.runtime.Memory;
import php.runtime.common.Function;
import php.runtime.common.Messages;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.CriticalException;
import php.runtime.exceptions.FatalException;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.invoke.cache.*;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.*;
import php.runtime.reflection.helper.GeneratorEntity;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.*;

import static org.objectweb.asm.Opcodes.*;

public class ClassStmtCompiler extends StmtCompiler<ClassEntity> {
    protected JPHPClassWriter cw;
    public final ClassNode node;
    public final ClassStmtToken statement;
    public final List<TraceInfo> traceList = new ArrayList<TraceInfo>();
    public final List<Memory> memoryConstants = new ArrayList<Memory>();
    public final List<Collection<Memory>> memoryArrayConstants = new ArrayList<Collection<Memory>>();

    private boolean external = false;
    private boolean isSystem = false;
    private boolean isInterfaceCheck = true;
    private String functionName = "";

    private boolean initDynamicExists = false;
    private int callFuncCount = 0;
    private int callClassCount = 0;
    private int callMethCount = 0;
    private int callConstCount = 0;
    private int callPropCount = 0;

    private GeneratorEntity generatorEntity;

    protected List<ConstStmtToken.Item> dynamicConstants = new ArrayList<ConstStmtToken.Item>();
    protected List<ClassVarStmtToken> dynamicProperties = new ArrayList<ClassVarStmtToken>();
    private ClassStmtToken classContext;

    public ClassStmtCompiler(JvmCompiler compiler, ClassStmtToken statement) {
        super(compiler);
        this.statement = statement;
        this.node = new ClassNodeImpl();
    }

    public int getAndIncCallFuncCount() {
        return callFuncCount++;
    }

    public int getAndIncCallClassCount() {
        return callClassCount++;
    }

    public int getAndIncCallMethCount() {
        return callMethCount++;
    }

    public int getAndIncCallConstCount() {
        return callConstCount++;
    }

    public int getAndIncCallPropCount() {
        return callPropCount++;
    }

    public boolean isInitDynamicExists() {
        return initDynamicExists;
    }

    public boolean isClosure() {
        return functionName == null;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public GeneratorEntity getGeneratorEntity() {
        return generatorEntity;
    }

    public void setGeneratorEntity(GeneratorEntity generatorEntity) {
        this.generatorEntity = generatorEntity;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }

    public void setInterfaceCheck(boolean check) {
        isInterfaceCheck = check;
    }

    public boolean isExternal() {
        return external;
    }

    public void setExternal(boolean external) {
        this.external = external;
    }

    TraceInfo makeTraceInfo(int line, int position) {
        return new TraceInfo(compiler.getContext(), line, 0, position, 0);
    }

    TraceInfo makeTraceInfo(Token token) {
        return token.toTraceInfo(compiler.getContext());
    }

    int addTraceInfo(int line, int position) {
        traceList.add(makeTraceInfo(line, position));
        return traceList.size() - 1;
    }

    int addTraceInfo(Token token) {
        traceList.add(makeTraceInfo(token));
        return traceList.size() - 1;
    }

    int addMemoryConstant(Memory memory) {
        memoryConstants.add(memory);
        return memoryConstants.size() - 1;
    }

    int addMemoryArray(Collection<Memory> memories) {
        memoryArrayConstants.add(memories);
        return memoryArrayConstants.size() - 1;
    }

    @SuppressWarnings("unchecked")
    protected void writeDestructor() {
        if (entity.methodDestruct != null) {
            MethodNode destructor = new MethodNodeImpl();
            destructor.name = "finalize";
            destructor.access = ACC_PUBLIC;
            destructor.desc = Type.getMethodDescriptor(Type.getType(void.class));

            MethodStmtCompiler methodCompiler = new MethodStmtCompiler(this, destructor);
            ExpressionStmtCompiler expressionCompiler = new ExpressionStmtCompiler(methodCompiler, null);
            methodCompiler.writeHeader();

            LabelNode end = new LabelNode();
            LabelNode l0 = writeLabel(destructor, statement.getMeta().getStartLine());
            methodCompiler.addLocalVariable("~this", l0);

            expressionCompiler.writeVarLoad("~this");
            expressionCompiler.writeSysDynamicCall(null, "isFinalized", Boolean.TYPE);
            destructor.instructions.add(new JumpInsnNode(IFEQ, end));

            // --- if (!__finalized__) {
            expressionCompiler.writeVarLoad("~this");
            expressionCompiler.writePushDup();
            expressionCompiler.writeSysDynamicCall(null, "doFinalize", void.class);

            expressionCompiler.writePushEnvFromSelf();
            expressionCompiler.writePushConstNull();

            expressionCompiler.writeSysDynamicCall(
                    null, entity.methodDestruct.getInternalName(), Memory.class, Environment.class, Memory[].class
            );
            expressionCompiler.writePopAll(1);
            // ---- }
            destructor.instructions.add(end);

            // call parent
            // WARNING!!! It's commented for better performance, super.finalize empty in JDK 1.6, 1.7, 1.8
            /*expressionCompiler.writeVarLoad("~this");
            destructor.instructions.add(new MethodInsnNode(
                    INVOKEVIRTUAL,
                    Type.getInternalName(Object.class),
                    destructor.name,
                    destructor.desc
            ));*/

            destructor.instructions.add(new InsnNode(Opcodes.RETURN));
            methodCompiler.writeFooter();
            node.methods.add(destructor);
        }
    }

    protected void writeDefaultConstructors()
    {
        if (!isSystem && !isClosure() && entity.getParent() != null
                && entity.getParent().getNativeClass() != null
                && !BaseObject.class.isAssignableFrom(entity.getParent().getNativeClass())) {
            for (Constructor el : entity.getParent().getNativeClass().getConstructors()) {
                Class<?>[] parameterTypes = el.getParameterTypes();

                if (parameterTypes.length == 2
                        && parameterTypes[0] == Environment.class && parameterTypes[1] == ClassEntity.class) {
                    continue;
                }

                MethodNode constructor = new MethodNodeImpl();
                constructor.name       = Constants.INIT_METHOD;
                constructor.access     = el.getModifiers();
                constructor.exceptions = new ArrayList();

                MethodStmtCompiler methodCompiler = new MethodStmtCompiler(this, constructor);
                ExpressionStmtCompiler expressionCompiler = new ExpressionStmtCompiler(methodCompiler, null);

                LabelNode l0 = writeLabel(constructor, statement.getMeta().getStartLine());
                methodCompiler.addLocalVariable("~this", l0);

                Type[] argumentTypes = new Type[parameterTypes.length];

                int i = 0;
                for (Class type : parameterTypes) {
                    argumentTypes[i++] = Type.getType(type);

                    methodCompiler.addLocalVariable("arg" + i, l0, type);
                }

                constructor.desc = Type.getMethodDescriptor(
                        Type.getType(void.class), argumentTypes
                );

                methodCompiler.writeHeader();

                expressionCompiler.writeVarLoad("~this");
                for (i = 0; i < argumentTypes.length; i++) {
                    expressionCompiler.writeVarLoad("arg" + (i + 1));
                }

                constructor.instructions.add(new MethodInsnNode(
                        INVOKESPECIAL,
                        node.superName,
                        Constants.INIT_METHOD,
                        constructor.desc,
                        false
                ));

                methodCompiler.writeFooter();
                constructor.instructions.add(new InsnNode(RETURN));
                node.methods.add(constructor);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected void writeConstructor() {
        MethodNode constructor = new MethodNodeImpl();
        constructor.name = Constants.INIT_METHOD;
        constructor.access = ACC_PUBLIC;
        constructor.exceptions = new ArrayList();

        MethodStmtCompiler methodCompiler = new MethodStmtCompiler(this, constructor);
        ExpressionStmtCompiler expressionCompiler = new ExpressionStmtCompiler(methodCompiler, null);
        methodCompiler.writeHeader();

        LabelNode l0 = writeLabel(constructor, statement.getMeta().getStartLine());
        methodCompiler.addLocalVariable("~this", l0);

        if (isClosure() || generatorEntity != null) {
            constructor.desc = Type.getMethodDescriptor(
                    Type.getType(void.class),
                    Type.getType(Environment.class),
                    Type.getType(ClassEntity.class),
                    Type.getType(Memory.class),
                    Type.getType(Memory[].class)
            );

            if (isClosure()) {
                constructor.desc = Type.getMethodDescriptor(
                        Type.getType(void.class),
                        Type.getType(Environment.class),
                        Type.getType(ClassEntity.class),
                        Type.getType(Memory.class),
                        Type.getType(String.class),
                        Type.getType(Memory[].class)
                );
            }

            methodCompiler.addLocalVariable("~env", l0, Environment.class);
            methodCompiler.addLocalVariable("~class", l0, ClassEntity.class);
            methodCompiler.addLocalVariable("~self", l0, Memory.class);

            if (isClosure()) {
                methodCompiler.addLocalVariable("~context", l0, String.class);
            }

            methodCompiler.addLocalVariable("~uses", l0, Memory[].class);

            methodCompiler.writeHeader();
            expressionCompiler.writeVarLoad("~this");

            expressionCompiler.writeVarLoad("~env");
            expressionCompiler.writeVarLoad("~class");
            expressionCompiler.writeVarLoad("~self");

            if (isClosure()) {
                expressionCompiler.writeVarLoad("~context");
            }

            expressionCompiler.writeVarLoad("~uses");
            constructor.instructions.add(new MethodInsnNode(
                    INVOKESPECIAL,
                    node.superName,
                    Constants.INIT_METHOD,
                    constructor.desc,
                    false
            ));
        } else {
            constructor.desc = Type.getMethodDescriptor(
                    Type.getType(void.class), Type.getType(Environment.class), Type.getType(ClassEntity.class)
            );
            methodCompiler.addLocalVariable("~env", l0, Environment.class);
            methodCompiler.addLocalVariable("~class", l0, String.class);

            expressionCompiler.writeVarLoad("~this");
            expressionCompiler.writeVarLoad("~env");
            expressionCompiler.writeVarLoad("~class");
            constructor.instructions.add(new MethodInsnNode(
                    INVOKESPECIAL,
                    node.superName,
                    Constants.INIT_METHOD,
                    constructor.desc,
                    false
            ));

            // PROPERTIES
            for (ClassVarStmtToken property : statement.getProperties()) {
                ExpressionStmtCompiler expressionStmtCompiler = new ExpressionStmtCompiler(methodCompiler, null);
                Memory value = Memory.NULL;
                if (property.getValue() != null)
                    value = expressionStmtCompiler.writeExpression(property.getValue(), true, true, false);

                PropertyEntity prop = new PropertyEntity(compiler.getContext());
                prop.setName(property.getVariable().getName());
                prop.setModifier(property.getModifier());
                prop.setStatic(property.isStatic());
                prop.setDefaultValue(value);
                prop.setDefault(property.getValue() != null);
                prop.setTrace(property.toTraceInfo(compiler.getContext()));

                if (property.getDocComment() != null) {
                    prop.setDocComment(new DocumentComment(property.getDocComment().getComment()));
                }

                ClassEntity.PropertyResult result = entity.addProperty(prop);
                result.check(compiler.getEnvironment());

                if (value == null && property.getValue() != null) {
                    if (property.getValue().isSingle() && ValueExprToken.isConstable(property.getValue().getSingle(), true))
                        dynamicProperties.add(property);
                    else
                        compiler.getEnvironment().error(
                                property.getVariable().toTraceInfo(compiler.getContext()),
                                ErrorType.E_COMPILE_ERROR,
                                Messages.ERR_EXPECTED_CONST_VALUE.fetch(entity.getName() + "::$" + property.getVariable().getName())
                        );
                }
            }
        }

        methodCompiler.writeFooter();
        constructor.instructions.add(new InsnNode(RETURN));
        node.methods.add(constructor);
    }

    protected void writeConstant(ConstStmtToken constant) {
        MethodStmtCompiler methodStmtCompiler = new MethodStmtCompiler(this, (MethodStmtToken) null);
        ExpressionStmtCompiler expressionStmtCompiler = new ExpressionStmtCompiler(methodStmtCompiler, null);

        DocumentComment documentComment = null;
        if (constant.getDocComment() != null)
            documentComment = new DocumentComment(constant.getDocComment().getComment());

        for (ConstStmtToken.Item el : constant.items) {
            Memory value = expressionStmtCompiler.writeExpression(el.value, true, true, false);
            ConstantEntity constantEntity = new ConstantEntity(el.getFulledName(), value, true);
            constantEntity.setTrace(el.name.toTraceInfo(compiler.getContext()));
            constantEntity.setDocComment(documentComment);
            constantEntity.setModifier(constant.getModifier());

            if (value != null && !value.isArray()) {
                ConstantEntity c = entity.findConstant(el.getFulledName());
                if (c != null && c.getClazz().getId() == entity.getId()) {
                    compiler.getEnvironment().error(
                            constant.toTraceInfo(compiler.getContext()),
                            ErrorType.E_ERROR,
                            Messages.ERR_CANNOT_REDEFINE_CLASS_CONSTANT,
                            entity.getName() + "::" + el.getFulledName()
                    );
                    return;
                }
                entity.addConstant(constantEntity).check(compiler.getEnvironment());
            } else {
                if (ValueExprToken.isConstable(el.value.getSingle(), false)) {
                    dynamicConstants.add(el);
                    entity.addConstant(constantEntity).check(compiler.getEnvironment());
                } else {
                    compiler.getEnvironment().error(
                            constant.toTraceInfo(compiler.getContext()),
                            Messages.ERR_EXPECTED_CONST_VALUE.fetch(entity.getName() + "::" + el.getFulledName())
                    );
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected void writeSystemInfo() {
        node.fields.add(new FieldNode(
                ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "$FN",
                Type.getDescriptor(String.class),
                null,
                compiler.getSourceFile()
        ));

        node.fields.add(new FieldNode(
                ACC_PUBLIC + ACC_STATIC, "$TRC",
                Type.getDescriptor(TraceInfo[].class),
                null,
                null
        ));

        node.fields.add(new FieldNode(
                ACC_PUBLIC + ACC_STATIC, "$MEM",
                Type.getDescriptor(Memory[].class),
                null,
                null
        ));

        node.fields.add(new FieldNode(
                ACC_PUBLIC + ACC_STATIC, "$AMEM",
                Type.getDescriptor(Memory[][].class),
                null,
                null
        ));

        node.fields.add(new FieldNode(
                ACC_PUBLIC + ACC_STATIC, "$CALL_FUNC_CACHE",
                Type.getDescriptor(FunctionCallCache.class),
                null,
                null
        ));

        node.fields.add(new FieldNode(
                ACC_PUBLIC + ACC_STATIC, "$CALL_METH_CACHE",
                Type.getDescriptor(MethodCallCache.class),
                null,
                null
        ));

        node.fields.add(new FieldNode(
                ACC_PUBLIC + ACC_STATIC, "$CALL_PROP_CACHE",
                Type.getDescriptor(PropertyCallCache.class),
                null,
                null
        ));

        node.fields.add(new FieldNode(
                ACC_PUBLIC + ACC_STATIC, "$CALL_CONST_CACHE",
                Type.getDescriptor(ConstantCallCache.class),
                null,
                null
        ));

        node.fields.add(new FieldNode(
                ACC_PUBLIC + ACC_STATIC, "$CALL_CLASS_CACHE",
                Type.getDescriptor(ClassCallCache.class),
                null,
                null
        ));

        if (functionName != null) {
            node.fields.add(new FieldNode(
                    ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "$CL",
                    Type.getDescriptor(String.class),
                    null,
                    !functionName.isEmpty() ? functionName : entity.getName()
            ));
        }
    }

    @SuppressWarnings("unchecked")
    protected void writeInitEnvironment() {
        if (!dynamicConstants.isEmpty() || !dynamicProperties.isEmpty()) {
            initDynamicExists = true;
            MethodNode node = new MethodNodeImpl();
            node.access = ACC_STATIC + ACC_PUBLIC;
            node.name = "__$initEnvironment";
            node.desc = Type.getMethodDescriptor(
                    Type.getType(void.class), Type.getType(Environment.class)
            );
            if (entity.isTrait()) {
                node.desc = Type.getMethodDescriptor(
                        Type.getType(void.class), Type.getType(Environment.class), Type.getType(String.class)
                );
            }

            MethodStmtCompiler methodCompiler = new MethodStmtCompiler(this, node);
            ExpressionStmtCompiler expressionCompiler = new ExpressionStmtCompiler(methodCompiler, null);
            methodCompiler.writeHeader();

            LabelNode l0 = expressionCompiler.makeLabel();
            methodCompiler.addLocalVariable("~env", l0, Environment.class);
            if (entity.isTrait())
                methodCompiler.addLocalVariable("~class_name", l0, String.class);

            LocalVariable l_class = methodCompiler.addLocalVariable("~class", l0, ClassEntity.class);

            expressionCompiler.writePushEnv();
            if (entity.isTrait()) {
                expressionCompiler.writeVarLoad("~class_name");
                expressionCompiler.writePushDupLowerCase();
            } else {
                expressionCompiler.writePushConstString(entity.getName());
                expressionCompiler.writePushConstString(entity.getLowerName());
            }
            expressionCompiler.writePushConstBoolean(true);
            expressionCompiler.writeSysDynamicCall(
                    Environment.class, "fetchClass", ClassEntity.class, String.class, String.class, Boolean.TYPE
            );
            expressionCompiler.writeVarStore(l_class, false, false);

            // corrects defination of constants
            final List<ConstStmtToken.Item> first = new ArrayList<ConstStmtToken.Item>();
            final Set<String> usedNames = new HashSet<String>();
            final List<ConstStmtToken.Item> other = new ArrayList<ConstStmtToken.Item>();
            for (ConstStmtToken.Item el : dynamicConstants) {
                Token tk = el.value.getSingle();
                if (tk instanceof StaticAccessExprToken) {
                    StaticAccessExprToken access = (StaticAccessExprToken) tk;
                    boolean self = false;
                    if (access.getClazz() instanceof SelfExprToken)
                        self = true;
                    else if (access.getClazz() instanceof FulledNameToken
                            && ((FulledNameToken) access.getClazz()).getName().equalsIgnoreCase(entity.getName())) {
                        self = true;
                    }
                    if (self) {
                        String name = ((NameToken) access.getField()).getName();
                        if (usedNames.contains(el.getFulledName()))
                            first.add(0, el);
                        else
                            first.add(el);
                        usedNames.add(name);
                        continue;
                    }
                }

                if (usedNames.contains(el.getFulledName()))
                    first.add(0, el);
                else
                    other.add(el);
            }

            other.addAll(0, first);

            for (ConstStmtToken.Item el : other) {
                expressionCompiler.writeVarLoad(l_class);
                expressionCompiler.writePushEnv();
                expressionCompiler.writePushConstString(el.getFulledName());
                expressionCompiler.writeExpression(el.value, true, false, true);
                expressionCompiler.writePopBoxing(true);

                expressionCompiler.writeSysDynamicCall(
                        ClassEntity.class, "addDynamicConstant", void.class,
                        Environment.class, String.class, Memory.class
                );
            }

            for (ClassVarStmtToken el : dynamicProperties) {
                expressionCompiler.writeVarLoad(l_class);
                expressionCompiler.writePushEnv();
                expressionCompiler.writePushConstString(el.getVariable().getName());
                expressionCompiler.writeExpression(el.getValue(), true, false, true);
                expressionCompiler.writePopBoxing(true);

                expressionCompiler.writeSysDynamicCall(
                        ClassEntity.class,
                        el.isStatic() ? "addDynamicStaticProperty" : "addDynamicProperty",
                        void.class,
                        Environment.class, String.class, Memory.class
                );
            }

            node.instructions.add(new InsnNode(RETURN));
            methodCompiler.writeFooter();

            this.node.methods.add(node);
        }
    }

    @SuppressWarnings("unchecked")
    protected void writeInitStatic() {
        MethodNode node = new MethodNodeImpl();
        node.access = ACC_STATIC;
        node.name = Constants.STATIC_INIT_METHOD;
        node.desc = Type.getMethodDescriptor(Type.getType(void.class));

        MethodStmtCompiler methodCompiler = new MethodStmtCompiler(this, node);
        ExpressionStmtCompiler expressionCompiler = new ExpressionStmtCompiler(methodCompiler, null);
        methodCompiler.writeHeader();

        // trace list
        expressionCompiler.writePushSmallInt(traceList.size());
        node.instructions.add(new TypeInsnNode(ANEWARRAY, Type.getInternalName(TraceInfo.class)));
        expressionCompiler.stackPush(Memory.Type.REFERENCE);

        int i = 0;
        for (TraceInfo traceInfo : traceList) {
            expressionCompiler.writePushDup();
            expressionCompiler.writePushSmallInt(i);
            expressionCompiler.writePushCreateTraceInfo(traceInfo.getStartLine(), traceInfo.getStartPosition());

            node.instructions.add(new InsnNode(AASTORE));
            expressionCompiler.stackPop();
            expressionCompiler.stackPop();
            i++;
        }
        expressionCompiler.writePutStatic("$TRC", TraceInfo[].class);

        // memory constants
        expressionCompiler.writePushSmallInt(memoryConstants.size());
        node.instructions.add(new TypeInsnNode(ANEWARRAY, Type.getInternalName(Memory.class)));
        expressionCompiler.stackPush(Memory.Type.REFERENCE);

        i = 0;
        for (Memory memory : memoryConstants) {
            expressionCompiler.writePushDup();
            expressionCompiler.writePushSmallInt(i);

            expressionCompiler.writePushMemory(memory);
            expressionCompiler.writePopBoxing(true, false);

            node.instructions.add(new InsnNode(AASTORE));
            expressionCompiler.stackPop();
            expressionCompiler.stackPop();

            i++;
        }
        expressionCompiler.writePutStatic("$MEM", Memory[].class);

        // memory array constants
        expressionCompiler.writePushSmallInt(memoryArrayConstants.size());
        node.instructions.add(new TypeInsnNode(ANEWARRAY, Type.getInternalName(Memory[].class)));
        expressionCompiler.stackPush(Memory.Type.REFERENCE);

        i = 0;
        for (Collection<Memory> memories : memoryArrayConstants) {
            expressionCompiler.writePushDup();
            expressionCompiler.writePushSmallInt(i);

            expressionCompiler.writePushParameters(memories);

            node.instructions.add(new InsnNode(AASTORE));
            expressionCompiler.stackPop();
            expressionCompiler.stackPop();

            i++;
        }
        expressionCompiler.writePutStatic("$AMEM", Memory[][].class);

        // cached calls
        expressionCompiler.writePushNewObject(FunctionCallCache.class);
        expressionCompiler.writePutStatic("$CALL_FUNC_CACHE", FunctionCallCache.class);

        expressionCompiler.writePushNewObject(MethodCallCache.class);
        expressionCompiler.writePutStatic("$CALL_METH_CACHE", MethodCallCache.class);

        expressionCompiler.writePushNewObject(ConstantCallCache.class);
        expressionCompiler.writePutStatic("$CALL_CONST_CACHE", ConstantCallCache.class);

        expressionCompiler.writePushNewObject(PropertyCallCache.class);
        expressionCompiler.writePutStatic("$CALL_PROP_CACHE", PropertyCallCache.class);

        expressionCompiler.writePushNewObject(ClassCallCache.class);
        expressionCompiler.writePutStatic("$CALL_CLASS_CACHE", ClassCallCache.class);

        node.instructions.add(new InsnNode(RETURN));
        methodCompiler.writeFooter();

        this.node.methods.add(node);
    }

    protected void writeInterfaceMethod(MethodEntity method) {
        MethodNode node = new MethodNodeImpl();
        node.access = ACC_PUBLIC;
        node.name = method.getName();
        node.desc = Type.getMethodDescriptor(
                Type.getType(Memory.class),
                Type.getType(Environment.class),
                Type.getType(Memory[].class)
        );

        MethodStmtCompiler methodCompiler = new MethodStmtCompiler(this, node);
        ExpressionStmtCompiler expressionCompiler = new ExpressionStmtCompiler(methodCompiler, null);
        methodCompiler.writeHeader();

        LabelNode l0 = writeLabel(node, statement.getMeta().getStartLine());
        methodCompiler.addLocalVariable("~this", l0);
        methodCompiler.addLocalVariable("~env", l0);
        methodCompiler.addLocalVariable("~args", l0);

        expressionCompiler.writeVarLoad("~this");
        expressionCompiler.writeVarLoad("~env");
        expressionCompiler.writeVarLoad("~args");

        String internalName = entity.findMethod(method.getLowerName()).getInternalName();
        expressionCompiler.writeSysDynamicCall(null, internalName, Memory.class, Environment.class, Memory[].class);

        node.instructions.add(new InsnNode(ARETURN));
        methodCompiler.writeFooter();
        this.node.methods.add(node);
    }

    protected void writeInterfaceMethods(Collection<MethodEntity> methods) {
        for (MethodEntity method : methods) {
            writeInterfaceMethod(method);
        }
    }

    protected Set<ClassEntity> writeInterfaces(ClassEntity _interface) {
        Set<ClassEntity> result = new HashSet<ClassEntity>();
        writeInterfaces(_interface, result);
        return result;
    }

    protected void writeInterfaces(ClassEntity _interface, Set<ClassEntity> used) {
        if (used.add(_interface)) {
            if (_interface != null && _interface.isInternal())
                node.interfaces.add(_interface.getInternalName());

            if (_interface != null) {
                for (ClassEntity el : _interface.getInterfaces().values()) {
                    if (!_interface.isInternal() || !el.isInternal())
                        writeInterfaces(el, used);
                }
            }
        }
    }

    protected ClassEntity fetchClass(String name) {
        ClassEntity result = compiler.getModule().findClass(name);
        if (result == null)
            result = getCompiler().getEnvironment().fetchClass(name, true);

        return result;
    }

    protected ClassEntity fetchClassAndCheck(String name) {
        ClassEntity r = fetchClass(name);
        if (r == null)
            compiler.getEnvironment().error(
                    entity.getTrace(),
                    Messages.ERR_CLASS_NOT_FOUND.fetch(name)
            );
        return r;
    }

    @SuppressWarnings("unchecked")
    protected void writeImplements() {
        if (statement.getImplement() != null) {
            Environment env = compiler.getEnvironment();
            for (FulledNameToken name : statement.getImplement()) {
                ClassEntity implement = fetchClass(name.getName());
                Set<ClassEntity> needWriteInterfaceMethods = new HashSet<ClassEntity>();
                if (implement == null) {
                    env.error(
                            name.toTraceInfo(compiler.getContext()),
                            Messages.ERR_INTERFACE_NOT_FOUND.fetch(name.toName())
                    );
                } else {
                    if (implement.getType() != ClassEntity.Type.INTERFACE) {
                        env.error(
                                name.toTraceInfo(compiler.getContext()),
                                Messages.ERR_CANNOT_IMPLEMENT.fetch(entity.getName(), implement.getName())
                        );
                    }
                    if (!statement.isInterface())
                        needWriteInterfaceMethods = writeInterfaces(implement);
                }
                ClassEntity.ImplementsResult addResult = entity.addInterface(implement);
                addResult.check(env);

                for (ClassEntity el : needWriteInterfaceMethods) {
                    if (el.isInternal()) {
                        writeInterfaceMethods(el.getMethods().values());
                    }
                }
            }
        }
    }

    protected void writeCopiedMethod(ClassStmtToken.Alias alias, String methodName, ClassEntity trait) {
        final MethodEntity methodEntity = fetchClassAndCheck(alias.getTrait()).findMethod(methodName.toLowerCase());

        String name = alias.getName();
        if (name == null)
            name = methodName;

        MethodEntity origin = entity.findMethod(name.toLowerCase());
        if (origin != null) {
            if (origin.getClazz() == entity) {
                if (origin.getTrait() != null) {
                    compiler.getEnvironment().error(
                            entity.getTrace(),
                            Messages.ERR_TRAIT_METHOD_COLLISION.fetch(
                                    methodName, trait.getName(), origin.getTrait().getName(), entity.getName()
                            )
                    );
                }
                return;
            }
        }

        if (methodEntity == null) {
            compiler.getEnvironment().error(
                    entity.getTrace(),
                    Messages.ERR_METHOD_NOT_FOUND.fetch(alias.getTrait(), methodName)
            );
            return;
        }

        MethodEntity dup = methodEntity.duplicateForInject();
        dup.setClazz(entity);
        dup.setTrait(trait);
        if (alias.getName() != null)
            dup.setName(alias.getName());

        if (alias.getModifier() != null)
            dup.setModifier(alias.getModifier());

        MethodNodeImpl methodNode = MethodNodeImpl.duplicate(methodEntity.getAdditionalData("methodNode", MethodNode.class, new Function<MethodNode>() {
            @Override
            public MethodNode call() {
                ClassNode classNode = methodEntity.getClazz().getAdditionalData("classNode", ClassNode.class, new Function<ClassNode>() {
                    @Override
                    public ClassNode call() {
                        ClassReader classReader;
                        if (methodEntity.getClazz().getData() != null)
                            classReader = new ClassReader(methodEntity.getClazz().getData());
                        else {
                            try {
                                classReader = new ClassReader(methodEntity.getClazz().getName());
                            } catch (IOException e) {
                                throw new CriticalException(e);
                            }
                        }
                        ClassNode classNode = new ClassNode();
                        classReader.accept(classNode, 0);

                        return classNode;
                    }
                });

                for (Object m : classNode.methods) {
                    MethodNode method = (MethodNode) m;
                    if (method.name.equals(methodEntity.getInternalName())) {
                        return method;
                    }
                }

                throw new CriticalException("Cannot find MethodNode for method - " + methodEntity.getName() + "(" + methodEntity.getSignatureString(true) + ")");
            }
        }));

        if (origin != null) {
            dup.setPrototype(origin);
        }

        dup.setInternalName(dup.getName() + "$" + entity.nextMethodIndex());
        methodNode.name = dup.getInternalName();

        ClassEntity.SignatureResult result = entity.addMethod(dup, null);
        result.check(compiler.getEnvironment());

        node.methods.add(methodNode);
    }

    protected void writeCopiedMethod(MethodEntity methodEntity, ClassEntity trait) {
        ClassStmtToken.Replacement replacement = statement.findReplacement(methodEntity.getName());
        if (replacement != null && replacement.hasTrait(trait.getName())) {
            return;
        }

        List<ClassStmtToken.Alias> aliases = statement.findAliases(methodEntity.getName());
        if (aliases == null)
            aliases = Arrays.asList(new ClassStmtToken.Alias(trait.getName(), null, methodEntity.getName()));
        else {
            boolean replaceExists = false;
            boolean replaceAlias = false;

            for (ClassStmtToken.Alias alias : aliases) {
                if (replacement != null && alias.getTrait().equalsIgnoreCase(replacement.getOrigin())) {
                    replaceExists = true;
                    break;
                }
                if ((alias.getModifier() == null || alias.getModifier() == methodEntity.getModifier())
                        || alias.getName() == null)
                    replaceAlias = true;
            }

            if (replacement != null && !replaceExists)
                aliases.add(new ClassStmtToken.Alias(replacement.getOrigin(), null, methodEntity.getName()));

            if (!replaceAlias)
                aliases.add(new ClassStmtToken.Alias(trait.getName(), null, methodEntity.getName()));
        }

        for (ClassStmtToken.Alias alias : aliases) {
            writeCopiedMethod(alias, methodEntity.getName(), trait);
        }
    }

    protected void writeTraitProperties(ClassEntity trait, Collection<PropertyEntity> props) {
        for (PropertyEntity el : props) {
            PropertyEntity origin = entity.properties.get(el.getLowerName());

            if (origin != null) {
                // doesn't work with parent private properties of non-traits  (see: the traits/property006.php test)
                boolean isSkip = origin.getTrait() == null && !origin.getClazz().equals(entity) && origin.isPrivate();
                if (origin.getTrait() != null && origin.getTrait().equals(trait))
                    isSkip = true;

                if (!isSkip) {
                    Environment env = compiler.getEnvironment();
                    String ownerName = origin.getClazz().getName();
                    if (origin.getTrait() != null)
                        ownerName = origin.getTrait().getName();

                    boolean isFatal = origin.getModifier() != el.getModifier();
                    if (origin.getDefaultValue() != null && el.getDefaultValue() == null)
                        isFatal = true;
                    else if (origin.getDefaultValue() == null && el.getDefaultValue() != null)
                        isFatal = true;
                    else if (origin.getDefaultValue() == null) {
                        // nop
                    } else if (!origin.getDefaultValue().identical(el.getDefaultValue()))
                        isFatal = true;

                    if (isFatal) {
                        env.error(
                                entity.getTrace(),
                                Messages.ERR_TRAIT_SAME_PROPERTY.fetch(
                                        ownerName,
                                        trait.getName(), el.getName(), entity.getName()
                                )
                        );
                    } else {
                        env.error(
                                entity.getTrace(), ErrorType.E_STRICT,
                                Messages.ERR_TRAIT_SAME_PROPERTY_STRICT.fetch(
                                        ownerName, trait.getName(), el.getName(), entity.getName()
                                )
                        );
                    }
                }
            }

            if (origin != null && origin.getClazz().getId() == entity.getId())
                continue;

            PropertyEntity p = el.duplicate();
            p.setClazz(entity);
            p.setTrait(trait);
            ClassEntity.PropertyResult r = entity.addProperty(p);
            r.check(compiler.getEnvironment());
        }
    }

    protected void writeTraitProperties(ClassEntity trait) {
        writeTraitProperties(trait, trait.getProperties());
        writeTraitProperties(trait, trait.getStaticProperties());
    }

    protected void writeTrait(ClassEntity trait) {
        entity.addTrait(trait);
        initDynamicExists = true;

        writeTraitProperties(trait);
        for (MethodEntity methodEntity : trait.getMethods().values()) {
            writeCopiedMethod(methodEntity, trait);
        }
    }

    protected void checkRequiredTrait(String trait) {
        if (!entity.hasTrait(trait.toLowerCase())) {
            compiler.getEnvironment().error(
                    entity.getTrace(),
                    Messages.ERR_TRAIT_WAS_NOT_ADDED.fetch(trait, entity.getName())
            );
        }
    }

    protected void checkAliasAndReplacementsTraits() {
        if (statement.getAliases() != null)
            for (List<ClassStmtToken.Alias> aliases : statement.getAliases().values()) {
                for (ClassStmtToken.Alias alias : aliases) {
                    checkRequiredTrait(alias.getTrait());
                }
            }

        if (statement.getReplacements() != null)
            for (ClassStmtToken.Replacement replacement : statement.getReplacements().values()) {
                checkRequiredTrait(replacement.getOrigin());
                for (String e : replacement.getTraits())
                    checkRequiredTrait(e);
            }
    }

    protected List<ClassEntity> fetchTraits() {
        List<ClassEntity> r = new ArrayList<ClassEntity>();
        for (NameToken one : statement.getUses()) {
            ClassEntity trait = fetchClass(one.getName());
            if (trait == null) {
                compiler.getEnvironment().error(
                        one.toTraceInfo(compiler.getContext()),
                        Messages.ERR_TRAIT_NOT_FOUND.fetch(one.getName())
                );
                return null;
            }

            if (!trait.isTrait()) {
                compiler.getEnvironment().error(
                        one.toTraceInfo(compiler.getContext()),
                        Messages.ERR_CANNOT_USE_NON_TRAIT.fetch(
                                entity.getName(), one.getName()
                        )
                );
            } else
                r.add(trait);
        }
        return r;
    }

    protected void writeTraits(Collection<ClassEntity> traits) {
        for (ClassEntity trait : traits) {
            writeTrait(trait);
        }
    }

    @Override
    public ClassEntity compile() {
        entity = new ClassEntity(compiler.getContext());
        entity.setId(compiler.getScope().nextClassIndex());

        entity.setFinal(statement.isFinal());
        entity.setAbstract(statement.isAbstract());
        entity.setName(statement.getFulledName());

        if (statement.getDocComment() != null) {
            entity.setDocComment(new DocumentComment(statement.getDocComment().getComment()));
        }

        entity.setTrace(statement.toTraceInfo(compiler.getContext()));
        entity.setType(statement.getClassType());

        List<ClassEntity> traits = fetchTraits();
        for (ClassEntity e : traits) {
            entity.addTrait(e);
        }

        checkAliasAndReplacementsTraits();

        if (statement.getExtend() != null) {
            ClassEntity parent = fetchClass(statement.getExtend().getName().getName());
            if (parent == null)
                compiler.getEnvironment().error(
                        statement.getExtend().toTraceInfo(compiler.getContext()),
                        Messages.ERR_CLASS_NOT_FOUND.fetch(statement.getExtend().getName().toName())
                );

            ClassEntity.ExtendsResult result = entity.setParent(parent, false);

            if (isInterfaceCheck) {
                result.check(compiler.getEnvironment());
            }
        }

        if (!isSystem) {
            if (entity.isUseJavaLikeNames()) {
                entity.setInternalName(
                        entity.getName().replace('\\', '/')
                );
            } else {
                entity.setInternalName(
                        compiler.getModule().getInternalName() + "_class" + compiler.getModule().getClasses().size()
                );
            }
        }

        if (compiler.getModule().findClass(entity.getLowerName()) != null
                || compiler.getEnvironment().isLoadedClass(entity.getLowerName())) {
            throw new FatalException(
                    Messages.ERR_CANNOT_REDECLARE_CLASS.fetch(entity.getName()),
                    statement.getName().toTraceInfo(compiler.getContext())
            );
        }

        if (!statement.isInterface()) {
            node.access = ACC_SUPER + ACC_PUBLIC;
            node.name = !isSystem /*&& !statement.isTrait()*/
                    ? entity.getCompiledInternalName()
                    : statement.getFulledName(Constants.NAME_DELIMITER);

            node.superName = entity.getParent() == null
                    ? Type.getInternalName(BaseObject.class)
                    : entity.getParent().getInternalName();

            node.sourceFile = compiler.getSourceFile();

            /*if (!isSystem) {
                AnnotationNode annotationNode = new AnnotationNode(Type.getInternalName(Reflection.Name.class));
                annotationNode.values = Arrays.asList("value", entity.getName());

                node.visibleAnnotations.add(annotationNode);
            } */

            writeSystemInfo();
            writeConstructor();
            writeDefaultConstructors();
        }

        // constants
        if (statement.getConstants() != null)
            for (ConstStmtToken constant : statement.getConstants()) {
                writeConstant(constant);
            }

        if (statement.getMethods() != null) {
            for (MethodStmtToken method : statement.getMethods()) {
                ClassEntity.SignatureResult result = entity.addMethod(
                        compiler.compileMethod(this, method, external, generatorEntity), null);
                result.check(compiler.getEnvironment());
            }
        }

        writeTraits(traits);
        ClassEntity.SignatureResult result = entity.updateParentBody();
        if (isInterfaceCheck) {
            result.check(compiler.getEnvironment());
        }

        writeImplements();
        entity.doneDeclare();

        if (!statement.isInterface()) {
            writeDestructor();

            if (entity.getType() != ClassEntity.Type.INTERFACE) {
                writeInitEnvironment();
            }

            writeInitStatic();
            cw = new JPHPClassWriter(entity.isTrait());
            node.accept(cw);

            entity.setData(cw.toByteArray());
        }
        return entity;
    }

    public void setClassContext(ClassStmtToken classContext) {
        this.classContext = classContext;
    }

    public ClassStmtToken getClassContext() {
        return classContext;
    }
}
