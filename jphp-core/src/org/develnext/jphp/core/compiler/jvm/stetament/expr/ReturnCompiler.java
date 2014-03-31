package org.develnext.jphp.core.compiler.jvm.stetament.expr;

import org.develnext.jphp.core.compiler.jvm.misc.LocalVariable;
import org.develnext.jphp.core.compiler.jvm.stetament.ExpressionStmtCompiler;
import org.develnext.jphp.core.tokenizer.token.stmt.ReturnStmtToken;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.invoke.InvokeHelper;

import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.GOTO;

public class ReturnCompiler extends BaseStatementCompiler<ReturnStmtToken> {
    public ReturnCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(ReturnStmtToken token) {
        Memory result = Memory.NULL;
        boolean isImmutable = method.getEntity().isImmutable();
        if (token.getValue() != null)
            result = expr.writeExpression(token.getValue(), true, true);

        if (result != null) {
            if (isImmutable) {
                if (method.getEntity().getResult() == null)
                    method.getEntity().setResult(result);
            }
            expr.writePushMemory(result);
        } else {
            method.getEntity().setImmutable(false);
        }

        if (expr.stackEmpty(false))
            expr.writePushNull();
        else
            expr.writePopBoxing(false);

        if (method.getEntity().isReturnReference()){
            expr.writePushDup();
            expr.writePushEnv();
            expr.writePushTraceInfo(token);
            expr.writeSysStaticCall(
                    InvokeHelper.class,
                    "checkReturnReference",
                    void.class,
                    Memory.class, Environment.class, TraceInfo.class
            );
        } else
            expr.writePopImmutable();

        if (!method.getTryStack().empty()){
            LocalVariable variable = method.getLocalVariable("~result~");
            if (variable == null)
                variable = method.addLocalVariable("~result~", null, Memory.class);

            expr.writeVarStore(variable, false, false);
            add(new JumpInsnNode(GOTO, method.getTryStack().peek().getReturnLabel()));
        } else {
            add(new InsnNode(ARETURN));
            //removeStackFrame();
            expr.stackPop();
        }
    }
}
