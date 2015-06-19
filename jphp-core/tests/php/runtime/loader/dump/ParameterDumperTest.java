package php.runtime.loader.dump;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import php.runtime.common.HintType;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.Memory;
import php.runtime.reflection.ParameterEntity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ParameterDumperTest {
    private final Context context = new Context(new File("unknown"));
    private final Environment environment = new Environment();
    private final ParameterDumper dumper = new ParameterDumper(context, environment, true);

    @Test
    public void testBasic() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        ParameterEntity entity = new ParameterEntity(context);
        entity.setTypeClass("FooBar");
        entity.setType(HintType.CALLABLE);
        entity.setReference(true);
        entity.setDefaultValue(Memory.CONST_INT_5);
        entity.setName("Foobar");
        dumper.save(entity, output);


        ParameterEntity copyEntity = dumper.load(new ByteArrayInputStream(output.toByteArray()));

        Assert.assertEquals("Foobar", copyEntity.getName());
        Assert.assertEquals(Memory.CONST_INT_5, copyEntity.getDefaultValue());
        Assert.assertTrue(copyEntity.isReference());
        Assert.assertEquals(HintType.CALLABLE, copyEntity.getType());
        Assert.assertNull(copyEntity.getTypeClass());
    }
}
