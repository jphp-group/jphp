package org.develnext.jphp.ext.mongo.bind;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.util.Map.Entry;
import org.bson.conversions.Bson;
import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;

public class BasicDBObjectMemoryOperation extends MemoryOperation<BasicDBObject> {

    @Override
    public Class<?>[] getOperationClasses() {
        return new Class[] { BasicDBObject.class, Bson.class, DBObject.class };
    }

    @Override
    public BasicDBObject convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        if (arg.isNull()) return null;

        ForeachIterator iterator = arg.getNewIterator(env);

        BasicDBObject dbObject = new BasicDBObject();

        while (iterator.next()) {
            if (iterator.getValue().isTraversable()) {
                dbObject.append(iterator.getStringKey(), convert(env, trace, iterator.getValue()));
            } else {
                dbObject.append(iterator.getStringKey(), Memory.unwrap(env, iterator.getValue()));
            }
        }

        return dbObject;
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, BasicDBObject arg) throws Throwable {
        if (arg == null) return Memory.NULL;

        ArrayMemory result = ArrayMemory.createHashed(arg.size());

        for (Entry<String, Object> entry : arg.entrySet()) {
            if (entry.getValue() instanceof BasicDBObject) {
                result.put(entry.getKey(), unconvert(env, trace, (BasicDBObject) entry.getValue()));
            } else {
                result.put(entry.getKey(), Memory.wrap(env, entry.getValue()));
            }
        }

        return null;
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setType(HintType.ITERABLE);
    }
}
