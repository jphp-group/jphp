package ru.regenix.jphp.compiler.jvm.stetament;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.compiler.jvm.JvmCompiler;
import ru.regenix.jphp.exceptions.ParseException;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.Token;
import ru.regenix.jphp.runtime.reflection.Entity;

abstract public class StmtCompiler<T extends Entity> {

    protected final JvmCompiler compiler;
    protected T entity;

    public StmtCompiler(JvmCompiler compiler){
        this.compiler = compiler;
    }

    abstract public T compile();

    public JvmCompiler getCompiler() {
        return compiler;
    }

    protected Label writeLabel(MethodVisitor mv, int lineNumber){
        Label label = new Label();
        mv.visitLabel(label);
        if (lineNumber != -1)
            mv.visitLineNumber(lineNumber, label);

        return label;
    }

    protected Label writeLabel(MethodVisitor mv){
        return writeLabel(mv, -1);
    }

    /**
     * @throws ParseException
     * @param token
     */
    protected void unexpectedToken(Token token){
        Object unexpected = token.getWord();
        if (token.getType() == TokenType.T_J_CUSTOM)
            unexpected = token.getWord();

        compiler.getContext().triggerError(new ParseException(
                Messages.ERR_PARSE_UNEXPECTED_X.fetch(unexpected),
                token.toTraceInfo(compiler.getContext())
        ));
    }

    protected void unexpectedToken(Token token, Object expected){
        Object unexpected = token.getWord();
        if (token.getType() == TokenType.T_J_CUSTOM)
            unexpected = token.getWord();

        compiler.getContext().triggerError(new ParseException(
                Messages.ERR_PARSE_UNEXPECTED_X_EXPECTED_Y.fetch(unexpected, expected),
                token.toTraceInfo(compiler.getContext())
        ));
    }
}
