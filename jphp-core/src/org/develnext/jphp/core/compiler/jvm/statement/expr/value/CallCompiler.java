package org.develnext.jphp.core.compiler.jvm.statement.expr.value;

import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
import org.develnext.jphp.core.compiler.common.misc.StackItem;
import org.develnext.jphp.core.compiler.jvm.statement.ExpressionStmtCompiler;
import org.develnext.jphp.core.compiler.jvm.statement.expr.BaseExprCompiler;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.token.expr.ClassExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.DynamicAccessAssignExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.DynamicAccessExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.*;
import org.objectweb.asm.Opcodes;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.ext.support.compile.CompileFunction;
import php.runtime.invoke.InvokeArgumentHelper;
import php.runtime.invoke.InvokeHelper;
import php.runtime.invoke.ObjectInvokeHelper;
import php.runtime.invoke.cache.FunctionCallCache;
import php.runtime.invoke.cache.MethodCallCache;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.FunctionEntity;
import php.runtime.reflection.MethodEntity;

import java.lang.reflect.Method;

public class CallCompiler extends BaseExprCompiler<CallExprToken> {
    public static class PushCallStatistic {
        public StackItem.Type returnType = StackItem.Type.REFERENCE;
    }

    public CallCompiler(ExpressionStmtCompiler exprCompiler) {
        super(exprCompiler);
    }

    /**
     * {expr_any}()
     */
    protected Memory writeByAny(CallExprToken function, boolean returnValue, boolean writeOpcode, PushCallStatistic statistic) {
        if (!writeOpcode)
            return null;

        expr.getMethod().getEntity().setImmutable(false);
        expr.writeLineNumber(function);

        expr.writePush((ValueExprToken) function.getName(), true, false);
        expr.writePopBoxing();
        expr.writePushParameters(function.getParameters());

        expr.writePushEnv();
        expr.writePushTraceInfo(function);

        expr.writeSysStaticCall(
                InvokeHelper.class, "callAny", Memory.class,
                Memory.class, Memory[].class, Environment.class, TraceInfo.class
        );

        if (!returnValue) {
            expr.writePopAll(1);
        }

        return null;
    }

    /**
     * functionName()
     */
    protected Memory writeByName(CallExprToken function, boolean returnValue, boolean writeOpcode, PushCallStatistic statistic) {
        Token name = function.getName();

        String realName = ((NameToken) name).getName();
        CompileFunction compileFunction = compiler.getScope().findCompileFunction(realName);

        // try find system function, like max, sin, cos, etc.
        if (compileFunction == null
                && name instanceof FulledNameToken
                && compiler.getEnvironment().fetchFunction(realName) == null
                && compiler.findFunction(realName) == null) {
            String tryName = ((FulledNameToken) name).getLastName().getName();
            compileFunction = compiler.getScope().findCompileFunction(tryName);
        }

        if (compileFunction != null) {
            return expr.writePushCompileFunction(function, compileFunction, returnValue, writeOpcode, statistic);
        } else {
            FunctionEntity localFunction = compiler.findFunction(realName);
            if (localFunction != null) {
                if (localFunction.isLikeConstant() && function.getParameters().isEmpty()) {
                    if (!writeOpcode) {
                        if (statistic != null) {
                            statistic.returnType = StackItem.Type.valueOf(Memory.class);
                        }

                        return localFunction.getResult();
                    } else {
                        if (returnValue) {
                            expr.writePushMemory(localFunction.getResult());
                        }

                        return null;
                    }
                }
            }

            if (!writeOpcode) {
                return null;
            }

            expr.getMethod().getEntity().setImmutable(false);
            int index = expr.getMethod().clazz.getAndIncCallFuncCount();

            expr.writePushEnv();
            expr.writePushTraceInfo(function);
            expr.writePushString(realName.toLowerCase());
            expr.writePushString(realName);
            expr.writePushParameters(function.getParameters());

            expr.writeGetStatic("$CALL_FUNC_CACHE", FunctionCallCache.class);
            expr.writePushConstInt(index);

            expr.writeSysStaticCall(
                    InvokeHelper.class, "call", Memory.class,
                    Environment.class, TraceInfo.class, String.class, String.class, Memory[].class,
                    FunctionCallCache.class, Integer.TYPE
            );

            if (!returnValue) {
                expr.writePopAll(1);
            }
        }

        return null;
    }


