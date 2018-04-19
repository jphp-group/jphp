package org.develnext.jphp.ext.mongo.bind;

import com.mongodb.client.model.CountOptions;
import com.mongodb.client.model.IndexOptions;
import java.util.concurrent.TimeUnit;
import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.ext.core.classes.time.WrapTimer;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;

public class CountOptionsMemoryOperation extends MemoryOperation<CountOptions> {

    @Override
    public Class<?>[] getOperationClasses() {
        return new Class[] { CountOptions.class };
    }

    @Override
    public CountOptions convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        if (arg.isNull()) return null;

        ArrayMemory arr = arg.toValue(ArrayMemory.class);
        CountOptions options = new CountOptions();

        if (arr.containsKey("skip")) options.skip(arg.valueOfIndex("skip").toInteger());
        if (arr.containsKey("limit")) options.limit(arg.valueOfIndex("limit").toInteger());
        if (arr.containsKey("maxTime")) {
            options.maxTime(WrapTimer.parsePeriod(arg.valueOfIndex("maxTime").toString()), TimeUnit.MILLISECONDS);
        }

        return options;
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, CountOptions arg) throws Throwable {
        if (arg == null) return Memory.NULL;

        ArrayMemory options = ArrayMemory.createHashed(12);

        options.put("skip", arg.getSkip());
        options.put("limit", arg.getLimit());
        options.put("maxTime", arg.getMaxTime(TimeUnit.MILLISECONDS));

        return options;
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setType(HintType.ARRAY);
    }
}
