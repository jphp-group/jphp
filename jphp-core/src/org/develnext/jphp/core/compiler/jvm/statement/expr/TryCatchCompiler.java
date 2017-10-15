package org.develnext.jphp.core.compiler.jvm.statement.expr;

import org.develnext.jphp.core.compiler.jvm.misc.LocalVariable;
import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.compiler.jvm.statement.MethodStmtCompiler;
import org.develnext.jphp.core.tokenizer.token.expr.value.FulledNameToken;
import org.develnext.jphp.core.tokenizer.token.stmt.BodyStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.CatchStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.TryStmtToken;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.TryCatchBlockNode;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.lang.BaseException;
import php.runtime.lang.exception.BaseBaseException;

import java.util.List;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Opcodes.GOTO;

public class TryCatchCompiler extends BaseStatementCompiler<TryStmtToken> {
    public TryCatchCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(TryStmtToken token) {
        if (token.getBody() == null || token.getBody().getInstructions().isEmpty()) {
            if (token.getFinally() != null)
                expr.write(BodyStmtToken.class, token.getFinally());
            return;
        }

        expr.writeDefineVariables(token.getLocal());
        LabelNode tryStart = expr.writeLabel(node, token.getMeta().getStartLine());
        LabelNode tryEnd = new LabelNode();
        LabelNode catchStart = new LabelNode();
        LabelNode catchEnd = new LabelNode();
        LabelNode returnLabel = new LabelNode();

        method.node.tryCatchBlocks.add(0,
                new TryCatchBlockNode(tryStart, tryEnd, catchStart, Type.getInternalName(BaseBaseException.class))
        );

        if (token.getFinally() != null) {
            method.getTryStack().push(new MethodStmtCompiler.TryCatchItem(token, returnLabel));
        }
        expr.write(BodyStmtToken.class, token.getBody());

        if (token.getFinally() != null) {
            method.getTryStack().pop();
        }

        add(tryEnd);

        add(new JumpInsnNode(GOTO, catchEnd));
        add(catchStart);

        LocalVariable exception = method.addLocalVariable(
                "~catch~" + method.nextStatementIndex(BaseException.class), catchStart, BaseBaseException.class
        );
        exception.setEndLabel(catchEnd);
        expr.makeVarStore(exception);


        LabelNode nextCatch = null;
        int i = 0, size = token.getCatches().size();
        LocalVariable local = null;
        LabelNode catchFail = new LabelNode();


        for (CatchStmtToken _catch : token.getCatches()) {
            if (nextCatch != null) {
                add(nextCatch);
            }
            if (i == size - 1) {
                nextCatch = catchFail;
            } else {
                nextCatch = new LabelNode();
            }

            local = method.getLocalVariable(_catch.getVariable().getName());

            List<FulledNameToken> catchExceptions = _catch.getExceptions();

            LabelNode bodyLabel = new LabelNode();

            LabelNode nextCatchLocal = new LabelNode();

            int j = 0;

            for (FulledNameToken catchException : catchExceptions) {
                if (j > 0) {
                    add(nextCatchLocal);
                    nextCatchLocal = new LabelNode();
                }

                expr.writePushEnv();
                expr.writeVarLoad(exception);

                expr.writePushConstString(catchException.toName());
                expr.writePushConstString(catchException.toName().toLowerCase());
                expr.writeSysDynamicCall(
                        Environment.class, "__throwCatch", Memory.class, BaseBaseException.class, String.class, String.class
                );

                expr.writeVarAssign(local, _catch.getVariable(), true, false);
                expr.writePopBoolean();
                add(new JumpInsnNode(IFEQ, j == catchExceptions.size() - 1 ? nextCatch : nextCatchLocal));
                expr.stackPop();

                if (catchExceptions.size() > 1) {
                    add(new JumpInsnNode(GOTO, bodyLabel));
                }

                j++;
            }

            if (catchExceptions.size() > 1) {
                add(bodyLabel);
            }

            expr.write(BodyStmtToken.class, _catch.getBody());
            add(new JumpInsnNode(GOTO, catchEnd));

            i++;
        }
        add(catchFail);

        if (token.getFinally() != null) {
            expr.write(BodyStmtToken.class, token.getFinally());
        }

        /*if (method.getTryStack().empty()) {
            expr.writePushEnv();
            expr.writeVarLoad(exception);
            expr.writeSysDynamicCall(Environment.class, "__throwFailedCatch", void.class, BaseException.class);
        } else {*/
        expr.makeVarLoad(exception);
        add(new InsnNode(ATHROW));
        //}

        add(catchEnd);

        if (token.getFinally() != null) {
            LabelNode skip = new LabelNode();
            add(new JumpInsnNode(GOTO, skip));

            // finally for return
            add(returnLabel);
            expr.write(BodyStmtToken.class, token.getFinally());
            if (method.getTryStack().empty()) {
                // all finally blocks are done
                LocalVariable retVar = method.getOrAddLocalVariable("~result~", null, Memory.class);
                expr.writeVarLoad(retVar);
                add(new InsnNode(ARETURN));
                expr.stackPop();
            } else {
                // goto next finally block
                add(new JumpInsnNode(GOTO, method.getTryStack().peek().getReturnLabel()));
            }

            add(skip);
            // other finally
            expr.write(BodyStmtToken.class, token.getFinally());
        }

        expr.writeUndefineVariables(token.getLocal());
        method.prevStatementIndex(BaseBaseException.class);
    }
}
