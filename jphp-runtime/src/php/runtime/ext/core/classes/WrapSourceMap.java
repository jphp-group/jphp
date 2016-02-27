package php.runtime.ext.core.classes;

import php.runtime.Memory;
import php.runtime.annotation.Reflection.Arg;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.lang.ForeachIterator;
import php.runtime.loader.sourcemap.SourceMap;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import java.util.Map;

@Name("php\\lang\\SourceMap")
public class WrapSourceMap extends BaseWrapper<SourceMap> {
    public WrapSourceMap(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public WrapSourceMap(Environment env, SourceMap wrappedObject) {
        super(env, wrappedObject);
    }

    @Signature(@Arg("moduleName"))
    public Memory __construct(Environment env, Memory... args) {
        __wrappedObject = new SourceMap(args[0].toString());
        return Memory.NULL;
    }

    @Signature
    public Memory getModuleName(Environment env, Memory... args) {
        return StringMemory.valueOf(getWrappedObject().getModuleName());
    }

    @Signature({
            @Arg("compiledLine"),
    })
    public Memory getSourceLine(Environment env, Memory... args) {
        int sourceLine = getWrappedObject().getSourceLine(args[0].toInteger());
        return LongMemory.valueOf(sourceLine);
    }

    @Signature({
            @Arg("sourceLine"),
    })
    public Memory getCompiledLine(Environment env, Memory... args) {
        int sourceLine = getWrappedObject().getCompiledLine(args[0].toInteger());
        return LongMemory.valueOf(sourceLine);
    }

    @Signature({
            @Arg(value = "inserts", type = HintType.ARRAY),
            @Arg("allCountLines")
    })
    public Memory insertLines(Environment env, Memory... args) {
        ArrayMemory _inserts = args[0].toValue(ArrayMemory.class);

        int[][] inserts = new int[_inserts.size()][];

        ForeachIterator iterator = _inserts.getNewIterator(env);

        int i = 0;
        while (iterator.next()) {
            Memory value = iterator.getValue();
            inserts[i++] = new int[] {value.valueOfIndex(0).toInteger(), value.valueOfIndex(1).toInteger()};
        }

        getWrappedObject().insertLines(inserts, args[1].toInteger());
        return Memory.NULL;
    }

    @Signature({
            @Arg("sourceLine"),
            @Arg("compiledLine")
    })
    public Memory addLine(Environment env, Memory... args) {
        getWrappedObject().addLine(args[0].toInteger(), args[1].toInteger());
        return Memory.NULL;
    }

    @Signature
    public Memory clear(Environment env, Memory... args) {
        getWrappedObject().clear();
        return Memory.NULL;
    }

    @Signature
    public Memory toArray(Environment env, Memory... args) {
        Map<Integer, SourceMap.Item> itemsByLine = getWrappedObject().getItemsByCompiled();
        ArrayMemory r = new ArrayMemory();

        for (Map.Entry<Integer, SourceMap.Item> entry : itemsByLine.entrySet()) {
            r.refOfIndex(entry.getKey()).assign(entry.getValue().sourceLine);
        }

        return r.toConstant();
    }
}
