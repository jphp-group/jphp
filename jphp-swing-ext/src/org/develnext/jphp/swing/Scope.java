package org.develnext.jphp.swing;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.lang.IObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Scope {
    protected final Map<String, Memory> values;
    protected final Map<String, List<Bind>> binds;

    protected static final Map<String, Bind> binds_ = new ConcurrentHashMap<String, Bind>();

    public Scope() {
        this.binds  = new ConcurrentHashMap<String, List<Bind>>();
        this.values = new ConcurrentHashMap<String, Memory>();
    }

    public Memory get(Environment env, String name) {
        Memory result = values.get(name);
        return result == null ? Memory.NULL : result;
    }

    synchronized public void bind(Environment env, String name, IObject object, String property) {
        Bind oldBind = binds_.get(object.getPointer() + "#" + property.toLowerCase());
        if (oldBind != null && oldBind.scope.get() != null) {
            Scope scope = oldBind.scope.get();

            if (scope != null) {
                List<Bind> oldBinds = scope.binds.get(oldBind.name);

                if (oldBinds != null) {
                    int del = -1;

                    for (int i = 0; i < oldBinds.size(); i++) {
                        Bind el = oldBinds.get(i);

                        if (el.object == object && el.property.equalsIgnoreCase(property)) {
                            del = i;
                            break;
                        }
                    }

                    if (del > -1) {
                        oldBinds.remove(del);
                    }
                }
            }
        }

        List<Bind> list = binds.get(name);

        if (list == null) {
            binds.put(name, list = new ArrayList<Bind>());
        }

        Bind bind = new Bind(this, name, object, property);

        list.add(bind);
        binds_.put(object.getPointer() + "#" + property.toLowerCase(), bind);
    }

    public void set(Environment env, String name, Memory value) throws Throwable {
        values.put(name, value);

        List<Bind> list = binds.get(name);

        if (list != null) {
            for (Bind bind : list) {
                bind.object.getReflection().setProperty(env, env.trace(), bind.object, bind.property, value, null);
            }
        }
    }

    public static class Bind {
        protected final WeakReference<Scope> scope;
        protected final String name;
        protected final IObject object;
        protected final String property;

        public Bind(Scope scope, String name, IObject object, String property) {
            this.name = name;
            this.scope = new WeakReference<Scope>(scope);
            this.object = object;
            this.property = property;
        }
    }
}
