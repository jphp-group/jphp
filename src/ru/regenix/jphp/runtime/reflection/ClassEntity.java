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

    protected final Map<String, MethodEntity> methods;
    protected MethodEntity constructor;

    protected final Map<String, ClassEntity> interfaces;
    protected final Map<String, ClassEntity> traits;

    protected final Map<String, ConstantEntity> constants;
    protected final Map<String, PropertyEntity> properties;
    protected final Map<String, PropertyEntity> staticProperties;

    protected ClassEntity parent;
    protected DocumentComment docComment;

    protected boolean isAbstract = false;
    protected boolean isFinal = false;
    protected Type type = Type.CLASS;

    /**** CACHE *****/
    protected Map<String, MethodEntity> allMethods;
    protected Map<String, ConstantEntity> allConstants;
    protected Map<String, PropertyEntity> allProperties;
    protected Map<String, PropertyEntity> allStaticProperties;

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
        return getAllMethods().get(name.toLowerCase());
    }

    public ClassEntity getParent() {
        return parent;
    }

    public void setParent(ClassEntity parent) {
        this.parent = parent;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void addInterface(ClassEntity _interface) {
        this.interfaces.put(_interface.getLowerName(), _interface);
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

    public Map<String, MethodEntity> getAllMethods(){
        if (allMethods != null)
            return allMethods;

        synchronized(this){
            Map<String, MethodEntity> result = new LinkedHashMap<String, MethodEntity>(this.methods);
            for(ClassEntity _interface : interfaces.values()){
                result.putAll(_interface.getAllMethods());
            }
            if (parent != null)
                result.putAll(parent.getAllMethods());
            allMethods = result;
        }
        return allMethods;
    }

    public Map<String, ConstantEntity> getAllConstants(){
        if (allConstants != null)
            return allConstants;

        synchronized(this){
            Map<String, ConstantEntity> result = new LinkedHashMap<String, ConstantEntity>(this.constants);
            if (parent != null)
                result.putAll(parent.getAllConstants());
            allConstants = result;
        }
        return allConstants;
    }

    public Map<String, PropertyEntity> getAllProperties(){
        if (allProperties != null)
            return allProperties;

        synchronized (this){
            Map<String, PropertyEntity> result = new LinkedHashMap<String, PropertyEntity>(this.properties);
            if (parent != null)
                result.putAll(parent.getAllProperties());
            allProperties = result;
        }
        return allProperties;
    }

    public Map<String, PropertyEntity> getAllStaticProperties(){
        if (allStaticProperties != null)
            return allStaticProperties;

        synchronized (this){
            Map<String, PropertyEntity> result = new LinkedHashMap<String, PropertyEntity>(this.staticProperties);
            if (parent != null)
                result.putAll(parent.getAllStaticProperties());
            allStaticProperties = result;
        }
        return allStaticProperties;
    }

    public void clearCache(){
        allMethods = null;
        allConstants = null;
        allProperties = null;
        allStaticProperties = null;
    }

    public Class<?> getNativeClazz() {
        return nativeClazz;
    }

    public void setNativeClazz(Class<?> nativeClazz) {
        this.nativeClazz = nativeClazz;
    }
}
