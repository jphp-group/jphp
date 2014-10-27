package php.runtime.loader.dump;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import php.runtime.common.HintType;
import php.runtime.common.Modifier;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.reflection.MethodEntity;
import php.runtime.reflection.ParameterEntity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MethodDumperTest {
    private final Context context = new Context(new File("unknown"));
    private final Environment environment = new Environment();
    private final MethodDumper dumper = new MethodDumper(context, environment, true);

    @Test
    public void testBasic() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        MethodEntity entity = new MethodEntity(context);
        entity.setName("foobar");
        entity.setAbstract(true);
        entity.setFinal(true);
        entity.setModifier(Modifier.PROTECTED);
        entity.setAbstractable(true);
        entity.setImmutable(true);
        entity.setReturnReference(true);

        dumper.save(entity, output);

        MethodEntity copyEntity = dumper.load(new ByteArrayInputStream(output.toByteArray()));

        Assert.assertEquals("foobar", copyEntity.getName());
        Assert.assertEquals(Modifier.PROTECTED, copyEntity.getModifier());
        Assert.assertTrue(copyEntity.isAbstract());
        Assert.assertTrue(copyEntity.isFinal());
        Assert.assertTrue(copyEntity.isAbstractable());
        Assert.assertTrue(copyEntity.isImmutable());
        Assert.assertTrue(copyEntity.isReturnReference());
    }

    @Test
    public void testWithArgs() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        MethodEntity entity = new MethodEntity(context);
        entity.setName("foobar");
        entity.setModifier(Modifier.PUBLIC);
        entity.setParameters(new ParameterEntity[]{
                new ParameterEntity(context){{
                    setName("param1");
                    setType(HintType.ARRAY);
                }},
                new ParameterEntity(context){{
                    setName("param2");
                }}
        });


        dumper.save(entity, output);
        MethodEntity copyEntity = dumper.load(new ByteArrayInputStream(output.toByteArray()));
        Assert.assertEquals("foobar", copyEntity.getName());
        Assert.assertEquals(2, copyEntity.getParameters().length);
        Assert.assertEquals("param1", copyEntity.getParameters()[0].getName());
        Assert.assertEquals(HintType.ARRAY, copyEntity.getParameters()[0].getType());
        Assert.assertEquals("param2", copyEntity.getParameters()[1].getName());
    }
}
