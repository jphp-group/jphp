package php.runtime.memory.serialization;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.StdClass;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.output.serialization.Deserializer;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DeserializerTest {

    private final Environment environment = new Environment();

    private Memory unserialize(String value){
        Deserializer deserializer = new Deserializer(environment, TraceInfo.UNKNOWN);
        return deserializer.read(value);
    }

    @Test
    public void testScalar(){
        Assert.assertEquals(Memory.NULL, unserialize("N;"));

        // bool
        Assert.assertEquals(Memory.TRUE, unserialize("b:1;"));
        Assert.assertEquals(Memory.FALSE, unserialize("b:0;"));

        // long
        Assert.assertEquals(100500, unserialize("i:100500;").toLong());
        Assert.assertEquals(Long.MAX_VALUE, unserialize("i:"+Long.MAX_VALUE+";").toLong());
        Assert.assertEquals(Long.MIN_VALUE, unserialize("i:"+Long.MIN_VALUE+";").toLong());

        // double
        Assert.assertEquals(3.14, unserialize("d:3.14;").toDouble(), 0.00000001);
        Assert.assertEquals(0.0, unserialize("d:0.0;").toDouble(), 0.00000001);

        // string
        Assert.assertEquals("foobar", unserialize("s:6:\"foobar\";").toString());
        Assert.assertEquals("foo\nbar", unserialize("s:7:\"foo\nbar\";").toString());
        Assert.assertEquals("foo\0bar", unserialize("s:7:\"foo\0bar\";").toString());
    }

    @Test
    public void testArrays(){
        Memory value = unserialize("a:0:{}");
        Assert.assertTrue(value instanceof ArrayMemory);
        Assert.assertEquals(0, ((ArrayMemory) value).size());

        value = unserialize("a:2:{i:0;i:100;i:1;i:500;}");
        Assert.assertTrue(value instanceof ArrayMemory);
        Assert.assertEquals(2, ((ArrayMemory) value).size());
        Assert.assertEquals(100, value.valueOfIndex(0).toLong());
        Assert.assertEquals(500, value.valueOfIndex(1).toLong());

        value = unserialize("a:2:{s:1:\"x\";i:100;s:1:\"y\";i:500;}");
        Assert.assertTrue(value instanceof ArrayMemory);
        Assert.assertEquals(2, ((ArrayMemory) value).size());
        Assert.assertEquals(100, value.valueOfIndex("x").toLong());
        Assert.assertEquals(500, value.valueOfIndex("y").toLong());

        value = unserialize("a:2:{i:0;a:2:{i:0;i:100;i:1;i:500;}i:1;a:2:{i:0;i:200;i:1;i:600;}}");
        Assert.assertTrue(value instanceof ArrayMemory);
        Assert.assertEquals(2, ((ArrayMemory) value).size());
        Assert.assertTrue(value.valueOfIndex(0).toValue() instanceof ArrayMemory);
        Assert.assertTrue(value.valueOfIndex(1).toValue() instanceof ArrayMemory);

        Assert.assertEquals(100, value.valueOfIndex(0).toValue(ArrayMemory.class).valueOfIndex(0).toLong());
        Assert.assertEquals(500, value.valueOfIndex(0).toValue(ArrayMemory.class).valueOfIndex(1).toLong());
        Assert.assertEquals(200, value.valueOfIndex(1).toValue(ArrayMemory.class).valueOfIndex(0).toLong());
        Assert.assertEquals(600, value.valueOfIndex(1).toValue(ArrayMemory.class).valueOfIndex(1).toLong());

        value = unserialize("a:2:{s:1:\"a\";s:4:\"test\";s:1:\"b\";N;}");
        Assert.assertTrue(value instanceof ArrayMemory);
        Assert.assertEquals(2, ((ArrayMemory) value).size());
        Assert.assertEquals("test", value.valueOfIndex("a").toString());
        Assert.assertEquals(Memory.NULL, value.valueOfIndex("b").toValue());
    }

    @Test
    public void testObjects(){
        Memory value = unserialize("O:8:\"stdClass\":0:{}");

        Assert.assertTrue(value instanceof ObjectMemory);
        Assert.assertTrue(value.toValue(ObjectMemory.class).value instanceof StdClass);
        Assert.assertEquals(0, value.toValue(ObjectMemory.class).getProperties().size());

        value = unserialize("O:8:\"stdClass\":2:{s:1:\"x\";s:3:\"foo\";s:1:\"y\";s:3:\"bar\";}");

        Assert.assertTrue(value instanceof ObjectMemory);
        Assert.assertTrue(value.toValue(ObjectMemory.class).value instanceof StdClass);
        Assert.assertEquals(2, value.toValue(ObjectMemory.class).getProperties().size());
        Assert.assertEquals("foo", value.toValue(ObjectMemory.class).getProperties().valueOfIndex("x").toString());
        Assert.assertEquals("bar", value.toValue(ObjectMemory.class).getProperties().valueOfIndex("y").toString());
    }
}
