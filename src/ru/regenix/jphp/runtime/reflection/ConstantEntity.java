package ru.regenix.jphp.runtime.reflection;

import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.support.Entity;

public class ConstantEntity extends Entity {

    protected ModuleEntity module;
    protected ClassEntity clazz;

    protected DocumentComment docComment;
    protected Memory value;
    public final String name;
    public final boolean caseSensitise;

    public ConstantEntity(Context context) {
        super(context);
        this.caseSensitise = true;
        this.name = null;
    }

    public ConstantEntity(String name, Memory value, boolean caseSensitise) {
        super(null);
        this.name = name;
        this.value = value;
        this.caseSensitise = caseSensitise;
    }

    public boolean isDeprecated(){
        return false; // TODO
    }

    public DocumentComment getDocComment() {
        return docComment;
    }

    public void setDocComment(DocumentComment docComment) {
        this.docComment = docComment;
    }

    public ClassEntity getClazz() {
        return clazz;
    }

    public void setClazz(ClassEntity clazz) {
        this.clazz = clazz;
        this.module = clazz == null ? null : clazz.getModule();
    }

    public ModuleEntity getModule() {
        return module;
    }

    public void setModule(ModuleEntity module) {
        this.module = module;
    }

    public Memory getValue() {
        return value;
    }

    public void setValue(Memory value) {
        synchronized (this){
            this.value = value;
        }
    }

    public boolean isCaseSensitise() {
        return caseSensitise;
    }
}
