package ru.regenix.jphp.runtime.reflection;

import ru.regenix.jphp.runtime.env.Context;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ClassEntity extends Entity {
    // types
    public enum Type { CLASS, INTERFACE, TRAIT }

    /** byte code */
    protected byte[] data;
    protected Class<?> nativeClazz;

    public final Map<String, MethodEntity> methods;
    protected MethodEntity constructor;

    protected final Map<String, ClassEntity> interfaces;
    protected final Map<String, ClassEntity> traits;

    public final Map<String, ConstantEntity> constants;
    public final Map<String, PropertyEntity> properties;
    public final Map<String, PropertyEntity> staticProperties;

    protected ClassEntity parent;
    protected DocumentComment docComment;

    protected boolean isAbstract = false;
    protected boolean isFinal = false;
    protected Type type = Type.CLASS;

    public ClassEntity(Context context) {
        super(context);
        this.methods = new LinkedHashMap<String, MethodEntity>();
        this.interfaces = new LinkedHashMap<String, ClassEntity>();
        this.traits = new LinkedHashMap<String, ClassEntity>();
        this.properties = new LinkedHashMap<String, PropertyEntity>();
        this.staticProperties = new LinkedHashMap<String, PropertyEntity>();
        this.constants = new LinkedHashMap<String, ConstantEntity>();
    }

    public boolean isDeprecated(){
        return false; // TODO
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean anAbstract) {
        isAbstract = anAbstract;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Map<String, MethodEntity> getMethods() {
        return methods;
    }

    public void addMethod(MethodEntity method){
        this.methods.put(method.getLowerName(), method);
    }

    public MethodEntity findMethod(String name){
        return methods.get(name);
    }

    public ClassEntity getParent() {
        return parent;
    }

    public void setParent(ClassEntity parent) {
        this.parent = parent;
        for(MethodEntity method : parent.getMethods().values()){
            MethodEntity implMethod = findMethod(method.getLowerName());
            if (implMethod == null){
                addMethod(method);
            } else {
                // TODO check signature impl method
            }
        }
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void addInterface(ClassEntity _interface) {
        this.interfaces.put(_interface.getLowerName(), _interface);
        for(MethodEntity method : _interface.getMethods().values()){
            MethodEntity implMethod = findMethod(method.getLowerName());
            if (implMethod == null){
                addMethod(method);
                // TODO throw no implemented method
            } else {
                // TODO check signature
            }
        }
    }

    public Map<String, ClassEntity> getInterfaces() {
        return interfaces;
    }

    public void addTrait(ClassEntity trait) {
        this.traits.put(trait.getLowerName(), trait);
    }

    public Map<String, ClassEntity> getTraits() {
        return traits;
    }

    public Collection<ConstantEntity> getConstants() {
        return constants.values();
    }

    public Collection<PropertyEntity> getProperties() {
        return properties.values();
    }

    public Collection<PropertyEntity> getStaticProperties() {
        return staticProperties.values();
    }

    public void addConstant(ConstantEntity constant){
        constants.put(constant.getLowerName(), constant);
        constant.setClazz(this);
    }

    protected void addProperty(PropertyEntity property){
        if (property.isStatic())
            throw new IllegalArgumentException("Property must be non-static");

        properties.put(property.getLowerName(), property);
        property.setClazz(this);
    }

    protected void addStaticProperty(PropertyEntity property){
        if (!property.isStatic())
            throw new IllegalArgumentException("Property must be static");

        staticProperties.put(property.getLowerName(), property);
        property.setClazz(this);
    }

    public MethodEntity getConstructor() {
        return constructor;
    }

    public void setConstructor(MethodEntity constructor) {
        this.constructor = constructor;
        constructor.setClazz(this);
    }

    public DocumentComment getDocComment() {
        return docComment;
    }

    public void setDocComment(DocumentComment docComment) {
        this.docComment = docComment;
    }

    public Class<?> getNativeClazz() {
        return nativeClazz;
    }

    public void setNativeClazz(Class<?> nativeClazz) {
        this.nativeClazz = nativeClazz;
    }
}
