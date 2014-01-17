package php.runtime.ext;

import ru.regenix.jphp.compiler.CompileScope;
import ru.regenix.jphp.compiler.common.Extension;
import ru.regenix.jphp.compiler.common.compile.CompileConstant;
import ru.regenix.jphp.exceptions.support.ErrorType;
import php.runtime.ext.core.*;

public class CoreExtension extends Extension {
    @Override
    public String getName() {
        return "Core";
    }

    @Override
    public String getVersion() {
        return "~";
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerConstants(new LangConstants());
        registerFunctions(new LangFunctions());
        registerConstants(new InfoConstants());
        registerFunctions(new InfoFunctions());

        registerConstants(new MathConstants());
        registerFunctions(new MathFunctions());
        registerConstants(new StringConstants());
        registerFunctions(new StringFunctions());
        registerConstants(new ArrayConstants());
        registerFunctions(new ArrayFunctions());
        registerFunctions(new OutputFunctions());

        // T_ERROR
        for (ErrorType el : ErrorType.values())
            constants.put(el.name(), new CompileConstant(el.name(), el.value));
    }
}
