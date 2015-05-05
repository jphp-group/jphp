package sql.classes;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import sql.SqlJvmTestCase;


@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SqlLibraryTest extends SqlJvmTestCase {
    @Test
    public void testDriverManager() {
        check("sql/SqlDriverManager_001.php");
        check("sql/SqlDriverManager_002.php");
    }

    @Test
    public void testComplex() {
        check("sql/complex_001.php");
        check("sql/complex_002.php");
        check("sql/complex_003.php");
        check("sql/complex_004.php");
        check("sql/complex_005.php");
        check("sql/complex_006.php");
        check("sql/complex_007.php");
    }
}
