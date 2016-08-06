package php.runtime.common;

abstract public class Callback<RETURN, PARAM> {
    abstract public RETURN call(PARAM param);
}
