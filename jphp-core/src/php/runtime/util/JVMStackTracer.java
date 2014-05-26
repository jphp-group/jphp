package php.runtime.util;


import php.runtime.lang.Closure;
import php.runtime.loader.RuntimeClassLoader;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.FunctionEntity;
import php.runtime.reflection.MethodEntity;
import php.runtime.reflection.ModuleEntity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JVMStackTracer implements Iterable<JVMStackTracer.Item> {
    protected final RuntimeClassLoader classLoader;
    protected final List<Item> result;

    public JVMStackTracer(RuntimeClassLoader classLoader, StackTraceElement[] elements){
        this.classLoader = classLoader;
        this.result = new ArrayList<Item>();

        for(StackTraceElement e : elements) {
            result.add(new Item(classLoader, e));
        }
    }

    @Override
    public Iterator<Item> iterator() {
        return result.iterator();
    }

    public static class Item {
        public final String fileName;
        public final int lineNumber;
        public final ModuleEntity module;
        public final ClassEntity clazz;
        public final MethodEntity method;
        public final FunctionEntity function;

        protected final StackTraceElement element;

        public Item(RuntimeClassLoader classLoader, StackTraceElement el){
            element = el;
            fileName = el.getFileName();
            lineNumber = el.getLineNumber();
            String className = el.getClassName();
            FunctionEntity f_e = classLoader.getFunction(className);
            String realMethodName = null;
            if (el.getMethodName() != null){
                if (el.getMethodName().indexOf('$') > -1)
                    realMethodName = el.getMethodName().substring(0, el.getMethodName().indexOf('$'));
                else
                    realMethodName = el.getMethodName();

                realMethodName = realMethodName.toLowerCase();
            }

            if (f_e != null){
                function = f_e;
                module = f_e.getModule();
                clazz = null;
                method = null;
            } else {
                ClassEntity c_e = classLoader.getClass(className);
                if (c_e != null){
                    clazz = c_e;
                    function = null;
                    method = c_e.findMethod(realMethodName);
                    module = c_e.getModule();
                } else {
                    module = classLoader.getModule(className);
                    clazz = null;
                    method = null;
                    function = null;
                }
            }
        }

        public boolean isSystem() {
            return element.getClassName().startsWith("sun.");
        }

        public boolean isInternal(){
            return function == null && clazz == null && module == null;
        }

        public String getSignature() {
            String result;
            if (function != null)
                result = function.getName();
            else if (clazz != null && method == null)
                result = clazz.getName() + ".<init>";
            else if (clazz != null && clazz.isInstanceOf(Closure.class))
                result = "<Closure>";
            else if (clazz != null)
                result = clazz.getName() + "." + method.getName();
            else if (module != null)
                result = "include ";
            else
                result = element.getClassName() + "." + element.getMethodName();

            return result;
        }

        @Override
        public String toString() {
            String result = getSignature();
            result = result +
                    (fileName != null && lineNumber >= 0 ?
                                    "(" + fileName + ":" + lineNumber + ")" :
                                    (fileName != null ?  "("+fileName+")" : "(Unknown Source)")
                    );

            return result;
        }
    }
}
