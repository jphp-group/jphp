package org.develnext.jphp.ext.mongo.classes;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.develnext.jphp.ext.mongo.MongoExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

@Name("MongoDatabase")
@Namespace(MongoExtension.NS)
public class WrapMongoDatabase extends BaseWrapper<MongoDatabase> {
    interface WrappedInterface {
        @Property String name();
    }

    public WrapMongoDatabase(Environment env, MongoDatabase wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapMongoDatabase(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private void __construct() {
    }

    @Signature
    public void drop() {
        getWrappedObject().drop();
    }

    @Signature
    public MongoCollection<Document> collection(String name) {
        return getWrappedObject().getCollection(name);
    }

    @Signature
    public WrapMongoIterable collections(Environment env) {
        return new WrapMongoIterable(env, getWrappedObject().listCollections());
    }

    @Signature
    public void createCollection(String name) {
        getWrappedObject().createCollection(name);
    }

    @Signature
    public Document runCommand(BasicDBObject command) {
        return getWrappedObject().runCommand(command);
    }
}
