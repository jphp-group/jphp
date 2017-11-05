package org.develnext.jphp.core.compiler.jvm.statement;

import org.develnext.jphp.core.compiler.common.misc.StackItem;
import org.develnext.jphp.core.compiler.jvm.Constants;
import org.develnext.jphp.core.compiler.jvm.misc.JumpItem;
import org.develnext.jphp.core.compiler.jvm.misc.LocalVariable;
import org.develnext.jphp.core.compiler.jvm.node.MethodNodeImpl;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.value.NameToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.StaticAccessExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.*;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import php.runtime.Memory;
import php.runtime.common.Messages;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.lang.IObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.helper.ClassConstantMemory;
import php.runtime.memory.helper.ConstantMemory;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.DocumentComment;
import php.runtime.reflection.MethodEntity;
import php.runtime.reflection.ParameterEntity;
import php.runtime.reflection.helper.GeneratorEntity;
import php.runtime.reflection.support.TypeChecker;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.NEW;

public class MethodStmtCompiler extends StmtCompiler<MethodEntity> {
    public final ClassStmtCompiler clazz;
    public MethodStmtToken statement;
    public final MethodNode node;

    protected int statementIndex = 0;

    private Stack<StackItem> stack = new Stack<StackItem>();
    private final List<JumpItem> jumpStack = new ArrayList<JumpItem>();
    final Stack<TryCatchItem> tryStack = new Stack<TryCatchItem>();
    final List<BodyStmtToken> finallyBlocks = new ArrayList<>();

    private int stackLevel = 0;
    private int stackSize = 0;
    private int stackMaxSize = 0;

    private Map<String, LabelNode> gotoLabels;
    private Map<String, LocalVariable> localVariables;
    protected String realName;

    private boolean external = false;

    private long methodId;
    private LabelNode labelStart;

    private Map<Class<?>, AtomicInteger> statementIndexes = new HashMap<Class<?>, AtomicInteger>();
    private Set<Integer> lineTickHandled = new HashSet<>();

    private GeneratorEntity generatorEntity;

    public MethodStmtCompiler(ClassStmtCompiler clazz, MethodNode node){
        super(clazz.getCompiler());
        this.clazz = clazz;
        this.statement = null;
        this.node   = node;

        this.localVariables = new LinkedHashMap<>();

        entity = new MethodEntity(getCompiler().getContext());
        entity.setClazz(clazz.entity);
        entity.setName(node.name);
        realName = entity.getName();

        methodId = compiler.getScope().nextMethodIndex();
    }

    public MethodStmtCompiler(ClassStmtCompiler clazz, MethodStmtToken statement) {
        super(clazz.getCompiler());
        this.clazz = clazz;
        this.statement = statement;
        this.node  = new MethodNodeImpl();

        this.localVariables = new LinkedHashMap<>();

        entity = new MethodEntity(getCompiler().getContext());
        entity.setClazz(clazz.entity);
        if (statement != null)
            entity.setName(statement.getName().getName());

        realName = entity.getName();
        methodId = compiler.getScope().nextMethodIndex();
    }

    public long getMethodId() {
        return methodId;
    }

