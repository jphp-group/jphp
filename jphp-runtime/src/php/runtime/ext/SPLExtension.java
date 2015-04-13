package php.runtime.ext;

import php.runtime.env.CompileScope;
import php.runtime.ext.spl.SPLFunctions;
import php.runtime.ext.support.Extension;
import php.runtime.lang.spl.Countable;
import php.runtime.lang.spl.exception.*;
import php.runtime.lang.spl.exception.RuntimeException;
import php.runtime.lang.spl.iterator.*;

public class SPLExtension extends Extension {
    @Override
    public String getName() {
        return "SPL";
    }

    @Override
    public Status getStatus() {
        return Status.STABLE;
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerFunctions(new SPLFunctions());

        registerClass(scope, Countable.class);
        registerClass(scope, OuterIterator.class);
        registerClass(scope, RecursiveIterator.class);
        registerClass(scope, SeekableIterator.class);
        registerClass(scope, MultipleIterator.class);
        registerClass(scope, IteratorIterator.class);
        registerClass(scope, EmptyIterator.class);
        registerClass(scope, FilterIterator.class);
        registerClass(scope, InfiniteIterator.class);

        registerClass(scope, LogicException.class);
        registerClass(scope, BadFunctionCallException.class);
        registerClass(scope, BadMethodCallException.class);
        registerClass(scope, DomainException.class);
        registerClass(scope, InvalidArgumentException.class);
        registerClass(scope, LengthException.class);

        registerClass(scope, RuntimeException.class);
        registerClass(scope, OutOfBoundsException.class);
        registerClass(scope, OutOfRangeException.class);
        registerClass(scope, OverflowException.class);
        registerClass(scope, RangeException.class);
        registerClass(scope, UnderflowException.class);
        registerClass(scope, UnexpectedValueException.class);
    }
}
