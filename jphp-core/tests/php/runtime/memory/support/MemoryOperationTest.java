package php.runtime.memory.support;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.LongMemory;
import php.runtime.memory.StringMemory;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MemoryOperationTest {

    private static class TestClass {
        protected Memory memory;

        private TestClass(Memory memory) {
            this.memory = memory;
        }

        public Memory getMemory() {
            return memory;
        }
    }

    @Test
    public void testGlobal() throws Throwable {
        assertNull(MemoryOperation.get(TestClass.class, null));

        MemoryOperation.register(new MemoryOperation<TestClass>() {
            @Override
            public Class<?>[] getOperationClasses() {
                return new Class<?>[]{TestClass.class};
            }

            @Override
            public TestClass convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
                return new TestClass(arg);
            }

            @Override
            public Memory unconvert(Environment env, TraceInfo trace, TestClass arg) throws Throwable {
                return arg.getMemory();
            }
        });

        assertNotNull(MemoryOperation.get(TestClass.class, null));

        MemoryOperation op = MemoryOperation.get(TestClass.class, null);
        assertEquals(Memory.NULL, ((TestClass)op.convert(null, null, Memory.NULL)).getMemory());

        assertEquals(Memory.NULL, op.unconvert(null, null, new TestClass(Memory.NULL)));
    }

    @Test
    public void testString() throws Throwable {
        MemoryOperation operation = MemoryOperation.get(String.class, null);

        assertEquals("foobar", operation.convert(null, null, StringMemory.valueOf("foobar")));
        assertEquals("foobar", operation.unconvert(null, null, "foobar").toString());
        assertTrue(operation.unconvert(null, null, "foobar") instanceof StringMemory);
    }

    @Test
    public void testInteger() throws Throwable {
        MemoryOperation operation = MemoryOperation.get(Integer.class, null);

        assertEquals(123, operation.convert(null, null, LongMemory.valueOf(123)));
        assertEquals(123, operation.unconvert(null, null, 123).toInteger());
        assertTrue(operation.unconvert(null, null, 123) instanceof LongMemory);
    }
}