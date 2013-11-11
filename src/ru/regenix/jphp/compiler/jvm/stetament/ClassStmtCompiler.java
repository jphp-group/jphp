package ru.regenix.jphp.compiler.jvm.stetament;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import ru.regenix.jphp.compiler.jvm.Constants;
import ru.regenix.jphp.compiler.jvm.JvmCompiler;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.stmt.ClassStmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.ConstStmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.MethodStmtToken;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.memory.Memory;
import ru.regenix.jphp.runtime.reflection.ClassEntity;

import java.util.ArrayList;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;

public class ClassStmtCompiler extends StmtCompiler<ClassEntity> {
    protected ClassWriter cw;
    public final ClassStmtToken clazz;
    public final List<TraceInfo> traceList = new ArrayList<TraceInfo>();

    public ClassStmtCompiler(JvmCompiler compiler, ClassStmtToken clazz) {
        super(compiler);
        this.clazz = clazz;

        entity = new ClassEntity(compiler.getContext());
        entity.setFinal(clazz.isFinal());
        entity.setAbstract(clazz.isAbstract());
        entity.setType(ClassEntity.Type.CLASS);
        entity.setName(clazz.getFulledName());
    }

    int addTraceInfo(int line, int position){
        traceList.add(new TraceInfo(compiler.getContext(), line, 0, position, 0));
        return traceList.size() - 1;
    }

    int addTraceInfo(Token token){
        traceList.add(token.toTraceInfo(compiler.getContext()));
        return traceList.size() - 1;
    }

    protected void writeConstructor(){
        MethodVisitor mv;

        mv = cw.visitMethod(ACC_PUBLIC, Constants.INIT_METHOD, "()V", null, null);
        mv.visitCode();
        Label l0 = writeLabel(mv, clazz.getMeta().getStartLine());

        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, Constants.OBJECT_CLASS, Constants.INIT_METHOD, "()V");
        mv.visitInsn(RETURN);

        mv.visitLocalVariable(
                "this",
                "L_" + clazz.getFulledName(Constants.NAME_DELIMITER) + ";", null, l0, l0, 0
        );
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }

    protected void writeConstant(ConstStmtToken constant){
        new ConstantStmtCompiler(this, constant).compile();
    }

    protected void writeSystemInfo(){
        cw.visitField(
                ACC_PROTECTED + ACC_FINAL + ACC_STATIC, "__FN",
                Type.getDescriptor(String.class),
                null,
                compiler.getSourceFile()
        );

        cw.visitField(
                ACC_PROTECTED + ACC_STATIC, "__TRACE",
                Type.getDescriptor(TraceInfo[].class),
                null,
                null
        );
    }

    protected void writeInitStatic(){
        MethodVisitor mv = cw.visitMethod(
                ACC_STATIC, Constants.STATIC_INIT_METHOD,
                Type.getMethodDescriptor(Type.getType(void.class)),
                null, null
        );


        MethodStmtCompiler methodCompiler = new MethodStmtCompiler(this, mv, Constants.STATIC_INIT_METHOD);
        ExpressionStmtCompiler expressionCompiler = new ExpressionStmtCompiler(methodCompiler, null);
        methodCompiler.writeHeader();

        expressionCompiler.writePushSmallInt(traceList.size());
        mv.visitTypeInsn(ANEWARRAY, Type.getInternalName(TraceInfo.class));
        expressionCompiler.stackPush(Memory.Type.REFERENCE);

        int i = 0;
        for(TraceInfo traceInfo : traceList){
            expressionCompiler.writePushDup();
            expressionCompiler.writePushSmallInt(i);
            expressionCompiler.writePushCreateTraceInfo(traceInfo.getStartLine(), traceInfo.getStartPosition());
            mv.visitInsn(AASTORE);
            expressionCompiler.stackPop();
            expressionCompiler.stackPop();
            i++;
        }
        expressionCompiler.writePutStatic("__TRACE", TraceInfo[].class);

        mv.visitInsn(RETURN);
        methodCompiler.writeFooter();
    }

    @Override
    public ClassEntity compile() {
        cw = new ClassWriter(0);
        cw.visit(
                V1_6, ACC_SUPER + ACC_PUBLIC, clazz.getFulledName(Constants.NAME_DELIMITER), null,
                Constants.OBJECT_CLASS, null
        );
        cw.visitSource(compiler.getSourceFile(), null);
        writeConstructor();

        // constants
        for(ConstStmtToken constant : clazz.getConstants()){
            writeConstant(constant);
        }

        for (MethodStmtToken method : clazz.getMethods()){
            entity.addMethod(compiler.compileMethod(this, method));
        }

        writeSystemInfo();
        writeInitStatic();
        cw.visitEnd();
        entity.setData(cw.toByteArray());
        return entity;
    }

    ClassWriter getClassWriter() {
        return cw;
    }
}
