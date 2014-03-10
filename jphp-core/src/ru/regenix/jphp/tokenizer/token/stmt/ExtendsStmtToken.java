package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.expr.value.FulledNameToken;
import ru.regenix.jphp.tokenizer.TokenMeta;

public class ExtendsStmtToken extends StmtToken {
    private FulledNameToken name;

    public ExtendsStmtToken(TokenMeta meta) {
        super(meta, TokenType.T_EXTENDS);
    }

    public FulledNameToken getName() {
        return name;
    }

    public void setName(FulledNameToken name) {
        this.name = name;
    }

    public static ExtendsStmtToken valueOf(String... fulledName){
        ExtendsStmtToken result = new ExtendsStmtToken(TokenMeta.empty());
        result.setName(FulledNameToken.valueOf(fulledName));
        return result;
    }
}
