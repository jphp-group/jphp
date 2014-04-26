package org.develnext.jphp.genapi.description;

import org.develnext.jphp.core.tokenizer.token.stmt.ArgumentStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.FunctionStmtToken;
import org.develnext.jphp.genapi.DocAnnotations;
import org.develnext.jphp.genapi.parameter.MethodParamParameter;
import org.develnext.jphp.genapi.parameter.MethodReturnParameter;

import java.util.*;

public class FunctionDescription<T extends FunctionStmtToken> extends BaseDescription<T> {

    protected Map<String, ArgumentDescription> arguments;
    protected DocAnnotations annotations;
    protected MethodReturnParameter returnParameter;
    protected MethodReturnParameter[] throwsParameters;

    public FunctionDescription(T token) {
        super(token);
    }

    @Override
    protected void parse() {
        arguments = new LinkedHashMap<String, ArgumentDescription>();

        annotations = new DocAnnotations(
                token.getDocComment() == null ? "" : token.getDocComment().getComment()
        );

        DocAnnotations.Parameter returnParam = annotations.getParameter("return");
        if (returnParam != null) {
            this.returnParameter = new MethodReturnParameter(token.getNamespace(), returnParam.value());
        }

        DocAnnotations.Parameter throwsParam = annotations.getParameter("throws");
        if (throwsParam != null) {
            this.throwsParameters = new MethodReturnParameter[throwsParam.values().size()];
            int i = 0;
            for(String e : throwsParam.values()) {
                this.throwsParameters[i] = new MethodReturnParameter(token.getNamespace(), e);
                i++;
            }
        }

        Map<String, MethodParamParameter> paramDescription = new HashMap<String, MethodParamParameter>();
        DocAnnotations.Parameter param = annotations.getParameter("param");
        if (param != null) {
            for(String el : param.values()) {
                MethodParamParameter tmp = new MethodParamParameter(token.getNamespace(), el);
                paramDescription.put(tmp.getArgument(), tmp);
            }
        }

        for(ArgumentStmtToken el : token.getArguments()) {
            MethodParamParameter desc = paramDescription.get(el.getName().getName());

            arguments.put(el.getName().getName(), new ArgumentDescription(el, desc));
        }
    }

    public Collection<ArgumentDescription> getArguments() {
        return arguments.values();
    }

    public String getDescription() {
        return annotations == null ? null : annotations.getDescription();
    }

    public String getName() {
        return token.getName().getName();
    }

    public boolean isReturnReference() {
        return token.isReturnReference();
    }

    public String[] getReturnTypes() {
        return returnParameter == null ? null : returnParameter.getTypes();
    }

    public String getReturnDescription() {
        return returnParameter == null ? "" : returnParameter.getDescription();
    }

    public MethodReturnParameter[] getThrowsParameters() {
        return throwsParameters;
    }
}
