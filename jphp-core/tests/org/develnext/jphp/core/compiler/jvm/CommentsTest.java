package org.develnext.jphp.core.compiler.jvm;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

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
}
