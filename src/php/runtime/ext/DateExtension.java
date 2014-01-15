package php.runtime.ext;

import ru.regenix.jphp.compiler.CompileScope;
import ru.regenix.jphp.compiler.common.Extension;
import php.runtime.ext.date.DateFunctions;

public class DateExtension extends Extension {
    @Override
    public String getName() {
        return "date";
    }

    @Override
    public String getVersion() {
        return "~";
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerFunctions(new DateFunctions());
    }
}
