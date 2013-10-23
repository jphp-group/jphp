package ru.regenix.jphp.syntax.generators;

import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.expr.FulledNameToken;
import ru.regenix.jphp.lexer.tokens.stmt.BodyStmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.NamespaceStmtToken;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;
import ru.regenix.jphp.syntax.generators.manually.BodyGenerator;

import java.util.ListIterator;

public class NamespaceGenerator extends Generator<NamespaceStmtToken> {

    public NamespaceGenerator(SyntaxAnalyzer analyzer) {
        super(analyzer);
    }

    protected void processBody(NamespaceStmtToken namespace, ListIterator<Token> iterator){
        BodyStmtToken body = analyzer.generator(BodyGenerator.class).getToken(
                nextToken(iterator), iterator
        );
        namespace.setBody(body);
    }

    @Override
    public NamespaceStmtToken getToken(Token current, ListIterator<Token> iterator) {
        if (current instanceof NamespaceStmtToken){
            NamespaceStmtToken result = (NamespaceStmtToken)current;
            FulledNameToken name = analyzer.generator(NameGenerator.class).getToken(
                    nextToken(iterator), iterator
            );
            result.setName(name);

            if (name == null)
                iterator.previous();

            analyzer.setNamespace(result);
            processBody(result, iterator);
            analyzer.setNamespace(NamespaceStmtToken.getDefault());
            return result;
        }
        return null;
    }
}
