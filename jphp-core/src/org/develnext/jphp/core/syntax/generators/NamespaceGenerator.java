package org.develnext.jphp.core.syntax.generators;

import org.develnext.jphp.core.syntax.SyntaxAnalyzer;
import org.develnext.jphp.core.tokenizer.token.SemicolonToken;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.token.expr.BraceExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.FulledNameToken;
import org.develnext.jphp.core.tokenizer.token.stmt.NamespaceStmtToken;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class NamespaceGenerator extends Generator<NamespaceStmtToken> {

    public NamespaceGenerator(SyntaxAnalyzer analyzer) {
        super(analyzer);
    }

    protected void processBody(NamespaceStmtToken namespace, ListIterator<Token> iterator){
        Token next = nextToken(iterator);
        if (next instanceof SemicolonToken){
            List<Token> tree = analyzer.process(iterator);
            namespace.setTree(tree);
            namespace.setTokenRegistered(true);
        } else if (isOpenedBrace(next, BraceExprToken.Kind.BLOCK)) {
            boolean done = false;
            List<Token> tree = new ArrayList<Token>();
            while (iterator.hasNext()){
                Token item = analyzer.processNext(iterator);
                if (isClosedBrace(item, BraceExprToken.Kind.BLOCK)){
                    done = true;
                    break;
                }
                tree.add(item);
            }
            if (!done)
                nextToken(iterator);

            namespace.setTree(tree);
        } else
            unexpectedToken(next);
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
