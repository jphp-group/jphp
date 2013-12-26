package ru.regenix.jphp.runtime.env;

import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.compiler.CompileScope;
import ru.regenix.jphp.compiler.common.compile.CompileConstant;
import ru.regenix.jphp.compiler.jvm.JvmCompiler;
import ru.regenix.jphp.exceptions.CompileException;
import ru.regenix.jphp.exceptions.FatalException;
import ru.regenix.jphp.exceptions.support.ErrorException;
import ru.regenix.jphp.runtime.env.message.NoticeMessage;
import ru.regenix.jphp.runtime.env.message.SystemMessage;
import ru.regenix.jphp.runtime.env.message.WarningMessage;
import ru.regenix.jphp.runtime.invoke.Invoker;
import ru.regenix.jphp.runtime.lang.BaseException;
import ru.regenix.jphp.runtime.lang.ForeachIterator;
import ru.regenix.jphp.runtime.lang.IObject;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.ObjectMemory;
import ru.regenix.jphp.runtime.memory.ReferenceMemory;
import ru.regenix.jphp.runtime.memory.StringMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.output.OutputBuffer;
import ru.regenix.jphp.runtime.reflection.ClassEntity;
import ru.regenix.jphp.runtime.reflection.ConstantEntity;
import ru.regenix.jphp.runtime.reflection.FunctionEntity;
import ru.regenix.jphp.runtime.reflection.ModuleEntity;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;
import ru.regenix.jphp.tokenizer.Tokenizer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.*;

import static ru.regenix.jphp.exceptions.support.ErrorException.Type.*;

public class Environment {
    private Locale locale = Locale.getDefault();
    private Set<String> includePaths;
    protected SplClassLoader autoLoader = null;
    protected List<SplClassLoader> classLoaders = new LinkedList<SplClassLoader>();

    private ThreadLocal<Integer> errorFlags = new ThreadLocal<Integer>(){
        @Override
        protected Integer initialValue() {
            return E_ALL.value ^ (E_NOTICE.value | E_STRICT.value | E_DEPRECATED.value);
        }
    };

    private ThreadLocal<Stack<Integer>> silentFlags = new ThreadLocal<Stack<Integer>>(){
        @Override
        public Stack<Integer> initialValue(){
            return new Stack<Integer>();
        }
    };

    private SystemMessage lastMessage;

    private ErrorHandler previousErrorHandler;
    private ErrorHandler errorHandler;

    private ExceptionHandler previousExceptionHandler;
    private ExceptionHandler exceptionHandler;

    private OutputBuffer defaultBuffer;
    private Stack<OutputBuffer> outputBuffers;

    public final CompileScope scope;
    public final Map<String, Memory> configuration = new HashMap<String, Memory>();

    private Charset defaultCharset = Charset.forName("UTF-8");

    private final ArrayMemory globals;
   // private final ArrayMemory statics;
    private final Map<String, ReferenceMemory> statics;
    private final Map<String, ConstantEntity> constants;
    private final Map<String, ModuleEntity> included;

    //
    public final Map<String, ClassEntity> classMap = new LinkedHashMap<String, ClassEntity>();
    public final Map<String, FunctionEntity> functionMap = new LinkedHashMap<String, FunctionEntity>();
    public final Map<String, ConstantEntity> constantMap = new LinkedHashMap<String, ConstantEntity>();

    // call stack
    private final static int CALL_STACK_INIT_SIZE = 255;

    private int callStackTop = 0;
    private int maxCallStackTop = -1;
    private CallStackItem[] callStack    = new CallStackItem[CALL_STACK_INIT_SIZE];

    public Environment(CompileScope scope, OutputStream output) {
        this.scope = scope;
        this.outputBuffers = new Stack<OutputBuffer>();

        this.defaultBuffer = new OutputBuffer(this, null);
        this.outputBuffers.push(defaultBuffer);
        this.defaultBuffer.setOutput(output);

        this.includePaths = new HashSet<String>();
        this.setErrorFlags(E_ALL.value ^ (E_NOTICE.value | E_STRICT.value | E_DEPRECATED.value));

        this.globals = new ArrayMemory();
        this.statics = new HashMap<String, ReferenceMemory>();
        this.included = new LinkedHashMap<String, ModuleEntity>();
        this.setErrorHandler(new ErrorHandler() {
            @Override
            public boolean onError(SystemMessage error) {
                Environment.this.echo(error.getDebugMessage());
                Environment.this.echo("\n");
                return false;
            }
        });

        this.globals.put("GLOBALS", this.globals);
        this.constants = new HashMap<String, ConstantEntity>();

        Memory splAutoloader = new StringMemory("spl_autoload");
        Invoker invoker = Invoker.valueOf(this, null, splAutoloader);
        if (invoker != null)
            this.autoLoader = new SplClassLoader(invoker, splAutoloader);

        classMap.putAll(scope.getClassMap());
        functionMap.putAll(scope.getFunctionMap());
        constantMap.putAll(scope.getConstantMap());
    }

