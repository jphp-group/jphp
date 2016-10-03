package php.runtime.common;

abstract public class CallbackW<RETURN, PARAM1, PARAM2> {
    abstract public RETURN call(PARAM1 param1, PARAM2 param2);
}
