package php.runtime.env;

import php.runtime.Memory;
import php.runtime.common.AbstractCompiler;
import php.runtime.common.CompilerFactory;
import php.runtime.common.LangMode;
import php.runtime.common.Messages;
import php.runtime.env.handler.EntityFetchHandler;
import php.runtime.exceptions.ConflictException;
import php.runtime.exceptions.CriticalException;
import php.runtime.ext.CoreExtension;
import php.runtime.ext.JavaExtension;
import php.runtime.ext.NetExtension;
import php.runtime.ext.java.JavaException;
import php.runtime.ext.support.Extension;
import php.runtime.ext.support.compile.CompileClass;
import php.runtime.ext.support.compile.CompileConstant;
import php.runtime.ext.support.compile.CompileFunction;
import php.runtime.lang.*;
import php.runtime.lang.spl.ArrayAccess;
import php.runtime.lang.spl.ErrorException;
import php.runtime.lang.spl.Serializable;
import php.runtime.lang.spl.Traversable;
import php.runtime.lang.spl.iterator.IteratorAggregate;
import php.runtime.loader.RuntimeClassLoader;
import php.runtime.reflection.*;
import php.runtime.reflection.support.ReflectionUtils;
import php.runtime.util.JVMStackTracer;
import php.runtime.wrap.ClassWrapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CompileScope {
    public final int id;

    protected RuntimeClassLoader classLoader;
    public final Set<String> superGlobals;

    protected final static AtomicInteger scopeCount = new AtomicInteger(0);
    protected final AtomicInteger moduleCount = new AtomicInteger(0);
    protected final AtomicLong classCount = new AtomicLong(0);
    protected final AtomicLong methodCount = new AtomicLong(0);

    public final ConcurrentHashMap<String, ModuleEntity> moduleMap;
    public final ConcurrentHashMap<String, ModuleEntity> moduleIndexMap;

    protected final Map<String, ClassEntity> classMap;
    protected final Map<String, FunctionEntity> functionMap;
    protected final Map<String, ConstantEntity> constantMap;
    protected final Map<Class<? extends Throwable>, Class<? extends JavaException>> exceptionMap;
    protected final Map<String, Class<? extends JavaException>> exceptionMapForContext;

    protected Map<String, Extension> extensions;

    protected Map<String, CompileConstant> compileConstantMap;
    protected Map<String, CompileFunction> compileFunctionMap;
    protected Map<String, CompileClass> compileClassMap;

    protected CompilerFactory compilerFactory;

    protected List<EntityFetchHandler> classEntityFetchHandler;
    protected List<EntityFetchHandler> functionEntityFetchHandler;
    protected List<EntityFetchHandler> constantEntityFetchHandler;

    public Map<String, Memory> configuration;

    // flags
    public boolean debugMode = false;
    public LangMode langMode = LangMode.JPHP;

    public CompileScope(CompileScope parent) {
        id = scopeCount.getAndIncrement();
        classLoader = parent.classLoader;

        moduleMap = new ConcurrentHashMap<String, ModuleEntity>();
        moduleIndexMap = new ConcurrentHashMap<String, ModuleEntity>();

        classMap = new HashMap<String, ClassEntity>();
        functionMap = new HashMap<String, FunctionEntity>();
        constantMap = new HashMap<String, ConstantEntity>();
        exceptionMap = new HashMap<Class<? extends Throwable>, Class<? extends JavaException>>();
        exceptionMapForContext = new HashMap<String, Class<? extends JavaException>>();

        extensions = new LinkedHashMap<String, Extension>();

        compileConstantMap = new HashMap<String, CompileConstant>();
        compileFunctionMap = new HashMap<String, CompileFunction>();
        compileClassMap    = new HashMap<String, CompileClass>();

        superGlobals = new HashSet<String>();
        superGlobals.addAll(parent.superGlobals);

        classMap.putAll(parent.classMap);
        compileClassMap.putAll(parent.compileClassMap);

        functionMap.putAll(parent.functionMap);
        compileFunctionMap.putAll(parent.compileFunctionMap);

        constantMap.putAll(parent.constantMap);
        compileConstantMap.putAll(parent.compileConstantMap);

        exceptionMap.putAll(parent.exceptionMap);

        moduleMap.putAll(parent.moduleMap);
        moduleIndexMap.putAll(parent.moduleIndexMap);

        classCount.set(parent.classCount.longValue());
        moduleCount.set(parent.moduleCount.intValue());
        methodCount.set(parent.methodCount.longValue());

        compilerFactory = parent.compilerFactory;

        classEntityFetchHandler = new ArrayList<EntityFetchHandler>(parent.classEntityFetchHandler);
        functionEntityFetchHandler = new ArrayList<EntityFetchHandler>(parent.functionEntityFetchHandler);
        constantEntityFetchHandler = new ArrayList<EntityFetchHandler>(parent.constantEntityFetchHandler);

        extensions.putAll(parent.extensions);
    }

    public CompileScope() {
        this(new RuntimeClassLoader(Thread.currentThread().getContextClassLoader()));
    }

    public CompileScope(RuntimeClassLoader classLoader) {
        id = scopeCount.getAndIncrement();
        this.classLoader = classLoader;

        moduleMap = new ConcurrentHashMap<String, ModuleEntity>();
        moduleIndexMap = new ConcurrentHashMap<String, ModuleEntity>();

        classMap = new HashMap<String, ClassEntity>();
        functionMap = new HashMap<String, FunctionEntity>();
        constantMap = new HashMap<String, ConstantEntity>();

        extensions = new LinkedHashMap<String, Extension>();
        compileConstantMap = new HashMap<String, CompileConstant>();
        compileFunctionMap = new HashMap<String, CompileFunction>();
        compileClassMap    = new HashMap<String, CompileClass>();
        exceptionMap = new HashMap<Class<? extends Throwable>, Class<? extends JavaException>>();
        exceptionMapForContext = new HashMap<String, Class<? extends JavaException>>();

        classEntityFetchHandler = new ArrayList<EntityFetchHandler>();
        constantEntityFetchHandler = new ArrayList<EntityFetchHandler>();
        functionEntityFetchHandler = new ArrayList<EntityFetchHandler>();

        superGlobals = new HashSet<String>();

        superGlobals.add("GLOBALS");
        superGlobals.add("_ENV");
        superGlobals.add("_SERVER");
        superGlobals.add("_POST");
        superGlobals.add("_GET");
        superGlobals.add("_REQUEST");
        superGlobals.add("_FILES");
        superGlobals.add("_SESSION");
        superGlobals.add("_COOKIE");

        try {
            final Class<?> jvmCompilerClass = Class.forName("org.develnext.jphp.core.compiler.jvm.JvmCompiler");
            final Constructor<?> jvmConstructor = jvmCompilerClass.getConstructor(Environment.class, Context.class);

            compilerFactory = new CompilerFactory() {
                @Override
                public AbstractCompiler getCompiler(Environment env, Context context) throws Throwable {
                    return (AbstractCompiler) jvmConstructor.newInstance(env, context);
                }
            };
        } catch (ClassNotFoundException e) {
            // nop.
        } catch (NoSuchMethodException e) {
            // nop.
        }

        CoreExtension extension = new CoreExtension();

        //registerClass(closureEntity = new ClassEntity(extension, this, Closure.class));
        //registerClass(stdClassEntity = new ClassEntity(extension, this, StdClass.class));

        registerLazyClass(extension, Closure.class);
        registerLazyClass(extension, Generator.class);
        registerLazyClass(extension, StdClass.class);
        registerLazyClass(extension, BaseException.class);
        registerLazyClass(extension, ErrorException.class);
        registerLazyClass(extension, ArrayAccess.class);

        //registerClass(new ClassEntity(extension, this, ErrorException.class));
        //registerClass(new ClassEntity(extension, this, ArrayAccess.class));

        // iterators
        registerLazyClass(extension, Traversable.class);
        registerLazyClass(extension, php.runtime.lang.spl.iterator.Iterator.class);
        registerLazyClass(extension, IteratorAggregate.class);
        registerLazyClass(extension, Serializable.class);

        /*registerClass(new ClassEntity(extension, this, Traversable.class));
        registerClass(new ClassEntity(extension, this, php.runtime.lang.spl.iterator.Iterator.class));
        registerClass(new ClassEntity(extension, this, IteratorAggregate.class));
        registerClass(new ClassEntity(extension, this, Serializable.class)); */

        registerExtension(new JavaExtension());
        registerExtension(new NetExtension());
        registerExtension(extension);
    }

    public AbstractCompiler createCompiler(Environment env, Context context) throws Throwable {
        if (compilerFactory == null) {
            throw new NullPointerException("compilerFactory is not set");
        }

        try {
            return compilerFactory.getCompiler(env, context);
        } catch (InvocationTargetException e) {
            env.__throwException(e);
            return null;
        }
    }

    public RuntimeClassLoader getClassLoader() {
        return classLoader;
    }

    public void setNativeClassLoader(ClassLoader classLoader) {
        this.classLoader = new RuntimeClassLoader(classLoader);
    }

    public LangMode getLangMode() {
        return langMode;
    }

    public void setLangMode(LangMode langMode) {
        this.langMode = langMode;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public Map<String, ClassEntity> getClassMap() {
        return classMap;
    }

    public Map<String, FunctionEntity> getFunctionMap() {
        return functionMap;
    }

    public Map<String, ConstantEntity> getConstantMap() {
        return constantMap;
    }

    public int nextModuleIndex(){
        return moduleCount.incrementAndGet();
    }

    public long nextClassIndex(){
        return classCount.incrementAndGet();
    }

    public long nextMethodIndex(){
        return methodCount.incrementAndGet();
    }

    public void registerLazyClass(Extension extension, Class<?> clazz) {
        CompileClass el = new CompileClass(extension, clazz);
        compileClassMap.put(el.getLowerName(), el);
    }

    @SuppressWarnings("unchecked")
    public void registerExtension(String extClass) {
        try {
            registerExtension((Class<? extends Extension>) Class.forName(extClass));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void registerExtension(Class<? extends Extension> extClass) {
        try {
            registerExtension(extClass.newInstance());
        } catch (Exception e) {
            throw new CriticalException(e);
        }
    }

    public void registerExtension(Extension extension) {
        if (extensions.containsKey(extension.getName()))
            return;

        // required
        for(String dep : extension.getRequiredExtensions()){
            try {
                Extension el = (Extension) Class.forName(dep).newInstance();
                registerExtension(el);
            } catch (Exception e) {
                throw new CriticalException(e);
            }
        }

        // optional
        for (String dep : extension.getOptionalExtensions()) {
            try {
                Extension el = (Extension) Class.forName(dep).newInstance();
                registerExtension(el);
            } catch (ClassNotFoundException e) {
                // do nothing ...
            } catch (Exception e) {
                throw new CriticalException(e);
            }
        }

        // conflicts
        for(String dep : extension.getConflictExtensions()) {
            if (extensions.containsKey(dep))
                throw new ConflictException(
                        "'" + dep + "' extension conflicts with '" + extension.getClass().getName() + "'"
                );
        }

        extension.onRegister(this);
        compileConstantMap.putAll(extension.getConstants());
        compileFunctionMap.putAll(extension.getFunctions());

        for(Class<?> clazz : extension.getClasses().values()){
            registerLazyClass(extension, clazz);
        }

        for(CompileFunction function : extension.getFunctions().values()){
            functionMap.put(function.name.toLowerCase(), new CompileFunctionEntity(extension, function));
        }

        extensions.put(extension.getName().toLowerCase(), extension);
    }

    public Extension getExtension(String name){
        return extensions.get(name.toLowerCase());
    }

    public Set<String> getExtensions(){
        return extensions.keySet();
    }

    public void addClassEntityFetchHandler(EntityFetchHandler classEntityFetchHandler) {
        this.classEntityFetchHandler.add(classEntityFetchHandler);
    }

    public void addFunctionEntityFetchHandler(EntityFetchHandler functionEntityFetchHandler) {
        this.functionEntityFetchHandler.add(functionEntityFetchHandler);
    }

    public void addConstantEntityFetchHandler(EntityFetchHandler constantEntityFetchHandler) {
        this.constantEntityFetchHandler.add(constantEntityFetchHandler);
    }

    public void registerJavaException(Class<? extends JavaException> clazz, Class<? extends Throwable> throwClazz) {
        exceptionMap.put(throwClazz, clazz);
    }

    public void registerJavaExceptionForContext(Class<? extends JavaException> clazz, Class<? extends IObject> context) {
        exceptionMapForContext.put(ReflectionUtils.getClassName(context).toLowerCase(), clazz);
    }

    public void registerClass(ClassEntity clazz) {
        classMap.put(clazz.getLowerName(), clazz);
    }

    public void registerModule(ModuleEntity module) {
        addUserModule(module);

        for(ClassEntity entity : module.getClasses()) {
            if (entity.isStatic()){
                if (classMap.put(entity.getLowerName(), entity) != null) {
                    throw new CriticalException(Messages.ERR_CANNOT_REDECLARE_CLASS.fetch(entity.getName()));
                }
            }
        }

        for(FunctionEntity entity : module.getFunctions()) {
            if (entity.isStatic()) {
                if (functionMap.put(entity.getLowerName(), entity) != null) {
                    throw new CriticalException(Messages.ERR_CANNOT_REDECLARE_FUNCTION.fetch(entity.getName()));
                }
            }
        }

        for(ConstantEntity entity : module.getConstants()) {
            if (constantMap.put(entity.getLowerName(), entity) != null) {
                throw new CriticalException(Messages.ERR_CANNOT_REDECLARE_CONSTANT.fetch(entity.getName()));
            }
        }
    }

    public void addUserModule(ModuleEntity module){
        moduleMap.put(module.getName(), module);
        moduleIndexMap.put(module.getInternalName(), module);
    }

    public void registerFunction(FunctionEntity function){
        functionMap.put(function.getLowerName(), function);
    }

    public void registerConstant(ConstantEntity constant){
        constantMap.put(constant.getLowerName(), constant);
    }

    public ModuleEntity findUserModule(String name){
        return moduleMap.get(name);
    }

    public ClassEntity fetchUserClass(Class<? extends IObject> clazz) {
        return fetchUserClass(ReflectionUtils.getClassName(clazz));
    }

    public ClassEntity fetchUserClass(String name) {
        name = name.toLowerCase();
        ClassEntity entity;

        if (classEntityFetchHandler != null) {
            for (EntityFetchHandler handler : classEntityFetchHandler) {
                handler.fetch(this, name);
            }
        }

        entity = classMap.get(name);
        if (entity != null) {
            return entity;
        }

        CompileClass compileClass = compileClassMap.get(name);
        if (compileClass == null)
            return null;

        entity = new ClassEntity(new ClassWrapper(
                compileClass.getExtension(), this, compileClass.getNativeClass()
        ));
        entity.setId(nextClassIndex());

        synchronized (classMap) {
            classMap.put(name, entity);
        }

        return entity;
    }

    public FunctionEntity findUserFunction(String name) {
        name = name.toLowerCase();

        FunctionEntity entity = functionMap.get(name);

        if (entity == null && functionEntityFetchHandler != null) {
            for (EntityFetchHandler handler : functionEntityFetchHandler) {
                handler.fetch(this, name);
            }

            entity = functionMap.get(name);
        }

        return entity;
    }

    public ConstantEntity findUserConstant(String name){
        name = name.toLowerCase();

        ConstantEntity entity = constantMap.get(name.toLowerCase());

        if (entity == null && constantEntityFetchHandler != null) {
            for (EntityFetchHandler handler : constantEntityFetchHandler) {
                handler.fetch(this, name);
            }

            entity = constantMap.get(name);
        }

        return entity;
    }

    public Collection<ConstantEntity> getConstants(){
        return constantMap.values();
    }

    public CompileConstant findCompileConstant(String name){
        return compileConstantMap.get(name);
    }

    public CompileFunction findCompileFunction(String name){
        return compileFunctionMap.get(name.toLowerCase());
    }

    public CompileClass findCompileClass(String name) {
        return compileClassMap.get(name.toLowerCase());
    }

    public Class<? extends JavaException> findJavaException(Class<? extends Throwable> clazz) {
        return exceptionMap.get(clazz);
    }

    public Class<? extends JavaException> findJavaExceptionForContext(String className) {
        return exceptionMapForContext.get(className);
    }

    /**
     * Loads a module by name (filename) for classLoader
     * @param name a filename
     * @return
     */
    public ModuleEntity loadModule(String name, boolean withBytecode) {
        ModuleEntity entity = findUserModule(name);
        if (entity == null)
            return null;

        classLoader.loadModule(entity, withBytecode);
        return entity;
    }

    public ModuleEntity loadModule(String name) {
        return loadModule(name, true);
    }

    /**
     * Loads a module for classLoader
     * @param module a module
     */
    public void loadModule(ModuleEntity module, boolean withBytecode) {
        classLoader.loadModule(module, withBytecode);
    }

    public void loadModule(ModuleEntity module) {
        loadModule(module, true);
    }

    public JVMStackTracer getStackTracer(StackTraceElement[] elements) {
        return new JVMStackTracer(classLoader, elements);
    }

    public JVMStackTracer getStackTracer(Throwable throwable){
        return getStackTracer(throwable.getStackTrace());
    }

    public JVMStackTracer getStackTracer(){
        return getStackTracer(Thread.currentThread().getStackTrace());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompileScope)) return false;

        CompileScope that = (CompileScope) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