    public void pushCall(TraceInfo trace, IObject self, Memory[] args, String function, String clazz){
        if (callStackTop >= callStack.length){
            CallStackItem[] newCallStack = new CallStackItem[callStack.length * 2];
            System.arraycopy(callStack, 0, newCallStack, 0, callStack.length);
            callStack = newCallStack;
        }

        if (callStackTop < maxCallStackTop)
            callStack[callStackTop++].setParameters(trace, self, args, function, clazz);
        else
            callStack[callStackTop++] = new CallStackItem(trace, self, args, function, clazz);

        maxCallStackTop = callStackTop;
    }

    public void pushCall(IObject self, String method, Memory... args){
        pushCall(null, self, args, method, self.getReflection().getName());
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

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
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

    public ClassEntity autoloadCall(String name){
        StringMemory tmp = new StringMemory(name);
        for(SplClassLoader loader : classLoaders)
            loader.load(tmp);

        if (autoLoader != null)
            autoLoader.load(tmp);

        return fetchClass(name, false, false);
    }

    public ClassEntity fetchClass(String name, boolean magic) {
        return fetchClass(name, magic, false);
    }

    public ClassEntity fetchClass(String name, boolean magic, boolean autoLoad) {
        return fetchClass(name, name.toLowerCase(), magic, autoLoad);
    }

    public ClassEntity fetchClass(String name, String nameL, boolean magic, boolean autoLoad) {
        ClassEntity entity = classMap.get(nameL);
        if (magic && entity == null){
            if ("self".equals(nameL)){
                ClassEntity e = getContextClass();
                if (e == null)
                    e = getLateStaticClass();
                return e;
            }
            if ("static".equals(nameL))
                return getLastClassOnStack();
        }

        if (entity == null){
            return autoLoad ? autoloadCall(name) : null;
        } else {
            if (isLoadedClass(nameL) || entity.isInternal())
                return entity;
            else {
                if (autoLoad){
                    return autoloadCall(name);
                }
                return null;
            }
        }
    }

    public Memory findConstant(String name){
        String nameL = name.toLowerCase();

        ConstantEntity entity = constants.get(nameL);
        if (entity != null) {
            if (!entity.caseSensitise || name.equals(entity.getName()))
                return entity.getValue();
        }

        CompileConstant constant = scope.findCompileConstant(name);
        if (constant != null)
            return constant.value;

        return null;
    }

    public boolean defineConstant(String name, Memory value, boolean caseSensitise){
        Memory constant = findConstant(name);
        if (constant != null)
            return false;

        constants.put(name.toLowerCase(), new ConstantEntity(name, value, caseSensitise));
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
        return configuration.put(name, value);
    }

    public void restoreConfigValue(String name){
        configuration.remove(name);
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

    public Integer getErrorFlags() {
        return errorFlags.get();
    }

    public void setErrorFlags(int errorFlags) {
        this.errorFlags.set(errorFlags);
    }

    public SystemMessage getLastMessage() {
        return lastMessage;
    }

    public ExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    public void setExceptionHandler(ExceptionHandler exceptionHandler) {
        this.previousExceptionHandler = this.exceptionHandler;
        this.exceptionHandler = exceptionHandler;
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.previousErrorHandler = this.errorHandler;
        this.errorHandler = errorHandler;
    }

    public ErrorHandler getPreviousErrorHandler() {
        return previousErrorHandler;
    }

    public ExceptionHandler getPreviousExceptionHandler() {
        return previousExceptionHandler;
    }

    public void triggerError(ErrorException err){
        ErrorException.Type type = err.getType();
        throw err;
    }

    public void triggerMessage(SystemMessage message){
        ErrorException.Type type = message.getType();
        if (isHandleErrors(type)){
            lastMessage = message;
            if (errorHandler != null)
                if (errorHandler.onError(message))
                    return;
        }
    }

    public boolean isHandleErrors(ErrorException.Type type){
        return ErrorException.Type.check(errorFlags.get(), type);
    }

    public void warning(String message, Object... args){
        if (isHandleErrors(E_WARNING))
            triggerMessage(new WarningMessage(peekCall(0), new Messages.Item(message), args));
    }

    public void warning(TraceInfo trace, String message, Object... args){
        if (isHandleErrors(E_WARNING))
            triggerMessage(new WarningMessage(new CallStackItem(trace), new Messages.Item(message), args));
    }

    public void warning(TraceInfo trace, Messages.Item message, Object... args){
        if (isHandleErrors(E_WARNING))
            triggerMessage(new WarningMessage(new CallStackItem(trace), message, args));
    }

    public void notice(String message, Object... args){
        if (isHandleErrors(E_WARNING))
            triggerMessage(new NoticeMessage(peekCall(0), new Messages.Item(message), args));
    }

    public Context createContext(String code){
        return new Context(this, code);
    }

    public Context createContext(File file){
        return new Context(this, file);
    }

    public Context createContext(File file, Charset charset){
        return new Context(this, file, charset);
    }

    public OutputBuffer getDefaultBuffer() {
        return defaultBuffer;
    }

    public OutputBuffer pushOutputBuffer(Memory callback, int chunkSize, boolean erase){
        OutputBuffer buffer = new OutputBuffer(this, peekOutputBuffer(), callback, chunkSize, erase);
        outputBuffers.push(buffer);
        return buffer;
    }

    public OutputBuffer popOutputBuffer(){
        return outputBuffers.pop();
    }

    public OutputBuffer peekOutputBuffer(){
        return outputBuffers.empty() ? null : outputBuffers.peek();
    }

    public void echo(String value){
        OutputBuffer buffer = peekOutputBuffer();
        buffer.write(value);
    }

    public void echo(InputStream input) throws IOException {
        OutputBuffer buffer = peekOutputBuffer();
        buffer.write(input);
    }

    public void flushAll() throws IOException {
        while (peekOutputBuffer() != null){
            popOutputBuffer().flush();
        }
    }

    public ModuleEntity importModule(File file) throws IOException {
        Context context = new Context(this, file);
        ModuleEntity module = scope.findUserModule(context.getModuleName());
        if (module == null){
            Tokenizer tokenizer = new Tokenizer(context);
            SyntaxAnalyzer analyzer = new SyntaxAnalyzer(tokenizer);
            JvmCompiler compiler = new JvmCompiler(this, context, analyzer);

            module = compiler.compile(true);
            scope.loadModule(module);
        }

        registerModule(module);
        return module;
    }

    public void registerModule(ModuleEntity module){
        for(ClassEntity entity : module.getClasses()) {
            if (entity.isStatic())
                classMap.put(entity.getLowerName(), entity);
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
    public Memory getConstant(String name, TraceInfo trace){
        Memory constant = findConstant(name);

        if (constant == null){
            if (isHandleErrors(E_NOTICE)) {
                triggerMessage(new NoticeMessage(new CallStackItem(trace), Messages.ERR_NOTICE_USE_UNDEFINED_CONSTANT, name, name));
            }
            return StringMemory.valueOf(name);
        }

        return constant;
    }

    public Memory include(String fileName) throws IllegalAccessException, IOException, InvocationTargetException {
        return include(fileName, globals, null);
    }

    public Memory include(String fileName, ArrayMemory locals, TraceInfo trace, boolean once)
            throws InvocationTargetException, IllegalAccessException, IOException {
        File file = new File(fileName);
        if (!file.exists()){
            warning(trace, Messages.ERR_WARNING_INCLUDE_FAILED, "include", fileName);
            return Memory.FALSE;
        } else {
            ModuleEntity module = importModule(file);
            included.put(module.getName(), module);

            Memory result;
            pushCall(trace, null, new Memory[]{new StringMemory(fileName)}, once ? "include_once" : "include", null);
            try {
                result = module.include(this, locals);
            } finally {
                popCall();
            }
            return result;
        }
    }

    public Memory includeOnce(String fileName, ArrayMemory locals, TraceInfo trace)
            throws InvocationTargetException, IllegalAccessException, IOException {
        Context context = new Context(this, new File(fileName));
        if (included.containsKey(context.getModuleName()))
            return Memory.TRUE;
        return include(fileName, locals, trace, true);
    }

    public Memory include(String fileName, ArrayMemory locals, TraceInfo trace)
            throws InvocationTargetException, IllegalAccessException, IOException {
        return include(fileName, locals, trace, false);
    }

    public void require(String fileName, ArrayMemory locals, TraceInfo trace)
            throws InvocationTargetException, IllegalAccessException, IOException {
        File file = new File(fileName);
        if (!file.exists()){
            triggerError(new CompileException(
                    Messages.ERR_FATAL_CALL_TO_UNDEFINED_FUNCTION.fetch("require", fileName),
                    trace
            ));
        } else {
            ModuleEntity module = importModule(file);
            included.put(fileName, module);
            module.include(this, locals);
        }
    }

    public Memory newObject(String originName, String lowerName, TraceInfo trace, Memory[] args)
            throws InstantiationException, IllegalAccessException, InvocationTargetException {
        ClassEntity entity = classMap.get(lowerName);
        if (entity == null){
            triggerError(new CompileException(
                    Messages.ERR_FATAL_CLASS_NOT_FOUND.fetch(originName),
                    trace
            ));
        }
        assert entity != null;
        return new ObjectMemory( entity.newObject(this, trace, args) );
    }

    private static ForeachIterator invalidIterator = new ForeachIterator(false, false, false) {
        @Override
        protected boolean init() { return false; }

        @Override
        protected boolean nextValue() { return false; }

        @Override
        protected boolean prevValue() { return false; }
    };

    public ForeachIterator getIterator(TraceInfo trace, Memory memory, boolean getReferences, boolean getKeyReferences){
        ForeachIterator iterator = memory.getNewIterator(this, getReferences, getKeyReferences);
        if (iterator == null){
            warning(trace, "Invalid argument supplied for foreach()");
            return invalidIterator;
        }
        return iterator;
    }

    public ClassEntity __getClosure(int moduleIndex, int index){
        return scope.moduleIndexMap.get(moduleIndex).findClosure(index);
    }

    public Memory getSingletonClosure(int moduleIndex, int index){
        return scope.moduleIndexMap.get(moduleIndex).findClosure(index).getSingleton();
    }

    public void __throwException(TraceInfo trace, Memory exception){
        if (exception.isObject() ) {
            IObject object;
            if ((object = exception.toValue(ObjectMemory.class).value) instanceof BaseException){
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
        ClassEntity cause = fetchClass(className, lowerClassName, false, true);

        if (cause.isInstanceOf(origin))
            return new ObjectMemory(e);
        else
            return Memory.NULL;
    }

    public void __pushSilent(){
        silentFlags.get().push(errorFlags.get());
        setErrorFlags(0);
    }

    public void __popSilent(){
        Integer flags = silentFlags.get().pop();
        setErrorFlags(flags);
    }

    public void __clearSilent(){
        Stack<Integer> silents = silentFlags.get();
        Integer flags = 0;
        while (!silents.empty())
            flags = silents.pop();

        setErrorFlags(flags);
    }

    public void __defineFunction(TraceInfo trace, int moduleIndex, int index){
        ModuleEntity module = scope.moduleIndexMap.get(moduleIndex);
        FunctionEntity function = module.findFunction(index);

        if (functionMap.put(function.getLowerName(), function) != null){
            triggerError(new FatalException(
                    Messages.ERR_FATAL_CANNOT_REDECLARE_FUNCTION.fetch(function.getName()),
                    trace
            ));
        }
    }

    public void die(Memory value) {
        if (value != null){
            if (!value.isNumber())
                echo(value.toString());

            throw new DieException(value);
        } else
            throw new DieException(Memory.NULL);
    }

    public String getLateStatic(){
        CallStackItem item = peekCall(0);
        if (item == null || item.clazz == null)
            return "";
        else
            return item.clazz;
    }

    public ClassEntity getLateStaticClass() {
        CallStackItem item = peekCall(0);
        if (item == null || item.clazz == null)
            return null;
        else {
            if (item.classEntity != null)
                return item.classEntity;

            return item.classEntity = fetchClass(item.clazz, false);
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

            return item.classEntity = fetchClass(item.clazz, false, false);
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

    public void autoloadRegister(SplClassLoader classLoader, boolean prepend){
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

    public boolean autoloadUnregister(SplClassLoader classLoader){
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
}
