package ru.regenix.jphp.runtime.reflection;

import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.common.Modifier;
import ru.regenix.jphp.exceptions.support.ErrorType;
import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.support.Entity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class PropertyEntity extends Entity {
    protected ClassEntity clazz;
    protected Modifier modifier = Modifier.PUBLIC;
    private Memory defaultValue;
    protected DocumentComment docComment;

    protected boolean isStatic;
    protected Field field;

    protected String specificName;
    protected PropertyEntity prototype;

    public PropertyEntity(Context context) {
        super(context);
    }

    public PropertyEntity getPrototype() {
        return prototype;
    }

    public void setPrototype(PropertyEntity prototype) {
        this.prototype = prototype;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        field.setAccessible(true);
        this.field = field;
    }

    public DocumentComment getDocComment() {
        return docComment;
    }

    public void setDocComment(DocumentComment docComment) {
        this.docComment = docComment;
    }

    public Memory getDefaultValue(Environment env) {
        if (defaultValue == null) {
            Memory r = env.getStatic(internalName);
            return r == null ? Memory.NULL : r;
        } else
            return defaultValue;
    }

    public void setDefaultValue(Memory defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Modifier getModifier() {
        return modifier;
    }

    public boolean isPrivate(){
        return modifier == Modifier.PRIVATE;
    }

    public boolean isProtected(){
        return modifier == Modifier.PROTECTED;
    }

    public boolean isPublic(){
        return modifier == Modifier.PUBLIC;
    }

    public void setModifier(Modifier modifier) {
        this.modifier = modifier;
        updateSpecificName();
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    public ClassEntity getClazz() {
        return clazz;
    }

    public void setClazz(ClassEntity clazz) {
        this.clazz = clazz;
        updateSpecificName();
    }

    public boolean isDeprecated(){
        return false; // TODO
    }

    public void updateSpecificName(){
        switch (modifier){
            case PRIVATE: if (clazz != null) { specificName = "\0" + clazz.getName() + "\0" + name; } break;
            case PROTECTED: specificName = "\0*\0" + name; break;
            default:
                specificName = name;
        }

        if (isStatic && clazz != null)
            specificName = "\0" + clazz.getLowerName() + "#" + specificName;

        if (clazz != null)
            internalName = "\0" + clazz.getLowerName() + "\0#" + name;
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        updateSpecificName();
    }

    public String getSpecificName() {
        return specificName;
    }

    /**
     * 0 - success
     * 1 - invalid protected
     * 2 - invalid private
     * @param env
     * @return
     */
    public int canAccess(Environment env) throws InvocationTargetException, IllegalAccessException {
        switch (modifier){
            case PUBLIC: return 0;
            case PRIVATE:
                ClassEntity cl = env.getLastClassOnStack();
                return cl != null && cl.getId() == this.clazz.getId() ? 0 : 2;
            case PROTECTED:
                ClassEntity clazz = env.getLastClassOnStack();
                if (clazz == null)
                    return 1;

                long id = this.clazz.getId();
                do {
                    if (clazz.getId() == id)
                        return 0;
                    clazz = clazz.parent;
                } while (clazz != null);
        }
        return 2;
    }

    public boolean canAccessAsNonStatic(Environment env, TraceInfo trace){
        if (isStatic){
            env.error(
                    trace, ErrorType.E_STRICT, Messages.ERR_ACCESSING_STATIC_PROPERTY_AS_NON_STATIC,
                    getClazz().getName(), name
            );
            return false;
        }
        return true;
    }
}
