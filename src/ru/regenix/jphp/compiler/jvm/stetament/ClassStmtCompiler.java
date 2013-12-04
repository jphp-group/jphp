package ru.regenix.jphp.compiler.jvm.stetament;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.compiler.jvm.Constants;
import ru.regenix.jphp.compiler.jvm.JvmCompiler;
import ru.regenix.jphp.compiler.jvm.node.ClassNodeImpl;
import ru.regenix.jphp.compiler.jvm.node.MethodNodeImpl;
import ru.regenix.jphp.exceptions.FatalException;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.lang.PHPObject;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.ClassEntity;
import ru.regenix.jphp.tokenizer.token.Token;
import ru.regenix.jphp.tokenizer.token.stmt.ClassStmtToken;
import ru.regenix.jphp.tokenizer.token.stmt.ConstStmtToken;
import ru.regenix.jphp.tokenizer.token.stmt.MethodStmtToken;

import java.util.ArrayList;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;

public class ClassStmtCompiler extends StmtCompiler<ClassEntity> {
    protected ClassWriter cw;
    public final ClassNode node;
    public final ClassStmtToken statement;
    public final List<TraceInfo> traceList = new ArrayList<TraceInfo>();
    private boolean external = false;
    private boolean isSystem = false;
    private String functionName = "";

    public ClassStmtCompiler(JvmCompiler compiler, ClassStmtToken statement) {
        super(compiler);
        this.statement = statement;
        this.node = new ClassNodeImpl();
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
    protected void writeConstructor(){
        MethodNode constructor = new MethodNodeImpl();
        constructor.name = Constants.INIT_METHOD;
        constructor.access = ACC_PUBLIC;
        constructor.desc = Type.getMethodDescriptor(Type.getType(void.class), Type.getType(ClassEntity.class));
        constructor.exceptions = new ArrayList();

        LabelNode l0 = writeLabel(constructor, statement.getMeta().getStartLine());
        constructor.instructions.add(new VarInsnNode(ALOAD, 0)); // this
        constructor.instructions.add(new VarInsnNode(ALOAD, 1)); // __class__
        constructor.instructions.add(new MethodInsnNode(
                INVOKESPECIAL,
                node.superName,
                Constants.INIT_METHOD,
                constructor.desc
        ));
        constructor.instructions.add(new InsnNode(RETURN));
        constructor.localVariables = new ArrayList();

        constructor.localVariables.add(new LocalVariableNode(
           "this",
           "L_" + statement.getFulledName(Constants.NAME_DELIMITER) + ";",
            null, l0, l0, 0
        ));
        constructor.maxStack = 1;
        constructor.maxLocals = 1;

        node.methods.add(constructor);
    }

    protected void writeConstant(ConstStmtToken constant){
        new ConstantStmtCompiler(this, constant).compile();
    }

    protected void writeSystemInfo(){
        node.fields.add(new FieldNode(
                ACC_PROTECTED + ACC_FINAL + ACC_STATIC, "$FN",
                Type.getDescriptor(String.class),
                null,
                compiler.getSourceFile()
        ));

        node.fields.add(new FieldNode(
                ACC_PROTECTED + ACC_STATIC, "$TRACE",
                Type.getDescriptor(TraceInfo[].class),
                null,
                null
        ));
    }

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
        expressionCompiler.writePutStatic("$TRACE", TraceInfo[].class);

        node.instructions.add(new InsnNode(RETURN));
        methodCompiler.writeFooter();

        this.node.methods.add(node);
    }

    @Override
    public ClassEntity compile() {
        entity = new ClassEntity(compiler.getContext());
        entity.setFinal(statement.isFinal());
        entity.setAbstract(statement.isAbstract());
        entity.setType(ClassEntity.Type.CLASS);
        entity.setName(statement.getFulledName());

        if (compiler.getModule().findClass(entity.getLowerName()) != null
              || compiler.getEnvironment().isLoadedClass(entity.getLowerName())){
            throw new FatalException(
                    Messages.ERR_FATAL_CANNOT_REDECLARE_CLASS.fetch(entity.getName()),
                    statement.getName().toTraceInfo(compiler.getContext())
            );
        }

        node.access = ACC_SUPER + ACC_PUBLIC;
        node.name = statement.getFulledName(Constants.NAME_DELIMITER);
        node.superName = Type.getInternalName(PHPObject.class);
        node.sourceFile = compiler.getSourceFile();

        writeConstructor();

        // constants
        if (statement.getConstants() != null)
        for(ConstStmtToken constant : statement.getConstants()){
//            writeConstant(constant);
        }

        if (statement.getMethods() != null)
        for (MethodStmtToken method : statement.getMethods()){
            entity.addMethod(compiler.compileMethod(this, method, external));
        }

        writeSystemInfo();
        writeInitStatic();

        cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES); // !!! IMPORTANT use COMPUTE_FRAMES
        node.accept(cw);

        entity.doneDeclare();
        entity.setData(cw.toByteArray());
        return entity;
    }

    ClassWriter getClassWriter() {
        return cw;
    }
}
