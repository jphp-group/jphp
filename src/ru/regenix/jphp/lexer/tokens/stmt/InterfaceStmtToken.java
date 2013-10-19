package ru.regenix.jphp.lexer.tokens.stmt;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.FulledNameToken;
import ru.regenix.jphp.lexer.tokens.NameToken;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

import java.util.List;

public class InterfaceStmtToken extends StmtToken {
    private FulledNameToken name;
    private List<FulledNameToken> extend;
    private List<MethodStmtToken> methods;

    public InterfaceStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_INTERFACE);
    }

    public FulledNameToken getName() {
        return name;
    }

    public void setName(FulledNameToken name) {
        this.name = name;
    }

    public List<FulledNameToken> getExtend() {
        return extend;
    }

    public void setExtend(List<FulledNameToken> extend) {
        this.extend = extend;
    }

    public List<MethodStmtToken> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodStmtToken> methods) {
        this.methods = methods;
    }
}
