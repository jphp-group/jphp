package org.develnext.jphp.ext.orientdb.classes;

import com.orientechnologies.orient.core.id.ORID;
import org.develnext.jphp.ext.orientdb.OrientDbExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Setter;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.BaseWrapper;
import php.runtime.lang.support.ICloneableObject;
import php.runtime.reflection.ClassEntity;

@Namespace(OrientDbExtension.NS)
public class ODocument extends BaseWrapper<com.orientechnologies.orient.core.record.impl.ODocument> implements ICloneableObject<ODocument> {
    public ODocument(Environment env, com.orientechnologies.orient.core.record.impl.ODocument wrappedObject) {
        super(env, wrappedObject);
    }

    public ODocument(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(String typeName) {
        __wrappedObject = new com.orientechnologies.orient.core.record.impl.ODocument(typeName);
    }

    @Getter
    public String getId() {
        ORID identity = getWrappedObject().getIdentity();

        if (identity.isNew()) {
            return null;
        }

        StringBuilder buffer = new StringBuilder();
        return identity.toString(buffer).toString();
    }

    @Signature
    public void resetId() {
        getWrappedObject().getIdentity().reset();
    }

    @Signature
    public boolean isNew() {
        return getWrappedObject().getIdentity().isNew();
    }

    @Signature
    public void __set(String name, Object value) {
        getWrappedObject().field(name, value);
    }

    @Signature
    public Object __get(String name) {
        return getWrappedObject().field(name);
    }

    @Signature
    public void __unset(String name) {
        getWrappedObject().removeField(name);
    }

    @Signature
    public boolean __isset(String name) {
        return getWrappedObject().containsField(name);
    }

    @Override
    public ODocument __clone(Environment env, TraceInfo trace) {
        com.orientechnologies.orient.core.record.impl.ODocument copy = getWrappedObject().copy();

        return new ODocument(env, copy);
    }

    @Signature
    public void save() {
        getWrappedObject().save();
    }

    @Signature
    public void clear() {
        getWrappedObject().clear();
    }

    @Signature
    public void delete() {
        getWrappedObject().delete();
    }

    @Getter
    public String getClassName() {
        return getWrappedObject().getClassName();
    }

    @Setter
    public void setClassName(String typeName) {
        getWrappedObject().setClassName(typeName);
    }

    @Signature
    public void reset() {
        getWrappedObject().reset();
    }

    @Signature
    public void undo() {
        getWrappedObject().undo();
    }

    @Signature
    public void undo(String field) {
        getWrappedObject().undo(field);
    }

    @Signature
    public void load() {
        getWrappedObject().load();
    }

    @Signature
    public void reload() {
        getWrappedObject().reload();
    }

    @Signature
    public String toJson() {
        return getWrappedObject().toJSON();
    }

    @Signature
    public void fromJson(String json) {
        getWrappedObject().fromJSON(json);
    }
}
