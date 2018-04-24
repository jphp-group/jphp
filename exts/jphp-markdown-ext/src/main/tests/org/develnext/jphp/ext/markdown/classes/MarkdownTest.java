package org.develnext.jphp.ext.markdown.classes;

import org.develnext.jphp.ext.markdown.MarkdownJvmTestCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MarkdownTest extends MarkdownJvmTestCase {
    @Test
    public void testBasic() {
        check("markdown/markdown_001.php");
    }
}
