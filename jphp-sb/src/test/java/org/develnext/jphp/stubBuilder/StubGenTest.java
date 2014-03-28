package org.develnext.jphp.stubBuilder;

import junit.framework.TestCase;
import org.develnext.jphp.stubBuilder.tree.StubPhpFile;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author VISTALL
 * @since 18:22/23.03.14
 */
public class StubGenTest extends TestCase {
    public void testFileObject() throws Exception {

    }

    public void testWrapEnvironment() throws Exception {

    }

    public void testJsonFunctions() throws Exception {

    }

    public void testJsonConstants() throws Exception {
    }

    @Override
    protected void runTest() throws Throwable {
        String name = getName();
        name = name.substring(4, name.length());

        StubBuilder builder = new StubBuilder(StubGenTest.class.getResourceAsStream("/" + name + ".class_"));
        StubPhpFile generate = builder.generate();
        assertNotNull(builder);

        InputStream resourceAsStream = StubGenTest.class.getResourceAsStream("/" + generate.getName() + ".result");
        assertTrue(generate.getName() + " is not found", resourceAsStream != null);

        InputStreamReader fileReader = new InputStreamReader(resourceAsStream);
        StringBuilder b = new StringBuilder();
        int c;
        while ((c = fileReader.read()) != -1) {
            b.append((char) c);
        }
        assertEquals(b.toString(), generate.toString());
    }
}
