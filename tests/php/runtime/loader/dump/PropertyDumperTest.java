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
import php.runtime.reflection.PropertyEntity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PropertyDumperTest {

    private final Context context = new Context(new File("unknown"));
    private final Environment environment = new Environment();
    private final PropertyDumper propertyDumper = new PropertyDumper(context, environment, true);

    @Test
    public void testBasic() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        PropertyEntity propertyEntity = new PropertyEntity(context);
        propertyEntity.setModifier(Modifier.PROTECTED);
        propertyEntity.setName("foobar");
        propertyEntity.setStatic(true);
        propertyEntity.setDefaultValue(Memory.CONST_INT_5);
        propertyEntity.setClazz(null);
        propertyDumper.save(propertyEntity, output);

        PropertyEntity copyEntity = propertyDumper.load(new ByteArrayInputStream(output.toByteArray()));

        Assert.assertEquals("foobar", copyEntity.getName());
        Assert.assertEquals(Memory.CONST_INT_5, copyEntity.getDefaultValue(null));
        Assert.assertTrue(copyEntity.isStatic());
        Assert.assertEquals(Modifier.PROTECTED, copyEntity.getModifier());
    }
}
