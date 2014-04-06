package org.develnext.jphp.core.tokenizer.token.expr.value;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;

import java.util.List;

public class NewExprToken extends ValueExprToken implements CallableExprToken {
    private Token name;
    private ExprStmtToken exprName;
    private List<ExprStmtToken> parameters;

    public NewExprToken(TokenMeta meta) {
        super(meta, TokenType.T_NEW);
    }

    public Token getName() {
        return name;
    }

    public void setName(FulledNameToken name) {
        this.name = name;
    }

    public void setName(SelfExprToken name) {
        this.name = name;
    }

    public void setName(ExprStmtToken name){
        this.exprName = name;
    }

    public void setName(StaticExprToken name){
        this.name = name;
    }

    public boolean isDynamic(){
        return exprName != null;
    }

    public List<ExprStmtToken> getParameters() {
        return parameters;
    }

    public void setParameters(List<ExprStmtToken> parameters) {
        this.parameters = parameters;
    }

    @Override
    public boolean isNamedToken() {
        return true;
    }

    public ExprStmtToken getExprName() {
        return exprName;
    }
}
