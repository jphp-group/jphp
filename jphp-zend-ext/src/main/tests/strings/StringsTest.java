package strings;

import org.develnext.jphp.zend.ZendJvmTestCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StringsTest extends ZendJvmTestCase {

    @Test
    public void testLevenshtein(){
        check("ext/strings/levenshtein_001.php");
        check("ext/strings/levenshtein_002.php");
        check("ext/strings/levenshtein_003.php");
    }

    @Test
    public void testLTrim(){
        check("ext/strings/ltrim_001.php");
    }

    @Test
    public void testRTrim(){
        check("ext/strings/rtrim_001.php");
    }

    @Test
    public void testTrim(){
        check("ext/strings/trim_001.php");
    }

    @Test
    public void testMd5(){
        check("ext/strings/md5_001.php");
    }

    @Test
    public void testMd5File(){
        check("ext/strings/md5_file_001.php");
    }

    @Test
    public void testSha1(){
        check("ext/strings/sha1_001.php");
    }

    @Test
    public void testSha1File(){
        check("ext/strings/sha1_file_001.php");
    }
}
