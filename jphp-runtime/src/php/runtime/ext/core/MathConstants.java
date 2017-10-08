package php.runtime.ext.core;

import php.runtime.Memory;
import php.runtime.ext.support.compile.ConstantsContainer;
import php.runtime.memory.DoubleMemory;
import php.runtime.memory.LongMemory;

final public class MathConstants extends ConstantsContainer {
    public Memory M_PI = DoubleMemory.valueOf(Math.PI);
    public Memory M_E = DoubleMemory.valueOf(Math.E);
    public Memory M_LOG2E = DoubleMemory.valueOf(1.4426950408889634074);
    public Memory M_LOG10E = DoubleMemory.valueOf(0.43429448190325182765);
    public Memory M_LN2 = DoubleMemory.valueOf(0.69314718055994530942);
    public Memory M_LN10 = DoubleMemory.valueOf(2.30258509299404568402);
    public Memory M_PI_2 = DoubleMemory.valueOf(1.57079632679489661923);
    public Memory M_PI_4 = DoubleMemory.valueOf(0.78539816339744830962);
    public Memory M_1_PI = DoubleMemory.valueOf(0.31830988618379067154);
    public Memory M_2_PI = DoubleMemory.valueOf(0.63661977236758134308);
    public Memory M_SQRTPI = DoubleMemory.valueOf(1.77245385090551602729);
    public Memory M_2_SQRTPI = DoubleMemory.valueOf(1.12837916709551257390);
    public Memory M_SQRT2 = DoubleMemory.valueOf(1.41421356237309504880);
    public Memory M_SQRT3 = DoubleMemory.valueOf(1.73205080756887729352);
    public Memory M_SQRT1_2 = DoubleMemory.valueOf(0.70710678118654752440);
    public Memory M_LNPI = DoubleMemory.valueOf(1.14472988584940017414);
    public Memory M_EULER =	DoubleMemory.valueOf(0.57721566490153286061);
    public Memory PHP_ROUND_HALF_UP = LongMemory.valueOf(1);
    public Memory PHP_ROUND_HALF_DOWN = LongMemory.valueOf(2);
    public Memory PHP_ROUND_HALF_EVEN = LongMemory.valueOf(3);
    public Memory PHP_ROUND_HALF_ODD = LongMemory.valueOf(4);
    public Memory NAN = DoubleMemory.valueOf(Double.NaN);
    public Memory INF = DoubleMemory.valueOf(Double.POSITIVE_INFINITY);
}
