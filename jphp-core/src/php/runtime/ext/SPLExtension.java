package php.runtime.ext;

import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;
import php.runtime.ext.spl.SPLFunctions;
import php.runtime.lang.spl.Countable;
import php.runtime.lang.spl.iterator.OuterIterator;
import php.runtime.lang.spl.iterator.RecursiveIterator;
import php.runtime.lang.spl.iterator.SeekableIterator;

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
    }
}
