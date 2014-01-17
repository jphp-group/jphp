package ru.regenix.jphp.compiler.jvm.stetament;

import ru.regenix.jphp.common.Modifier;
import ru.regenix.jphp.compiler.jvm.JvmCompiler;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.FunctionEntity;
import php.runtime.reflection.MethodEntity;
import php.runtime.reflection.ModuleEntity;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.value.NameToken;
import ru.regenix.jphp.tokenizer.token.stmt.ClassStmtToken;
import ru.regenix.jphp.tokenizer.token.stmt.FunctionStmtToken;
import ru.regenix.jphp.tokenizer.token.stmt.MethodStmtToken;
import ru.regenix.jphp.tokenizer.token.stmt.NamespaceStmtToken;

import java.util.Arrays;


public class FunctionStmtCompiler extends StmtCompiler<FunctionEntity> {

    protected FunctionStmtToken statement;

    public FunctionStmtCompiler(JvmCompiler compiler, FunctionStmtToken statement) {
        super(compiler);
        this.statement = statement;
    }

    @Override
    public FunctionEntity compile() {
        //
        ModuleEntity module = compiler.getModule();
        FunctionEntity entity = new FunctionEntity(compiler.getContext());
        entity.setModule(module);
        entity.setName(statement.getFulledName());
        entity.setReturnReference(statement.isReturnReference());
        entity.setInternalName(compiler.getModule().getInternalName() + "_func" + statement.getId());
        entity.setTrace(statement.toTraceInfo(compiler.getContext()));

        NamespaceStmtToken namespace = NamespaceStmtToken.getDefault();
        ClassStmtToken token = new ClassStmtToken(statement.getMeta());
        token.setFinal(true);
        token.setNamespace(namespace);
        token.setName(new NameToken(TokenMeta.of(entity.getInternalName())));

        MethodStmtToken methodToken = new MethodStmtToken(statement);
        methodToken.setClazz(token);
        methodToken.setFinal(true);
        methodToken.setStatic(true);
        methodToken.setReturnReference(entity.isReturnReference());
        methodToken.setModifier(Modifier.PUBLIC);
        methodToken.setName(new NameToken(TokenMeta.of("__invoke")));
        token.setMethods(Arrays.asList(methodToken));

        ClassStmtCompiler classStmtCompiler = new ClassStmtCompiler(compiler, token);
        classStmtCompiler.setSystem(true);
        classStmtCompiler.setFunctionName(entity.getName());
        ClassEntity clazzEntity = classStmtCompiler.compile();
        entity.setData(clazzEntity.getData());

        MethodEntity methodEntity = clazzEntity.findMethod("__invoke");
        entity.setParameters(methodEntity.parameters);
        entity.setEmpty(methodEntity.isEmpty());
        entity.setImmutable(methodEntity.isImmutable());
        entity.setResult(methodEntity.getResult());

        return entity;
    }
}
