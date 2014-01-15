package php.runtime.ext.core;

import ru.regenix.jphp.compiler.common.compile.FunctionsContainer;
import php.runtime.Memory;
import php.runtime.env.Environment;

public class OutputFunctions extends FunctionsContainer {

    public static Memory print(Environment environment, Memory memory){
        environment.echo(memory.toString());
        return Memory.CONST_INT_1;
    }
}
