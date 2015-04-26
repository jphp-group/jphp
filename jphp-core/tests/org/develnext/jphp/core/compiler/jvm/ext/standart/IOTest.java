package org.develnext.jphp.core.compiler.jvm.ext.standart;

import org.develnext.jphp.core.compiler.jvm.JvmCompilerCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IOTest extends JvmCompilerCase {

    @Test
    public void testFileObject() {
        check("ext/standard/io/file_object_001.php");
        check("ext/standard/io/file_object_002.php");
        check("ext/standard/io/file_object_003.php");
    }

    @Test
    public void testStream() {
        check("ext/standard/io/stream_001.php");
        check("ext/standard/io/stream_002.php");
    }
}
