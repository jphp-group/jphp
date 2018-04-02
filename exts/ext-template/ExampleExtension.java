package $package$;

import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;

public class $className$ extends Extension {
    public static final String NS = "$ns$";

    @Override
    public Status getStatus() {
        return Status.EXPERIMENTAL;
    }

    @Override
    public void onRegister(CompileScope scope) {
        // register classes ...
    }
}
