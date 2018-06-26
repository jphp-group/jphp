package org.develnext.jphp.ext.mongo.classes;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import org.develnext.jphp.ext.mongo.MongoExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import java.util.List;

@Name("MongoClient")
@Namespace(MongoExtension.NS)
public class WrapMongoClient extends BaseWrapper<MongoClient> {

    public WrapMongoClient(Environment env, MongoClient wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapMongoClient(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __construct("127.0.0.1");
    }

    @Signature
    public void __construct(String host) {
        __construct(host, 27017);
    }

    @Signature
    public void __construct(String host, int port) {
        __wrappedObject = new MongoClient(host, port);
    }

    @Signature
    static public WrapMongoClient createFromURI(Environment env, String uri) {
        return new WrapMongoClient(env, new MongoClient(new MongoClientURI(uri)));
    }

    @Signature
    public MongoDatabase database(String database) {
        return getWrappedObject().getDatabase(database);
    }

    @Signature
    public WrapMongoIterable databases(Environment env) {
        return new WrapMongoIterable(env, getWrappedObject().listDatabases());
    }

    @Signature
    public void close() {
        getWrappedObject().close();
    }
}
