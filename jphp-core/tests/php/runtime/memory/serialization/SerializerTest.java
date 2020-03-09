package php.runtime.memory.serialization;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import php.runtime.Memory;
import php.runtime.common.collections.map.LinkedMap;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.StdClass;
import php.runtime.memory.*;
import php.runtime.memory.output.serialization.Serializer;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SerializerTest {

    private final Environment environment = new Environment();

    private String serialize(Memory value){
        StringBuilder sb = new StringBuilder();
        Serializer serializer = new Serializer(environment, TraceInfo.UNKNOWN, sb);
        serializer.write(value);
        return sb.toString();
    }

    @Test
    public void testScalar(){
        // NULL, UNDEFINED
        Assert.assertEquals("N;", serialize(Memory.NULL));
        Assert.assertEquals("N;", serialize(Memory.UNDEFINED));

        // LONG
        Assert.assertEquals("i:100500;", serialize(LongMemory.valueOf(100500)));
        Assert.assertEquals("i:"+Long.MAX_VALUE+";", serialize(LongMemory.valueOf(Long.MAX_VALUE)));
        Assert.assertEquals("i:"+Long.MIN_VALUE+";", serialize(LongMemory.valueOf(Long.MIN_VALUE)));

        // BOOL
        Assert.assertEquals("b:1;", serialize(Memory.TRUE));
        Assert.assertEquals("b:0;", serialize(Memory.FALSE));

        // DOUBLE
        Assert.assertEquals("d:3.14;", serialize(DoubleMemory.valueOf(3.14)));
        Assert.assertEquals("d:0;", serialize(DoubleMemory.valueOf(0)));

        // STRING
        Assert.assertEquals("s:6:\"foobar\";", serialize(new StringMemory("foobar")));
        Assert.assertEquals("s:7:\"foo\nbar\";", serialize(new StringMemory("foo\nbar")));
        Assert.assertEquals("s:7:\"foo\0bar\";", serialize(new StringMemory("foo\0bar")));
        Assert.assertEquals("s:7:\"foo\0bar\";", serialize(new StringBuilderMemory("foo\0bar")));
        Assert.assertEquals("s:7:\"foo\0bar\";", serialize(new BinaryMemory("foo\0bar")));
    }

    @Test
    public void testArrays(){
        Assert.assertEquals("a:0:{}", serialize(new ArrayMemory()));
        Assert.assertEquals("a:2:{i:0;i:100;i:1;i:500;}", serialize(new ArrayMemory(100, 500)));
        Assert.assertEquals("a:2:{s:1:\"x\";i:100;s:1:\"y\";i:500;}", serialize(new ArrayMemory(new LinkedMap(){{
            put("x", 100);
            put("y", 500);
        }})));

        Assert.assertEquals("a:2:{i:0;a:2:{i:0;i:100;i:1;i:500;}i:1;a:2:{i:0;i:200;i:1;i:600;}}",
                serialize(new ArrayMemory(new ArrayMemory(100, 500), new ArrayMemory(200, 600))));
    }

    @Test
    public void testObjects() {
        Assert.assertEquals("O:8:\"stdClass\":0:{}", serialize(new ObjectMemory(new StdClass(environment))));

        StdClass stdClass = new StdClass(environment);
        stdClass.getProperties().refOfIndex("x").assign("foo");
        stdClass.getProperties().refOfIndex("y").assign("bar");

        Assert.assertEquals("O:8:\"stdClass\":2:{s:1:\"x\";s:3:\"foo\";s:1:\"y\";s:3:\"bar\";}",
                serialize(new ObjectMemory(stdClass)));
    }
}
