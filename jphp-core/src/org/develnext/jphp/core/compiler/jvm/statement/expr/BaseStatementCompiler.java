package org.develnext.jphp.core.compiler.jvm.statement.expr;

import org.develnext.jphp.core.compiler.jvm.JvmCompiler;
import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.compiler.jvm.statement.MethodStmtCompiler;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.token.stmt.MethodStmtToken;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;
import php.runtime.env.Environment;

abstract public class BaseStatementCompiler<T extends Token> {
    protected final MethodNode node;
    protected final MethodStmtCompiler method;
    protected final MethodStmtToken methodStatement;
    protected final JvmCompiler compiler;
    protected final Environment env;
    protected final ExpressionStmtCompiler expr;

    protected BaseStatementCompiler(ExpressionStmtCompiler exprCompiler) {
        this.expr = exprCompiler;
        this.env = exprCompiler.getCompiler().getEnvironment();
        this.compiler = exprCompiler.getCompiler();
        this.node = exprCompiler.getMethodNode();
        this.method = exprCompiler.getMethod();
        this.methodStatement = exprCompiler.getMethodStatement();
    }

    protected void add(AbstractInsnNode node) {
        method.node.instructions.add(node);
    }

    abstract public void write(T token);
}
