package org.develnext.jphp.core.syntax.generators;

import org.develnext.jphp.core.tokenizer.token.SemicolonToken;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.token.expr.CommaToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.FulledNameToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.NameToken;
import org.develnext.jphp.core.tokenizer.token.stmt.*;
import org.develnext.jphp.core.syntax.SyntaxAnalyzer;

import java.util.ListIterator;

public class UseGenerator extends Generator<NamespaceUseStmtToken> {

    public UseGenerator(SyntaxAnalyzer analyzer) {
        super(analyzer);
    }

    @Override
    public NamespaceUseStmtToken getToken(Token current, ListIterator<Token> iterator) {
        if (current instanceof NamespaceUseStmtToken){
            if (analyzer.getClazz() != null)
                unexpectedToken(current);

            NamespaceUseStmtToken use = (NamespaceUseStmtToken) current;
            boolean first = true;

            do {
                Token next = nextToken(iterator);

                if (next instanceof FunctionStmtToken) {
                    if (!first) {
                        unexpectedToken(next);
                    }

                    use.setUseType(NamespaceUseStmtToken.UseType.FUNCTION);
                    next = nextToken(iterator);
                } else if (next instanceof ConstStmtToken) {
                    if (!first) {
                        unexpectedToken(next);
                    }

                    use.setUseType(NamespaceUseStmtToken.UseType.CONSTANT);
                    next = nextToken(iterator);
                }

                FulledNameToken name = analyzer.generator(NameGenerator.class).getToken(
                        next, iterator
                );

                if (name == null) {
                    unexpectedToken(iterator.previous());
                }
                
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

                NamespaceStmtToken namespace = analyzer.getNamespace();
                namespace.getUses().add(use);

                if (token instanceof CommaToken){
                    use = new NamespaceUseStmtToken(current.getMeta());
                } else if (!(token instanceof SemicolonToken)){
                    unexpectedToken(token);
                } else
                    break;

                first = false;

            } while (true);

            return use;
        }

        return null;
    }
}
