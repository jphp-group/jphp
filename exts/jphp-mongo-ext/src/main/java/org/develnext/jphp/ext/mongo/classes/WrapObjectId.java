package org.develnext.jphp.ext.mongo.classes;

import java.util.Date;
import org.bson.types.ObjectId;
import org.develnext.jphp.ext.mongo.MongoExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

@Name("ObjectId")
@Namespace(MongoExtension.NS)
public class WrapObjectId extends BaseWrapper<ObjectId> {
    public WrapObjectId(Environment env, ObjectId wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapObjectId(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(String hexString) {
        __wrappedObject = new ObjectId(hexString);
    }

    @Signature
    public ArrayMemory __debugInfo() {
        return ArrayMemory.ofPair("*", getWrappedObject().toHexString()).toConstant();
    }

    @Signature
    public Date date() {
        return getWrappedObject().getDate();
    }

    @Signature
    public int counter() {
        return getWrappedObject().getCounter();
    }

    @Signature
    public long timestamp() {
        return getWrappedObject().getTimestamp();
    }

    @Signature
    public int machineId() {
        return getWrappedObject().getMachineIdentifier();
    }

    @Signature
    public short processId() {
        return getWrappedObject().getProcessIdentifier();
    }

    @Signature
    public String __toString() {
        return getWrappedObject().toHexString();
    }

    @Signature
    public static boolean isValid(String hexString) {
        return ObjectId.isValid(hexString);
    }
}
