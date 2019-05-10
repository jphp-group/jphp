package php.runtime.util;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.develnext.jphp.core.compiler.jvm.JvmCompilerCase;
import org.develnext.jphp.zend.ext.standard.StandardExtension;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

import php.runtime.Memory;
import php.runtime.env.CompileScope;
import php.runtime.memory.LongMemory;
import php.runtime.memory.StringMemory;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PrintFTest extends JvmCompilerCase {
    static final Memory INT_MAX = LongMemory.valueOf(Integer.MAX_VALUE);
    static final Memory INT_MIN = LongMemory.valueOf(Integer.MIN_VALUE);

    static final Memory LONG_MAX = LongMemory.valueOf(Long.MAX_VALUE);
    static final Memory LONG_MIN = LongMemory.valueOf(Long.MIN_VALUE);

    @Override
    protected CompileScope newScope() {
        CompileScope scope = super.newScope();
        scope.registerExtension(new StandardExtension());
        return scope;
    }

    @Test
    public void unsigned() {
        assertEquals("2147483647", printf("%u", INT_MAX));
        assertEquals("18446744071562067968", printf("%u", INT_MIN));

        assertEquals("9223372036854775807", printf("%u", LONG_MAX));
        assertEquals("9223372036854775808", printf("%u", LONG_MIN));
    }

    @Test
    public void ldModifier() {
        Memory arg = LongMemory.valueOf(32);

        Map<Character, String> map = new HashMap<>();
        map.put('d', "32");
        map.put('u', "32");
        map.put('x', "20");
        map.put('X', "20");
        map.put('b', "100000");
        map.put('B', "100000");

        map.forEach((modifier, expected) -> assertEquals(expected, printf("%l" + modifier, arg)));
    }

    @Test
    public void skippingUnknownModifiers() {
        assertEquals("u", printf("%hu", INT_MAX));
        assertEquals("", printf("%h", INT_MAX));

        assertEquals("", printf("%L", INT_MAX));
        assertEquals("u", printf("%Lu", INT_MAX));
        assertEquals("", printf("%q", INT_MAX));
    }

    @Test
    public void lModifierWithPadding() {
        // %ld with left padding
        assertEquals("00001", printf("%05ld", Memory.CONST_INT_1));
        assertEquals("1    ", printf("%-5ld", Memory.CONST_INT_1));
        assertEquals("    1", printf("%5ld", Memory.CONST_INT_1));
    }

    private String printf(String format, Memory... args) {
        PrintF printF = new PrintF(environment.getLocale(), format, args);
        String result = printF.toString();
        return result;
    }
}