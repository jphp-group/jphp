/**
 * A "yield return" implementation for Java
 * By Jim Blackler (jimblackler@gmail.com)
 *
 * http://jimblackler.net/blog/?p=61
 * http://svn.jimblackler.net/jimblackler/trunk/IdeaProjects/YieldAdapter/
 */
package php.runtime.util.generator;

/**
 * An exception class that can be thrown by collectors or results handlers in order to abort or
 * signal abortion of the collecting process, for any reason.
 */
public class CollectionAbortedException extends Exception {

    public CollectionAbortedException() {
    }

    public CollectionAbortedException(String message) {
        super(message);
    }

    public CollectionAbortedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CollectionAbortedException(Throwable cause) {
        super(cause);
    }
}
