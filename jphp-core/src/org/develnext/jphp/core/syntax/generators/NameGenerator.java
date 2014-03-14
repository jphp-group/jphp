package org.develnext.jphp.core.syntax.generators;

import org.develnext.jphp.core.syntax.SyntaxAnalyzer;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.token.expr.value.FulledNameToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.NameToken;

import java.util.Arrays;
import java.util.ListIterator;

public class NameGenerator extends Generator<FulledNameToken> {

    public NameGenerator(SyntaxAnalyzer analyzer) {
        super(analyzer);
    }

    protected void checkFullName(FulledNameToken name){
        for(NameToken one : name.getNames())
            if (one.getName().isEmpty())
                unexpectedToken(one);
    }

    @Override
    public FulledNameToken getToken(Token current, ListIterator<Token> iterator) {
        if (current instanceof NameToken){
            if (current instanceof FulledNameToken){
                checkFullName((FulledNameToken)current);
                return (FulledNameToken)current;
            } else
                return new FulledNameToken(TokenMeta.of(current), Arrays.asList((NameToken)current));
        }
        return null;
    }

    @Override
    public boolean isAutomatic() {
        return false;
    }
}
