package org.develnext.jphp.core.compiler.jvm;


import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import php.runtime.Memory;
import php.runtime.invoke.Invoker;
import php.runtime.memory.DoubleMemory;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LambdaTest extends JvmCompilerCase {

    @Test
    public void testSimple() {
        assertEquals("success", includeResource("lambdas/lambda_001.php").toString());
    }

    @Test
    public void testUses() {
        assertEquals("success", includeResource("lambdas/lambda_uses_001.php").toString());
        assertEquals("success", includeResource("lambdas/lambda_uses_002.php").toString());
    }

    @Test
    public void testThis() {
        assertEquals("success", includeResource("lambdas/lambda_this_001.php").toString());
    }

    @Test
    public void testStaticVars() {
        assertEquals("success", includeResource("lambdas/lambda_static_vars_001.php").toString());
    }

    @Test
    public void testTypeHinting() {
        assertEquals("2", Invoker.create(environment, run("fn(): int => 2.0;")).callAny().toString());
        assertEquals("2", Invoker.create(environment, run("fn(int $x) => $x;")).callAny(DoubleMemory.valueOf(2.0)).toString());
    }

    @Test
    public void testSyntax() {
        assertEquals("success", run("fn() => { return 'success'; }()").toString());
        assertEquals("success", run("fn => { return 'success'; }()").toString());
        assertEquals("success", run("fn => { echo '1'; return 'success'; }()").toString());
        assertEquals("3", run("fn($x, $y = 2) => { return $x + $y; }(1)").toString());
        assertEquals("6", run("fn($x, $y = [1, 2, 3]) => { return $x + $y[0] + $y[1] + $y[2]; }(0)").toString());
    }

    @Test
    public void testBug261() {
        assertEquals("success", includeResource("lambdas/bug261.php").toString());
    }
}
