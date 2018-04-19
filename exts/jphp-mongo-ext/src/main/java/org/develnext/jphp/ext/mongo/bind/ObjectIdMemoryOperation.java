package org.develnext.jphp.ext.mongo.bind;

import com.mongodb.client.model.IndexOptions;
import org.bson.types.ObjectId;
import org.develnext.jphp.ext.mongo.classes.WrapObjectId;
import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;
import php.runtime.reflection.support.ReflectionUtils;
import php.runtime.reflection.support.TypeChecker;

public class ObjectIdMemoryOperation extends MemoryOperation<ObjectId> {

    @Override
    public Class<?>[] getOperationClasses() {
        return new Class[] { IndexOptions.class };
    }

    @Override
    public ObjectId convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        if (arg.isNull()) return null;

        if (arg.instanceOf(WrapObjectId.class)) {
            return arg.toObject(WrapObjectId.class).getWrappedObject();
        }

        return new ObjectId(arg.toString());
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, ObjectId arg) throws Throwable {
        if (arg == null) return Memory.NULL;

        return new ObjectMemory(new WrapObjectId(env, arg));
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setTypeChecker(new TypeChecker() {
            @Override
            public String getSignature() {
                return ReflectionUtils.getClassName(WrapObjectId.class);
            }

            @Override
            public String getHumanString() {
                return "valid objectId hex-string or an ObjectId instance";
            }

            @Override
            public boolean check(Environment env, Memory value, boolean nullable, String staticClassName) {
                return value.instanceOf(WrapObjectId.class) || ObjectId.isValid(value.toString());
            }

            @Override
            public Memory apply(Environment env, Memory value, boolean nullable, boolean strict) {
                return null;
            }
        });
    }
}
