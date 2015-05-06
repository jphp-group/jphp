package webserver.classes;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import webserver.WebServerJvmTestCase;


@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WebServerLibraryTest extends WebServerJvmTestCase {
    @Test
    public void testBasic() {
        check("webserver/basic_001.php");
    }

    @Test
    public void testPostBody() {
        check("webserver/post_body.php");
    }

    @Test
    public void testResponse() {
        check("webserver/response.php");
    }
}
