package php.runtime.ext;

import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;
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
