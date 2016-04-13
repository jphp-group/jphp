package org.develnext.jphp.core.compiler.jvm.statement.expr;

import org.develnext.jphp.core.compiler.jvm.misc.LocalVariable;
import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.compiler.jvm.statement.expr.value.ListCompiler;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.token.expr.operator.DynamicAccessExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.ListExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.VariableExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.BodyStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ForeachStmtToken;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.FatalException;
import php.runtime.invoke.ObjectInvokeHelper;
import php.runtime.invoke.cache.PropertyCallCache;
import php.runtime.lang.ForeachIterator;

import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.IFEQ;

public class ForeachCompiler extends BaseStatementCompiler<ForeachStmtToken> {
    public ForeachCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    @Override
    public void write(ForeachStmtToken token) {
        expr.writeDefineVariables(token.getLocal());

        LabelNode start = new LabelNode();
        LabelNode end = new LabelNode();

        LabelNode l = new LabelNode();
        add(l);

        expr.writePushEnv();
        expr.writePushTraceInfo(token);
        expr.writeExpression(token.getIterator(), true, false, true);
        expr.writePopBoxing();
        expr.writePushConstBoolean(token.isValueReference());
        expr.writePushConstBoolean(token.isKeyReference());
        expr.writeSysDynamicCall(Environment.class, "__getIterator", ForeachIterator.class, TraceInfo.class, Memory.class, Boolean.TYPE, Boolean.TYPE);

        String name = "~foreach~" + method.nextStatementIndex(ForeachIterator.class);
        LocalVariable foreachVariable = method.getLocalVariable(name);
        if (foreachVariable == null)
            foreachVariable = method.addLocalVariable(name, l, ForeachIterator.class);

        /*LocalVariable foreachVariable = method.addLocalVariable(
                "~foreach~" + method.nextStatementIndex(ForeachIterator.class), l, ForeachIterator.class
        );*/
        foreachVariable.setEndLabel(end);

        expr.writeVarStore(foreachVariable, false, false);

        method.pushJump(end, start);

        add(start);
        expr.writeVarLoad(foreachVariable);

        expr.writeSysDynamicCall(ForeachIterator.class, "next", Boolean.TYPE);
        add(new JumpInsnNode(IFEQ, end));
        expr.stackPop();

        // $key
        if (token.getKey() != null) {
            LocalVariable key = method.getLocalVariable(token.getKey().getName());
            expr.checkAssignableVar(token.getKey());

            expr.writeVarLoad(foreachVariable);
            expr.writeSysDynamicCall(ForeachIterator.class, "getMemoryKey", Memory.class);
            if (token.isKeyReference()) {
                throw new FatalException(
                        "Key element cannot be a reference",
                        token.getKey().toTraceInfo(compiler.getContext())
                );
                // writeVarStore(key, false, false);
            } else
                expr.writeVarAssign(key, null, false, false);
        }

        // $var
        //LocalVariable variable = method.getLocalVariable(token.getValue().getName());
        Token last = token.getValue().getLast();
        VariableExprToken var = null;
        if (last instanceof DynamicAccessExprToken){
            DynamicAccessExprToken setter = (DynamicAccessExprToken)last;

            ExprStmtToken value = new ExprStmtToken(this.env, this.compiler.getContext(), token.getValue().getTokens());
            value.getTokens().remove(value.getTokens().size() - 1);
            value.updateAsmExpr(this.env, this.compiler.getContext());

            expr.writeExpression(value, true, false);

            expr.writeVarLoad(foreachVariable);
            expr.writeSysDynamicCall(ForeachIterator.class, "getValue", Memory.class);
            if (!token.isValueReference())
                expr.writePopImmutable();

            expr.writeDynamicAccessInfo(setter, false);

            expr.writeGetStatic("$CALL_PROP_CACHE", PropertyCallCache.class);
            expr.writePushConstInt(method.clazz.getAndIncCallPropCount());

            expr.writeSysStaticCall(ObjectInvokeHelper.class,
                    "assignProperty", Memory.class,
                    Memory.class, Memory.class, String.class, Environment.class, TraceInfo.class,
                    PropertyCallCache.class, int.class
            );
        } else {
            if (token.getValue().getSingle() instanceof VariableExprToken)
                expr.checkAssignableVar(var = (VariableExprToken)token.getValue().getSingle());

            expr.writeVarLoad(foreachVariable);
            expr.writeSysDynamicCall(ForeachIterator.class, "getValue", Memory.class);
            if (!token.isValueReference())
                expr.writePopImmutable();

            ExprStmtToken value = token.getValue();

            if (value.isSingle() && value.getSingle() instanceof ListExprToken) {
                ListCompiler listCompiler = (ListCompiler)expr.getCompiler(ListExprToken.class);
                listCompiler.write((ListExprToken)value.getSingle(), false, false);
            } else {
                expr.writeExpression(value, true, false);
                if (expr.stackPeek().immutable)
                    expr.unexpectedToken(value.getLast());

                expr.writeSysStaticCall(Memory.class,
                        token.isValueReference() ? "assignRefRight" : "assignRight", Memory.class, Memory.class, Memory.class
                );
            }
        }
        expr.writePopAll(1);

        /*
        if (token.isValueReference())
            writeVarStore(variable, false, false);
        else
            writeVarAssign(variable, false, true); */

        // body
        expr.write(BodyStmtToken.class, token.getBody());

        add(new JumpInsnNode(GOTO, start));
        add(end);

        /*if (compiler.getLangMode() == LangMode.JPHP){
            if (token.isValueReference() && var != null){
                expr.writeVarLoad(var.getName());
                expr.writeSysDynamicCall(Memory.class, "unset", void.class);
            }
        }*/

        method.popJump();
        expr.writeUndefineVariables(token.getLocal());
        method.prevStatementIndex(ForeachIterator.class);
    }
}
