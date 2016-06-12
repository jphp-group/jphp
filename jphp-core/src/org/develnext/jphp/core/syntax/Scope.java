package org.develnext.jphp.core.syntax;

import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.token.expr.value.VariableExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.LabelStmtToken;

import java.util.*;

public class Scope {
    protected boolean staticExists = false;
    protected Set<VariableExprToken> variables;
    protected Map<Token, ExpressionInfo> typeInfo;

    protected Map<String, LabelStmtToken> labels;
    protected final Scope parent;
    protected boolean levelForGoto = false;
    protected int cachedGotoLevel = -1;

    public Scope(Scope parent) {
        this.parent = parent;
        variables = new HashSet<>();
        labels = new HashMap<>();
        typeInfo = new LinkedHashMap<>();
    }

    public void appendScope(Scope scope) {
        variables.addAll(scope.getVariables());
        labels.putAll(scope.getLabels());
        staticExists = staticExists || scope.staticExists;
    }

    public Map<Token, ExpressionInfo> getTypeInfo() {
        return typeInfo;
    }

    synchronized public ExpressionInfo typeInfoOf(Token token) {
        ExpressionInfo info = typeInfo.get(token);

        if (info == null) {
            typeInfo.put(token, info = new ExpressionInfo());
        }

        return info;
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
        labelStmtToken.setLevel(getGotoLevel());
        labels.put(labelStmtToken.getName().toLowerCase(), labelStmtToken);
    }

    public Map<String, LabelStmtToken> getLabels() {
        return labels;
    }

    public boolean isStaticExists() {
        return staticExists;
    }

    public void setStaticExists(boolean staticExists) {
        this.staticExists = staticExists;
    }

    public boolean isLevelForGoto() {
        return levelForGoto;
    }

    public Scope setLevelForGoto(boolean applyGoto) {
        this.levelForGoto = applyGoto;
        this.cachedGotoLevel = -1;
        return this;
    }

    public int getGotoLevel() {
        if (cachedGotoLevel > -1)
            return cachedGotoLevel;

        int level = isLevelForGoto() ? 1 : 0;
        Scope pr = parent;
        while (pr != null) {
            level += pr.isLevelForGoto() ? 1 : 0;
            pr = pr.parent;
        }
        return cachedGotoLevel = level;
    }
}
