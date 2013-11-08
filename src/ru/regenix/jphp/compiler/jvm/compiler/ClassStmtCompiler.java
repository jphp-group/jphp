package ru.regenix.jphp.compiler.jvm.compiler;

import com.sun.xml.internal.ws.org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import ru.regenix.jphp.compiler.jvm.Constants;
import ru.regenix.jphp.compiler.jvm.JvmCompiler;
import ru.regenix.jphp.lexer.tokens.stmt.ClassStmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.ConstStmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.MethodStmtToken;
import ru.regenix.jphp.runtime.reflection.ClassEntity;

import static org.objectweb.asm.Opcodes.*;

public class ClassStmtCompiler extends StmtCompiler<ClassEntity> {
    protected ClassWriter cw;
    public final ClassStmtToken clazz;
    private ClassVisitor classWriter;

    public ClassStmtCompiler(JvmCompiler compiler, ClassStmtToken clazz) {
        super(compiler);
        this.clazz = clazz;

        entity = new ClassEntity(compiler.getContext());
        entity.setFinal(clazz.isFinal());
        entity.setAbstract(clazz.isAbstract());
        entity.setType(ClassEntity.Type.CLASS);
        entity.setName(clazz.getFulledName());
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

        //writeInitStatic();
        cw.visitEnd();
        entity.setData(cw.toByteArray());
        return entity;
    }

    public ClassWriter getClassWriter() {
        return cw;
    }
}
