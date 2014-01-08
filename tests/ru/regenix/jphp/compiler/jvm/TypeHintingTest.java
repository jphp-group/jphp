package ru.regenix.jphp.compiler.jvm;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import ru.regenix.jphp.common.HintType;
import ru.regenix.jphp.runtime.memory.*;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.ParameterEntity;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TypeHintingTest extends JvmCompilerCase {

    @Test
    public void testArray(){
        check("type_hinting/array.php", true);
    }

    @Test
    public void testArrayRef(){
        check("type_hinting/array_ref.php", true);
    }

    @Test
    public void testArrayDefault(){
        check("type_hinting/array_default.php", true);
    }

    @Test
    public void testCallable(){
        check("type_hinting/callable.php", true);
    }

    @Test
    public void testCallableRef(){
        check("type_hinting/callable_ref.php", true);
    }

    @Test
    public void testObject(){
        check("type_hinting/object.php", true);
    }

    @Test
         public void testObjectDefNull(){
        check("type_hinting/object_def_null.php", true);
    }

    @Test
    public void testNumber(){
        check("type_hinting/number.php", true);
    }

    @Test
    public void testCoreNumber(){
        ParameterEntity param = new ParameterEntity(null);
        param.setType(HintType.NUMBER);

        Assert.assertFalse(param.checkTypeHinting(null, new StringMemory(""), false));
        Assert.assertFalse(param.checkTypeHinting(null, new ObjectMemory(null), false));
        Assert.assertFalse(param.checkTypeHinting(null, new ArrayMemory(), false));
        Assert.assertFalse(param.checkTypeHinting(null, Memory.NULL, false));
        Assert.assertFalse(param.checkTypeHinting(null, Memory.FALSE, false));
        Assert.assertFalse(param.checkTypeHinting(null, Memory.TRUE, false));

        Assert.assertTrue(param.checkTypeHinting(null, new LongMemory(0), false));
        Assert.assertTrue(param.checkTypeHinting(null, new DoubleMemory(0), false));
    }

    @Test
    public void testCoreScalar(){
        ParameterEntity param = new ParameterEntity(null);
        param.setType(HintType.SCALAR);

        Assert.assertTrue(param.checkTypeHinting(null, new StringMemory(""), false));
        Assert.assertFalse(param.checkTypeHinting(null, new ObjectMemory(null), false));
        Assert.assertFalse(param.checkTypeHinting(null, new ArrayMemory(), false));
        Assert.assertFalse(param.checkTypeHinting(null, Memory.NULL, false));
        Assert.assertTrue(param.checkTypeHinting(null, Memory.FALSE, false));
        Assert.assertTrue(param.checkTypeHinting(null, Memory.TRUE, false));

        Assert.assertTrue(param.checkTypeHinting(null, new LongMemory(0), false));
        Assert.assertTrue(param.checkTypeHinting(null, new DoubleMemory(0), false));
    }

    @Test
    public void testCoreInt(){
        ParameterEntity param = new ParameterEntity(null);
        param.setType(HintType.INT);

        Assert.assertFalse(param.checkTypeHinting(null, new StringMemory(""), false));
        Assert.assertFalse(param.checkTypeHinting(null, new ObjectMemory(null), false));
        Assert.assertFalse(param.checkTypeHinting(null, new ArrayMemory(), false));
        Assert.assertFalse(param.checkTypeHinting(null, Memory.NULL, false));
        Assert.assertFalse(param.checkTypeHinting(null, Memory.FALSE, false));
        Assert.assertFalse(param.checkTypeHinting(null, Memory.TRUE, false));

        Assert.assertTrue(param.checkTypeHinting(null, new LongMemory(0), false));
        Assert.assertFalse(param.checkTypeHinting(null, new DoubleMemory(0), false));
    }

    @Test
    public void testCoreDouble(){
        ParameterEntity param = new ParameterEntity(null);
        param.setType(HintType.DOUBLE);

        Assert.assertFalse(param.checkTypeHinting(null, new StringMemory(""), false));
        Assert.assertFalse(param.checkTypeHinting(null, new ObjectMemory(null), false));
        Assert.assertFalse(param.checkTypeHinting(null, new ArrayMemory(), false));
        Assert.assertFalse(param.checkTypeHinting(null, Memory.NULL, false));
        Assert.assertFalse(param.checkTypeHinting(null, Memory.FALSE, false));
        Assert.assertFalse(param.checkTypeHinting(null, Memory.TRUE, false));

        Assert.assertFalse(param.checkTypeHinting(null, new LongMemory(0), false));
        Assert.assertTrue(param.checkTypeHinting(null, new DoubleMemory(0), false));
    }

    @Test
    public void testCoreString(){
        ParameterEntity param = new ParameterEntity(null);
        param.setType(HintType.STRING);

        Assert.assertTrue(param.checkTypeHinting(null, new StringMemory(""), false));
        Assert.assertFalse(param.checkTypeHinting(null, new ObjectMemory(null), false));
        Assert.assertFalse(param.checkTypeHinting(null, new ArrayMemory(), false));
        Assert.assertFalse(param.checkTypeHinting(null, Memory.NULL, false));
        Assert.assertFalse(param.checkTypeHinting(null, Memory.FALSE, false));
        Assert.assertFalse(param.checkTypeHinting(null, Memory.TRUE, false));

        Assert.assertFalse(param.checkTypeHinting(null, new LongMemory(0), false));
        Assert.assertFalse(param.checkTypeHinting(null, new DoubleMemory(0), false));
    }

    @Test
    public void testCoreBool(){
        ParameterEntity param = new ParameterEntity(null);
        param.setType(HintType.BOOLEAN);

        Assert.assertFalse(param.checkTypeHinting(null, new StringMemory(""), false));
        Assert.assertFalse(param.checkTypeHinting(null, new ObjectMemory(null), false));
        Assert.assertFalse(param.checkTypeHinting(null, new ArrayMemory(), false));
        Assert.assertFalse(param.checkTypeHinting(null, Memory.NULL, false));
        Assert.assertTrue(param.checkTypeHinting(null, Memory.FALSE, false));
        Assert.assertTrue(param.checkTypeHinting(null, Memory.TRUE, false));

        Assert.assertFalse(param.checkTypeHinting(null, new LongMemory(0), false));
        Assert.assertFalse(param.checkTypeHinting(null, new DoubleMemory(0), false));
    }
}
