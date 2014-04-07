package org.develnext.jphp.core.syntax;

public class VariableStats {
    protected boolean passed; // variable is passed to call func/method, e.g: call($var)
    protected boolean mutable; // variable can change its value, e.g: $var = ..
    protected boolean arrayAccess; // variable is used as getting element of array, e.g: $var[key]
    protected boolean reference; // variable is used with references, e.g: $var =& $var2;
    protected boolean unstable; // variable cannot store constant values, option for optimization
    protected boolean used;

    public VariableStats() {
    }

    public boolean isMutable() {
        return mutable;
    }

    public boolean isPassed() {
        return passed;
    }

    public boolean isArrayAccess() {
        return arrayAccess;
    }

    public boolean isReference() {
        return reference;
    }

    public boolean isUnstable() {
        return unstable;
    }

    public boolean isUsed() {
        return used;
    }

    public VariableStats setMutable(boolean mutable) {
        this.mutable = mutable;
        return this;
    }

    public VariableStats setPassed(boolean passed) {
        this.passed = passed;
        return this;
    }

    public VariableStats setArrayAccess(boolean arrayAccess) {
        this.arrayAccess = arrayAccess;
        return this;
    }

    public VariableStats setReference(boolean reference) {
        this.reference = reference;
        return this;
    }

    public VariableStats setUnstable(boolean unstable) {
        this.unstable = unstable;
        return this;
    }

    public VariableStats setUsed(boolean used) {
        this.used = used;
        return this;
    }

    public boolean isUnused() {
        return !passed && !arrayAccess && !reference && !unstable && !mutable && !used;
    }
}
