package php.runtime.ext;

import php.runtime.env.CompileScope;
import php.runtime.env.Environment;
import php.runtime.ext.core.reflection.*;
import php.runtime.ext.core.stream.FileObject;
import php.runtime.ext.core.stream.FileStream;
import php.runtime.ext.core.stream.PHP_IOException;
import php.runtime.ext.core.stream.Stream;
import php.runtime.ext.support.Extension;
import php.runtime.ext.support.compile.CompileConstant;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.ext.core.*;

public class CoreExtension extends Extension {
    @Override
    public String getName() {
        return "Core";
    }

    @Override
    public String getVersion() {
        return "~";
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerConstants(new LangConstants());
        registerFunctions(new LangFunctions());

        registerConstants(new InfoConstants());
        registerFunctions(new InfoFunctions());

        registerConstants(new MathConstants());
        registerFunctions(new MathFunctions());

        registerConstants(new StringConstants());
        registerFunctions(new StringFunctions());

        registerConstants(new ArrayConstants());
        registerFunctions(new ArrayFunctions());

        registerConstants(new OutputConstants());
        registerFunctions(new OutputFunctions());

        registerConstants(new FileConstants());
        registerFunctions(new FileFunctions());

        // T_ERROR
        for (ErrorType el : ErrorType.values())
            constants.put(el.name(), new CompileConstant(el.name(), el.value));

        registerNativeClass(scope, Reflector.class);
        registerNativeClass(scope, Reflection.class);
        registerNativeClass(scope, ReflectionException.class);
        registerNativeClass(scope, ReflectionExtension.class);
        registerNativeClass(scope, ReflectionFunctionAbstract.class);
        registerNativeClass(scope, ReflectionFunction.class);
        registerNativeClass(scope, ReflectionParameter.class);
        registerNativeClass(scope, ReflectionProperty.class);
        registerNativeClass(scope, ReflectionMethod.class);
        registerNativeClass(scope, ReflectionClass.class);
        registerNativeClass(scope, ReflectionObject.class);

        // stream
        registerNativeClass(scope, PHP_IOException.class);
        registerNativeClass(scope, FileObject.class);
        registerNativeClass(scope, Stream.class);
        registerNativeClass(scope, FileStream.class);
    }

    @Override
    public void onLoad(Environment env) {
        Stream.initEnvironment(env);
    }
}
