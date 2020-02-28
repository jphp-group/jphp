package org.develnext.jphp.core.syntax;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import php.runtime.common.LangMode;
import php.runtime.exceptions.support.ErrorException;
import php.runtime.env.Context;
import org.develnext.jphp.core.tokenizer.Tokenizer;
import org.develnext.jphp.core.tokenizer.token.stmt.ExprStmtToken;

import java.io.IOException;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SyntaxAnalyzerTest extends AbstractSyntaxTestCase {

    @Test
    public void testSimple() throws IOException {
        Tokenizer tokenizer = new Tokenizer(new Context("foobar;"));
        SyntaxAnalyzer analyzer = new SyntaxAnalyzer(environment, tokenizer);

        Assert.assertTrue(analyzer.getTree().size() == 1);
        Assert.assertTrue(analyzer.getTree().listIterator().next() instanceof ExprStmtToken);
    }

    @Test(expected = ErrorException.class)
    public void testUnexpectedEnd() throws IOException {
        Tokenizer tokenizer = new Tokenizer(new Context("foobar"));
        environment.scope.setLangMode(LangMode.DEFAULT);
        new SyntaxAnalyzer(environment, tokenizer);
    }

}
