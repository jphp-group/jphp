package org.develnext.jphp.json.classes;

import org.develnext.jphp.json.JsonJvmTestCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;


@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JsonProcessorTest extends JsonJvmTestCase {
    @Test
    public void testBasic() {
        check("json/JsonProcessor_001.php");
        check("json/JsonProcessor_002.php");
        check("json/JsonProcessor_003.php");
        check("json/JsonProcessor_004.php");
        check("json/JsonProcessor_005.php");
        check("json/JsonProcessor_006.php");
        check("json/JsonProcessor_007.php");
        check("json/JsonProcessor_008.php");
        check("json/JsonProcessor_009.php");
        check("json/JsonProcessor_010.php");
        check("json/JsonProcessor_011.php");
    }
}
