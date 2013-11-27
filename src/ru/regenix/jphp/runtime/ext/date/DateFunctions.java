package ru.regenix.jphp.runtime.ext.date;

import ru.regenix.jphp.compiler.common.compile.FunctionsContainer;
import ru.regenix.jphp.runtime.memory.DoubleMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.memory.StringMemory;

public class DateFunctions extends FunctionsContainer {

    public static Memory microtime(boolean getAsFloat){
        double now = System.currentTimeMillis() / 1000.0;
        int s = (int)now;

        return getAsFloat
                ? new DoubleMemory(now)
                : new StringMemory((Math.round((now - s) * 1000) / 1000) + " " + s);
    }

    public static Memory microtime(){
        return microtime(false);
    }
}