    /**
     * Class::method()
     */
    public Memory writePushStaticMethod(CallExprToken function, boolean returnValue, boolean writeOpcode,
                                        PushCallStatistic statistic) {
        StaticAccessExprToken access = (StaticAccessExprToken) function.getName();

        if (!writeOpcode)
            return null;

        expr.getMethod().getEntity().setImmutable(false);

        expr.writeLineNumber(function);

        if (expr.writePushFastStaticMethod(function, returnValue))
            return null;

        Runnable writeParams = () -> {
            expr.writePushEnv();
            expr.writePushTraceInfo(function.getName());
        };

        String methodName = null;

        ValueExprToken clazz = access.getClazz();
        if ((clazz instanceof NameToken || (clazz instanceof SelfExprToken && !method.clazz.getEntity().isTrait()))
                && access.getField() instanceof NameToken) {
            String className;
            if (clazz instanceof SelfExprToken) {
                className = expr.getMacros(clazz).toString();
            } else {
                className = ((NameToken) clazz).getName();
            }

            methodName = ((NameToken) access.getField()).getName();

            ClassEntity localClass = compiler.findClass(className);
            if (localClass != null) {
                MethodEntity localClassMethod = localClass.findMethod(methodName.toLowerCase());
                if (localClassMethod != null && localClassMethod.isLikeConstant()) {
                    if (returnValue) {
                        expr.writePushMemory(localClassMethod.getResult());

                        if (statistic != null) {
                            statistic.returnType = StackItem.Type.REFERENCE;
                        }
                    }

                    return null;
                }
            }

            writeParams.run();

            expr.writePushString(className.toLowerCase());

            expr.writePushString(methodName.toLowerCase());

            expr.writePushString(className);
            expr.writePushString(methodName);

            expr.writePushParameters(function.getParameters());

            if (expr.getMethod().clazz.statement.isTrait()) {
                expr.writePushConstNull();
                expr.writePushConstInt(0);
            } else {
                int cacheIndex = expr.getMethod().clazz.getAndIncCallMethCount();
                expr.writeGetStatic("$CALL_METH_CACHE", MethodCallCache.class);
                expr.writePushConstInt(cacheIndex);
            }

            expr.writeSysStaticCall(
                    InvokeHelper.class, "callStatic", Memory.class,
                    Environment.class, TraceInfo.class,
                    String.class, String.class, // lower sign name
                    String.class, String.class, // origin names
                    Memory[].class,
                    MethodCallCache.class, Integer.TYPE
            );
        } else {
            writeParams.run();

            if (clazz instanceof NameToken) {
                expr.writePushString(((NameToken) clazz).getName());
                expr.writePushDupLowerCase();
            } else if (clazz instanceof SelfExprToken) {
                expr.writePushSelf(true);
            } else if (clazz instanceof StaticExprToken) {
                expr.writePushStatic();
                expr.writePushDupLowerCase();
            } else {
                expr.writePush(clazz, true, false);
                expr.writePopString();
                expr.writePushDupLowerCase();
            }

            if (access.getField() != null) {
                ValueExprToken field = access.getField();
                if (field instanceof NameToken) {
                    expr.writePushString(((NameToken) field).getName());
                    expr.writePushString(((NameToken) field).getName().toLowerCase());
                } else if (field instanceof ClassExprToken) {
                    expr.unexpectedToken(field);
                } else {
                    expr.writePush(access.getField(), true, false);
                    expr.writePopString();
                    expr.writePushDupLowerCase();
                }
            } else {
                expr.writeExpression(access.getFieldExpr(), true, false);
                expr.writePopString();
                expr.writePushDupLowerCase();
            }

            expr.writePushParameters(function.getParameters());

            if (clazz instanceof StaticExprToken || expr.getMethod().clazz.statement.isTrait()
                    || clazz instanceof VariableExprToken) {
                expr.writePushConstNull();
                expr.writePushConstInt(0);
            } else {
                int cacheIndex = expr.getMethod().clazz.getAndIncCallMethCount();
                expr.writeGetStatic("$CALL_METH_CACHE", MethodCallCache.class);
                expr.writePushConstInt(cacheIndex);
            }

            expr.writeSysStaticCall(
                    InvokeHelper.class, "callStaticDynamic", Memory.class,
                    Environment.class, TraceInfo.class,
                    String.class, String.class,
                    String.class, String.class,
                    Memory[].class,
                    MethodCallCache.class, Integer.TYPE
            );
        }

        if (statistic != null) {
            statistic.returnType = StackItem.Type.REFERENCE;
        }

        return null;
    }

