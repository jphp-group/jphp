package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.value.VariableExprToken;

import java.util.List;
import java.util.Set;

public class SwitchStmtToken extends StmtToken {

    private Set<VariableExprToken> local;
    private ExprStmtToken value;
    private List<CaseStmtToken> cases;
    private DefaultStmtToken defaultCase;

    public SwitchStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_SWITCH);
    }

    public List<CaseStmtToken> getCases() {
        return cases;
    }

    public void setCases(List<CaseStmtToken> cases) {
        this.cases = cases;
    }

    public DefaultStmtToken getDefaultCase() {
        return defaultCase;
    }

    public void setDefaultCase(DefaultStmtToken defaultCase) {
        this.defaultCase = defaultCase;
    }

    public Set<VariableExprToken> getLocal() {
        return local;
    }

    public void setLocal(Set<VariableExprToken> local) {
        this.local = local;
    }

    public ExprStmtToken getValue() {
        return value;
    }

    public void setValue(ExprStmtToken value) {
        this.value = value;
    }
}
