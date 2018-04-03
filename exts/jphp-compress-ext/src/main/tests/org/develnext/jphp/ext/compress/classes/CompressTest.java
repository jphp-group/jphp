package org.develnext.jphp.ext.compress.classes;

import org.develnext.jphp.ext.compress.CompressJvmTestCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CompressTest extends CompressJvmTestCase {
    @Test
    public void testTar() {
        check("compress/compress_tar.php");
        check("compress/compress_tar_write.php");
    }

    @Test
    public void testZip() {
        check("compress/compress_zip.php");
        check("compress/compress_zip_write.php");
    }

    @Test
    public void testGZip() {
        check("compress/compress_gz.php");
    }

    @Test
    public void testBz2() {
        check("compress/compress_bz2.php");
    }

    @Test
    public void testLz4() {
        check("compress/compress_lz4.php");
        check("compress/compress_lz4_framed.php");
    }
}
