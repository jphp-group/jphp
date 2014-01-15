package php.runtime.util;


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
            result.add(new Item(e));
        }
    }

    @Override
    public Iterator<Item> iterator() {
        return result.iterator();
    }

    public class Item {
        public final String fileName;
        public final int lineNumber;
        public final ModuleEntity module;
        public final ClassEntity clazz;
        public final MethodEntity method;
        public final FunctionEntity function;

        protected final StackTraceElement element;

        protected Item(StackTraceElement el){
            element = el;
            fileName = el.getFileName();
            lineNumber = el.getLineNumber();
            String className = el.getClassName();
            FunctionEntity f_e = JVMStackTracer.this.classLoader.getFunction(className);
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
                ClassEntity c_e = JVMStackTracer.this.classLoader.getClass(className);
                if (c_e != null){
                    clazz = c_e;
                    function = null;
                    if (c_e != null)
                        method = c_e.findMethod(realMethodName);
                    else
                        method = null;

                    module = c_e == null ? null : c_e.getModule();
                } else {
                    ModuleEntity m_e = JVMStackTracer.this.classLoader.getModule(className);
                    module = m_e;
                    clazz = null;
                    method = null;
                    function = null;
                }
            }
        }

        public boolean isInternal(){
            return function == null && clazz == null && module == null;
        }

        @Override
        public String toString() {
            String result;
            if (function != null)
                result = function.getName();
            else if (clazz != null && method == null)
                result = clazz.getName() + ".<init>";
            else if (clazz != null)
                result = clazz.getName() + "." + method.getName();
            else if (module != null)
                result = "include ";
            else
                result = element.getClassName() + "." + element.getMethodName();

            result = result +
                    (fileName != null && lineNumber >= 0 ?
                                    "(" + fileName + ":" + lineNumber + ")" :
                                    (fileName != null ?  "("+fileName+")" : "(Unknown Source)")
                    );

            return result;
        }
    }
}
