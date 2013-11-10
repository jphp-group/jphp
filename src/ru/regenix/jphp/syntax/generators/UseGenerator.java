package ru.regenix.jphp.syntax.generators;

import ru.regenix.jphp.lexer.tokens.SemicolonToken;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.expr.FulledNameToken;
import ru.regenix.jphp.lexer.tokens.expr.value.NameToken;
import ru.regenix.jphp.lexer.tokens.stmt.AsStmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.NamespaceStmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.NamespaceUseStmtToken;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;

import java.util.ListIterator;

public class UseGenerator extends Generator<NamespaceUseStmtToken> {

    public UseGenerator(SyntaxAnalyzer analyzer) {
        super(analyzer);
    }

    @Override
    public NamespaceUseStmtToken getToken(Token current, ListIterator<Token> iterator) {
        if (current instanceof NamespaceUseStmtToken){
            if (analyzer.getClazz() != null)
                unexpectedToken(current); // TODO: add support for use in traits

            NamespaceUseStmtToken use = (NamespaceUseStmtToken) current;
            FulledNameToken name = analyzer.generator(NameGenerator.class).getToken(
                    nextToken(iterator), iterator
            );

            if (name == null)
                unexpectedToken(iterator.previous());
            use.setName(name);

            Token token = nextToken(iterator);
            if (token instanceof AsStmtToken){
                token = nextToken(iterator);
                if (token instanceof NameToken){
                    use.setAs((NameToken)token);
                    token = nextToken(iterator);
                } else
                    unexpectedToken(token);
            }

            if (!(token instanceof SemicolonToken)){
                unexpectedToken(token);
            }

            NamespaceStmtToken namespace = analyzer.getNamespace();
            namespace.getUses().add(use);
            return use;
        }

        return null;
    }
}
