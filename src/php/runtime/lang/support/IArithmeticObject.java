package php.runtime.lang.support;

import php.runtime.Memory;

public interface IArithmeticObject {
    Memory __negative();
    Memory __plus(Memory value);
    Memory __minus(Memory value);
    Memory __mul(Memory value);
    Memory __div(Memory value);
    Memory __mod(Memory value);
}
