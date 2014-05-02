package org.develnext.jphp.core.compiler.jvm.statement.expr;

import org.develnext.jphp.core.compiler.jvm.JvmCompiler;
import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

public class ClassInitEnvironmentCompiler extends BaseStatementCompiler<JvmCompiler.ClassInitEnvironment> {
    public ClassInitEnvironmentCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(JvmCompiler.ClassInitEnvironment token) {
        expr.writePushEnv();
        expr.writePushConstString(token.getEntity().getName());
        expr.writePushConstString(token.getEntity().getLowerName());
        expr.writePushScalarBoolean(false);
        expr.writeSysDynamicCall(
                Environment.class, "fetchClass", ClassEntity.class, String.class, String.class, Boolean.TYPE
        );
        expr.writePushEnv();
        expr.writeSysDynamicCall(ClassEntity.class, "initEnvironment", void.class, Environment.class);
    }
}
