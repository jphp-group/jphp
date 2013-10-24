package ru.regenix.jphp.compiler.jvm.entity;

import ru.regenix.jphp.compiler.jvm.JvmCompiler;
import ru.regenix.jphp.lexer.tokens.stmt.MethodStmtToken;

public class MethodEntity extends Entity {
    public final ClassEntity clazz;
    public final MethodStmtToken method;

    protected int varCount = 0;
    protected int stackSize = 0;

    public MethodEntity(JvmCompiler compiler, ClassEntity clazz, MethodStmtToken method) {
        super(compiler);
        this.clazz = clazz;
        this.method = method;
    }

    public void incStackSize(){
        stackSize += 1;
    }

    public void incVarCount(){
        varCount += 1;
    }

    @Override
    public void getResult() {

    }
}
