package org.develnext.jphp.zend.ext.json;

import org.develnext.jphp.core.compiler.jvm.JvmCompilerCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import php.runtime.Memory;
import php.runtime.env.CompileScope;
import php.runtime.env.Environment;
import php.runtime.lang.StdClass;
import php.runtime.memory.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class JsonFunctionsTest extends JvmCompilerCase {

    protected final Environment env = new Environment(newScope());

    @Override
    protected CompileScope newScope() {
        CompileScope scope = super.newScope();
        scope.registerExtension(new JsonExtension());
        return scope;
    }

    @Test
    public void testScalarJsonEncode() {
        assertEquals("null", JsonFunctions.json_encode(Memory.NULL));
        assertEquals("null", JsonFunctions.json_encode(Memory.UNDEFINED));

        assertEquals("true", JsonFunctions.json_encode(Memory.TRUE));
        assertEquals("false", JsonFunctions.json_encode(Memory.FALSE));

        assertEquals("100500", JsonFunctions.json_encode(LongMemory.valueOf(100500)));
        assertEquals("100.5", JsonFunctions.json_encode(DoubleMemory.valueOf(100.500)));

        assertEquals("\"\"", JsonFunctions.json_encode(StringMemory.valueOf("")));
        assertEquals("\"OK\"", JsonFunctions.json_encode(StringMemory.valueOf("OK")));
        assertEquals("\"OK\"", JsonFunctions.json_encode(new StringBuilderMemory("OK")));
        assertEquals("\"OK\"", JsonFunctions.json_encode(new BinaryMemory("OK")));

        assertEquals("true", JsonFunctions.json_encode(new ReferenceMemory(Memory.TRUE)));
    }

    @Test
    public void testArrayJsonEncode() {
        // simple
        assertEquals("[1,2,3,4]", JsonFunctions.json_encode(new ArrayMemory(1,2,3,4)));
        assertEquals("[1,\"foo\",3.5,true]", JsonFunctions.json_encode(new ArrayMemory(1,"foo",3.5,true)));

        // nested
        assertEquals("[[1,2],[3,4],5]", JsonFunctions.json_encode(new ArrayMemory(
                new ArrayMemory(1,2), new ArrayMemory(3,4), 5
        )));
    }

    @Test
    public void testObjectJsonEncode() {
        assertEquals("{\"0\":100,\"1\":500}", JsonFunctions.json_encode(
                new ArrayMemory(100,500), JsonConstants.JSON_FORCE_OBJECT
        ));

        ArrayMemory array = new ArrayMemory(100, 500);
        array.put("x", new LongMemory(100500));

        assertEquals("{\"0\":100,\"1\":500,\"x\":100500}", JsonFunctions.json_encode(array));

        StdClass stdClass = new StdClass(env);
        stdClass.getProperties().put("x", new LongMemory(100));
        stdClass.getProperties().put("y", new LongMemory(500));
        stdClass.getProperties().put("\0*\0z", new LongMemory(100500));

        assertEquals("{\"x\":100,\"y\":500}", JsonFunctions.json_encode(new ObjectMemory(stdClass)));
    }

    @Test
    public void testScalarJsonDecode() {
        assertEquals(Memory.TRUE, JsonFunctions.json_decode(env, "true"));
        assertEquals(Memory.FALSE, JsonFunctions.json_decode(env, "false"));
        assertEquals(Memory.NULL, JsonFunctions.json_decode(env, "null"));
        assertEquals(LongMemory.valueOf(5), JsonFunctions.json_decode(env, "5"));
        assertEquals(3.1, JsonFunctions.json_decode(env, "3.1").toDouble(), 0.00000001);
        assertEquals("foobar", JsonFunctions.json_decode(env, "\"foobar\"").toString());
    }

    @Test
    public void testArrayJsonDecode() {
        Memory r = JsonFunctions.json_decode(env, "[1,2,3]");
        assertTrue(r.isArray());
        assertEquals(1, r.valueOfIndex(0).toLong());
        assertEquals(2, r.valueOfIndex(1).toLong());
        assertEquals(3, r.valueOfIndex(2).toLong());
        assertEquals(3, r.toValue(ArrayMemory.class).size());

        // nested
        r = JsonFunctions.json_decode(env, "[1,[2,3],[4,5]]");
        assertTrue(r.isArray());
        assertEquals(1, r.valueOfIndex(0).toLong());
        assertTrue(r.valueOfIndex(1).isArray());
            assertEquals(2, r.valueOfIndex(1).valueOfIndex(0).toLong());
            assertEquals(3, r.valueOfIndex(1).valueOfIndex(1).toLong());
        assertTrue(r.valueOfIndex(2).isArray());
            assertEquals(4, r.valueOfIndex(2).valueOfIndex(0).toLong());
            assertEquals(5, r.valueOfIndex(2).valueOfIndex(1).toLong());
        assertEquals(3, r.toValue(ArrayMemory.class).size());
    }

    @Test
    public void testObjectJsonDecode() {
        Memory r = JsonFunctions.json_decode(env, "{\"x\":100, \"y\":500}");
        assertTrue(r.instanceOf(StdClass.class));

        StdClass stdClass = r.toObject(StdClass.class);
        assertEquals(2, stdClass.getProperties().size());
        assertEquals(100, stdClass.getProperties().valueOfIndex("x").toLong());
        assertEquals(500, stdClass.getProperties().valueOfIndex("y").toLong());
    }

    @Test
    public void testErrorJsonDecode() {
        Memory r = JsonFunctions.json_decode(env, "{\"x\":100");
        assertEquals(Memory.NULL, r);

        assertEquals(JsonConstants.JSON_ERROR_SYNTAX, JsonFunctions.json_last_error(env));
    }

    @Test
    public void testJsonSerializableJsonEncode() {
        check("json/json_serializable.php");
    }
}
