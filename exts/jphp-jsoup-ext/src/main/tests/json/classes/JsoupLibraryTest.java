package json.classes;

import json.JsoupJvmTestCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;


@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JsoupLibraryTest extends JsoupJvmTestCase {
    @Test
    public void testBasic() {
        check("jsoup/Jsoup_001.php");
        check("jsoup/Jsoup_002.php");
    }
}
