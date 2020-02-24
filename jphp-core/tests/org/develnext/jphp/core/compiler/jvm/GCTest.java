package org.develnext.jphp.core.compiler.jvm;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import php.runtime.Memory;
import php.runtime.exceptions.support.ErrorException;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GCTest extends JvmCompilerCase {

    @Test
    public void testWeakRef(){
        check("gc/weak_ref.phpt");
    }

    @Test
    public void testWeakRefClosure(){
        check("gc/weak_ref_closure.phpt");
    }

    @Test
    public void testWeakRefConstructor(){
        check("gc/weak_ref_errors.phpt", true);
    }
}
