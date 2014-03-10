package php.runtime.common;

abstract public class Callback<T, K> {
    abstract public T call(K param);
}
