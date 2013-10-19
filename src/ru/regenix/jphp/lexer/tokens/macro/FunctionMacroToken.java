package ru.regenix.jphp.lexer.tokens.macro;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class FunctionMacroToken extends MacroToken {
    public FunctionMacroToken(TokenMeta meta) {
        super(meta, TokenType.T_FUNC_C);
    }
}
