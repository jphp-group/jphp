package org.develnext.jphp.core.compiler.jvm;

import org.develnext.jphp.zend.ext.ZendExtension;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import php.runtime.env.CompileScope;
import php.runtime.env.DieException;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GeneratorsTest extends JvmCompilerCase {

    @Override
    protected CompileScope newScope() {
        CompileScope scope = super.newScope();
        scope.registerExtension(new ZendExtension());
        return scope;
    }

    @Test
    public void testFibonacci() {
        check("generators/fibonacci.php");
    }

    @Test
    public void testFuncGetArgs() {
        check("generators/func_get_args.php");
    }

    @Test
    public void testDynamicCall() {
        check("generators/dynamic_call.php");
    }

    @Test
    public void testAutoIncrementingKeys() {
        check("generators/auto_incrementing_keys.php");
    }

    @Test
    public void testGeneratorClosure() {
        check("generators/generator_closure.php");
    }

    @Test
    public void testGeneratorClosureWithThis() {
        check("generators/generator_closure_with_this.php");
    }

    @Test
    public void testGeneratorStaticMethod() {
        check("generators/generator_static_method.php");
    }

    @Test
    public void testGeneratorInMultipleIterator() {
        check("generators/generator_in_multipleiterator.php");
    }

    @Test
    public void testClone() {
        check("generators/clone.php", true);
    }

    @Test
    public void testGeneratorMethod() {
        check("generators/generator_method.php");
    }

    @Test
    public void testGeneratorMethodByRef() {
        check("generators/generator_method_by_ref.php");
    }

    @Test
    public void testGeneratorReturnsGenerator() {
        check("generators/generator_returns_generator.php");
    }

    @Test
    public void testGeneratorRewind() {
        check("generators/generator_rewind.php");
    }

    @Test
    public void testGeneratorSend() {
        check("generators/generator_send.php");
    }

    @Test
    public void testGeneratorThrowingDuringFunctionCall() {
        check("generators/generator_throwing_during_function_call.php");
    }

    @Test
    public void testGeneratorThrowingException() {
        check("generators/generator_throwing_exception.php");
    }

    @Test
    public void testGeneratorThrowingInForeach() {
        check("generators/generator_throwing_in_foreach.php");
    }

    @Test
    public void testGeneratorWithNonscalarKeys() {
        check("generators/generator_with_nonscalar_keys.php");
    }

    @Test
    public void testIgnoredSendLeak() {
        check("generators/ignored_send_leak.php");
    }

    @Test
    public void testNestedMethodCalls() {
        check("generators/nested_method_calls.php");
    }

    @Test
    public void testSendAfterClose() {
        check("generators/send_after_close.php");
    }

    @Test
    public void testSendReturnsCurrent() {
        check("generators/send_returns_current.php");
    }

    @Test
    public void testThrowAlreadyClosed() {
        check("generators/throw_already_closed.php");
    }

    @Test
    public void testThrowCaught() {
        check("generators/throw_caught.php");
    }

    @Test
    public void testThrowRethrow() {
        check("generators/throw_rethrow.php");
    }

    @Test
    public void testThrowUncaught() {
        check("generators/throw_uncaught.php");
    }

    @Test
    public void testXrange() {
        check("generators/xrange.php");
    }

    @Test
    public void testYield() {
        check("generators/yield_array_key.php");
        check("generators/yield_array_offset_by_ref.php");
        check("generators/yield_by_reference.php");
        check("generators/yield_closure.php");
        check("generators/yield_during_function_call.php");
        check("generators/yield_during_method_call.php");
        check("generators/yield_in_finally.php");
        check("generators/yield_in_parenthesis.php");
        check("generators/yield_ref_function_call_by_reference.php");
        check("generators/yield_without_value.php");
    }

    @Test
    public void testErrors() {
        //check("generators/errors/generator_cannot_return_before_yield_error.php", true);
        //check("generators/errors/generator_cannot_return_error.php", true);
        check("generators/errors/generator_extend_error.php", true);
        check("generators/errors/generator_instantiate_error.php", true);
        check("generators/errors/non_ref_generator_iterated_by_ref_error.php", true);
        check("generators/errors/resume_running_generator_error.php", true);
        check("generators/errors/serialize_unserialize_error.php", true);
        check("generators/errors/yield_const_by_ref_error.php", true);
        check("generators/errors/yield_non_ref_function_call_by_ref_error.php", true);
        check("generators/errors/yield_outside_function_error.php", true);
        //check("generators/errors/yield_in_force_closed_finally_error.php", true);
    }

    @Test
    public void testFinally() {
        check("generators/finally/finally_ran_on_close.php");
        check("generators/finally/return_return.php");
        check("generators/finally/return_yield.php");
        check("generators/finally/run_on_dtor.php");
        check("generators/finally/throw_yield.php");
        check("generators/finally/yield_return.php");
        check("generators/finally/yield_throw.php");
        check("generators/finally/yield_yield.php");
    }

    @Test
    public void testNestedCallsWithDie() {
        try {
            includeResource("generators/nested_calls_with_die.php");
        } catch (DieException e) {
            Assert.assertEquals(e.getMessage(), "Test");
            return;
        }

        Assert.assertTrue(false);
    }

    @Test
    public void testCountError() {
        check("generators/count_error.phpt");
    }

    @Test
    public void testBug262() {
        check("generators/bug262.php");
    }

    @Test
    public void testBug369() {
        check("generators/bug369.phpt");
    }

    @Test
    public void testBug371() {
        check("generators/bug371.phpt");
    }
}
