package org.develnext.jphp.zend.ext.standard;

import org.develnext.jphp.zend.ZendJvmTestCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ArrayFunctionsTest extends ZendJvmTestCase {

    @Test
    public void testArrayKeyFirst() {
        check("ext/array/array_key_first.phpt");
        check("ext/array/array_key_first_variation.phpt");
    }

    @Test
    public void testArrayKeyLast() {
        check("ext/array/array_key_last.phpt");
        check("ext/array/array_key_last_variation.phpt");
    }
}