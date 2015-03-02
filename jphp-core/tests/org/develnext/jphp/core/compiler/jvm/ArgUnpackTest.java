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
}
