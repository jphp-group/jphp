package org.develnext.jphp.core.compiler.jvm.statement.expr;

import org.develnext.jphp.core.compiler.jvm.misc.LocalVariable;
import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.tokenizer.token.stmt.ReturnStmtToken;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import php.runtime.Memory;
import php.runtime.common.Function;
import php.runtime.common.HintType;
import php.runtime.common.StringUtils;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.invoke.InvokeHelper;
import php.runtime.lang.Generator;
import php.runtime.reflection.support.TypeChecker;

import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.GOTO;

public class ReturnCompiler extends BaseStatementCompiler<ReturnStmtToken> {
    public ReturnCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(ReturnStmtToken token) {
        boolean isGeneratorReturn = false;

        if (token.getValue() != null && method.getGeneratorEntity() != null) {
            isGeneratorReturn = true;

            expr.writeVarLoad("~this");
            expr.writePushEnv();
            expr.writePushTraceInfo(token);

            /*env.error(
                    token.toTraceInfo(compiler.getContext()),
                    ErrorType.E_ERROR,
                    "Generators cannot return values using \"return\""
            );*/
        }

        Memory result = token.isEmpty() ? Memory.UNDEFINED : Memory.NULL;

        boolean isImmutable = method.getEntity().isImmutable();

        if (token.getValue() != null) {
            result = expr.writeExpression(token.getValue(), true, true);

            if (methodStatement.getReturnHintType() == HintType.VOID) {
                String suffix = result != null && result.isNull() ?
                        " (did you mean \"return;\" instead of \"return null;\"?)" : "";

                env.error(
                        token.toTraceInfo(compiler.getContext()),
                        ErrorType.E_ERROR,
                        "A void function must not return a value" + suffix
                );
            }
        }

        if (result != null) {
            if (methodStatement.getReturnHintType() == HintType.VOID) {
                if (methodStatement.isReturnOptional()) {
                    env.error(
                            token.toTraceInfo(compiler.getContext()),
                            ErrorType.E_ERROR,
                            "Void type cannot be nullable"
                    );
                }
            }

            if (isImmutable) {
                Memory r = method.getEntity().getResult();
                if (r == null || r.isUndefined()) {
                    method.getEntity().setResult(result);
                }
            }
            expr.writePushMemory(result);
        } else {
            method.getEntity().setImmutable(false);
        }

        if (expr.stackEmpty(false)) {
            expr.writePushNull();
        } else {
            expr.writePopBoxing(false);
        }

        if (method.getEntity().isReturnReference()) {
            expr.writePushDup();
            expr.writePushEnv();
            expr.writePushTraceInfo(token);
            expr.writeSysStaticCall(
                    InvokeHelper.class,
                    "checkReturnReference",
                    void.class,
                    Memory.class, Environment.class, TraceInfo.class
            );
        } else {
            expr.writePopImmutable();
        }

        if (isGeneratorReturn) {
            expr.writeSysDynamicCall(Generator.class, "setReturn", Memory.class, Environment.class, TraceInfo.class, Memory.class);
        }

        if (!method.getTryStack().empty()) {
            LocalVariable variable = method.getLocalVariable("~result~");
            if (variable == null) {
                variable = method.addLocalVariable("~result~", null, Memory.class);
            }

            expr.writeVarStore(variable, false, false);
            add(new JumpInsnNode(GOTO, method.getTryStack().peek().getReturnLabel()));
        } else {
            add(new InsnNode(ARETURN));
            //removeStackFrame();
            expr.stackPop();
        }
    }
}
