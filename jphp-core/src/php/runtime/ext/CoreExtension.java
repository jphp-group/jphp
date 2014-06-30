package php.runtime.ext;

import php.runtime.env.CompileScope;
import php.runtime.env.Environment;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.ext.core.*;
import php.runtime.ext.core.classes.*;
import php.runtime.ext.core.classes.format.WrapProcessor;
import php.runtime.ext.core.classes.lib.*;
import php.runtime.ext.core.classes.net.WrapServerSocket;
import php.runtime.ext.core.classes.net.WrapSocket;
import php.runtime.ext.core.classes.net.WrapSocketException;
import php.runtime.ext.core.classes.time.WrapTime;
import php.runtime.ext.core.classes.time.WrapTimeFormat;
import php.runtime.ext.core.classes.time.WrapTimeZone;
import php.runtime.ext.core.classes.util.*;
import php.runtime.ext.core.reflection.*;
import php.runtime.ext.core.classes.stream.*;
import php.runtime.ext.support.Extension;
import php.runtime.ext.support.compile.CompileConstant;

import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.TimeoutException;

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

        registerJavaException(scope, WrapJavaExceptions.IllegalArgumentException.class, IllegalArgumentException.class);
        registerJavaException(scope, WrapJavaExceptions.IllegalStateException.class, IllegalStateException.class);
        registerJavaException(scope, WrapJavaExceptions.NumberFormatException.class, NumberFormatException.class);

        registerNativeClass(scope, CharUtils.class);
        registerNativeClass(scope, StrUtils.class);
        registerNativeClass(scope, BinUtils.class);
        registerNativeClass(scope, NumUtils.class);
        registerNativeClass(scope, ItemsUtils.class);

        registerNativeClass(scope, WrapLocale.class);
        registerNativeClass(scope, WrapScanner.class);
        registerNativeClass(scope, WrapFlow.class);
        registerNativeClass(scope, WrapRegex.class);
        registerJavaExceptionForContext(scope, WrapRegex.RegexException.class, WrapRegex.class);

        registerNativeClass(scope, WrapTimeZone.class);
        registerNativeClass(scope, WrapTimeFormat.class);
        registerNativeClass(scope, WrapTime.class);

        registerNativeClass(scope, WrapInvoker.class);
        registerNativeClass(scope, WrapModule.class);
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
        registerJavaException(scope, WrapJavaExceptions.TimeoutException.class, TimeoutException.class);

        registerNativeClass(scope, WrapProcessor.class);

        registerNativeClass(scope, WrapProcess.class);
    }

    @Override
    public void onLoad(Environment env) {
        Stream.initEnvironment(env);
    }
}
