package ru.regenix.jphp.syntax.generators;

import ru.regenix.jphp.tokenizer.token.Token;
import ru.regenix.jphp.tokenizer.token.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.BackslashExprToken;
import ru.regenix.jphp.tokenizer.token.expr.FulledNameToken;
import ru.regenix.jphp.tokenizer.token.expr.value.NameToken;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class NameGenerator extends Generator<FulledNameToken> {

    public NameGenerator(SyntaxAnalyzer analyzer) {
        super(analyzer);
    }

    @Override
    public FulledNameToken getToken(Token current, ListIterator<Token> iterator) {
        if (current instanceof NameToken || current instanceof BackslashExprToken){
            Token prev = null;
            List<Token> names = new ArrayList<Token>();
            do {
                if (current instanceof BackslashExprToken){
                    if (!(prev instanceof NameToken))
                        unexpectedToken(current);
                } else if (current instanceof NameToken){
                    names.add(current);
                } else {
                    iterator.previous();
                    break;
                }

                prev = current;
                if (iterator.hasNext())
                    current = iterator.next();
                else
                    current = null;
            } while (current != null);

            return new FulledNameToken(TokenMeta.of(names), names);
        }
        return null;
    }

    @Override
    public boolean isAutomatic() {
        return false;
    }
}
