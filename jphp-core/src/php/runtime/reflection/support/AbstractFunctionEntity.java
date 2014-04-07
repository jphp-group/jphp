package php.runtime.reflection.support;


import php.runtime.env.Context;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.ext.support.Extension;
import php.runtime.lang.Closure;
import php.runtime.reflection.DocumentComment;
import php.runtime.reflection.ParameterEntity;

abstract public class AbstractFunctionEntity extends Entity {
    protected boolean isImmutable;
    protected boolean isEmpty = false;
    protected DocumentComment docComment;
    protected boolean returnReference;
    public ParameterEntity[] parameters;

    protected Memory result;
    protected boolean abstractable = false;

    protected Extension extension;
    protected boolean deprecated;
    protected boolean usesStackTrace = false;

    protected AbstractFunctionEntity(Context context) {
        super(context);
        docComment = new DocumentComment();
    }

    public Closure getClosure(Environment env){
        return null;
    }

    public boolean isDeprecated() {
        return deprecated;
    }

    public void setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }

    public Extension getExtension() {
        return extension;
    }

    public void setExtension(Extension extension) {
        this.extension = extension;
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
            for(Memory argument : arguments) {
                if (argument == null) continue;

                ParameterEntity param = parameters != null && x < parameters.length ? parameters[x] : null;
                if (param == null || (param.isUsed() && param.isMutable() && !param.isReference())) {
                    if (argument.isArray())
                        argument.unset();
                }
                x++;
            }
        }
    }

    public boolean isAbstractable() {
        return abstractable;
    }

    public void setAbstractable(boolean abstractable) {
        this.abstractable = abstractable;
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
        if (isImmutable && !abstractable)
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

    public boolean isUsesStackTrace() {
        return usesStackTrace;
    }

    public void setUsesStackTrace(boolean usesStackTrace) {
        this.usesStackTrace = usesStackTrace;
    }
}
