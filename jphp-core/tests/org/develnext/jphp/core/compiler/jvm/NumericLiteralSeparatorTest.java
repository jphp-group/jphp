package org.develnext.jphp.core.compiler.jvm;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import php.runtime.Memory;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NumericLiteralSeparatorTest extends JvmCompilerCase {

    @Test
    public void testCommon(){
        check("numeric_literal_separator/numeric_literal_separator_001.phpt", true);
    }

    @Test
    public void testInvalid(){
        check("numeric_literal_separator/numeric_literal_separator_002.phpt", true);
        check("numeric_literal_separator/numeric_literal_separator_003.phpt", true);
        check("numeric_literal_separator/numeric_literal_separator_004.phpt", true);
        check("numeric_literal_separator/numeric_literal_separator_005.phpt", true);
        check("numeric_literal_separator/numeric_literal_separator_006.phpt", true);
        check("numeric_literal_separator/numeric_literal_separator_007.phpt", true);
        check("numeric_literal_separator/numeric_literal_separator_008.phpt", true);
        check("numeric_literal_separator/numeric_literal_separator_009.phpt", true);
    }
}
