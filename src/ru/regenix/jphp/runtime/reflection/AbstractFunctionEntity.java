package ru.regenix.jphp.runtime.reflection;


import ru.regenix.jphp.runtime.env.Context;

import java.util.ArrayList;
import java.util.List;

abstract public class AbstractFunctionEntity extends Entity {

    protected DocumentComment docComment;
    protected final List<ParameterEntity> parameters;

    protected AbstractFunctionEntity(Context context) {
        super(context);
        this.parameters = new ArrayList<ParameterEntity>();
    }

    public DocumentComment getDocComment() {
        return docComment;
    }

    public void setDocComment(DocumentComment docComment) {
        this.docComment = docComment;
    }


    public List<ParameterEntity> getParameters() {
        return parameters;
    }

    public void addParameter(ParameterEntity parameter){
        parameters.add(parameter);
    }

    public int getParametersHash(){
        int hash = parameters.size();
        for(ParameterEntity param : parameters){
            hash += param.hashCode();
        }

        return hash;
    }
}
