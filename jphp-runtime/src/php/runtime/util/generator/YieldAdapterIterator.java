/**
 * A "yield return" implementation for Java
 * By Jim Blackler (jimblackler@gmail.com)
 *
 * http://jimblackler.net/blog/?p=61
 * http://svn.jimblackler.net/jimblackler/trunk/IdeaProjects/YieldAdapter/
 */
package php.runtime.util.generator;

import java.io.Closeable;
import java.util.Iterator;

/**
 * A version of a standard Iterator<> used by the yield adapter. The only addition is a close()
 * function to clear resources manually when required.
 */
abstract public class YieldAdapterIterator<T> implements Iterator<T>, Closeable {

    /**
     * Because the Yield Adapter starts a separate thread for duration of the collection, this can
     * be left open if the calling code only reads part of the collection. If the iterator goes out
     * of scope, when it is GCed its finalize() will close the collection thread. However garbage
     * collection is sporadic and the VM will not trigger it simply because there is a lack of
     * available threads. So, if a lot of partial reads are happening, it will be wise to manually
     * close the iterator (which will clear the resources immediately).
     */

    protected Throwable throwable;

    protected T currentValue;

    public T getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(T currentValue) {
        this.currentValue = currentValue;
    }

    public T sendValue(T value) {
        this.currentValue = value;
        return next();
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public void sendThrow(Throwable throwable) {
        this.throwable = throwable;
        next();
    }
}