package org.develnext.jphp.ext.text.classes;

import org.develnext.jphp.ext.text.TextJvmTestCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TextTest extends TextJvmTestCase {
    @Test
    public void testBasic() {
        check("text/text_basic.php");
    }

    @Test
    public void testSimilar() {
        check("text/text_similar.php");
    }
}
