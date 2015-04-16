package php.runtime.memory.support.operation.iterator;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.CriticalException;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.memory.support.operation.GenericMemoryOperation;
import php.runtime.reflection.ParameterEntity;

import java.lang.reflect.Type;
import java.util.Iterator;

public class IterableMemoryOperation extends GenericMemoryOperation<Iterable> {

    public IterableMemoryOperation(Type... genericTypes) {
        super(genericTypes);
        if (genericTypes == null) {
            operations = new MemoryOperation[] { MemoryOperation.get(Memory.class, null) };
        }
    }

    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { Iterable.class };
    }

    @Override
    public Iterable convert(final Environment env, final TraceInfo trace, Memory arg) throws Throwable {
        final ForeachIterator iterator = arg.getNewIterator(env);

        return new Iterable() {
            @Override
            public Iterator iterator() {
                return new Iterator() {
                    protected Boolean hasNext;

                    @Override
                    public boolean hasNext() {
                        if (hasNext == null) {
                            hasNext = iterator.next();
                        }
                        return hasNext;
                    }

                    @Override
                    public Object next() {
                        if (hasNext == null) {
                            next();
                        } else {
                            hasNext = null;
                        }

                        return operations[0].convertNoThrow(env, trace, iterator.getValue());
                    }

                    @Override
                    public void remove() {
                        throw new IllegalStateException("Unsupported remove() method");
                    }
                };
            }
        };
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Iterable arg) throws Throwable {
        throw new CriticalException("Unsupported operation");
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setType(HintType.TRAVERSABLE);
    }
}
