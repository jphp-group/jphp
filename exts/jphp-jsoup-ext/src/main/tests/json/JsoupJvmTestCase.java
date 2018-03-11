package json;

import org.develnext.jphp.core.compiler.jvm.JvmCompilerCase;
import org.develnext.jphp.ext.jsoup.JsoupExtension;
import php.runtime.env.CompileScope;

public class JsoupJvmTestCase extends JvmCompilerCase {

    @Override
    protected CompileScope newScope() {
        CompileScope scope = super.newScope();
        scope.registerExtension(new JsoupExtension());
        return scope;
    }
}
