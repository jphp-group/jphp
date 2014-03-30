package org.develnext.jphp.core.syntax;

import org.develnext.jphp.core.tokenizer.token.expr.value.VariableExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.LabelStmtToken;

import java.util.*;

public class Scope {
    protected Set<VariableExprToken> variables;
    protected Map<String, LabelStmtToken> labels;

    public Scope() {
        variables = new HashSet<VariableExprToken>();
        labels = new HashMap<String, LabelStmtToken>();
    }

    public void appendScope(Scope scope) {
        variables.addAll(scope.getVariables());
        labels.putAll(scope.getLabels());
    }

    public Set<VariableExprToken> getVariables() {
        return variables;
    }

    public void addVariable(VariableExprToken var) {
        variables.add(var);
    }

    public void addVariables(Collection<VariableExprToken> vars){
        variables.addAll(vars);
    }

    public void addLabel(LabelStmtToken labelStmtToken) {
        labels.put(labelStmtToken.getName().toLowerCase(), labelStmtToken);
    }

    public Map<String, LabelStmtToken> getLabels() {
        return labels;
    }
}
