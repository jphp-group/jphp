package ru.regenix.jphp.compiler.jvm.stetament;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.compiler.jvm.Constants;
import ru.regenix.jphp.compiler.jvm.JvmCompiler;
import ru.regenix.jphp.compiler.jvm.node.ClassNodeImpl;
import ru.regenix.jphp.compiler.jvm.node.MethodNodeImpl;
import ru.regenix.jphp.exceptions.CompileException;
import ru.regenix.jphp.exceptions.FatalException;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.lang.BaseObject;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.ClassEntity;
import ru.regenix.jphp.runtime.reflection.ConstantEntity;
import ru.regenix.jphp.runtime.reflection.MethodEntity;
import ru.regenix.jphp.runtime.reflection.PropertyEntity;
import ru.regenix.jphp.tokenizer.token.Token;
import ru.regenix.jphp.tokenizer.token.expr.value.FulledNameToken;
import ru.regenix.jphp.tokenizer.token.stmt.ClassStmtToken;
import ru.regenix.jphp.tokenizer.token.stmt.ClassVarStmtToken;
import ru.regenix.jphp.tokenizer.token.stmt.ConstStmtToken;
import ru.regenix.jphp.tokenizer.token.stmt.MethodStmtToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;

public class ClassStmtCompiler extends StmtCompiler<ClassEntity> {
    protected ClassWriter cw;
    public final ClassNode node;
    public final ClassStmtToken statement;
    public final List<TraceInfo> traceList = new ArrayList<TraceInfo>();
    private boolean external = false;
    private boolean isSystem = false;
    private boolean isInterfaceCheck = true;
    private String functionName = "";

    public ClassStmtCompiler(JvmCompiler compiler, ClassStmtToken statement) {
        super(compiler);
        this.statement = statement;
        this.node = new ClassNodeImpl();
    }

    public boolean isClosure(){
        return functionName == null;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }

    public void setInterfaceCheck(boolean check){
        isInterfaceCheck = check;
    }

    public boolean isExternal() {
        return external;
    }

    public void setExternal(boolean external) {
        this.external = external;
    }

    int addTraceInfo(int line, int position){
        traceList.add(new TraceInfo(compiler.getContext(), line, 0, position, 0));
        return traceList.size() - 1;
    }

    int addTraceInfo(Token token){
        traceList.add(token.toTraceInfo(compiler.getContext()));
        return traceList.size() - 1;
    }

