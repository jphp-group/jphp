package php.runtime.ext;

import php.runtime.Startup;
import php.runtime.env.CompileScope;
import php.runtime.env.Environment;
import php.runtime.env.handler.ProgramShutdownHandler;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.ext.core.*;
import php.runtime.ext.core.classes.*;
import php.runtime.ext.core.classes.format.WrapProcessor;
import php.runtime.ext.core.classes.lib.*;
import php.runtime.ext.core.classes.lib.legacy.OldBinUtils;
import php.runtime.ext.core.classes.lib.legacy.OldItemsUtils;
import php.runtime.ext.core.classes.lib.legacy.OldMirrorUtils;
import php.runtime.ext.core.classes.lib.legacy.OldNumUtils;
import php.runtime.ext.core.classes.net.WrapServerSocket;
import php.runtime.ext.core.classes.net.WrapSocket;
import php.runtime.ext.core.classes.net.WrapSocketException;
import php.runtime.ext.core.classes.stream.*;
import php.runtime.ext.core.classes.time.*;
import php.runtime.ext.core.classes.util.*;
import php.runtime.ext.core.reflection.*;
import php.runtime.ext.support.Extension;
import php.runtime.ext.support.compile.CompileConstant;
import php.runtime.loader.sourcemap.SourceMap;

import java.io.IOException;
import java.net.SocketException;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;

public class CoreExtension extends Extension implements ProgramShutdownHandler {
    public final static String NAMESPACE = "php\\";

    @Override
    public String getName() {
        return "Core";
    }

    @Override
    public String[] getPackageNames() {
        return new String[] { "std", "core" };
    }

    @Override
    public Status getStatus() {
        return Status.STABLE;
    }

    @Override
    public void onRegister(CompileScope scope) {
        scope.registerProgramShutdownHandler(this);
        
        registerFunctions(new LangFunctions());
        registerConstants(new LangConstants());

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
        registerJavaException(scope, WrapJavaExceptions.InterruptedException.class, InterruptedException.class);

        registerClass(scope, CharUtils.class);
        registerClass(scope, StrUtils.class);
        registerClass(scope, BinUtils.class);
        registerClass(scope, NumUtils.class);
        registerClass(scope, ItemsUtils.class);
        registerClass(scope, MirrorUtils.class);
        registerClass(scope, FsUtils.class);

        registerClass(scope, OldBinUtils.class);
        registerClass(scope, OldItemsUtils.class);
        registerClass(scope, OldNumUtils.class);
        registerClass(scope, OldMirrorUtils.class);

        registerClass(scope, SharedUtils.SharedMemory.class);
        registerClass(scope, SharedUtils.SharedCollection.class);
        registerClass(scope, SharedUtils.SharedValue.class);
        registerClass(scope, SharedUtils.SharedStack.class);
        registerClass(scope, SharedUtils.SharedQueue.class);
        registerClass(scope, SharedUtils.SharedMap.class);
        registerClass(scope, SharedUtils.class);

        registerClass(scope, WrapPackageLoader.class);
        registerClass(scope, WrapClassLoader.class);
        registerClass(scope, WrapClassLoader.WrapLauncherClassLoader.class);

        registerClass(scope, WrapLocale.class);
        registerClass(scope, WrapScanner.class);
        registerClass(scope, WrapFlow.class);
        registerClass(scope, WrapConfiguration.class);
        registerClass(scope, WrapRegex.class);
        registerJavaExceptionForContext(scope, WrapRegex.RegexException.class, WrapRegex.class);

        registerClass(scope, WrapTimeZone.class);
        registerClass(scope, WrapTimeFormat.class);
        registerClass(scope, WrapTime.class);
        registerWrapperClass(scope, TimerTask.class, WrapTimer.class);

        registerClass(scope, WrapInvoker.class);
        registerClass(scope, WrapModule.class);
        registerClass(scope, WrapPackage.class);
        registerWrapperClass(scope, SourceMap.class, WrapSourceMap.class);
        registerClass(scope, WrapEnvironment.class);
        registerClass(scope, WrapEnvironmentVariables.class);
        registerClass(scope, WrapThreadGroup.class);
        registerClass(scope, WrapThread.class);
        registerClass(scope, WrapSystem.class);

        registerClass(scope, Reflector.class);
        registerClass(scope, Reflection.class);
        registerClass(scope, ReflectionException.class);
        registerClass(scope, ReflectionExtension.class);
        registerClass(scope, ReflectionFunctionAbstract.class);
        registerClass(scope, ReflectionFunction.class);
        registerClass(scope, ReflectionParameter.class);
        registerClass(scope, ReflectionProperty.class);
        registerClass(scope, ReflectionMethod.class);
        registerClass(scope, ReflectionClass.class);
        registerClass(scope, ReflectionObject.class);

        registerClass(scope, WrapJavaExceptions.NotImplementedException.class);

        // stream
        registerJavaException(scope, WrapIOException.class, IOException.class);
        registerClass(scope, FileObject.class);
        registerClass(scope, Stream.class);
        registerClass(scope, FileStream.class);
        registerClass(scope, MiscStream.class);
        registerClass(scope, MemoryMiscStream.class);
        registerClass(scope, ResourceStream.class);

        // net
        registerClass(scope, WrapSocket.class);
        registerClass(scope, WrapServerSocket.class);
        registerJavaException(scope, WrapSocketException.class, SocketException.class);

        registerClass(scope, WrapThreadPool.class);
        registerClass(scope, WrapFuture.class);
        registerJavaException(scope, WrapJavaExceptions.TimeoutException.class, TimeoutException.class);

        registerClass(scope, WrapProcessor.class);
        registerClass(scope, WrapProcessor.ProcessorException.class);
        registerClass(scope, WrapProcess.class);
    }

    @Override
    public void onLoad(Environment env) {
        Stream.initEnvironment(env);
    }


    @Override
    public void onShutdown(CompileScope scope, Environment env) {
        WrapTimer.cancelAll();
    }
}
