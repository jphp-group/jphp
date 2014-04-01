package org.develnext.jphp.zend.ext.standard;

import php.runtime.Memory;
import php.runtime.ext.support.compile.FunctionsContainer;
import php.runtime.memory.DoubleMemory;
import php.runtime.memory.StringMemory;

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
