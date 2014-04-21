package php.runtime.env;

import org.develnext.jphp.core.compiler.jvm.JvmCompiler;
import php.runtime.Information;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.common.Constants;
import php.runtime.common.Messages;
import php.runtime.common.StringUtils;
import php.runtime.env.handler.*;
import php.runtime.env.message.CustomSystemMessage;
import php.runtime.env.message.NoticeMessage;
import php.runtime.env.message.SystemMessage;
import php.runtime.env.message.WarningMessage;
import php.runtime.exceptions.CriticalException;
import php.runtime.exceptions.CustomErrorException;
import php.runtime.exceptions.FatalException;
import php.runtime.exceptions.JPHPException;
import php.runtime.exceptions.support.ErrorException;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.ext.java.JavaReflection;
import php.runtime.ext.support.Extension;
import php.runtime.ext.support.compile.CompileConstant;
import php.runtime.invoke.Invoker;
import php.runtime.invoke.ObjectInvokeHelper;
import php.runtime.lang.BaseException;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.IObject;
import php.runtime.lang.UncaughtException;
import php.runtime.loader.dump.ModuleDumper;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.ReferenceMemory;
import php.runtime.memory.StringMemory;
import php.runtime.output.OutputBuffer;
import php.runtime.reflection.*;
import php.runtime.util.JVMStackTracer;

import java.io.File;
import java.io.OutputStream;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static php.runtime.exceptions.support.ErrorType.*;

public class Environment {
    public final int id;
    public final CompileScope scope;
    public final Map<String, Memory> configuration = new HashMap<String, Memory>();
    public final static Map<String, ConfigChangeHandler> configurationHandler;

    private Set<String> includePaths;

    public SplClassLoader __autoload = null;
    public SplClassLoader defaultAutoLoader = null;
    protected final List<SplClassLoader> classLoaders = new LinkedList<SplClassLoader>();

    // errors
    private int errorFlags = E_ALL.value ^ (E_NOTICE.value | E_STRICT.value | E_DEPRECATED.value);
    private Stack<Integer> silentFlags = new Stack<Integer>();

    private SystemMessage lastMessage;

    private ErrorReportHandler errorReportHandler;
    private ErrorHandler previousErrorHandler;
    private ErrorHandler errorHandler;
    private ShellExecHandler shellExecHandler = ShellExecHandler.DEFAULT;

    private ExceptionHandler previousExceptionHandler;
    private ExceptionHandler exceptionHandler = ExceptionHandler.DEFAULT;

    private OutputBuffer defaultBuffer;
    private Stack<OutputBuffer> outputBuffers;

    private List<ShutdownHandler> shutdownFunctions = new LinkedList<ShutdownHandler>();

    // charset, locale
    private Locale locale = Locale.getDefault();
    private Charset defaultCharset = Charset.forName("UTF-8");

    // vars
    protected final ArrayMemory globals;
    protected final Map<String, ReferenceMemory> statics;
    protected final Map<String, ConstantEntity> constants;
    protected final Map<String, ModuleEntity> included;
    protected final Map<String, Object> userValues = new HashMap<String, Object>();

    // classes, funcs, consts
    public final Map<String, ClassEntity> classMap = new LinkedHashMap<String, ClassEntity>();
    public final Map<String, FunctionEntity> functionMap = new LinkedHashMap<String, FunctionEntity>();
    public final Map<String, ConstantEntity> constantMap = new LinkedHashMap<String, ConstantEntity>();

    // call stack
    protected final static int CALL_STACK_INIT_SIZE = 255;

    private int callStackTop = 0;
    private int maxCallStackTop = -1;
    private CallStackItem[] callStack = new CallStackItem[CALL_STACK_INIT_SIZE];

    protected static final ThreadLocal<Environment> environment = new ThreadLocal<Environment>();

    /**
     * Gets Environment for current thread context
     * @return
     */
    public static Environment current(){
        return environment.get();
    }

    private final ReferenceQueue<IObject> gcObjectRefQueue = new ReferenceQueue<IObject>();
    private final Set<WeakReference<IObject>> gcObjects = new HashSet<WeakReference<IObject>>();
    private static final AtomicInteger ids = new AtomicInteger();
    private static final Stack<Integer> freeIds = new Stack<Integer>();

    public void doFinal() throws Throwable {
        for (ShutdownHandler handler : shutdownFunctions){
            try {
                handler.call();
            } catch (DieException e){
                finalizeObjects();
                catchUncaught(e);
                break;
            } catch (BaseException e){
                catchUncaught(e);
                break;
            } catch (ErrorException e){
                catchUncaught(e);
                break;
            } catch (Exception e){
                catchUncaught(e);
                break;
            }
        }

        finalizeObjects();
        flushAll();
        lastMessage = null;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        freeIds.push(id);
    }

