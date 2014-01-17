package php.runtime.loader.dump;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.Memory;
import php.runtime.reflection.ConstantEntity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConstantDumperTest {

    private final Context context = new Context(new File("unknown"));
    private final Environment environment = new Environment();
    private final ConstantDumper constantDumper = new ConstantDumper(context, environment, true);

    @Test
    public void testBasic() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        ConstantEntity constantEntity = new ConstantEntity("foobar", Memory.CONST_INT_5, false);
        constantDumper.save(constantEntity, output);

        ConstantEntity copyEntity = constantDumper.load(new ByteArrayInputStream(output.toByteArray()));

        Assert.assertEquals("foobar", copyEntity.getName());
        Assert.assertEquals(Memory.CONST_INT_5, copyEntity.getValue());
        Assert.assertFalse(copyEntity.isCaseSensitise());
    }
}
