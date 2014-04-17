package org.develnext.jphp.gendocs;

import org.develnext.jphp.core.tokenizer.GrammarUtils;

public class DocAnnotations {
    protected final String text;
    protected String description;

    public DocAnnotations(String docComment) {
        this.text = docComment;
        parse();
    }

    protected void parse(){
        int len = text.length();
        for(int i = 0; i < len; i++) {
            char ch = text.charAt(i);
            char prev_ch = i < 1 ? '\0' : text.charAt(i - 1);

            if (ch == '@' && GrammarUtils.isNewline(prev_ch)) {
                if (description == null) {
                    description = text.substring(0, i - 1);
                }
            }
        }
    }

    public String getDescription() {
        return description;
    }
}
