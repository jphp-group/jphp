package org.develnext.jphp.core.compiler.jvm.statement;

import org.develnext.jphp.core.compiler.jvm.JvmCompiler;
import org.develnext.jphp.core.tokenizer.token.expr.value.NameToken;
import org.develnext.jphp.core.tokenizer.token.stmt.*;
import php.runtime.common.Modifier;
import php.runtime.lang.Generator;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.helper.GeneratorEntity;

import java.util.Arrays;
import java.util.Collections;

public class GeneratorStmtCompiler extends StmtCompiler<GeneratorEntity> {
    protected final FunctionStmtToken statement;

    public GeneratorStmtCompiler(JvmCompiler compiler, FunctionStmtToken statement) {
        super(compiler);
        this.statement = statement;
    }

    @Override
    public GeneratorEntity compile() {
        GeneratorEntity entity = new GeneratorEntity(getCompiler().getContext());
        entity.setReturnReference(statement.isReturnReference());
        entity.setInternalName(compiler.getModule().getInternalName() + "_generator" + statement.getId());
        entity.setId(statement.getGeneratorId());
        entity.setTrace(statement.toTraceInfo(compiler.getContext()));

        ClassStmtToken classStmtToken = new ClassStmtToken(statement.getMeta());
        classStmtToken.setNamespace(NamespaceStmtToken.getDefault());
        classStmtToken.setName(NameToken.valueOf(entity.getInternalName()));
        classStmtToken.setExtend(ExtendsStmtToken.valueOf(Generator.class.getSimpleName()));

        MethodStmtToken methodToken = new MethodStmtToken(statement);
        methodToken.setClazz(classStmtToken);
        methodToken.setGenerator(false);
        methodToken.setReturnReference(false);
        methodToken.setModifier(Modifier.PROTECTED);
        methodToken.setName(NameToken.valueOf("_run"));
        methodToken.setUses(statement.getArguments());
        methodToken.setArguments(Collections.<ArgumentStmtToken>emptyList());
        classStmtToken.setMethods(Arrays.asList(methodToken));

        ClassStmtCompiler classStmtCompiler = new ClassStmtCompiler(this.compiler, classStmtToken);
        classStmtCompiler.setSystem(true);
        classStmtCompiler.setInterfaceCheck(false);
        classStmtCompiler.setGeneratorEntity(entity);

        classStmtCompiler.setFunctionName(statement.getFulledName());
        ClassEntity clazzEntity = classStmtCompiler.compile();

        entity.getMethods().putAll(clazzEntity.getMethods());
        if (clazzEntity.getParent() != null)
            entity.setParent(clazzEntity.getParent());

        entity.setData(clazzEntity.getData());
        entity.setType(ClassEntity.Type.GENERATOR);
        entity.doneDeclare();

        return entity;
    }
}
