package ru.regenix.jphp.runtime.lang.support;

import ru.regenix.jphp.runtime.memory.support.Memory;

public interface IArithmeticObject {
    Memory __negative();
    Memory __plus(Memory value);
    Memory __minus(Memory value);
    Memory __mul(Memory value);
    Memory __div(Memory value);
    Memory __mod(Memory value);
}