    public long getClassId(){
        return clazz.entity.getId();
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public boolean isExternal() {
        return external;
    }

    public void setExternal(boolean external) {
        this.external = external;
    }

    public GeneratorEntity getGeneratorEntity() {
        return generatorEntity;
    }

    public void setGeneratorEntity(GeneratorEntity generatorEntity) {
        this.generatorEntity = generatorEntity;
    }

    public Stack<TryCatchItem> getTryStack() {
        return tryStack;
    }

    public Map<String, LocalVariable> getLocalVariables() {
        return localVariables;
    }

    public boolean registerTickTrigger(int line) {
        return lineTickHandled.add(line);
    }

    public int nextStatementIndex(Class<?> clazz){
        AtomicInteger atomic = statementIndexes.get(clazz);
        if (atomic == null)
            statementIndexes.put(clazz, atomic = new AtomicInteger());

        return atomic.getAndIncrement();
    }

    public int prevStatementIndex(Class<?> clazz){
        AtomicInteger atomic = statementIndexes.get(clazz);
        if (atomic == null)
            statementIndexes.put(clazz, atomic = new AtomicInteger());

        return atomic.getAndDecrement();
    }

    public void pushJump(LabelNode breakLabel, LabelNode continueLabel, int stackSize){
        jumpStack.add(new JumpItem(breakLabel, continueLabel, stackSize));
    }

    public void pushJump(LabelNode breakLabel, LabelNode continueLabel){
        pushJump(breakLabel, continueLabel, 0);
    }

    public JumpItem getJump(int level){
        if (jumpStack.size() - level < 0)
            return null;
        if (jumpStack.size() - level >= jumpStack.size())
            return null;

        return jumpStack.get(jumpStack.size() - level);
    }

    public int getJumpStackSize(int level){
        int size = 0;
        for(int i = jumpStack.size(); i >= 0 && jumpStack.size() - i < level; i--){
            JumpItem item = getJump(i);
            size += item.stackSize;
        }
        return size;
    }

    public void popJump(){
        jumpStack.remove(jumpStack.size() - 1);
    }

    void push(StackItem item) {
        if (item.getLevel() == -1)
            item.setLevel(stackLevel++);

        stack.push(item);
        stackSize += item.size;
        if (stackMaxSize < stackSize)
            stackMaxSize = stackSize;
    }

    int getStackSize(){
        return stackSize;
    }

    int getStackCount(){
        return stack.size();
    }

    StackItem pop(){
        StackItem item = stack.pop();
        stackSize -= item.size;
        return item;
    }

    void popAll(){
        stackSize = 0;
        stack.clear();
    }

    StackItem peek(){
        return stack.peek();
    }

    public LabelNode getGotoLabel(String name) {
        return gotoLabels == null ? null : gotoLabels.get(name.toLowerCase());
    }

    public LabelNode getOrCreateGotoLabel(String name) {
        name = name.toLowerCase();
        if (gotoLabels == null)
            gotoLabels = new HashMap<String, LabelNode>();

        LabelNode label = gotoLabels.get(name);
        if (label == null)
            gotoLabels.put(name, label = new LabelNode());

        return label;
    }

    public LocalVariable addLocalVariable(String variable, LabelNode label, Class clazz){
        LocalVariable result;
        localVariables.put(
                variable,
                result = new LocalVariable(variable, localVariables.size(), label, clazz)
        );
        return result;
    }

    public LocalVariable getOrAddLocalVariable(String variable, LabelNode label, Class clazz){
        LocalVariable local = getLocalVariable(variable);
        if (local == null){
            local = addLocalVariable(variable, label, clazz);
        } else {
            if (local.getClazz() != clazz)
                throw new RuntimeException(
                        "Invalid class for " + variable + " variable, " + local.getClazz() + " != " + clazz.getClass()
                );
        }
        return local;
    }

    public LocalVariable addLocalVariable(String variable, LabelNode label){
        return addLocalVariable(variable, label, Memory.class);
    }

    public LocalVariable getLocalVariable(String variable){
        return localVariables.get(variable);
    }

    void writeHeader(){
        int access = 0;
        if (statement != null){
            if (compiler.getScope().isDebugMode()) {
                statement.setDynamicLocal(true);
            }

            switch (statement.getModifier()){
                case PRIVATE: access += Opcodes.ACC_PRIVATE; break;
                case PROTECTED: access += Opcodes.ACC_PROTECTED; break;
                case PUBLIC: access += Opcodes.ACC_PUBLIC; break;
            }

            if (statement.isStatic()) access += Opcodes.ACC_STATIC;
            //if (statement.isAbstract()) access += Opcodes.ACC_ABSTRACT;
            if (statement.isFinal()) access += Opcodes.ACC_FINAL;

            node.access = access;
            node.name = clazz.isSystem() || entity == null ? statement.getName().getName() : entity.getInternalName();
            node.desc = Type.getMethodDescriptor(
                    Type.getType(Memory.class),
                    Type.getType(Environment.class),
                    Type.getType(Memory[].class)
            );

            if (external){
                node.desc = Type.getMethodDescriptor(
                        Type.getType(Memory.class),
                        Type.getType(Environment.class),
                        Type.getType(Memory[].class),
                        Type.getType(ArrayMemory.class)
                );
            }
        }

        if (statement != null){
            LabelNode label = labelStart = writeLabel(node, statement.getMeta().getStartLine());

            if (!statement.isStatic())
                addLocalVariable("~this", label, Object.class);

            ExpressionStmtCompiler expressionCompiler = new ExpressionStmtCompiler(this, null);

            addLocalVariable("~env", label, Environment.class); // Environment env
            LocalVariable args = addLocalVariable("~args", label, Memory[].class);  // Memory[] arguments

            if (statement.isDynamicLocal()){
                if (external)
                    addLocalVariable("~passedLocal", label, ArrayMemory.class);

                LocalVariable local = addLocalVariable("~local", label, ArrayMemory.class);

                if (external){
                    expressionCompiler.writeVarLoad("~passedLocal");
                    expressionCompiler.writeSysStaticCall(ArrayMemory.class, "valueOfRef", ArrayMemory.class, ArrayMemory.class);
                    expressionCompiler.setStackPeekAsImmutable();
                    expressionCompiler.writeVarStore(local, false, true);
                } else {
                    expressionCompiler.writePushConstNull();
                    expressionCompiler.writeSysStaticCall(ArrayMemory.class, "valueOfRef", ArrayMemory.class, ArrayMemory.class);
                    expressionCompiler.setStackPeekAsImmutable();
                    expressionCompiler.writeVarStore(local, false, true);
                }
            }

            if (statement.getUses() != null && !statement.getUses().isEmpty()){
                int i = 0;
                expressionCompiler.writeVarLoad("~this");
                expressionCompiler.writeGetDynamic("uses", Memory[].class);

                for (ArgumentStmtToken argument : statement.getUses()) {
                    LocalVariable local;
                    if (statement.isDynamicLocal()) {
                        expressionCompiler.writeDefineVariable(argument.getName());
                        local = getLocalVariable(argument.getName().getName());
                    } else {
                        local = addLocalVariable(argument.getName().getName(), label, Memory.class);
                    }

                    if (argument.isReference()){
                        local.setReference(true);
                        statement.variable(argument.getName()).setUnstable(true);
                    }

                    expressionCompiler.writePushDup();
                    expressionCompiler.writePushGetFromArray(i, Memory.class);

                    if (statement.isDynamicLocal()) {
                        expressionCompiler.writeVarAssign(local, argument.getName(), false, false);
                    } else {
                        expressionCompiler.writeVarStore(local, false, false);
                    }

                    local.pushLevel();
                    i++;
                }
                expressionCompiler.writePopAll(1);
            }

            int i = 0;

            for(ArgumentStmtToken argument : statement.getArguments()){
                if (argument.isReference()){
                    statement.variable(argument.getName())
                            .setReference(true)
                            .setUnstable(true);
                }

                LabelNode next = new LabelNode();

                expressionCompiler.writeDefineVariable(argument.getName());
                LocalVariable local = getLocalVariable(argument.getName().getName());

                if (local != null) {
                    expressionCompiler.writeVarLoad(args);
                    expressionCompiler.writePushGetFromArray(i, Memory.class);
                    expressionCompiler.writeVarAssign(local, argument.getName(), true, false);

                    // if length <= i then undefined
                    node.instructions.add(new JumpInsnNode(Opcodes.IFNONNULL, next));
                    expressionCompiler.stackPop();

                    if (argument.getValue() == null)
                        expressionCompiler.writePushNull();
                    else
                        expressionCompiler.writeExpression(argument.getValue(), true, false);

                    expressionCompiler.writeVarAssign(local, argument.getName(), false, false);
                    node.instructions.add(next);

                    local.pushLevel();
                }

                i++;
            }
        } else {
            LabelNode label = labelStart = writeLabel(node, clazz.statement.getMeta().getStartLine());
        }
    }

    @SuppressWarnings("unchecked")
    void writeFooter(){
        LabelNode endL = new LabelNode();
        node.instructions.add(endL);

        for(LocalVariable variable : localVariables.values()){
            String description = Type.getDescriptor(variable.getClazz() == null ? Object.class : variable.getClazz());
            if (variable.name.equals("~this")) {
                //if (variable.getClazz() != Memory.class && !clazz.statement.isTrait()) {
                    description = "L" + clazz.entity.getCompiledInternalName() + ";";
                //}
            }

            node.localVariables.add(new LocalVariableNode(
                    variable.name,
                    description,
                    null,
                    variable.label == null ? labelStart : variable.label,
                    variable.getEndLabel() == null ? endL : variable.getEndLabel(),
                    variable.index
            ));
        }

        //node.maxStack = this.stackMaxSize;  !!! we don't need this, see: ClassWriter.COMPUTE_FRAMES
        //node.maxLocals = this.localVariables.size();
    }

    @Override
    public MethodEntity compile() {
        if (statement != null){
            if (external)
                statement.setDynamicLocal(true);

            if (statement.getDocComment() != null)
                entity.setDocComment(new DocumentComment(statement.getDocComment().getComment()));

            entity.setAbstract(statement.isAbstract());
            entity.setAbstractable(statement.getBody() == null);
            entity.setFinal(statement.isFinal());
            entity.setStatic(statement.isStatic());
            entity.setModifier(statement.getModifier());
            entity.setReturnReference(statement.isReturnReference());
            entity.setTrace(statement.toTraceInfo(compiler.getContext()));
            entity.setImmutable(statement.getArguments().isEmpty());
            entity.setGeneratorEntity(generatorEntity);

            if (statement.getReturnHintTypeClass() != null) {
                entity.setReturnTypeChecker(TypeChecker.of(statement.getReturnHintTypeClass().getName()));
            } else if (statement.getReturnHintType() != null) {
                entity.setReturnTypeChecker(TypeChecker.of(statement.getReturnHintType()));
            }

            entity.setReturnTypeNullable(statement.isReturnOptional());

            if (clazz.isSystem())
                entity.setInternalName(entity.getName());
            else
                entity.setInternalName(entity.getName() + "$" + clazz.entity.nextMethodIndex());

            ParameterEntity[] parameters = new ParameterEntity[statement.getArguments().size()];
            int i = 0;
            for(ArgumentStmtToken argument : statement.getArguments()){
                parameters[i] = new ParameterEntity(compiler.getContext());
                ParameterEntity parameter = parameters[i];

                parameter.setReference(argument.isReference());
                parameter.setName(argument.getName().getName());
                parameter.setTrace(argument.toTraceInfo(compiler.getContext()));
                parameter.setNullable(argument.isOptional());

                parameter.setMutable(
                        statement.isDynamicLocal() || statement.variable(argument.getName()).isMutable()
                );

                parameter.setUsed(
                        !statement.isUnusedVariable(argument.getName())
                );

                parameter.setVariadic(argument.isVariadic());

                parameter.setType(argument.getHintType());

                if (argument.getHintTypeClass() != null) {
                    parameter.setTypeClass(argument.getHintTypeClass().getName());
                }

                ExpressionStmtCompiler expressionStmtCompiler = new ExpressionStmtCompiler(compiler);
                ExprStmtToken value = argument.getValue();

                if (value != null) {
                    Memory defaultValue = expressionStmtCompiler.writeExpression(value, true, true, false);

                    // try detect constant
                    if (value.isSingle()) {
                        if (value.getSingle() instanceof NameToken){
                            parameter.setDefaultValueConstName(((NameToken) value.getSingle()).getName());
                            if (defaultValue == null) {
                                defaultValue = (new ConstantMemory(((NameToken) value.getSingle()).getName()));
                                parameter.setMutable(true);
                            }
                        } else if (value.getSingle() instanceof StaticAccessExprToken){
                            StaticAccessExprToken access = (StaticAccessExprToken)value.getSingle();

                            if (access.getClazz() instanceof NameToken && access.getField() instanceof NameToken){
                                if (defaultValue == null)
                                    defaultValue = (new ClassConstantMemory(
                                            ((NameToken) access.getClazz()).getName(),
                                            ((NameToken) access.getField()).getName()
                                    ));

                                parameter.setDefaultValueConstName(
                                        ((NameToken) access.getClazz()).getName() + "::" +
                                                ((NameToken) access.getField()).getName()
                                );
                                parameter.setMutable(true);
                            }
                        }
                    }

                    if (defaultValue == null)
                        compiler.getEnvironment().error(
                                argument.toTraceInfo(compiler.getContext()), ErrorType.E_COMPILE_ERROR,
                                Messages.ERR_EXPECTED_CONST_VALUE, "$" + argument.getName().getName()
                        );

                    parameter.setDefaultValue(defaultValue);
                }
                i++;
            }

            entity.setParameters(parameters);
        }

        if (statement != null && clazz.statement.isInterface()){
            if (!statement.isInterfacable()){
                compiler.getEnvironment().error(entity.getTrace(),
                        Messages.ERR_INTERFACE_FUNCTION_CANNOT_CONTAIN_BODY.fetch(entity.getSignatureString(false))
                );
            }
            if (statement.isAbstract() || statement.isFinal()){
                compiler.getEnvironment().error(entity.getTrace(),
                        Messages.ERR_ACCESS_TYPE_FOR_INTERFACE_METHOD.fetch(entity.getSignatureString(false))
                );
            }
        } else {
            writeHeader();

            if (statement.isGenerator()) {
                entity.setEmpty(false);
                entity.setImmutable(false);
                entity.setResult(null);
                GeneratorStmtCompiler generatorStmtCompiler = new GeneratorStmtCompiler(compiler, statement);
                entity.setGeneratorEntity(generatorStmtCompiler.compile());

                ExpressionStmtCompiler expr = new ExpressionStmtCompiler(this, null);

                expr.makeUnknown(new TypeInsnNode(NEW, entity.getGeneratorEntity().getInternalName()));
                expr.stackPush(Memory.Type.REFERENCE);
                expr.writePushDup();

                // env
                expr.writePushEnv();

                // classEntity
                expr.writePushDup();
                expr.writePushConstString(compiler.getModule().getInternalName());
                expr.writePushConstInt((int) entity.getGeneratorEntity().getId());
                expr.writeSysDynamicCall(Environment.class, "__getGenerator", ClassEntity.class, String.class, Integer.TYPE);

                // self
                expr.writePushThis();

                // uses
                expr.writeVarLoad("~args");

                expr.writeSysCall(
                        entity.getGeneratorEntity().getInternalName(), INVOKESPECIAL, Constants.INIT_METHOD, void.class,
                        Environment.class, ClassEntity.class, Memory.class, Memory[].class
                );
                expr.writeSysStaticCall(ObjectMemory.class, "valueOf", Memory.class, IObject.class);

                expr.makeUnknown(new InsnNode(Opcodes.ARETURN));
                expr.stackPop();
            } else {
                ExpressionStmtCompiler expr = new ExpressionStmtCompiler(this, null);

                entity.setEmpty(true);

                if (entity.getResult() == null) {
                    entity.setResult(Memory.UNDEFINED);
                }

                if (statement != null && statement.getBody() != null) {
                    expr.writeDefineVariables(statement.getLocal());
                    expr.write(statement.getBody());

                    if (!statement.getBody().getInstructions().isEmpty()) {
                        entity.setEmpty(false);

                        if (entity.getResult() != null && entity.getResult().isUndefined()) {
                            entity.setResult(null);
                        }
                    }
                }

                if (generatorEntity != null) {
                    expr.writeVarLoad("~this");
                    expr.writePushConstBoolean(false);
                    expr.writeSysDynamicCall(null, "_setValid", void.class, Boolean.TYPE);
                }

                ReturnStmtToken token = new ReturnStmtToken(new TokenMeta("", 0, 0, 0, 0));
                token.setValue(null);
                token.setEmpty(true);
                expr.getCompiler(ReturnStmtToken.class).write(token);
            }

            writeFooter();
        }
        return entity;
    }


    public static class TryCatchItem {
        private final TryStmtToken token;
        private final LabelNode returnLabel;

        public TryCatchItem(TryStmtToken token, LabelNode returnLabel) {
            this.token = token;
            this.returnLabel = returnLabel;
        }

        public TryStmtToken getToken() {
            return token;
        }

        public LabelNode getReturnLabel() {
            return returnLabel;
        }
    }
}