    /**
     * $var->method()
     */
    public Memory writePushDynamicMethod(CallExprToken function, boolean returnValue, boolean writeOpcode,
                                         PushCallStatistic statistic) {
        expr.getMethod().getEntity().setImmutable(false);

        if (!writeOpcode)
            return null;

        DynamicAccessExprToken access = (DynamicAccessExprToken) function.getName();
        if (access instanceof DynamicAccessAssignExprToken) {
            expr.unexpectedToken(access);
        }

        expr.writeLineNumber(function);
        expr.writeDynamicAccessPrepare(access, true);
        expr.writePushParameters(function.getParameters());

        expr.writeSysStaticCall(
                ObjectInvokeHelper.class, "invokeMethod",
                Memory.class,
                Memory.class, String.class, String.class, Environment.class, TraceInfo.class, Memory[].class
        );

        if (!returnValue) {
            expr.writePopAll(1);
        }

        if (statistic != null) {
            statistic.returnType = StackItem.Type.REFERENCE;
        }

        return null;
    }

    /**
     * parent::method()
     */
    public Memory writePushParentDynamicMethod(CallExprToken function, boolean returnValue, boolean writeOpcode,
                                               PushCallStatistic statistic) {
        if (!writeOpcode)
            return null;

        expr.getMethod().getEntity().setImmutable(false);

        StaticAccessExprToken dynamic = (StaticAccessExprToken) function.getName();
        //writeLineNumber(function);

        expr.writePushThis();
        if (dynamic.getField() != null) {
            if (dynamic.getField() instanceof NameToken) {
                String name = ((NameToken) dynamic.getField()).getName();
                expr.writePushConstString(name);
                expr.writePushConstString(name.toLowerCase());
            } else {
                expr.writePush(dynamic.getField(), true, false);
                expr.writePopString();
                expr.writePushDupLowerCase();
            }
        } else {
            expr.writeExpression(dynamic.getFieldExpr(), true, false);
            expr.writePopString();
            expr.writePushDupLowerCase();
        }

        expr.writePushEnv();
        expr.writePushTraceInfo(dynamic);
        expr.writePushParameters(function.getParameters());

        expr.writeSysStaticCall(
                ObjectInvokeHelper.class, "invokeParentMethod",
                Memory.class,
                Memory.class, String.class, String.class, Environment.class, TraceInfo.class, Memory[].class
        );

        if (!returnValue) {
            expr.writePopAll(1);
        }

        if (statistic != null) {
            statistic.returnType = StackItem.Type.REFERENCE;
        }

        return null;
    }


    @Override
    public void write(CallExprToken token, boolean returnValue) {
        write(token, returnValue, true, new PushCallStatistic());
    }

    public Memory write(CallExprToken function, boolean returnValue, boolean writeOpcode, PushCallStatistic statistic) {
        Token name = function.getName();
        if (name instanceof NameToken) {
            return writeByName(function, returnValue, writeOpcode, statistic);
        } else if (name instanceof StaticAccessExprToken) {
            if (((StaticAccessExprToken) name).isAsParent()) {
                return writePushParentDynamicMethod(function, returnValue, writeOpcode, statistic);
            } else {
                return writePushStaticMethod(function, returnValue, writeOpcode, statistic);
            }
        } else if (name instanceof DynamicAccessExprToken) {
            return writePushDynamicMethod(function, returnValue, writeOpcode, statistic);
        } else {
            return writeByAny(function, returnValue, writeOpcode, statistic);
        }
    }
}
