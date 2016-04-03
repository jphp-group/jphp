package php.runtime.ext.core.classes.util;

import php.runtime.Memory;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.common.StringUtils;
import php.runtime.env.Environment;
import php.runtime.ext.CoreExtension;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.lang.BaseObject;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.StringMemory;
import php.runtime.memory.TrueMemory;
import php.runtime.reflection.ClassEntity;

import java.io.*;
import java.util.Map;
import java.util.Properties;

@Name("Configuration")
@Namespace(CoreExtension.NAMESPACE + "util")
public class WrapConfiguration extends BaseObject {
    protected Properties properties;

    public WrapConfiguration(Environment env, Properties properties) {
        super(env);
        this.properties = properties;
    }

    public WrapConfiguration(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() throws IOException {
        __construct(null);
    }

    @Signature
    public void __construct(@Nullable InputStream in) throws IOException {
        __construct(in, "UTF-8");
    }

    @Signature
    public void __construct(@Nullable InputStream in, String encoding) throws IOException {
        properties = new Properties();

        if (in != null) {
            load(in, encoding);
        }
    }

    @Signature
    public Memory get(String key, Memory def) {
        if (!properties.containsKey(key)) {
            return def;
        }

        return StringMemory.valueOf(properties.getProperty(key, def.toString()));
    }

    @Signature
    public boolean has(String key) {
        return properties.containsKey(key);
    }

    @Signature
    public String remove(String key) {
        Object remove = properties.remove(key);

        return remove == null ? null : remove.toString();
    }

    @Signature
    public Memory get(String key) {
        return get(key, Memory.NULL);
    }

    @Signature
    public Memory getArray(String key, ArrayMemory def) {
        if (has(key)) {
            Memory memory = get(key);
            String[] split = StringUtils.split(memory.toString(), '|');

            ArrayMemory result = new ArrayMemory();

            for (String s : split) {
                result.add(s.trim());
            }

            return result.toConstant();
        } else {
            return def;
        }
    }

    @Signature
    public Memory getArray(String key) {
        return getArray(key, new ArrayMemory().toConstant());
    }

    @Signature
    public boolean getBoolean(String key, boolean def) {
        return get(key, TrueMemory.valueOf(def)).toBoolean();
    }

    @Signature
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    @Signature
    public Memory getNumber(String key, Memory def) {
        def = def.toNumeric();
        return get(key, def).toNumeric();
    }

    @Signature
    public Memory getNumber(String key) {
        return getNumber(key, Memory.CONST_INT_0);
    }

    @Signature
    public long getInteger(String key, long def) {
        return get(key, LongMemory.valueOf(def)).toLong();
    }

    @Signature
    public long getInteger(String key) {
        return getInteger(key, 0l);
    }

    @Signature
    public String set(String key, Memory value) {
        String s = value.toString();

        if (value.isArray()) {
            s = StringUtils.join(value.toValue(ArrayMemory.class).toStringArray(), "|");
        }

        Object property = properties.setProperty(key, s);

        return property == null ? null : property.toString();
    }

    @Signature
    public void put(Environment env, ForeachIterator iterator) throws Throwable {
        while (iterator.next()) {
            env.invokeMethod(this, "set", iterator.getMemoryKey(), iterator.getValue());
        }
    }

    @Signature
    public void clear() {
        properties.clear();
    }

    @Signature
    public void load(InputStream in, String encoding) throws IOException {
        properties.load(new InputStreamReader(in, encoding));
    }

    @Signature
    public void load(InputStream in) throws IOException {
        load(in, "UTF-8");
    }

    @Signature
    public void save(Environment env, Memory path, String encoding) throws IOException {
        OutputStream out = Stream.getOutputStream(env, path);

        if (out == null) {
            throw new IOException();
        }

        try {
            properties.store(new OutputStreamWriter(out, encoding), null);
        } finally {
            Stream.closeStream(env, out);
        }
    }

    @Signature
    public void save(Environment env, Memory path) throws IOException {
        save(env, path, "UTF-8");
    }

    @Signature
    public Memory toArray() {
        ArrayMemory result = new ArrayMemory(true);

        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            result.putAsKeyString(entry.getKey().toString(), StringMemory.valueOf(entry.getValue().toString()));
        }

        return result.toConstant();
    }

    @Signature
    protected void __clone() {
    }
}
