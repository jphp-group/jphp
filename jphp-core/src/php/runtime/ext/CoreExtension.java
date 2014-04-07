package php.runtime.ext;

import php.runtime.env.CompileScope;
import php.runtime.env.Environment;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.ext.core.*;
import php.runtime.ext.core.classes.*;
import php.runtime.ext.core.classes.net.WrapServerSocket;
import php.runtime.ext.core.classes.net.WrapSocket;
import php.runtime.ext.core.classes.net.WrapSocketException;
import php.runtime.ext.core.classes.util.BinUtils;
import php.runtime.ext.core.classes.util.StrUtils;
import php.runtime.ext.core.reflection.*;
import php.runtime.ext.core.stream.*;
import php.runtime.ext.support.Extension;
import php.runtime.ext.support.compile.CompileConstant;

import java.io.IOException;
import java.net.SocketException;

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

        registerFunctions(new StringFunctions());

        registerConstants(new ArrayConstants());
        registerFunctions(new ArrayFunctions());

        registerConstants(new OutputConstants());
        registerFunctions(new OutputFunctions());

        // T_ERROR
        for (ErrorType el : ErrorType.values())
            constants.put(el.name(), new CompileConstant(el.name(), el.value));

        registerNativeClass(scope, StrUtils.class);
        registerNativeClass(scope, BinUtils.class);

        registerNativeClass(scope, WrapEnvironment.class);
        registerNativeClass(scope, WrapThreadGroup.class);
        registerNativeClass(scope, WrapThread.class);
        registerNativeClass(scope, WrapSystem.class);

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
        registerJavaException(scope, WrapIOException.class, IOException.class);
        registerNativeClass(scope, FileObject.class);
        registerNativeClass(scope, Stream.class);
        registerNativeClass(scope, FileStream.class);
        registerNativeClass(scope, MiscStream.class);
        registerNativeClass(scope, MemoryMiscStream.class);
        registerNativeClass(scope, ResourceStream.class);

        // net
        registerNativeClass(scope, WrapSocket.class);
        registerNativeClass(scope, WrapServerSocket.class);
        registerJavaException(scope, WrapSocketException.class, SocketException.class);

        registerNativeClass(scope, WrapExecutorService.class);
        registerNativeClass(scope, WrapFuture.class);
    }

    @Override
    public void onLoad(Environment env) {
        Stream.initEnvironment(env);
    }
}
