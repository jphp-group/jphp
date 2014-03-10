package php.runtime.ext;

import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;
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