    @SuppressWarnings("unchecked")
    protected void writeDestructor(){
        if (entity.methodDestruct != null){
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

    @SuppressWarnings("unchecked")
    protected void writeConstructor(){
        MethodNode constructor = new MethodNodeImpl();
        constructor.name = Constants.INIT_METHOD;
        constructor.access = ACC_PUBLIC;
        constructor.exceptions = new ArrayList();

        MethodStmtCompiler methodCompiler = new MethodStmtCompiler(this, constructor);
        ExpressionStmtCompiler expressionCompiler = new ExpressionStmtCompiler(methodCompiler, null);
        methodCompiler.writeHeader();

        LabelNode l0 = writeLabel(constructor, statement.getMeta().getStartLine());
        methodCompiler.addLocalVariable("this", l0);

        if (isClosure()){
            constructor.desc = Type.getMethodDescriptor(
                    Type.getType(void.class),
                    Type.getType(ClassEntity.class),
                    Type.getType(Memory.class),
                    Type.getType(Memory[].class)
            );
            methodCompiler.addLocalVariable("~class", l0, ClassEntity.class);
            methodCompiler.addLocalVariable("~self", l0, Memory.class);
            methodCompiler.addLocalVariable("~uses", l0, Memory[].class);

            methodCompiler.writeHeader();
            expressionCompiler.writeVarLoad("this");
            expressionCompiler.writeVarLoad("~class");
            expressionCompiler.writeVarLoad("~self");
            expressionCompiler.writeVarLoad("~uses");
            constructor.instructions.add(new MethodInsnNode(
                    INVOKESPECIAL,
                    node.superName,
                    Constants.INIT_METHOD,
                    constructor.desc
            ));
        } else {
            constructor.desc = Type.getMethodDescriptor(
                    Type.getType(void.class), Type.getType(Environment.class), Type.getType(ClassEntity.class)
            );
            methodCompiler.addLocalVariable("~env", l0, Environment.class);
            methodCompiler.addLocalVariable("~class", l0, String.class);

            // PROPERTIES
            for(ClassVarStmtToken property : statement.getProperties()){
                ExpressionStmtCompiler expressionStmtCompiler = new ExpressionStmtCompiler(methodCompiler, null);
                Memory value = Memory.NULL;
                if (property.getValue() != null)
                    value = expressionStmtCompiler.writeExpression(property.getValue(), true, true, false);

                PropertyEntity prop = new PropertyEntity(compiler.getContext());
                prop.setName(property.getVariable().getName());
                prop.setModifier(property.getModifier());
                prop.setStatic(property.isStatic());
                prop.setDefaultValue(value);
                entity.addProperty(prop);

                if (value == null && property.getValue() != null) {
                    throw new CompileException(
                        Messages.ERR_COMPILE_EXPECTED_CONST_VALUE.fetch(property.getVariable().getName()),
                        property.getVariable().toTraceInfo(compiler.getContext())
                    );
                }
            }

            expressionCompiler.writeVarLoad("this");
            expressionCompiler.writeVarLoad("~env");
            expressionCompiler.writeVarLoad("~class");
            constructor.instructions.add(new MethodInsnNode(
                    INVOKESPECIAL,
                    node.superName,
                    Constants.INIT_METHOD,
                    constructor.desc
            ));
        }

        methodCompiler.writeFooter();
        constructor.instructions.add(new InsnNode(RETURN));
        node.methods.add(constructor);
    }

    protected void writeConstant(ConstStmtToken constant){
        MethodStmtCompiler methodStmtCompiler = new MethodStmtCompiler(this, (MethodStmtToken)null);
        ExpressionStmtCompiler expressionStmtCompiler = new ExpressionStmtCompiler(methodStmtCompiler, null);

        Memory value = expressionStmtCompiler.writeExpression(constant.getValue(), true, true, false);
        if (value != null && !value.isArray()) {
            entity.addConstant(new ConstantEntity(constant.getFulledName(), value, true));
        } else
            throw new CompileException(
                    Messages.ERR_COMPILE_EXPECTED_CONST_VALUE.fetch(constant.getFulledName()),
                    constant.getValue().toTraceInfo(compiler.getContext())
            );
    }

    @SuppressWarnings("unchecked")
    protected void writeSystemInfo(){
        node.fields.add(new FieldNode(
                ACC_PROTECTED + ACC_FINAL + ACC_STATIC, "$FN",
                Type.getDescriptor(String.class),
                null,
                compiler.getSourceFile()
        ));

        node.fields.add(new FieldNode(
                ACC_PROTECTED + ACC_STATIC, "$TRC",
                Type.getDescriptor(TraceInfo[].class),
                null,
                null
        ));

        if (functionName != null){
            node.fields.add(new FieldNode(
                    ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "$CL",
                    Type.getDescriptor(String.class),
                    null,
                    !functionName.isEmpty() ? functionName : entity.getName()
            ));
        }
    }

    @SuppressWarnings("unchecked")
    protected void writeInitStatic(){
        MethodNode node = new MethodNodeImpl();
        node.access = ACC_STATIC;
        node.name = Constants.STATIC_INIT_METHOD;
        node.desc = Type.getMethodDescriptor(Type.getType(void.class));

        MethodStmtCompiler methodCompiler = new MethodStmtCompiler(this, node);
        ExpressionStmtCompiler expressionCompiler = new ExpressionStmtCompiler(methodCompiler, null);
        methodCompiler.writeHeader();

        expressionCompiler.writePushSmallInt(traceList.size());
        node.instructions.add(new TypeInsnNode(ANEWARRAY, Type.getInternalName(TraceInfo.class)));
        expressionCompiler.stackPush(Memory.Type.REFERENCE);

        int i = 0;
        for(TraceInfo traceInfo : traceList){
            expressionCompiler.writePushDup();
            expressionCompiler.writePushSmallInt(i);
            expressionCompiler.writePushCreateTraceInfo(traceInfo.getStartLine(), traceInfo.getStartPosition());

            node.instructions.add(new InsnNode(AASTORE));
            expressionCompiler.stackPop();
            expressionCompiler.stackPop();
            i++;
        }
        expressionCompiler.writePutStatic("$TRC", TraceInfo[].class);

        node.instructions.add(new InsnNode(RETURN));
        methodCompiler.writeFooter();

        this.node.methods.add(node);
    }

    protected void writeInterfaceMethod(MethodEntity method){
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

        String internalName = entity.findMethod(method.getName()).getInternalName();
        expressionCompiler.writeSysDynamicCall(null, internalName, Memory.class, Environment.class, Memory[].class);

        node.instructions.add(new InsnNode(ARETURN));
        methodCompiler.writeFooter();
        this.node.methods.add(node);
    }

    protected void writeInterfaceMethods(Collection<MethodEntity> methods){
        for(MethodEntity method : methods){
            writeInterfaceMethod(method);
        }
    }

    protected ClassEntity fetchClass(String name){
        ClassEntity result = compiler.getModule().findClass(name);
        if (result == null)
            result = getCompiler().getEnvironment().fetchClass(name, true);

        return result;
    }

    @SuppressWarnings("unchecked")
    protected void writeImplements(){
        if (statement.getImplement() != null){
            Environment env = compiler.getEnvironment();
            for(FulledNameToken name : statement.getImplement()){
                ClassEntity implement = fetchClass(name.getName());
                if (implement == null) {
                    env.error(
                            name.toTraceInfo(compiler.getContext()),
                            Messages.ERR_FATAL_INTERFACE_NOT_FOUND.fetch(name.toName())
                    );
                } else {
                    if (implement.getType() != ClassEntity.Type.INTERFACE){
                        env.error(
                                name.toTraceInfo(compiler.getContext()),
                                Messages.ERR_FATAL_CANNOT_IMPLEMENT.fetch(entity.getName(), implement.getName())
                        );
                    }
                    if (implement.isInternal())
                        node.interfaces.add(implement.getInternalName());
                }
                ClassEntity.MethodsResult addResult = entity.addInterface(implement);
                addResult.check(env);

                if (implement != null && implement.isInternal())
                    writeInterfaceMethods(implement.getMethods().values());
            }
        }
    }

    @Override
    public ClassEntity compile() {
        entity = new ClassEntity(compiler.getContext());
        entity.setId(compiler.getScope().nextClassIndex());

        entity.setFinal(statement.isFinal());
        entity.setAbstract(statement.isAbstract());
        entity.setType(ClassEntity.Type.CLASS);
        entity.setName(statement.getFulledName());
        entity.setTrace(statement.toTraceInfo(compiler.getContext()));
        if (statement.isInterface()){
            entity.setType(ClassEntity.Type.INTERFACE);
        }

        if (statement.getExtend() != null) {
            ClassEntity parent = fetchClass(statement.getExtend().getName().getName());
            ClassEntity.ParentResult result = entity.setParent(parent, false);
            result.check(compiler.getEnvironment());
        }

        if (!isSystem)
            entity.setInternalName("$_php_class_" + compiler.getScope().nextClassIndex());

        if (compiler.getModule().findClass(entity.getLowerName()) != null
              || compiler.getEnvironment().isLoadedClass(entity.getLowerName())){
            throw new FatalException(
                    Messages.ERR_FATAL_CANNOT_REDECLARE_CLASS.fetch(entity.getName()),
                    statement.getName().toTraceInfo(compiler.getContext())
            );
        }

        if (!statement.isInterface()){
            node.access = ACC_SUPER + ACC_PUBLIC;
            node.name = !isSystem ? entity.getInternalName() : statement.getFulledName(Constants.NAME_DELIMITER);
            node.superName = entity.getParent() == null
                    ? Type.getInternalName(BaseObject.class)
                    : entity.getParent().getInternalName();

            node.sourceFile = compiler.getSourceFile();

            writeSystemInfo();
            writeConstructor();
        }

        // constants
        if (statement.getConstants() != null)
            for(ConstStmtToken constant : statement.getConstants()){
                  writeConstant(constant);
        }

        if (statement.getMethods() != null){
            for (MethodStmtToken method : statement.getMethods()){
                ClassEntity.MethodsResult result = entity.addMethod(compiler.compileMethod(this, method, external));
                result.check(compiler.getEnvironment());
            }
        }

        ClassEntity.MethodsResult result = entity.updateParentMethods();
        if (isInterfaceCheck)
            result.check(compiler.getEnvironment());

        writeImplements();
        entity.doneDeclare();

        if (!statement.isInterface()){
            writeDestructor();
            writeInitStatic();

            cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES); // !!! IMPORTANT use COMPUTE_FRAMES
            node.accept(cw);

            entity.setData(cw.toByteArray());
        }
        return entity;
    }

    ClassWriter getClassWriter() {
        return cw;
    }
}
