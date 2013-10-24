package ru.regenix.jphp.compiler.jvm.entity;

import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import ru.regenix.jphp.compiler.jvm.Constants;
import ru.regenix.jphp.compiler.jvm.JvmCompiler;
import ru.regenix.jphp.lexer.tokens.stmt.ConstStmtToken;

public class ConstantEntity extends Entity {

    protected final ClassEntity clazz;
    protected final ConstStmtToken constant;

    public ConstantEntity(JvmCompiler compiler, ClassEntity clazz, ConstStmtToken constant) {
        super(compiler);
        this.clazz = clazz;
        this.constant = constant;
    }

    @Override
    public void getResult() {
        FieldVisitor fv = clazz.getCw().visitField(
            Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC + Opcodes.ACC_FINAL,
            constant.getName().getName(),
            Constants.MEMORY_CLASS,
            null, null
        );
    }
}
