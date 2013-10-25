package ru.regenix.jphp.compiler.jvm.entity;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import ru.regenix.jphp.compiler.jvm.Constants;
import ru.regenix.jphp.compiler.jvm.JvmCompiler;
import ru.regenix.jphp.lexer.tokens.stmt.ClassStmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.ConstStmtToken;

import static org.objectweb.asm.Opcodes.*;

public class ClassEntity extends Entity {
    protected ClassWriter cw;
    public final ClassStmtToken clazz;
    public final int id;

    public ClassEntity(JvmCompiler compiler, ClassStmtToken clazz) {
        super(compiler);
        this.clazz = clazz;
        this.id = compiler.getScope().addClass(clazz);
    }

    protected void writeInitStatic(){
        MethodVisitor mv = cw.visitMethod(ACC_STATIC, Constants.STATIC_INIT_METHOD, "()V", null, null);
        MethodEntity method = new MethodEntity(compiler, this, mv);

        mv.visitCode();

        for(ConstStmtToken constant : clazz.getConstants()){
            ExpressionEntity expression = new ExpressionEntity(compiler, method, constant.getValue());
            expression.getResult();

            mv.visitFieldInsn(
                    PUTSTATIC,
                    clazz.getFulledName(Constants.NAME_DELIMITER),
                    constant.getName().getName(),
                    Constants.MEMORY_CLASS
            );
        }

        mv.visitEnd();
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
        new ConstantEntity(compiler, this, constant).getResult();
    }

    @Override
    public void getResult() {
        cw = new ClassWriter(0);
        cw.visit(
                V1_6, ACC_SUPER, clazz.getFulledName(Constants.NAME_DELIMITER), null,
                Constants.OBJECT_CLASS, null
        );
        cw.visitSource(compiler.getSourceFile(), null);
        writeConstructor();

        // constants
        for(ConstStmtToken constant : clazz.getConstants()){
            writeConstant(constant);
        }

        writeInitStatic();
        cw.visitEnd();
    }

    public ClassWriter getCw() {
        return cw;
    }
}
