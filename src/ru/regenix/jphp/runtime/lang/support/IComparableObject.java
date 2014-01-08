package ru.regenix.jphp.runtime.lang.support;

import ru.regenix.jphp.runtime.lang.IObject;

public interface IComparableObject<T extends IObject> {
    boolean __equal(T iObject);
    boolean __identical(T iObject);
    boolean __greater(T iObject);
    boolean __greaterEq(T iObject);
    boolean __smaller(T iObject);
    boolean __smallerEq(T iObject);
}
