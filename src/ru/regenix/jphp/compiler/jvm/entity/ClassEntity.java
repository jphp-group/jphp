package ru.regenix.jphp.compiler.jvm.entity;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import ru.regenix.jphp.compiler.jvm.Constants;
import ru.regenix.jphp.compiler.jvm.JvmCompiler;
import ru.regenix.jphp.lexer.tokens.stmt.ClassStmtToken;

import static org.objectweb.asm.Opcodes.*;

public class ClassEntity extends Entity {
    protected ClassWriter cw;
    protected final ClassStmtToken clazz;

    public ClassEntity(JvmCompiler compiler, ClassStmtToken clazz) {
        super(compiler);
        this.clazz = clazz;
    }

    protected void writeConstructor(ClassStmtToken token, ClassWriter cw){
        MethodVisitor mv;

        mv = cw.visitMethod(ACC_PUBLIC, Constants.INIT_METHOD, "()V", null, null);
        mv.visitCode();
        Label l0 = writeLabel(mv, token.getMeta().getStartLine());

        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, Constants.OBJECT_CLASS, Constants.INIT_METHOD, "()V");
        mv.visitInsn(RETURN);

        mv.visitLocalVariable(
                "this",
                "L_" + token.getFulledName(Constants.NAME_DELIMITER) + ";", null, l0, l0, 0
        );
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }

    @Override
    public void getResult() {
        cw = new ClassWriter(0);
    }

    public ClassWriter getCw() {
        return cw;
    }
}
