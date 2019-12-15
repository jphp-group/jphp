package zend.ext.crypto.csprng;

import org.develnext.jphp.zend.ZendJvmTestCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CSPRNGFunctionsTest extends ZendJvmTestCase {
    @Test
    public void testRandomBytes() {
        check("ext/crypto/csprng/random_bytes.phpt");
    }

    @Test
    public void testRandomBytesError() {
        check("ext/crypto/csprng/random_bytes_error.phpt");
    }

    @Test
    public void testRandomInt() {
        check("ext/crypto/csprng/random_int.phpt");
    }

    @Test
    public void testRandomIntError() {
        check("ext/crypto/csprng/random_int_error.phpt");
    }

    @Test
    public void testReflection() {
        check("ext/crypto/csprng/reflection.phpt");
    }
}
