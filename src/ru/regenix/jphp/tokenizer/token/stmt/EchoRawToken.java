package ru.regenix.jphp.tokenizer.token.stmt;

import ru.regenix.jphp.tokenizer.GrammarUtils;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.TokenType;

public class EchoRawToken extends StmtToken {
    protected boolean isShort = true;

    public EchoRawToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
        String code = meta.getWord();

        if (code.length() > 0){
            int i, length = code.length();
            for(i = 0; i < length; i++){
                char ch = code.charAt(i);
                if (!Character.isWhitespace(ch) || GrammarUtils.isNewline(ch)){
                    if (GrammarUtils.isNewline(ch))
                        i += 1;
                    /*if (i > 0 && GrammarUtils.isNewline(code.charAt(i - 1)))
                        i -= 1;*/
                    break;
                }
            }

            code = code.substring(i);
            /*if (code.length() > 0 && GrammarUtils.isNewline(code.charAt(code.length() - 1)))
                code = code.substring(0, code.length() - 1); */

            meta.setWord(code);
        }
    }

    public boolean isShort() {
        return isShort;
    }

    public void setShort(boolean aShort) {
        isShort = aShort;
    }
}
