package org.develnext.jphp.ext.mongo.classes;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoIterable;
import org.bson.Document;
import org.develnext.jphp.ext.mongo.MongoExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.spl.Traversable;
import php.runtime.reflection.ClassEntity;

@Name("MongoIterable")
@Namespace(MongoExtension.NS)
public class WrapMongoIterable extends BaseWrapper<MongoIterable<Document>> implements Traversable {

    public WrapMongoIterable(Environment env, MongoIterable wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapMongoIterable(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private void __construct() {
    }

    @Signature
    public Document first() {
        return getWrappedObject().first();
    }

    @Signature
    public WrapMongoIterable batchSize(Environment env, int size) {
        MongoIterable<Document> documents = getWrappedObject().batchSize(size);
        return new WrapMongoIterable(env, documents);
    }

    @Override
    public ForeachIterator getNewIterator(Environment env, boolean getReferences, boolean getKeyReferences) {
        MongoCursor<Document> iterator = getWrappedObject().iterator();

        return new ForeachIterator(getReferences, getKeyReferences, false) {
            private Memory counter = Memory.CONST_INT_0;

            @Override
            protected boolean init() {
                return true;
            }

            @Override
            protected boolean nextValue() {
                if (iterator.hasNext()) {
                    Document document = iterator.tryNext();
                    if (document == null) {
                        return false;
                    }

                    this.currentValue = Memory.wrap(env, document);
                    this.currentKeyMemory = counter;
                    this.currentKey = counter;

                    counter = counter.inc();

                    return true;
                } else {
                    return false;
                }
            }

            @Override
            protected boolean prevValue() {
                return false;
            }

            @Override
            public void reset() {
            }
        };
    }

    @Override
    public ForeachIterator getNewIterator(Environment env) {
        return getNewIterator(env, false, false);
    }
}
