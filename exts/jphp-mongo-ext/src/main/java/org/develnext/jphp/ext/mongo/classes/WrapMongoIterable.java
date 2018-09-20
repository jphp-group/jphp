package org.develnext.jphp.ext.mongo.classes;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoIterable;
import java.util.concurrent.TimeUnit;
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

    public WrapMongoIterable(Environment env, FindIterable wrappedObject) {
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
    public WrapMongoIterable skip(int n) {
        if (getWrappedObject() instanceof FindIterable) {
            return new WrapMongoIterable(this.__env__, ((FindIterable) getWrappedObject()).skip(n));
        } else {
            return this;
        }
    }

    @Signature
    public WrapMongoIterable projection(BasicDBObject projection) {
        if (getWrappedObject() instanceof FindIterable) {
            return new WrapMongoIterable(this.__env__, ((FindIterable) getWrappedObject()).projection(projection));
        } else {
            return this;
        }
    }

    @Signature
    public WrapMongoIterable hint(BasicDBObject hint) {
        if (getWrappedObject() instanceof FindIterable) {
            return new WrapMongoIterable(this.__env__, ((FindIterable) getWrappedObject()).hint(hint));
        } else {
            return this;
        }
    }

    @Signature
    public WrapMongoIterable comment(String comment) {
        if (getWrappedObject() instanceof FindIterable) {
            return new WrapMongoIterable(this.__env__, ((FindIterable) getWrappedObject()).comment(comment));
        } else {
            return this;
        }
    }

    @Signature
    public WrapMongoIterable max(BasicDBObject max) {
        if (getWrappedObject() instanceof FindIterable) {
            return new WrapMongoIterable(this.__env__, ((FindIterable) getWrappedObject()).max(max));
        } else {
            return this;
        }
    }

    @Signature
    public WrapMongoIterable min(BasicDBObject min) {
        if (getWrappedObject() instanceof FindIterable) {
            return new WrapMongoIterable(this.__env__, ((FindIterable) getWrappedObject()).min(min));
        } else {
            return this;
        }
    }

    @Signature
    public WrapMongoIterable filter(BasicDBObject filter) {
        if (getWrappedObject() instanceof FindIterable) {
            return new WrapMongoIterable(this.__env__, ((FindIterable) getWrappedObject()).filter(filter));
        } else {
            return this;
        }
    }

    @Signature
    public WrapMongoIterable maxAwaitTime(long millis) {
        if (getWrappedObject() instanceof FindIterable) {
            return new WrapMongoIterable(this.__env__, ((FindIterable) getWrappedObject()).maxAwaitTime(millis, TimeUnit.MILLISECONDS));
        } else {
            return this;
        }
    }

    @Signature
    public WrapMongoIterable maxTime(long millis) {
        if (getWrappedObject() instanceof FindIterable) {
            return new WrapMongoIterable(this.__env__, ((FindIterable) getWrappedObject()).maxTime(millis, TimeUnit.MILLISECONDS));
        } else {
            return this;
        }
    }

    @Signature
    public WrapMongoIterable maxScan(long maxScan) {
        if (getWrappedObject() instanceof FindIterable) {
            return new WrapMongoIterable(this.__env__, ((FindIterable) getWrappedObject()).maxScan(maxScan));
        } else {
            return this;
        }
    }

    @Signature
    public WrapMongoIterable returnKey(boolean returnKey) {
        if (getWrappedObject() instanceof FindIterable) {
            return new WrapMongoIterable(this.__env__, ((FindIterable) getWrappedObject()).returnKey(returnKey));
        } else {
            return this;
        }
    }

    @Signature
    public WrapMongoIterable partial(boolean partial) {
        if (getWrappedObject() instanceof FindIterable) {
            return new WrapMongoIterable(this.__env__, ((FindIterable) getWrappedObject()).partial(partial));
        } else {
            return this;
        }
    }

    @Signature
    public WrapMongoIterable snapshot(boolean snapshot) {
        if (getWrappedObject() instanceof FindIterable) {
            return new WrapMongoIterable(this.__env__, ((FindIterable) getWrappedObject()).snapshot(snapshot));
        } else {
            return this;
        }
    }

    @Signature
    public WrapMongoIterable sort(BasicDBObject sort) {
        if (getWrappedObject() instanceof FindIterable) {
            return new WrapMongoIterable(this.__env__, ((FindIterable) getWrappedObject()).sort(sort));
        } else {
            return this;
        }
    }

    @Signature
    public WrapMongoIterable limit(int c) {
        if (getWrappedObject() instanceof FindIterable) {
            return new WrapMongoIterable(this.__env__, ((FindIterable) getWrappedObject()).limit(c));
        } else {
            return this;
        }
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
