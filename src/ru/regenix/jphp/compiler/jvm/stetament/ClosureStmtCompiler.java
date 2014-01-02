package ru.regenix.jphp.compiler.jvm.stetament;

import ru.regenix.jphp.common.Modifier;
import ru.regenix.jphp.compiler.jvm.JvmCompiler;
import ru.regenix.jphp.runtime.lang.Closure;
import ru.regenix.jphp.runtime.reflection.ClassEntity;
import ru.regenix.jphp.runtime.reflection.helper.ClosureEntity;
import ru.regenix.jphp.tokenizer.token.expr.value.ClosureStmtToken;
import ru.regenix.jphp.tokenizer.token.expr.value.NameToken;
import ru.regenix.jphp.tokenizer.token.stmt.ClassStmtToken;
import ru.regenix.jphp.tokenizer.token.stmt.ExtendsStmtToken;
import ru.regenix.jphp.tokenizer.token.stmt.MethodStmtToken;
import ru.regenix.jphp.tokenizer.token.stmt.NamespaceStmtToken;

import java.util.Arrays;

public class ClosureStmtCompiler extends StmtCompiler<ClosureEntity> {
    protected ClosureStmtToken statement;

    public ClosureStmtCompiler(JvmCompiler compiler, ClosureStmtToken statement) {
        super(compiler);
        this.statement = statement;
    }

    @Override
    public ClosureEntity compile() {
        ClosureEntity entity = new ClosureEntity(getCompiler().getContext());
        entity.setReturnReference(statement.getFunction().isReturnReference());
        entity.setInternalName("$_php_closure_" + compiler.getModule().getId() + "_" + statement.getId());
        entity.setId(statement.getId());
        entity.setTrace(statement.toTraceInfo(compiler.getContext()));

        ClassStmtToken classStmtToken = new ClassStmtToken(statement.getMeta());
        classStmtToken.setNamespace(NamespaceStmtToken.getDefault());
        classStmtToken.setName(NameToken.valueOf(entity.getInternalName()));
        classStmtToken.setExtend(ExtendsStmtToken.valueOf(Closure.class.getSimpleName()));

        MethodStmtToken methodToken = new MethodStmtToken(statement.getFunction());
        methodToken.setClazz(classStmtToken);
        methodToken.setReturnReference(entity.isReturnReference());
        methodToken.setModifier(Modifier.PUBLIC);
        methodToken.setName(NameToken.valueOf("__invoke"));
        classStmtToken.setMethods(Arrays.asList(methodToken));

        ClassStmtCompiler classStmtCompiler = new ClassStmtCompiler(this.compiler, classStmtToken);
        classStmtCompiler.setSystem(true);
        classStmtCompiler.setInterfaceCheck(false);

        classStmtCompiler.setFunctionName(null);
        ClassEntity clazzEntity = classStmtCompiler.compile();

        entity.getMethods().putAll(clazzEntity.getMethods());
        if (clazzEntity.getParent() != null)
            entity.setParent(clazzEntity.getParent());

        entity.setData(clazzEntity.getData());
        entity.setParameters(clazzEntity.findMethod("__invoke").parameters);
        entity.doneDeclare();

        return entity;
    }
}
