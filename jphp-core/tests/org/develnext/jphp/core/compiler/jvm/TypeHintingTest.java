package org.develnext.jphp.core.compiler.jvm;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import php.runtime.common.HintType;
import php.runtime.memory.*;
import php.runtime.Memory;
import php.runtime.reflection.ParameterEntity;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TypeHintingTest extends JvmCompilerCase {

    @Test
    public void testArray() {
        check("type_hinting/array.php", true);
    }

    @Test
    public void testArrayRef() {
        check("type_hinting/array_ref.php", true);
    }

    @Test
    public void testArrayDefault() {
        check("type_hinting/array_default.php", true);
    }

    @Test
    public void testCallable() {
        check("type_hinting/callable.php", true);
    }

    @Test
    public void testCallableRef() {
        check("type_hinting/callable_ref.php", true);
    }

    @Test
    public void testObject() {
        check("type_hinting/object.php", true);
    }

    @Test
    public void testObjectDefNull() {
        check("type_hinting/object_def_null.php", true);
    }

    @Test
    public void testVoid() {
        check("type_hinting/void.php", true);
        check("type_hinting/void2.php", true);
        check("type_hinting/void3.php", true);
    }

    @Test
    public void testObjectHint() {
        check("type_hinting/object_hint.php", true);
    }

    @Test
    public void testNumberHint() {
        check("type_hinting/number_hint.php", true);
    }

    @Test
    public void testStrictTypes() {
        check("type_hinting/strict_types.php", true);
    }

    @Test
    public void testNullable() {
        check("type_hinting/nullable.php", true);
    }

    @Test
    public void testSelf() {
        check("type_hinting/self.php");
    }

    /*@Test
    public void testNumber(){
        check("type_hinting/number.php", true);
    }*/

    @Test
    public void testCoreInt() {
        ParameterEntity param = new ParameterEntity(null);
        param.setType(HintType.INT);

        Assert.assertFalse(param.checkTypeHinting(null, new StringMemory("")));
        Assert.assertFalse(param.checkTypeHinting(null, new ObjectMemory(null)));
        Assert.assertFalse(param.checkTypeHinting(null, new ArrayMemory()));
        Assert.assertFalse(param.checkTypeHinting(null, Memory.NULL));
        Assert.assertFalse(param.checkTypeHinting(null, Memory.FALSE));
        Assert.assertFalse(param.checkTypeHinting(null, Memory.TRUE));

        Assert.assertTrue(param.checkTypeHinting(null, new LongMemory(0)));
        Assert.assertFalse(param.checkTypeHinting(null, new DoubleMemory(0)));
    }

    @Test
    public void testCoreDouble() {
        ParameterEntity param = new ParameterEntity(null);
        param.setType(HintType.DOUBLE);

        Assert.assertFalse(param.checkTypeHinting(null, new StringMemory("")));
        Assert.assertFalse(param.checkTypeHinting(null, new ObjectMemory(null)));
        Assert.assertFalse(param.checkTypeHinting(null, new ArrayMemory()));
        Assert.assertFalse(param.checkTypeHinting(null, Memory.NULL));
        Assert.assertFalse(param.checkTypeHinting(null, Memory.FALSE));
        Assert.assertFalse(param.checkTypeHinting(null, Memory.TRUE));

        Assert.assertFalse(param.checkTypeHinting(null, new LongMemory(0)));
        Assert.assertTrue(param.checkTypeHinting(null, new DoubleMemory(0)));
    }

    @Test
    public void testCoreString() {
        ParameterEntity param = new ParameterEntity(null);
        param.setType(HintType.STRING);

        Assert.assertTrue(param.checkTypeHinting(null, new StringMemory("")));
        Assert.assertFalse(param.checkTypeHinting(null, new ObjectMemory(null)));
        Assert.assertFalse(param.checkTypeHinting(null, new ArrayMemory()));
        Assert.assertFalse(param.checkTypeHinting(null, Memory.NULL));
        Assert.assertFalse(param.checkTypeHinting(null, Memory.FALSE));
        Assert.assertFalse(param.checkTypeHinting(null, Memory.TRUE));

        Assert.assertFalse(param.checkTypeHinting(null, new LongMemory(0)));
        Assert.assertFalse(param.checkTypeHinting(null, new DoubleMemory(0)));
    }

    @Test
    public void testCoreBool() {
        ParameterEntity param = new ParameterEntity(null);
        param.setType(HintType.BOOLEAN);

        Assert.assertFalse(param.checkTypeHinting(null, new StringMemory("")));
        Assert.assertFalse(param.checkTypeHinting(null, new ObjectMemory(null)));
        Assert.assertFalse(param.checkTypeHinting(null, new ArrayMemory()));
        Assert.assertFalse(param.checkTypeHinting(null, Memory.NULL));
        Assert.assertTrue(param.checkTypeHinting(null, Memory.FALSE));
        Assert.assertTrue(param.checkTypeHinting(null, Memory.TRUE));

        Assert.assertFalse(param.checkTypeHinting(null, new LongMemory(0)));
        Assert.assertFalse(param.checkTypeHinting(null, new DoubleMemory(0)));
    }
}
