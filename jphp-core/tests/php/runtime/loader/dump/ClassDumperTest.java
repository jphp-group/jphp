package php.runtime.loader.dump;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import php.runtime.common.Modifier;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.Memory;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.ConstantEntity;
import php.runtime.reflection.MethodEntity;
import php.runtime.reflection.PropertyEntity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClassDumperTest {

    private final Context context = new Context(new File("unknown"));
    private final Environment environment = new Environment();
    private final ClassDumper dumper = new ClassDumper(context, null, environment, true);

    @Test
    public void testBasic() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        ClassEntity entity = new ClassEntity(context);
        entity.setAbstract(true);
        entity.setFinal(true);
        entity.setId(1);
        entity.setName("Foobar");
        entity.setData(new byte[]{1,2,3,4});

        dumper.save(entity, output);

        ClassEntity copyEntity = dumper.load(new ByteArrayInputStream(output.toByteArray()));

        Assert.assertEquals("Foobar", copyEntity.getName());
        Assert.assertTrue(copyEntity.isAbstract());
        Assert.assertTrue(copyEntity.isFinal());

        Assert.assertArrayEquals(new byte[]{1,2,3,4}, copyEntity.getData());
    }

    @Test
    public void testComplex() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        final ClassEntity entity = new ClassEntity(context);
        entity.setId(1);
        entity.setName("Foobar");
        entity.setData(new byte[]{1});

        entity.addConstant(new ConstantEntity("const1", Memory.TRUE, true));
        entity.addConstant(new ConstantEntity("const2", Memory.FALSE, true));
        entity.addConstant(new ConstantEntity("const3", Memory.NULL, true));

        entity.addProperty(new PropertyEntity(context){{
            setName("prop1");
            setStatic(true);
            setDefaultValue(Memory.CONST_INT_3);
        }});
        entity.addProperty(new PropertyEntity(context) {{
            setName("prop2");
            setStatic(false);
            setDefaultValue(Memory.CONST_INT_5);
        }});

        entity.addMethod(new MethodEntity(context) {{
            setName("method1");
            setModifier(Modifier.PUBLIC);
            setClazz(entity);
        }}, null);

        dumper.save(entity, output);

        ClassEntity copyEntity = dumper.load(new ByteArrayInputStream(output.toByteArray()));
        Assert.assertEquals("Foobar", copyEntity.getName());
        Assert.assertArrayEquals(new byte[]{1}, copyEntity.getData());

        Assert.assertEquals(3, copyEntity.constants.size());
        Assert.assertEquals(1, copyEntity.staticProperties.size());
        Assert.assertEquals(1, copyEntity.properties.size());
        Assert.assertEquals(1, copyEntity.getMethods().size());

        Assert.assertEquals("const1", copyEntity.findConstant("const1").getName());
        Assert.assertEquals("const2", copyEntity.findConstant("const2").getName());
        Assert.assertEquals("const3", copyEntity.findConstant("const3").getName());

        Assert.assertEquals("prop1", copyEntity.staticProperties.get("prop1").getName());
        Assert.assertEquals("prop2", copyEntity.properties.get("prop2").getName());

        Assert.assertEquals("method1", copyEntity.findMethod("method1").getName());
    }
}
