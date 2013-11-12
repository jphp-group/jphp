package ru.regenix.jphp.runtime.reflection;


import ru.regenix.jphp.runtime.env.Context;

abstract public class AbstractFunctionEntity extends Entity {

    protected boolean isImmutable;
    protected DocumentComment docComment;
    protected boolean returnReference;
    public ParameterEntity[] parameters;

    protected AbstractFunctionEntity(Context context) {
        super(context);
    }

    public DocumentComment getDocComment() {
        return docComment;
    }

    public void setDocComment(DocumentComment docComment) {
        this.docComment = docComment;
    }

    public void setParameters(ParameterEntity[] parameters) {
        this.parameters = parameters;
    }

    public int getParametersHash(){
        int hash = parameters.length;
        for(ParameterEntity param : parameters){
            hash += param.hashCode();
        }

        return hash;
    }

    public boolean isImmutable() {
        return isImmutable;
    }

    public void setImmutable(boolean immutable) {
        isImmutable = immutable;
    }

    public boolean isReturnReference() {
        return returnReference;
    }

    public void setReturnReference(boolean returnReference) {
        this.returnReference = returnReference;
    }
}
