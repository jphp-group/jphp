package ru.regenix.jphp.compiler.jvm.stetament;

import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import ru.regenix.jphp.compiler.jvm.Constants;
import ru.regenix.jphp.tokenizer.token.stmt.ConstStmtToken;
import ru.regenix.jphp.runtime.reflection.ConstantEntity;

public class ConstantStmtCompiler extends StmtCompiler<ConstantEntity> {

    protected final ClassStmtCompiler clazzStatement;
    protected final ConstStmtToken statement;
    protected FieldVisitor fv;

    public ConstantStmtCompiler(ClassStmtCompiler clazzStatement, ConstStmtToken statement) {
        super(clazzStatement.getCompiler());
        this.clazzStatement = clazzStatement;
        this.statement = statement;

        entity = new ConstantEntity(compiler.getContext());
        entity.setClazz(clazzStatement.entity);
        entity.setName(statement.getName().getName());
        entity.setTrace(statement.toTraceInfo(compiler.getContext()));
    }

    @Override
    public ConstantEntity compile() {
        if (clazzStatement != null){ // TODO
            this.fv = clazzStatement.getClassWriter().visitField(
                Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC + Opcodes.ACC_FINAL,
                statement.getName().getName(),
                Constants.MEMORY_CLASS,
                null, null
            );
        } else {

        }

        return entity;
    }
}
