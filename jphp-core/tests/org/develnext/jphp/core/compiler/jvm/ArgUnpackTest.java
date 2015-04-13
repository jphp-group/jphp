package org.develnext.jphp.core.compiler.jvm;

import org.develnext.jphp.zend.ext.ZendExtension;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import php.runtime.env.CompileScope;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ArgUnpackTest extends JvmCompilerCase {

    @Override
    protected CompileScope newScope() {
        CompileScope scope = super.newScope();
        scope.registerExtension(new ZendExtension());
        return scope;
    }

    @Test
    public void testBasic() {
        check("arg_unpack/basic.php");
    }

    @Test
    public void testByRef() {
        check("arg_unpack/by_ref.php");
    }

    @Test
    public void testDynamic() {
        check("arg_unpack/dynamic.php");
    }

    @Test
    public void testInvalidType() {
        check("arg_unpack/invalid_type.php");
    }

    @Test
    public void testManyArgs() {
        check("arg_unpack/many_args.php");
    }

    @Test
    public void testMethod() {
        check("arg_unpack/method.php");
    }

    @Test
    public void testNew() {
        check("arg_unpack/new.php");
    }

    @Test
    public void testPositionalArgAfterUnpackError() {
        check("arg_unpack/positional_arg_after_unpack_error.php", true);
    }

    @Test
    public void testTraversableThrowingException() {
        check("arg_unpack/traversable_throwing_exception.php", true);
    }
}
