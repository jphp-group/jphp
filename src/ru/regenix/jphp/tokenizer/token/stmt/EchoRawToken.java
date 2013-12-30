package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.GrammarUtils;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

public class EchoRawToken extends StmtToken {
    protected boolean isShort = true;

    public EchoRawToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
        int i, length = meta.getWord().length();
        for(i = 0; i < length; i++){
            char ch = meta.getWord().charAt(i);
            if (!Character.isWhitespace(ch)){
                if (i > 0 && GrammarUtils.isNewline(meta.getWord().charAt(i - 1)))
                    i -= 1;
                break;
            }
        }

        meta.setWord(getWord().substring(i));
    }

    public boolean isShort() {
        return isShort;
    }

    public void setShort(boolean aShort) {
        isShort = aShort;
    }
}
