package org.develnext.jphp.core.syntax.generators;

import org.develnext.jphp.core.syntax.SyntaxAnalyzer;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.token.expr.BraceExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.AssignExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.operator.EqualExprToken;
import org.develnext.jphp.core.tokenizer.token.expr.value.NameToken;
import org.develnext.jphp.core.tokenizer.token.stmt.DeclareStmtToken;
import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;

import java.util.ListIterator;

public class DeclareGenerator extends Generator<DeclareStmtToken> {
    public DeclareGenerator(SyntaxAnalyzer analyzer) {
        super(analyzer);
    }

    @Override
    public DeclareStmtToken getToken(Token current, ListIterator<Token> iterator) {
        if (current instanceof DeclareStmtToken) {
            BraceExprToken token = nextAndExpected(iterator, BraceExprToken.class);

            if (!isOpenedBrace(token, BraceExprToken.Kind.SIMPLE)) {
                unexpectedToken(token, "(");
            }

            NameToken nameToken = nextAndExpected(iterator, NameToken.class);
            nextAndExpected(iterator, AssignExprToken.class);

            ExprStmtToken value = analyzer.generator(ExprGenerator.class)
                    .getInBraces(BraceExprToken.Kind.SIMPLE, iterator, true);

            if (!value.isConstantly()) {
                unexpectedToken(value, "constant value");
            }

            DeclareStmtToken result = (DeclareStmtToken) current;
            result.setName(nameToken);
            result.setValue(value);

            return result;
        }

        return null;
    }
}
