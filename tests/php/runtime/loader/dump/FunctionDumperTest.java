package php.runtime.loader.dump;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import php.runtime.Memory;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.reflection.FunctionEntity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FunctionDumperTest {
    private final Context context = new Context(new File("unknown"));
    private final Environment environment = new Environment();
    private final FunctionDumper dumper = new FunctionDumper(context, environment, true);

    @Test
    public void testBasic() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        FunctionEntity entity = new FunctionEntity(context);
        entity.setStatic(true);
        entity.setImmutable(true);
        entity.setResult(Memory.TRUE);
        entity.setEmpty(true);
        entity.setName("Foobar");
        entity.setData(new byte[]{1,2,3,4});
        dumper.save(entity, output);

        FunctionEntity copyEntity = dumper.load(new ByteArrayInputStream(output.toByteArray()));

        Assert.assertEquals("Foobar", copyEntity.getName());
        Assert.assertTrue(copyEntity.isStatic());
        Assert.assertTrue(copyEntity.isImmutable());
        Assert.assertTrue(copyEntity.isEmpty());

        Assert.assertArrayEquals(new byte[]{1,2,3,4}, copyEntity.getData());
    }
}
