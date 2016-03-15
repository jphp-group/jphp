package php.runtime.invoke;

import php.runtime.Memory;
import php.runtime.common.Messages;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.MethodEntity;
import php.runtime.reflection.ParameterEntity;

public class StaticMethodInvoker extends Invoker {
    protected final MethodEntity method;
    protected final String calledClass;

    public StaticMethodInvoker(Environment env, TraceInfo trace, String calledClass, MethodEntity method) {
        super(env, trace);
        this.method = method;
        this.calledClass = calledClass;
    }

    @Override
    public ParameterEntity[] getParameters() {
        return method.getParameters();
    }

    @Override
    public void check(String name, TraceInfo trace) {
        if (!method.isStatic())
            env.warning(trace,
                    name + "(): non-static method "
                            + method.getSignatureString(false) +" should not be called statically");
    }

    @Override
    public String getName() {
        return calledClass + "::" + method.getName();
    }

    @Override
    public int getArgumentCount() {
        return method.getParameters() == null ? 0 : method.getParameters().length;
    }

    public MethodEntity getMethod() {
        return method;
    }

    public String getCalledClass() {
        return calledClass;
    }

    @Override
    public void pushCall(TraceInfo trace, Memory[] args) {
        env.pushCall(trace, null, args, method.getName(), method.getClazz().getName(), calledClass);
    }

    @Override
    protected Memory invoke(Memory... args) throws Throwable {
        return InvokeHelper.callStatic(env, trace == null ? TraceInfo.UNKNOWN : trace, method, null, args, false);
    }

    @Override
    public int canAccess(Environment env) {
        return method.canAccess(env);
    }

    public static StaticMethodInvoker valueOf(Environment env, TraceInfo trace, String className, String methodName){
        ClassEntity classEntity = env.fetchClass(className, true);
        if (classEntity == null)
            classEntity = env.fetchMagicClass(className);

        MethodEntity methodEntity = classEntity == null ? null : classEntity.findMethod(methodName.toLowerCase());

        if (methodEntity == null /*|| !methodEntity.isStatic()*/){
            if (classEntity != null && classEntity.methodMagicCallStatic != null){
                return new MagicStaticMethodInvoker(
                        env, trace, className, classEntity.methodMagicCallStatic, methodName
                );
            }
            if (trace == null)
                return null;

            env.error(trace, Messages.ERR_CALL_TO_UNDEFINED_METHOD.fetch(className + "::" + methodName));
            return null;
        }

        return new StaticMethodInvoker(env, trace, className, methodEntity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StaticMethodInvoker)) return false;

        StaticMethodInvoker that = (StaticMethodInvoker) o;

        return calledClass.equals(that.calledClass) && method.equals(that.method);

    }

    @Override
    public int hashCode() {
        int result = method.hashCode();
        result = 31 * result + calledClass.hashCode();
        return result;
    }
}
