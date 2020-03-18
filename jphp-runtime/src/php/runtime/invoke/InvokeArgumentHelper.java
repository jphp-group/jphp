package php.runtime.invoke;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.common.Messages;
import php.runtime.common.StringUtils;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.ext.core.classes.WrapJavaExceptions;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.Generator;
import php.runtime.lang.exception.BaseTypeError;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ReferenceMemory;
import php.runtime.memory.helper.VariadicMemory;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.MethodEntity;
import php.runtime.reflection.ModuleEntity;
import php.runtime.reflection.ParameterEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class InvokeArgumentHelper {

    private static final String INVALID_TYPE_MESSAGE = "Only arrays and Traversables can be unpacked";

    private static Memory[] argsToPassed(Environment env, TraceInfo trace, Memory[] args, ParameterEntity[] parameters) {
        args = unpackArgs(env, trace, args, parameters);

        Memory[] passed = args;

        if ((args == null && parameters.length > 0) || (args != null && args.length < parameters.length)) {
            passed = new Memory[parameters.length];

            if (args != null && args.length > 0) {
                System.arraycopy(args, 0, passed, 0, args.length);
            }
        }

        return passed;
    }

    private static Memory makeArg(Environment env, TraceInfo trace, ParameterEntity param,
                                  Memory arg, int i, String originClassName, String originMethodName, String staticClassName) {
        if (arg == null) {
            arg = makeDefaultArg(env, trace, param, i, originClassName, originMethodName);
        } else {
            arg = makeNormalArg(env, trace, param, arg);
        }

        return typeHintArg(env, trace, param, arg, i, originClassName, originMethodName, staticClassName);
    }

    private static Memory makeNormalArg(Environment env, TraceInfo trace, ParameterEntity param, Memory arg) {

        if (param.isReference()) {
            if (!arg.isReference() && !arg.isObject()) {
                env.error(trace, ErrorType.E_ERROR, Messages.ERR_ONLY_REF_CAN_BE_PASSED);
                return new ReferenceMemory(arg);
            } else if (arg.isDisallowReferenceOps()) {
                env.error(trace, ErrorType.E_ERROR, Messages.ERR_PASS_DISALLOWED_MEMORY_AS_REF, arg.getGivenString());
            }

            return arg;
        } else {
            return param.isMutable() ? arg.fast_toImmutable() : arg.toValue();
        }
    }

    private static Memory makeDefaultArg(Environment env, TraceInfo trace, ParameterEntity param,
                                         int i, String originClassName, String originMethodName) {
        Memory def = param.getDefaultValue();

        if (def != null) {
            if (!param.isReference()) {
                return param.isMutable() ? def.toImmutable(env, trace) : def;
            } else {
                return new ReferenceMemory(param.isMutable() ? def.toImmutable(env, trace) : def);
            }
        } else {
            if (param.getTypeClass() != null) {
                invalidType(env, trace, param, i + 1, null, originClassName, originMethodName);
            }

            env.error(trace, ErrorType.E_ERROR,
                    Messages.ERR_MISSING_ARGUMENT, (i + 1) + " ($" + param.getName() + ")",
                    originMethodName == null ? originClassName : originClassName + "::" + originMethodName
            );

            return param.isReference() ? new ReferenceMemory() : Memory.NULL;
        }
    }

    private static Memory typeHintArg(Environment env, TraceInfo trace, ParameterEntity param,
                                      Memory arg, int i, String originClassName, String originMethodName, String staticClassName) {
        if (!param.checkTypeHinting(env, arg, staticClassName)) {
            ModuleEntity module = env.getModuleManager().findModule(trace);

            Memory memory = param.applyTypeHinting(env, arg, module != null && module.isStrictTypes());

            if (memory != null) {
                return memory;
            } else {
                invalidType(env, trace, param, i + 1, arg, originClassName, originMethodName);
            }
        }

        return arg;
    }

    public static Memory[] makeArguments(Environment env, Memory[] args,
                                         ParameterEntity[] parameters,
                                         String originClassName, String originMethodName, String staticClassName,
                                         TraceInfo trace) {
        if (parameters == null) {
            return args;
        }

        Memory[] passed = argsToPassed(env, trace, args, parameters);

        int i = 0;

        if (passed != null) {
            boolean variadicMemoryExists = false;

            for (ParameterEntity param : parameters) {
                Memory arg = passed[i];

                if (param.isVariadic()) {
                    ArrayMemory variadicArgs = new ArrayMemory();

                    int _i = i;

                    while (arg != null) {
                        if (arg instanceof VariadicMemory) {
                            variadicMemoryExists = true;

                            ForeachIterator iterator = arg.getNewIterator(env, param.isReference(), false);

                            if (iterator == null) {
                                env.warning(trace, Messages.ERR_ONLY_ARR_AND_TRAV_CAN_UNPACKED);
                            } else {
                                makeVariadic(iterator, variadicArgs, param, env, trace, _i, originClassName, originMethodName);
                            }
                        } else {
                            if (variadicMemoryExists) {
                                env.error(trace, Messages.ERR_CANNOT_USE_POS_ARG_AFTER_UNPACK.fetch());
                            }

                            arg = typeHintArg(env, trace, param, arg, _i + 1, originClassName, originMethodName, staticClassName);

                            variadicArgs.add(makeValue(param, arg, env, trace));
                        }

                        i++;
                        if (i < passed.length) {
                            arg = passed[i];
                        } else {
                            break;
                        }
                    }

                    passed[_i] = variadicArgs;
                    break;
                }

                passed[i] = makeArg(env, trace, param, arg, i, originClassName, originMethodName, staticClassName);
                i++;
            }

            if (!variadicMemoryExists) {
                makeImmutable(passed, parameters);
            }
        }

        return passed;
    }


    private static void makeImmutable(Memory[] passed, ParameterEntity[] parameters) {
        for (int j = parameters.length; j < passed.length; j++) {
            passed[j] = passed[j].fast_toImmutable();
        }
    }

    public static Memory[] unpackArgs(Environment env, TraceInfo trace, Memory[] passed, ParameterEntity[] parameters) {
        List<Memory> varPassed = null;

        if (passed == null) {
            return null;
        }

        int cnt = 0, paramCnt = 0;

        ParameterEntity parameterEntity = null;
        boolean variadicMemoryExists = false;

        for (Memory arg : passed) {
            if (arg instanceof VariadicMemory) {
                variadicMemoryExists = true;

                if (varPassed == null) {
                    varPassed = new ArrayList<Memory>();

                    for (int i = 0; i < cnt; i++) {
                        varPassed.add(passed[i]);
                    }
                }

                boolean isGenerator = arg.instanceOf(Generator.class);

                ForeachIterator foreachIterator = arg.getNewIterator(env, !isGenerator, false);

                if (foreachIterator == null || (!isGenerator && !arg.isTraversable())) {
                    env.warning(trace, Messages.ERR_ONLY_ARR_AND_TRAV_CAN_UNPACKED);
                } else {
                    boolean isRef;

                    while (foreachIterator.next()) {
                        if (parameters != null) {
                            if (parameterEntity == null || !parameterEntity.isVariadic()) {
                                parameterEntity = paramCnt < parameters.length ? parameters[paramCnt] : null;
                            }
                        }

                        isRef = parameterEntity != null && parameterEntity.isReference();

                        Memory value = foreachIterator.getValue();
                        varPassed.add(isRef ? value : value.fast_toImmutable());

                        paramCnt++;

                        if (parameterEntity != null && !parameterEntity.isVariadic()) {
                            parameterEntity = null;
                        }
                    }
                }
            } else {
                if (variadicMemoryExists) {
                    env.error(trace, Messages.ERR_CANNOT_USE_POS_ARG_AFTER_UNPACK.fetch());
                }

                if (varPassed != null) {
                    varPassed.add(arg);
                }

                paramCnt++;
            }
            cnt++;
        }

        if (varPassed != null) {
            passed = varPassed.toArray(new Memory[varPassed.size()]);
        }

        return passed;
    }

    public static Memory[] checkType(Environment env, TraceInfo trace, MethodEntity methodEntity, Memory... args) {
        if (args == null) {
            return null;
        }

        ParameterEntity[] parameters = methodEntity.getParameters(args.length);

        int i = 0;

        for (Memory arg : args) {
            if (i > parameters.length - 1) {
                break;
            }

            args[i] = typeHintArg(
                    env, trace, parameters[i], arg, i, methodEntity.getClazzName(), methodEntity.getName(), null
            );

            i++;
        }

        return args;
    }

    public static String getGiven(Memory passed) {
        return getGiven(passed, false);
    }

    public static String getGiven(Memory passed, boolean _short) {
        String given;
        if (passed == null) {
            given = "none";
        } else {
            return passed.getGivenString(_short);
        }

        return given;
    }

    public static void invalidType(Environment env, TraceInfo trace, ParameterEntity param, int index, Memory passed,
                                   String originClassName, String originMethodName) {
        String given = getGiven(passed);

        String method = originMethodName == null ? originClassName : originClassName + "::" + originMethodName;

        if (param.getTypeClass() == null) {
            if (param.getTypeEnum() != null && param.getType() == HintType.ANY) {
                Field[] fields = param.getTypeEnum().getFields();
                String[] names = new String[fields.length];

                for (int i = 0; i < names.length; i++) {
                    names[i] = fields[i].getName();
                }

                String namesStr = StringUtils.join(names, ", ");
                env.exception(param.getTrace(), WrapJavaExceptions.IllegalArgumentException.class,
                        Messages.ERR_INVALID_RANGE_ARGUMENT_PASSED.fetch(
                                index,
                                method,
                                namesStr,

                                trace.getFileName(),
                                trace.getStartLine() + 1,
                                trace.getStartPosition() + 1
                        )
                );
            } else if (param.getTypeHintingChecker() != null) {
                env.exception(
                        param.getTrace(),
                        BaseTypeError.class,
                        Messages.ERR_INVALID_TYPE_ARGUMENT_PASSED.fetch(
                                index,
                                method,
                                param.getTypeHintingChecker().getNeeded(env, passed),

                                trace.getFileName(),
                                trace.getStartLine() + 1,
                                trace.getStartPosition() + 1
                        )
                );
            } else {
                env.exception(
                        param.getTrace(),
                        BaseTypeError.class,
                        Messages.ERR_INVALID_SIMPLE_TYPE_ARGUMENT_PASSED.fetch(
                                index,
                                method,
                                param.getType().toString(), given,

                                trace.getFileName(),
                                trace.getStartLine() + 1,
                                trace.getStartPosition() + 1
                        )
                );
            }
        } else {
            ClassEntity need = env.fetchClass(param.getTypeClass(), false);
            String what = ClassEntity.getNeededString(need, param);

            env.exception(
                    param.getTrace(),
                    BaseTypeError.class,
                    Messages.ERR_INVALID_OBJ_ARGUMENT_PASSED.fetch(
                            index,
                            method,

                            what, given,
                            trace.getFileName(),
                            trace.getStartLine() + 1,
                            trace.getStartPosition() + 1
                    )
            );
        }
    }

    public static Memory makeValue(ParameterEntity param, Memory arg, Environment env, TraceInfo trace) {
        if (param.isReference()) {
            if (!arg.isReference() && !arg.isObject()) {
                env.error(trace, ErrorType.E_ERROR, Messages.ERR_ONLY_REF_CAN_BE_PASSED);
                arg = new ReferenceMemory(arg);
            } else if (arg.isDisallowReferenceOps()) {
                env.error(trace, ErrorType.E_ERROR, Messages.ERR_PASS_DISALLOWED_MEMORY_AS_REF, arg.getGivenString());
            }
        } else {
            arg = param.isMutable() ? arg.fast_toImmutable() : arg.toValue();
        }

        return arg;
    }


    public static void makeVariadic(ForeachIterator iterator, ArrayMemory variadicArray, ParameterEntity param,
                                    Environment env, TraceInfo trace, int index, String originClassName,
                                    String originMethodName) {
        while (iterator.next()) {
            Memory arg = iterator.getValue();

            if (!param.checkTypeHinting(env, arg)) {
                invalidType(env, trace, param, index + 1, arg, originClassName, originMethodName);
            }

            variadicArray.add(makeValue(param, iterator.getValue(), env, trace));
        }
    }
}
