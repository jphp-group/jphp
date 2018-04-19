package org.develnext.jphp.ext.mongo.bind;

import com.mongodb.BasicDBObject;
import java.util.Map.Entry;
import org.bson.Document;
import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;

public class DocumentMemoryOperation extends MemoryOperation<Document> {

    @Override
    public Class<?>[] getOperationClasses() {
        return new Class[] { Document.class };
    }

    @Override
    public Document convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        if (arg.isNull()) return null;

        ForeachIterator iterator = arg.getNewIterator(env);

        Document dbObject = new Document();

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
    public Memory unconvert(Environment env, TraceInfo trace, Document arg) throws Throwable {
        if (arg == null) return Memory.NULL;

        ArrayMemory result = ArrayMemory.createHashed(arg.size());

        for (Entry<String, Object> entry : arg.entrySet()) {
            if (entry.getValue() instanceof Document) {
                result.put(entry.getKey(), unconvert(env, trace, (Document) entry.getValue()));
            } else {
                result.put(entry.getKey(), Memory.wrap(env, entry.getValue()));
            }
        }

        return result;
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setType(HintType.ITERABLE);
    }
}
