package ru.regenix.jphp.runtime.reflection;

import ru.regenix.jphp.runtime.env.Context;

public class ConstantEntity extends Entity {

    protected ClassEntity clazz;
    protected DocumentComment docComment;

    public ConstantEntity(Context context) {
        super(context);
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
    }
}
