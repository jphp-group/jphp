package org.develnext.jphp.core.compiler.jvm;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AnalyzerTest extends JvmCompilerCase {

    @Test
    public void testDynAccessWithKeyWords() {
        check("analyzer/dyn_access_with_key_words.php");
    }
}
