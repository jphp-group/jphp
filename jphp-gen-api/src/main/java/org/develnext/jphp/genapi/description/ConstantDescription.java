package org.develnext.jphp.genapi.description;

import org.develnext.jphp.core.tokenizer.token.stmt.ConstStmtToken;
import org.develnext.jphp.genapi.DocAnnotations;

public class ConstantDescription extends BaseDescription<ConstStmtToken> {
    private String description;

    public ConstantDescription(ConstStmtToken token) {
        super(token);
    }

    @Override
    protected void parse() {
        if (token.getDocComment() != null){
            DocAnnotations annotations = new DocAnnotations(token.getDocComment().getComment());
            description = annotations.getDescription();
        }
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return token.items.get(0).getFulledName();
    }

    public String getValue() {
        return token.items.get(0).value.getMeta().getWord();
    }
}
