package ru.regenix.jphp.compiler.jvm.entity;

import ru.regenix.jphp.compiler.jvm.JvmCompiler;
import ru.regenix.jphp.lexer.tokens.stmt.ExprStmtToken;

public class ExpressionEntity extends Entity {

    protected final MethodEntity method;
    protected final ExprStmtToken expression;

    public ExpressionEntity(JvmCompiler compiler, MethodEntity method, ExprStmtToken expression) {
        super(compiler);
        this.method = method;
        this.expression = expression;
    }

    @Override
    public void getResult() {

    }
}
