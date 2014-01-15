package php.runtime.ext;

import ru.regenix.jphp.compiler.CompileScope;
import ru.regenix.jphp.compiler.common.Extension;
import php.runtime.ext.ctype.CTypeFunctions;

public class CTypeExtension extends Extension {
    @Override
    public String getName() {
        return "ctype";
    }

    @Override
    public String getVersion() {
        return "~";
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerFunctions(new CTypeFunctions());
    }
}