    /**
     * Remove all method which does not destruct
     * @throws Throwable
     */
    public void finalizeObjects() throws Throwable {
        cleanGcObjects();
        for (WeakReference<IObject> el : gcObjects){
            IObject o = el.get();
            if (o == null)
                continue;

            ClassEntity entity = o.getReflection();
            if (entity.methodDestruct != null) {
                if (!o.isFinalized()) {
                    o.doFinalize();
                    pushCall(o, entity.methodDestruct.getName());
                    try {
                        entity.methodDestruct.invokeDynamic(o, this);
                    } finally {
                        popCall();
                    }
                }
            }
        }
        gcObjects.clear();
    }

    private void cleanGcObjects() throws Throwable {
        Reference<? extends IObject> object;
        while (true){
            object = gcObjectRefQueue.poll();
            if (object == null)
                break;
            else
                gcObjects.remove(object);
        }
    }

    public Environment(Environment parent) {
        this(parent.scope, parent.defaultBuffer.getOutput());

        configuration.putAll(parent.configuration);
        //constants.putAll(parent.constants);

        classMap.putAll(parent.classMap);
        for(ClassEntity e : classMap.values()) {
            try {
                e.initEnvironment(this);
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        }

        functionMap.putAll(parent.functionMap);
        constantMap.putAll(parent.constantMap);

        this.included.putAll(parent.included);
    }

    public Environment(CompileScope scope, OutputStream output) {
        this.scope = scope;
        synchronized (freeIds) {
            if (freeIds.empty()) {
                this.id = ids.getAndIncrement();
            } else {
                this.id = freeIds.peek();
            }
        }

        this.outputBuffers = new Stack<OutputBuffer>();

        this.defaultBuffer = new OutputBuffer(this, null);
        this.defaultBuffer.setOutput(output);
        this.getOutputBuffers().push(defaultBuffer);

        this.includePaths = new HashSet<String>();

        this.globals = new ArrayMemory();
        this.statics = new HashMap<String, ReferenceMemory>();
        this.included = new LinkedHashMap<String, ModuleEntity>();
        this.setErrorReportHandler(new ErrorReportHandler() {
            @Override
            public boolean onError(SystemMessage error) {
                Environment.this.echo(error.getDebugMessage());
                Environment.this.echo("\n");
                return false;
            }

            @Override
            public boolean onFatal(ErrorException error) {
                Environment.this.echo("\n");
                Environment.this.echo(error.getType().getTypeName() + ": " + error.getMessage());
                if (error.getTraceInfo() != null){
                    Environment.this.echo(
                            " in " + error.getTraceInfo().getFileName()
                                    + " on line " + (error.getTraceInfo().getStartLine() + 1)
                                    + ", position " + (error.getTraceInfo().getStartPosition() + 1)
                    );
                }
                return false;
            }
        });

        this.globals.put("GLOBALS", this.globals);
        this.constants = new HashMap<String, ConstantEntity>();

        //classMap.putAll(scope.getClassMap());
        functionMap.putAll(scope.getFunctionMap());
        constantMap.putAll(scope.getConstantMap());

        Memory splAutoloader = new StringMemory("__$jphp_spl_autoload");
        Invoker invoker = Invoker.valueOf(this, null, splAutoloader);
        if (invoker != null)
            this.defaultAutoLoader = new SplClassLoader(invoker, splAutoloader);

        for(Extension e: scope.extensions.values())
            e.onLoad(this);

        environment.set(this);
    }

    public void pushCall(TraceInfo trace, IObject self, Memory[] args, String function, String clazz, String staticClazz){
        if (callStackTop >= callStack.length){
            CallStackItem[] newCallStack = new CallStackItem[callStack.length * 2];
            System.arraycopy(callStack, 0, newCallStack, 0, callStack.length);
            callStack = newCallStack;
        }

        if (callStackTop < maxCallStackTop)
            callStack[callStackTop++].setParameters(trace, self, args, function, clazz, staticClazz);
        else
            callStack[callStackTop++] = new CallStackItem(trace, self, args, function, clazz, staticClazz);

        maxCallStackTop = callStackTop;
    }

    public void pushCall(IObject self, String method, Memory... args){
        pushCall(null, self, args, method, self.getReflection().getName(), null);
    }

    public void pushCall(TraceInfo trace, IObject self, String method, Memory... args){
        pushCall(trace, self, args, method, self.getReflection().getName(), null);
    }

    public void popCall(){
        callStack[--callStackTop].clear(); // clear for GC
    }

    public CallStackItem peekCall(int depth){
        if (callStackTop - depth > 0) {
            return callStack[callStackTop - depth - 1];
        } else {
            return null;
        }
    }

    public TraceInfo trace(){
        if (callStackTop <= 0)
            return TraceInfo.UNKNOWN;
        return peekCall(0).trace;
    }

    public TraceInfo trace(int systemOffsetStackTrace){
        return new TraceInfo(Thread.currentThread().getStackTrace()[systemOffsetStackTrace]);
    }

    public int getCallStackTop(){
        return callStackTop;
    }

    public CallStackItem[] getCallStackSnapshot(){
        CallStackItem[] result = new CallStackItem[callStackTop];
        int i = 0;
        for(CallStackItem el : callStack){
            if (i == callStackTop)
                break;

            result[i] = new CallStackItem(el);
            i++;
        }

        return result;
    }

    public Environment(OutputStream output){
        this(new CompileScope(), output);
    }

    public Environment(CompileScope scope){
        this(scope, null);
    }

    public Environment(){
        this((OutputStream) null);
    }

    public Stack<OutputBuffer> getOutputBuffers() {
        return outputBuffers;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String findInIncludePaths(String path){
        if (new File(path).exists())
            return path;

        for(String e : includePaths)
            if (new File(e, path).exists())
                return e + path;

        return null;
    }

    public Map<String, ModuleEntity> getIncluded() {
        return included;
    }

    public CompileScope getScope() {
        return scope;
    }

    public ArrayMemory getGlobals() {
        return globals;
    }

    public Charset getDefaultCharset() {
        return defaultCharset;
    }

    public Set<String> getIncludePaths() {
        return includePaths;
    }

    public void addIncludePath(String value){
        includePaths.add(value);
    }

    public void setIncludePaths(Set<String> includePaths) {
        this.includePaths = includePaths;
    }

    public Map<String, ClassEntity> getLoadedClasses() {
        return classMap;
    }

    public Map<String, FunctionEntity> getLoadedFunctions() {
        return functionMap;
    }

    public boolean isLoadedClass(String lowerName){
        return classMap.containsKey(lowerName);
    }

    public boolean isLoadedFunction(String lowerName){
        return functionMap.containsKey(lowerName);
    }

    public boolean isLoadedConstant(String lowerName){
        return constantMap.containsKey(lowerName);
    }

    private final Set<String> autoloadLocks = new HashSet<String>();

    public ClassEntity autoloadCall(String name, String lowerName) {
        // detect recursion in autoload
        if (StringUtils.isValidClassName(name) && autoloadLocks.add(lowerName)){
            StringMemory tmp = new StringMemory(name);
            for(SplClassLoader loader : classLoaders)
                loader.load(tmp);

            if (defaultAutoLoader != null){
                defaultAutoLoader.load(tmp);
            }

            autoloadLocks.remove(lowerName);
            return fetchClass(name, false);
        } else
            return null;
    }

    public ClassEntity fetchClass(Class<?> clazz){
        Reflection.Name name = clazz.getAnnotation(Reflection.Name.class);
        ClassEntity result;
        if (name != null)
            result = fetchClass(name.value(), false);
        else
            result = fetchClass(clazz.getSimpleName(), false);

        if (result == null)
            throw new CriticalException("Native class is not registered - " + clazz.getName());

        return result;
    }

    public ClassEntity fetchClass(String name) {
        return fetchClass(name, false);
    }

    public ClassEntity fetchClass(String name, boolean autoLoad) {
        return fetchClass(name, name.toLowerCase(), autoLoad);
    }

    public ClassEntity fetchMagicClass(String name){
        return fetchMagicClass(name, name.toLowerCase());
    }

    public ClassEntity fetchMagicClass(String name, String nameL){
        if ("self".equals(nameL)){
            ClassEntity e = getContextClass();
            if (e == null)
                e = getLateStaticClass();
            return e;
        } else if ("static".equals(nameL)){
            return getLateStaticClass();
        }/* else if ("parent".equals(nameL)){
            ClassEntity e = getContextClass();
            if (e == null)
                e = getLateStaticClass();
            return e == null ? null : e.getParent();
        }*/ else
            return null;
    }

    public ClassEntity fetchClass(String name, String nameL, boolean autoLoad) {
        ClassEntity entity = classMap.get(nameL);

        if (entity == null){
            entity = scope.fetchUserClass(nameL);
            if (entity != null) {
                try {
                    entity.initEnvironment(this);
                } catch (Throwable throwable) {
                    throw new CriticalException(throwable);
                }
                classMap.put(entity.getLowerName(), entity);
                return entity;
            }

            return autoLoad ? autoloadCall(name, nameL) : null;
        } else {
            return entity;/*
            if (isLoadedClass(nameL) || entity.isInternal())
                return entity;
            else {
                if (autoLoad){
                    return autoloadCall(name);
                }
                return null;
            }*/
        }
    }

    public Memory findConstant(String name, String nameLower){
        ConstantEntity entity = constantMap.get(nameLower);
        if (entity != null) {
            if (!entity.caseSensitise || name.equals(entity.getName()))
                return entity.getValue();
        }

        CompileConstant constant = scope.findCompileConstant(name);
        if (constant != null)
            return constant.value;

        return null;
    }

    public Memory findConstant(String name){
        return findConstant(name, name.toLowerCase());
    }

    public boolean defineConstant(String name, Memory value, boolean caseSensitise){
        Memory constant = findConstant(name);
        if (constant != null)
            return false;

        constantMap.put(name.toLowerCase(), new ConstantEntity(name, value, caseSensitise));
        return true;
    }

    public Memory getConfigValue(String name, Memory defaultValue){
        Memory result = null;
        if (scope.configuration == null || (result = configuration.get(name)) != null){
            if (result == null) result = configuration.get(name);
            if (result == null)
                return defaultValue;

            return result;
        }

        result = scope.configuration.get(name);
        return result == null ? defaultValue : result;
    }

    public Memory getConfigValue(String name){
        return getConfigValue(name, null);
    }

    public ArrayMemory getConfigValues(String prefix, boolean includingGlobal){
        if (prefix != null)
            prefix = prefix + ".";

        ArrayMemory result = new ArrayMemory();
        if (includingGlobal)
            if (scope.configuration != null){
                for(Map.Entry<String, Memory> entry : scope.configuration.entrySet()){
                    String key = entry.getKey();
                    if (prefix != null && !key.startsWith(prefix)) continue;

                    result.put(key, entry.getValue().toImmutable());
                }
            }

        for(Map.Entry<String, Memory> entry : configuration.entrySet()){
            String key = entry.getKey();

            if (prefix != null && !key.startsWith(prefix)) continue;
            result.put(key, entry.getValue().toImmutable());
        }
        return result;
    }

    public Memory setConfigValue(String name, Memory value){
        value = value.toValue();
        if (!value.isString())
            value = new StringMemory(value.toString());   // fix capability with zend php

        ConfigChangeHandler handler = configurationHandler.get(name);
        if (handler != null)
            handler.onChange(this, value);

        return configuration.put(name, value);
    }

    public void restoreConfigValue(String name){
        configuration.remove(name);

        ConfigChangeHandler handler = configurationHandler.get(name);
        if (handler != null)
            handler.onChange(this, getConfigValue(name));
    }

    public Memory getOrCreateGlobal(String name) {
        return globals.refOfIndex(name);
    }

    public Memory getOrCreateStatic(String name, Memory initValue) {
        ReferenceMemory result = statics.get(name);
        if (result == null) {
            result = new ReferenceMemory(initValue);
            statics.put(name, result);
        }
        return result; // globals.getByScalarOrCreate(name, initValue);
    }

    public Memory getStatic(String name){
        return statics.get(name);
    }

    @SuppressWarnings("unchecked")
    public <T> T getUserValue(String name, Class<T> clazz){
        return (T) userValues.get(name);
    }

    public void setUserValue(String name, Object value){
        userValues.put(name, value);
    }

    public boolean removeUserValue(String name){
        return userValues.remove(name) != null;
    }

    public int getErrorFlags() {
        return errorFlags;
    }

    public void setErrorFlags(int errorFlags) {
        this.errorFlags = errorFlags;
    }

    public SystemMessage getLastMessage() {
        return lastMessage;
    }

    public ShellExecHandler getShellExecHandler() {
        return shellExecHandler;
    }

    public void setShellExecHandler(ShellExecHandler shellExecHandler) {
        this.shellExecHandler = shellExecHandler;
    }

    public ExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    public void setExceptionHandler(ExceptionHandler exceptionHandler) {
        this.previousExceptionHandler = this.exceptionHandler;
        this.exceptionHandler = exceptionHandler == null ? ExceptionHandler.DEFAULT : exceptionHandler;
    }

    public ErrorReportHandler getErrorReportHandler() {
        return errorReportHandler;
    }

    public void setErrorReportHandler(ErrorReportHandler errorReportHandler) {
        this.errorReportHandler = errorReportHandler;
    }

    public ExceptionHandler getPreviousExceptionHandler() {
        return previousExceptionHandler;
    }

    public ErrorHandler getPreviousErrorHandler() {
        return previousErrorHandler;
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    protected void triggerError(ErrorException err){
        ErrorType type = err.getType();
        if (type.isFatal() || isHandleErrors(type)) {
            lastMessage = new CustomSystemMessage(
                    err.getType(), new CallStackItem(err.getTraceInfo()), new Messages.Item(err.getMessage())
            );
            throw err;
        }
    }

    public boolean catchUncaught(Exception e){
        if (e instanceof UncaughtException)
            return catchUncaught((UncaughtException)e);
        else if (e instanceof DieException){
            System.exit(((DieException) e).getExitCode());
            return true;
        } else if (e instanceof ErrorException) {
            ErrorException er = (ErrorException)e;
            echo("\n");

            echo("\n[" + er.getType().name() + "] " + e.getMessage());
            echo("\n    at line " + (er.getTraceInfo().getStartLine() + 1));
            echo(", position " + (er.getTraceInfo().getStartPosition() + 1));
            echo("\n");
            echo("\n    in '" + er.getTraceInfo().getFileName() + "'");

            JVMStackTracer tracer = scope.getStackTracer(e);
            for(JVMStackTracer.Item el : tracer){
                echo("\n\tat " + (el.isInternal() ? "" : "-> ") + el);
            }
            return true;
        } else if (e instanceof BaseException){
            BaseException be = (BaseException)e;
            try {
                ExceptionHandler.DEFAULT.onException(this, be);
                return true;
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        } else {
            throw new RuntimeException(e);
        }
    }

    public boolean catchUncaught(UncaughtException e){
        if (exceptionHandler != null){
            try {
                exceptionHandler.onException(this, e.getException());
            } catch (DieException _e){
                catchUncaught(_e);
            } catch (ErrorException _e){
                catchUncaught(_e);
            } catch (BaseException _e){
                catchUncaught(_e);
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
            return true;
        } else
            return false;
    }

    public void error(TraceInfo trace, ErrorType type, String message, Object... args){
        error(trace, type, new Messages.Item(message), args);
    }

    public void error(TraceInfo trace, ErrorType type, Messages.Item message, Object... args){
        if (type.isFatal()) {
            if (type.isHandled() && errorHandler != null && ErrorType.check(errorHandler.errorHandlerFlags, type)){
                triggerMessage(new CustomSystemMessage(type, new CallStackItem(trace), message, args));
            } else
                triggerError(new CustomErrorException(type, message.fetch(args), trace));
        } else {
            triggerMessage(new CustomSystemMessage(type, new CallStackItem(trace), message, args));
        }
    }

    public void error(ErrorType type, String message, Object... args){
        error(trace(), type, message, args);
    }

    public void error(TraceInfo trace, String message, Object... args){
        error(trace, E_ERROR, message, args);
    }

    public void triggerMessage(SystemMessage message){
        lastMessage = message;
        if (errorHandler != null){
            if (errorHandler.onError(this, message))
                return;
        }

        if (errorReportHandler != null && isHandleErrors(message.getType()))
            errorReportHandler.onError(message);
    }

    public void exception(TraceInfo trace, String message, Object... args){
        BaseException e = new BaseException(this);
        exception(trace, e, message, args);
    }

    public void exception(String message, Object... args){
        exception(trace(), message);
    }

    public void exception(TraceInfo trace, BaseException e, String message, Object... args){
        __clearSilent();
        if (args == null || args.length == 0)
            e.__construct(this, new StringMemory(
                    message
            ));
        else
            e.__construct(this, new StringMemory(
                    String.format(message, args)
            ));
        e.setTraceInfo(this, trace);
        throw e;
    }

    public void exception(BaseException e, String message, Object... args){
        exception(trace(), e, message, args);
    }

    public void exception(Class<? extends BaseException> e, String message, Object... args){
        ClassEntity entity = fetchClass(e);
        exception((BaseException) entity.newObjectWithoutConstruct(this), message, args);
    }

    public boolean isHandleErrors(ErrorType type){
        return ErrorType.check(errorFlags, type);
    }

    public void warning(String message, Object... args){
        triggerMessage(new WarningMessage(peekCall(0), new Messages.Item(message), args));
    }

    public void warning(TraceInfo trace, String message, Object... args){
        error(trace, E_WARNING, message, args);
    }

    public void warning(TraceInfo trace, Messages.Item message, Object... args){
        error(trace, E_WARNING, message, args);
    }

    public void notice(String message, Object... args){
        triggerMessage(new NoticeMessage(peekCall(0), new Messages.Item(message), args));
    }

    public OutputBuffer getDefaultBuffer() {
        return defaultBuffer;
    }

    public OutputBuffer pushOutputBuffer(Memory callback, int chunkSize, boolean erase){
        Stack<OutputBuffer> outputBuffers = getOutputBuffers();

        OutputBuffer buffer = new OutputBuffer(this, peekOutputBuffer(), callback, chunkSize, erase);
        buffer.setLevel(outputBuffers.size());
        buffer.setType(OutputBuffer.Type.USER);

        outputBuffers.push(buffer);
        return buffer;
    }

    public OutputBuffer popOutputBuffer() throws Throwable {
        Stack<OutputBuffer> outputBuffers = getOutputBuffers();

        if (outputBuffers.empty()) return null;
        if (outputBuffers.peek().isRoot()) return null;

        OutputBuffer result = outputBuffers.pop();
        result.close();
        return result;
    }

    public List<OutputBuffer> allOutputBuffers() {
        Stack<OutputBuffer> outputBuffers = getOutputBuffers();

        List<OutputBuffer> result = new ArrayList<OutputBuffer>();
        for(OutputBuffer el : outputBuffers)
            result.add(el);

        return result;
    }

    public OutputBuffer peekOutputBuffer(){
        Stack<OutputBuffer> outputBuffers = getOutputBuffers();

        return outputBuffers.empty() ? null : outputBuffers.peek();
    }

    public void echo(byte[] bytes, int length){
        OutputBuffer buffer = peekOutputBuffer();
        if (buffer != null)
            try {
                buffer.write(bytes, length);
            } catch (RuntimeException e){
                throw e;
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
    }

    public void echo(Memory value){
        OutputBuffer buffer = peekOutputBuffer();
        if (buffer != null)
            try {
                buffer.write(value);
            } catch (RuntimeException e){
                throw e;
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
    }

    public void echo(String value){
        OutputBuffer buffer = peekOutputBuffer();
        if (buffer != null)
            try {
                buffer.write(value);
            } catch (RuntimeException e){
                throw e;
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
    }

    public void flushAll() throws Throwable {
        while (popOutputBuffer() != null);
        defaultBuffer.close();
    }

    public ModuleEntity importCompiledModule(Context context, boolean debugInformation) throws Throwable {
        String moduleName = context.getModuleName();
        ModuleEntity module = moduleName == null ? null : scope.findUserModule(moduleName);
        if (module == null) {
            ModuleDumper moduleDumper = new ModuleDumper(context, this, debugInformation);
            module = moduleDumper.load(context.getInputStream(getDefaultCharset()));
            synchronized (scope) {
                scope.loadModule(module);
            }
        }

        registerModule(module);
        return module;
    }

    public ModuleEntity importModule(Context context) throws Throwable {
        String moduleName = context.getModuleName();
        ModuleEntity module = moduleName == null ? null : scope.findUserModule(moduleName);
        if (module == null){
            JvmCompiler compiler = new JvmCompiler(this, context);
            module = compiler.compile(true);
            synchronized (scope) {
                scope.loadModule(module);
            }
        }

        registerModule(module);
        return module;
    }

    public void registerModule(ModuleEntity module) {
        for(ClassEntity entity : module.getClasses()) {
            if (entity.isStatic()){
                classMap.put(entity.getLowerName(), entity);
                //entity.initEnvironment(this); // TODO : check and fix for traits
            }
        }

        for(FunctionEntity entity : module.getFunctions()) {
            if (entity.isStatic())
                functionMap.put(entity.getLowerName(), entity);
        }

        for(ConstantEntity entity : module.getConstants()) {
            constantMap.put(entity.getLowerName(), entity);
        }
    }

    /***** UTILS *****/
    public Memory __getConstant(String name, String lowerName, TraceInfo trace){
        Memory constant = findConstant(name, lowerName);

        if (constant == null){
            error(trace, E_NOTICE, Messages.ERR_USE_UNDEFINED_CONSTANT, name, name);
            int p = name.lastIndexOf(Information.NAMESPACE_SEP_CHAR);
            if (p > -1) // for global scope
                return StringMemory.valueOf(name.substring(p + 1));
            else
                return StringMemory.valueOf(name);
        }

        return constant;
    }

    private Memory __include(String fileName, ArrayMemory locals, TraceInfo trace, boolean once)
            throws Throwable {
        File file = new File(fileName);
        if (!file.exists()){
            warning(trace, Messages.ERR_INCLUDE_FAILED, "include", fileName);
            return Memory.FALSE;
        } else {
            ModuleEntity module = importModule(new Context(file, getDefaultCharset()));
            included.put(module.getName(), module);

            Memory result;
            pushCall(trace, null, new Memory[]{new StringMemory(fileName)}, once ? "include_once" : "include", null, null);
            try {
                result = module.include(this, locals);
            } finally {
                popCall();
            }
            return result;
        }
    }

    public Memory __include(String fileName) throws Throwable {
        return __include(fileName, globals, null);
    }

    public Memory __includeOnce(String fileName, ArrayMemory locals, TraceInfo trace)
            throws Throwable {
        Context context = new Context(new File(fileName), defaultCharset);
        if (included.containsKey(context.getModuleName()))
            return Memory.TRUE;
        return __include(fileName, locals, trace, true);
    }

    public Memory __include(String fileName, ArrayMemory locals, TraceInfo trace)
            throws Throwable {
        return __include(fileName, locals, trace, false);
    }

    private Memory __require(String fileName, ArrayMemory locals, TraceInfo trace, boolean once)
            throws Throwable {
        File file = new File(fileName);
        if (!file.exists()){
            error(trace, E_ERROR, Messages.ERR_REQUIRE_FAILED.fetch("require", fileName));
            return Memory.NULL;
        } else {
            ModuleEntity module = importModule(new Context(file, getDefaultCharset()));
            included.put(module.getName(), module);

            Memory result;
            pushCall(trace, null, new Memory[]{new StringMemory(fileName)}, once ? "require_once" : "require", null, null);
            try {
                result = module.include(this, locals);
            } finally {
                popCall();
            }
            return result;
        }
    }

    public Memory __require(String fileName, ArrayMemory locals, TraceInfo trace)
            throws Throwable {
        return __require(fileName, locals, trace, false);
    }

    public Memory __requireOnce(String fileName, ArrayMemory locals, TraceInfo trace)
            throws Throwable {
        Context context = new Context(new File(fileName), defaultCharset);
        if (included.containsKey(context.getModuleName()))
            return Memory.TRUE;
        return __require(fileName, locals, trace, true);
    }

    public Memory __newObject(String originName, String lowerName, TraceInfo trace, Memory[] args)
            throws Throwable {
        ClassEntity entity = fetchClass(originName, lowerName, true);
        if (entity == null) {
            error(trace, E_ERROR, Messages.ERR_CLASS_NOT_FOUND.fetch(originName));
        }

        assert entity != null;
        IObject object = entity.newObject(this, trace, true, args);

        if (entity.methodDestruct != null) {
            cleanGcObjects();

            WeakReference<IObject> wr = new WeakReference<IObject>(object, gcObjectRefQueue);
            gcObjects.add(wr);
        }

        return new ObjectMemory( object );
    }

    private static ForeachIterator invalidIterator = new ForeachIterator(false, false, false) {
        @Override
        protected boolean init() { return false; }

        @Override
        protected boolean nextValue() { return false; }

        @Override
        protected boolean prevValue() { return false; }
    };

    public ForeachIterator __getIterator(TraceInfo trace, Memory memory, boolean getReferences, boolean getKeyReferences){
        ForeachIterator iterator = memory.getNewIterator(this, getReferences, getKeyReferences);
        if (iterator == null){
            warning(trace, "Invalid argument supplied for foreach()");
            return invalidIterator;
        }
        return iterator;
    }

    public ClassEntity __getClosure(String moduleIndex, int index){
        return scope.moduleIndexMap.get(moduleIndex).findClosure(index);
    }

    public Memory __getSingletonClosure(String moduleIndex, int index){
        Memory result = scope.moduleIndexMap.get(moduleIndex).findClosure(index).getSingleton();
        assert result != null;
        return result;
    }

    public Memory __throwException(InvocationTargetException e) {
        Throwable throwable = e.getTargetException();
        if (throwable instanceof JPHPException)
            throw (RuntimeException)throwable;
        else {
            JavaReflection.exception(this, throwable);
        }

        return Memory.NULL;
    }

    public void __throwException(BaseException e){
        __clearSilent();
        e.setTraceInfo(this, trace());
        throw e;
    }

    public void __throwException(TraceInfo trace, Memory exception){
        if (exception.isObject() ) {
            IObject object;
            if ((object = exception.toValue(ObjectMemory.class).value) instanceof BaseException){
                __clearSilent();
                BaseException e = (BaseException)object;
                e.setTraceInfo(this, trace);
                throw e;
            } else {
                triggerError(new FatalException(
                        "Exceptions must be valid objects derived from the Exception base class", trace
                ));
            }
        } else {
            triggerError(new FatalException(
                    "Can only throw objects", trace
            ));
        }
    }

    public Memory __throwCatch(BaseException e, String className, String lowerClassName){
        ClassEntity origin = e.getReflection();
        ClassEntity cause = fetchClass(className, lowerClassName, false);

        if (cause != null && origin.isInstanceOf(cause))
            return new ObjectMemory(e);
        if (origin.isInstanceOfLower(lowerClassName))
            return new ObjectMemory(e);
        else
            return Memory.NULL;
    }

    public void __pushSilent(){
        silentFlags.push(errorFlags);
        setErrorFlags(0);
    }

    public void __popSilent(){
        Integer flags = silentFlags.pop();
        setErrorFlags(flags);
    }

    public void __clearSilent(){
        Stack<Integer> silents = silentFlags;
        Integer flags = 0;
        while (!silents.empty())
            flags = silents.pop();

        setErrorFlags(flags);
    }

    public Memory __getMacroClass() {
        CallStackItem item = peekCall(0);
        if (item != null && item.clazz != null){
            if (item.classEntity == null)
                item.classEntity = fetchClass(item.clazz, false);

            if (item.classEntity == null)
                return Memory.CONST_EMPTY_STRING;
            else {
                MethodEntity method = item.classEntity.findMethod(item.function);
                if (method == null)
                    return Memory.CONST_EMPTY_STRING;
                return new StringMemory(method.getClazz().getName());
            }
        } else
            return Memory.CONST_EMPTY_STRING;
    }

    public void __defineFunction(TraceInfo trace, String moduleInternalName, int index){
        ModuleEntity module = scope.moduleIndexMap.get(moduleInternalName);
        if (module == null)
            throw new CriticalException("Cannot find module: " + moduleInternalName);

        FunctionEntity function = module.findFunction(index);

        if (functionMap.put(function.getLowerName(), function) != null){
            triggerError(new FatalException(
                    Messages.ERR_CANNOT_REDECLARE_FUNCTION.fetch(function.getName()),
                    trace
            ));
        }
    }

    public String __shellExecute(String s) {
        if (shellExecHandler != null)
            return shellExecHandler.onExecute(s);
        return "";
    }

    public void die(Memory value) {
        if (value != null){
            if (!value.isNumber())
                echo(value.toString());

            throw new DieException(value);
        } else
            throw new DieException(Memory.NULL);
    }

    public Memory invokeMethod(TraceInfo trace, IObject object, String name, Memory... args) throws Throwable {
        return ObjectInvokeHelper.invokeMethod(new ObjectMemory(object), name, name.toLowerCase(), this, trace, args);
    }

    public Memory invokeMethod(IObject object, String name, Memory... args) throws Throwable {
        return ObjectInvokeHelper.invokeMethod(new ObjectMemory(object), name, name.toLowerCase(), this, trace(), args);
    }

    public Memory invokeMethod(TraceInfo trace, Memory object, String name, Memory... args) throws Throwable {
        return ObjectInvokeHelper.invokeMethod(object, name, name.toLowerCase(), this, trace, args);
    }

    public Memory invokeMethod(Memory object, String name, Memory... args) throws Throwable {
        return ObjectInvokeHelper.invokeMethod(object, name, name.toLowerCase(), this, trace(), args);
    }

    public String getLateStatic(){
        CallStackItem item = peekCall(0);
        if (item == null || item.clazz == null)
            return "";
        else
            return item.staticClazz != null ? item.staticClazz : item.clazz;
    }

    public IObject getLateObject(){
        CallStackItem item = peekCall(0);
        if (item == null)
            return null;
        else
            return item.object;
    }

    public ClassEntity getLateStaticClass() {
        CallStackItem item = peekCall(0);
        if (item == null || item.clazz == null)
            return null;
        else {
            if (item.staticClassEntity != null)
                return item.classEntity;

            return item.staticClassEntity = fetchClass(item.staticClazz != null ? item.staticClazz : item.clazz, false);
        }
    }

    public String getContext(){
        CallStackItem item = peekCall(1);
        return item == null ? "" : item.clazz == null ? "" : item.clazz;
    }

    public ClassEntity __getContextClass(int offset) {
        CallStackItem item = peekCall(offset);
        if (item == null || item.clazz == null)
            return null;
        else {
            if (item.classEntity != null)
                return item.classEntity;

            return item.classEntity = fetchClass(item.clazz, false);
        }
    }

    public ClassEntity getContextClass() {
        return __getContextClass(1);
    }

    public ClassEntity getLastClassOnStack() {
        int N = getCallStackTop();
        for (int i = 0; i < N; i++){
            CallStackItem item = peekCall(i);
            if (item != null && item.clazz != null){
                if (item.classEntity != null)
                    return item.classEntity;
                return item.classEntity = fetchClass(item.clazz, false);
            }
        }
        return null;
    }

    public ClassEntity __getParentClass(TraceInfo trace){
        ClassEntity context = getLastClassOnStack();
        if (context == null){
            error(trace, "Cannot access parent:: when no class scope is active");
            return null;
        }
        ClassEntity parent = context.getParent();
        if (parent == null) {
            error(trace, "Cannot access parent:: when current class scope has no parent");
            return null;
        }

        return parent;
    }

    public String __getParent(TraceInfo trace){
        return __getParentClass(trace).getName();
    }

    public String __getParent(TraceInfo trace, String className){
        ClassEntity o = fetchClass(className, true);
        if (o == null) {
            error(trace, ErrorType.E_ERROR, Messages.ERR_CLASS_NOT_FOUND, className);
            return null;
        }

        if (o.getParent() == null) {
            error(trace, "Cannot access parent:: when current class scope has no parent");
            return null;
        }

        return o.getParent().getName();
    }

    public void registerAutoloader(SplClassLoader classLoader, boolean prepend){
        for (SplClassLoader loader : classLoaders)
            if (loader.equals(classLoader))
                return;

        if (prepend) {
            classLoaders.add(0, classLoader);
        } else
            classLoaders.add(classLoader);
    }

    public List<SplClassLoader> getClassLoaders(){
        return classLoaders;
    }

    public boolean unRegisterAutoloader(SplClassLoader classLoader){
        boolean result = false;
        Iterator<SplClassLoader> iterator = classLoaders.iterator();
        while (iterator.hasNext()){
            if (iterator.next().equals(classLoader)){
                result = true;
                iterator.remove();
            }
        }
        return result;
    }

    public void setErrorHandler(ErrorHandler handler){
        previousErrorHandler = errorHandler;
        errorHandler = handler;
    }

    public void registerShutdownFunction(ShutdownHandler handler){
        shutdownFunctions.add(handler);
    }

    static {
        configurationHandler = new HashMap<String, ConfigChangeHandler>();
        configurationHandler.put("include_path", new ConfigChangeHandler() {
            @Override
            public void onChange(Environment env, Memory value) {
                if (value == null)
                    env.setIncludePaths(Collections.<String>emptySet());
                else {
                    String[] files = StringUtils.split(value.toString(), Constants.PATH_SEPARATOR, 255);
                    Set<String> paths = new HashSet<String>();
                    Collections.addAll(paths, files);

                    env.setIncludePaths(paths);
                }
            }
        });
    }
}
