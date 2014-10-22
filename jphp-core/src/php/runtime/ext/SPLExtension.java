package php.runtime.ext;

import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;
import php.runtime.ext.spl.SPLFunctions;
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
    public String getVersion() {
        return "~";
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerFunctions(new SPLFunctions());

        registerNativeClass(scope, Countable.class);
        registerNativeClass(scope, OuterIterator.class);
        registerNativeClass(scope, RecursiveIterator.class);
        registerNativeClass(scope, SeekableIterator.class);
        registerNativeClass(scope, MultipleIterator.class);
        registerNativeClass(scope, IteratorIterator.class);
        registerNativeClass(scope, EmptyIterator.class);
        registerNativeClass(scope, FilterIterator.class);
        registerNativeClass(scope, InfiniteIterator.class);

        registerNativeClass(scope, LogicException.class);
        registerNativeClass(scope, BadFunctionCallException.class);
        registerNativeClass(scope, BadMethodCallException.class);
        registerNativeClass(scope, DomainException.class);
        registerNativeClass(scope, InvalidArgumentException.class);
        registerNativeClass(scope, LengthException.class);

        registerNativeClass(scope, RuntimeException.class);
        registerNativeClass(scope, OutOfBoundsException.class);
        registerNativeClass(scope, OutOfRangeException.class);
        registerNativeClass(scope, OverflowException.class);
        registerNativeClass(scope, RangeException.class);
        registerNativeClass(scope, UnderflowException.class);
        registerNativeClass(scope, UnexpectedValueException.class);
    }
}
