package ru.regenix.jphp.syntax.generators;

import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.exceptions.ParseException;
import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.FulledNameToken;
import ru.regenix.jphp.lexer.tokens.NameToken;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.BackslashExprToken;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class NameGenerator extends Generator<NameToken> {

    public NameGenerator(SyntaxAnalyzer analyzer) {
        super(analyzer);
    }

    @Override
    public NameToken getToken(Token current, ListIterator<Token> iterator) {
        if (current instanceof NameToken || current instanceof BackslashExprToken){
            Token prev = current;
            List<Token> names = new ArrayList<Token>();
            while (iterator.hasNext()){
                Token item = iterator.next();
                if (item instanceof BackslashExprToken){
                    if (!(prev instanceof NameToken))
                        throw new ParseException(
                                Messages.ERR_PARSE_UNEXPECTED_END_OF_FILE.fetch(prev.getType(), TokenType.T_STRING),
                                item.getMeta().toTraceInfo(analyzer.getFile())
                        );
                } else if (item instanceof NameToken){
                    names.add(item);
                } else {
                    iterator.previous();
                    break;
                }

                prev = item;
            }

            return new FulledNameToken(TokenMeta.of(names), names);
        }
        return null;
    }
}
