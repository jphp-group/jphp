package org.develnext.jphp.core.compiler.jvm;

import org.develnext.jphp.core.tokenizer.Tokenizer;
import org.develnext.jphp.core.tokenizer.token.CommentToken;
import org.develnext.jphp.core.tokenizer.token.Token;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import php.runtime.env.Context;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CommentsTest extends JvmCompilerCase {

    @Test
    public void testDoctype(){
        check("comments/doctype.php");
    }

    @Test
    public void testSimpleWithCloseTag(){
        check("comments/simple_with_close_tag.php");
    }

    @Test
    public void testBugs() {
        check("comments/bug125.php");
    }

    @Test
    public void testBug154() throws IOException {
        Tokenizer tokenizer = new Tokenizer(
                new Context("/*// */")
        );

        Token token = tokenizer.nextToken();
        assertTrue(token instanceof CommentToken);
        assertEquals(CommentToken.Kind.BLOCK, ((CommentToken) token).getKind());
        assertEquals("// ", ((CommentToken) token).getComment());
    }
}
