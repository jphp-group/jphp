package org.develnext.jphp.core.tokenizer.token.stmt;

import php.runtime.common.GrammarUtils;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;

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
