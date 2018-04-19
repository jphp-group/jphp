package org.develnext.jphp.ext.mongo.bind;

import com.mongodb.client.model.IndexOptionDefaults;
import com.mongodb.client.model.IndexOptions;
import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;

public class IndexOptionsMemoryOperation extends MemoryOperation<IndexOptions> {

    @Override
    public Class<?>[] getOperationClasses() {
        return new Class[] { IndexOptions.class };
    }

    @Override
    public IndexOptions convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        if (arg.isNull()) return null;

        ArrayMemory arr = arg.toValue(ArrayMemory.class);
        IndexOptions options = new IndexOptions();

        if (arr.containsKey("background")) { options.background(arg.valueOfIndex("background").toBoolean()); }
        if (arr.containsKey("defaultLanguage")) { options.defaultLanguage(arg.valueOfIndex("defaultLanguage").toString()); }
        if (arr.containsKey("bits")) { options.bits(arg.valueOfIndex("bits").toInteger()); }
        if (arr.containsKey("name")) { options.name(arg.valueOfIndex("name").toString()); }
        if (arr.containsKey("max")) { options.max(arg.valueOfIndex("max").toDouble()); }
        if (arr.containsKey("min")) { options.min(arg.valueOfIndex("min").toDouble()); }
        if (arr.containsKey("languageOverride")) { options.languageOverride(arg.valueOfIndex("languageOverride").toString()); }

        if (arr.containsKey("sparse")) { options.sparse(arg.valueOfIndex("sparse").toBoolean()); }
        if (arr.containsKey("unique")) { options.unique(arg.valueOfIndex("unique").toBoolean()); }

        if (arr.containsKey("version")) { options.version(arg.valueOfIndex("version").toInteger()); }
        if (arr.containsKey("textVersion")) { options.textVersion(arg.valueOfIndex("textVersion").toInteger()); }
        if (arr.containsKey("sphereVersion")) { options.sphereVersion(arg.valueOfIndex("sphereVersion").toInteger()); }

        return options;
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, IndexOptions arg) throws Throwable {
        if (arg == null) return Memory.NULL;

        ArrayMemory options = ArrayMemory.createHashed(12);
        options.put("name", arg.getName());
        options.put("background", arg.isBackground());
        options.put("sparse", arg.isSparse());
        options.put("unique", arg.isUnique());

        if (arg.getDefaultLanguage() != null) options.put("defaultLanguage", arg.getDefaultLanguage());
        if (arg.getBits() != null) options.put("bits", arg.getBits());
        if (arg.getMax() != null) options.put("max", arg.getMax());
        if (arg.getMin() != null) options.put("min", arg.getMin());
        if (arg.getLanguageOverride() != null) options.put("languageOverride", arg.getLanguageOverride());
        if (arg.getVersion() != null) options.put("version", arg.getVersion());
        if (arg.getTextVersion() != null) options.put("textVersion", arg.getTextVersion());
        if (arg.getSphereVersion() != null) options.put("sphereVersion", arg.getSphereVersion());

        return options;
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setType(HintType.ARRAY);
    }
}
