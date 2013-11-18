package ru.regenix.jphp.compiler.jvm.stetament;

import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import ru.regenix.jphp.compiler.jvm.Constants;
import ru.regenix.jphp.tokenizer.token.stmt.ConstStmtToken;
import ru.regenix.jphp.runtime.reflection.ConstantEntity;

public class ConstantStmtCompiler extends StmtCompiler<ConstantEntity> {

    protected final ClassStmtCompiler clazz;
    protected final ConstStmtToken constant;
    protected FieldVisitor fv;

    public ConstantStmtCompiler(ClassStmtCompiler clazz, ConstStmtToken constant) {
        super(clazz.getCompiler());
        this.clazz = clazz;
        this.constant = constant;

        entity = new ConstantEntity(compiler.getContext());
        entity.setClazz(clazz.entity);
        entity.setName(constant.getName().getName());
    }

    @Override
    public ConstantEntity compile() {
        if (clazz != null){ // TODO
            this.fv = clazz.getClassWriter().visitField(
                Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC + Opcodes.ACC_FINAL,
                constant.getName().getName(),
                Constants.MEMORY_CLASS,
                null, null
            );
        } else {

        }

        return entity;
    }
}
