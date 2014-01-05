package ru.regenix.jphp.runtime.reflection.support;


import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.DocumentComment;
import ru.regenix.jphp.runtime.reflection.ParameterEntity;

abstract public class AbstractFunctionEntity extends Entity {
    protected boolean isImmutable;
    protected boolean isEmpty = false;
    protected DocumentComment docComment;
    protected boolean returnReference;
    public ParameterEntity[] parameters;

    protected Memory result;

    protected AbstractFunctionEntity(Context context) {
        super(context);
    }

    public Memory getResult() {
        return result;
    }

    public void setResult(Memory result) {
        this.result = result;
    }

    public void unsetArguments(Memory[] arguments){
        if (arguments != null){
            int x = 0;
            for(ParameterEntity argument : this.parameters) {
                if (argument.isArray()) {
                    arguments[x].unset();
                }
                x++;
            }
        }
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
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

    public Memory getImmutableResult(){
        if (isImmutable)
            return getResult().toImmutable();

        return null;
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
