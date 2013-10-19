package ru.regenix.jphp.lexer.tokens.expr;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.FulledNameToken;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.value.VariableExprToken;
import ru.regenix.jphp.lexer.tokens.stmt.ExprStmtToken;

import java.util.List;

public class NewExprToken extends ExprToken {
    private Token name;
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

    public void setName(VariableExprToken name){
        this.name = name;
    }

    public boolean isDynamic(){
        return name instanceof VariableExprToken;
    }

    public List<ExprStmtToken> getParameters() {
        return parameters;
    }

    public void setParameters(List<ExprStmtToken> parameters) {
        this.parameters = parameters;
    }
}
