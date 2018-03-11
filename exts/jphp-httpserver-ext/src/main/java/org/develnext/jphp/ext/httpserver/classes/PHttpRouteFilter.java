package org.develnext.jphp.ext.httpserver.classes;

import org.develnext.jphp.ext.httpserver.HttpServerExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseObject;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.exception.BaseTypeError;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Name("HttpRouteFilter")
@Namespace(HttpServerExtension.NS)
public class PHttpRouteFilter extends PHttpAbstractHandler {
    private Set<String> methods = new LinkedHashSet<>();
    private String path;
    private Pattern pathPattern;
    private List<String> pathParts = new ArrayList<>();
    private Invoker invoker;
    private Memory handler;

    private static Pattern pathConverterPattern = Pattern.compile("(\\{[\\w\\d\\-\\_\\*]+\\}|\\*\\*)", Pattern.CASE_INSENSITIVE);

    public PHttpRouteFilter(Environment env) {
        super(env);
    }

    public PHttpRouteFilter(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void reset(Environment env, Memory methods, String path, Memory handler) {
        this.invoker = Invoker.create(env, handler);
        this.handler = handler;

        if (this.invoker == null) {
            env.exception(BaseTypeError.class, "The handler argument must be callable");
        }

        this.path = path;

        if (methods.isTraversable()) {
            ForeachIterator iterator = methods.getNewIterator(env);
            while (iterator.next()) {
                this.methods.add(iterator.getValue().toString().toUpperCase());
            }
        } else {
            if (!methods.toString().equals("*")) {
                this.methods.add(methods.toString().toUpperCase());
            }
        }

        Matcher matcher = pathConverterPattern.matcher(path);

        StringBuffer sb = new StringBuffer();
        sb.append("^");

        while (matcher.find()) {
            String partName = matcher.group(1);

            String part = "([^\\/]+)";

            if ("**".equals(partName)) {
                part = "(.*)";
            }

            if (partName.startsWith("{")) {
                partName = partName.substring(1);
            }

            if (partName.endsWith("}")) {
                partName = partName.substring(0, partName.length() - 1);
            }

            pathParts.add(partName);
            matcher.appendReplacement(sb, part);
        }

        matcher.appendTail(sb);
        sb.append("$");

        this.pathPattern = Pattern.compile(sb.toString());
    }

    @Signature
    public void __construct(Environment env, Memory methods, String path, Memory handler) {
        this.reset(env, methods, path, handler);
    }

    @Signature
    public Memory __debugInfo() {
        ArrayMemory arr = ArrayMemory.createHashed();
        arr.refOfIndex("*path").assign(path);
        arr.refOfIndex("*methods").assign(ArrayMemory.ofStringCollection(methods));
        arr.refOfIndex("*handler").assign(handler.toImmutable());

        return arr;
    }

    @Signature
    public Memory handler() {
        return handler.toImmutable();
    }

    @Signature
    public Set<String> methods() {
        return methods;
    }

    @Signature
    public String path() {
        return path;
    }

    @Signature
    public boolean __invoke(PHttpServerRequest request, PHttpServerResponse response) throws Throwable {
        if (!methods.isEmpty()) {
            String method = request.method().toUpperCase();

            if (!methods.contains(method)) {
                return false;
            }
        }

        String path = request.path();

        Matcher matcher = pathPattern.matcher(path);

        int i = 0;
        while (matcher.find()) {
            if (matcher.groupCount() == 0) {
                i = 1;
                break;
            }

            String name = pathParts.get(i);
            String value = matcher.group(1);

            request.getRequest().setAttribute(name, value);

            i++;
        }

        if (i == 0) {
            return false;
        }

        Memory call = invoker.call(ObjectMemory.valueOf(request), ObjectMemory.valueOf(response));

        if (call.toBoolean()) {
            response.write(StringMemory.valueOf(call.toString()));
        }

        return true;
    }
}
