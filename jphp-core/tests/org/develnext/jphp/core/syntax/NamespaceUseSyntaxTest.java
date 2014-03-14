package org.develnext.jphp.core.syntax;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.develnext.jphp.core.tokenizer.token.stmt.NamespaceUseStmtToken;

import java.util.List;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NamespaceUseSyntaxTest extends AbstractSyntaxTestCase {

    @Test
    public void testSimple(){
        List<Token> tree = getSyntaxTree("use foo\\bar;");
        Assert.assertTrue(tree.size() == 1);
        Assert.assertTrue(tree.get(0) instanceof NamespaceUseStmtToken);

        NamespaceUseStmtToken token = (NamespaceUseStmtToken)tree.get(0);
        Assert.assertNull(token.getAs());
        Assert.assertNotNull(token.getName());
        Assert.assertEquals("foo\\bar", token.getName().toName());
    }

    @Test
    public void testAs(){
        List<Token> tree = getSyntaxTree("use foo\\bar as bar;");
        Assert.assertTrue(tree.size() == 1);
        Assert.assertTrue(tree.get(0) instanceof NamespaceUseStmtToken);

        NamespaceUseStmtToken token = (NamespaceUseStmtToken)tree.get(0);
        Assert.assertNotNull(token.getAs());
        Assert.assertEquals("bar", token.getAs().getName());
        Assert.assertNotNull(token.getName());
        Assert.assertEquals("foo\\bar", token.getName().toName());
    }
}
