package org.develnext.jphp.ext.mongo.classes;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.CountOptions;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.result.DeleteResult;
import java.util.List;
import org.bson.Document;
import org.develnext.jphp.ext.mongo.MongoExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

@Name("MongoCollection")
@Namespace(MongoExtension.NS)
public class WrapMongoCollection extends BaseWrapper<MongoCollection> {
    public WrapMongoCollection(Environment env, MongoCollection wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapMongoCollection(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private void __construct() {
    }

    @Signature
    public void createIndex(BasicDBObject keys) {
        getWrappedObject().createIndex(keys);
    }

    @Signature
    public void createIndex(BasicDBObject keys, IndexOptions options) {
        getWrappedObject().createIndex(keys, options);
    }

    @Signature
    public void dropIndex(BasicDBObject keys) {
        getWrappedObject().dropIndex(keys);
    }

    @Signature
    public void dropIndexByName(String name) {
        getWrappedObject().dropIndex(name);
    }

    @Signature
    public long count() {
        return getWrappedObject().count();
    }

    @Signature
    public long count(BasicDBObject filter) {
        return getWrappedObject().count(filter);
    }

    @Signature
    public long count(BasicDBObject filter, CountOptions options) {
        return getWrappedObject().count(filter, options);
    }

    @Signature
    public void drop() {
        getWrappedObject().drop();
    }

    @Signature
    public boolean deleteOne(BasicDBObject filter) {
        DeleteResult result = getWrappedObject().deleteOne(filter);
        return result.getDeletedCount() > 0;
    }

    @Signature
    public long deleteMany(BasicDBObject filter) {
        return getWrappedObject().deleteMany(filter).getDeletedCount();
    }

    @Signature
    public void dropIndexes() {
        getWrappedObject().dropIndexes();
    }

    @Signature
    public WrapMongoIterable find(Environment env) {
        return new WrapMongoIterable(env, getWrappedObject().find(Document.class));
    }

    @Signature
    public WrapMongoIterable find(Environment env, BasicDBObject filter) {
        return new WrapMongoIterable(env, getWrappedObject().find(filter, Document.class));
    }

    @Signature
    public Document findOneAndDelete(BasicDBObject filter) {
        return (Document) getWrappedObject().findOneAndDelete(filter);
    }

    @Signature
    public Document findOneAndUpdate(BasicDBObject filter, BasicDBObject update) {
        return (Document) getWrappedObject().findOneAndUpdate(filter, update);
    }

    @Signature
    public Document insertOne(Document doc) {
        getWrappedObject().insertOne(doc);
        return doc;
    }

    @Signature
    public void insertMany(List<Document> docs) {
        getWrappedObject().insertMany(docs);
    }

    @Signature
    public void updateOne(BasicDBObject filter, BasicDBObject update) {
        getWrappedObject().updateOne(filter, update);
    }

    @Signature
    public void updateMany(BasicDBObject filter, BasicDBObject update) {
        getWrappedObject().updateMany(filter, update);
    }
}
