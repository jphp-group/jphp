/**
 * A "yield return" implementation for Java
 * By Jim Blackler (jimblackler@gmail.com)
 *
 * http://jimblackler.net/blog/?p=61
 * http://svn.jimblackler.net/jimblackler/trunk/IdeaProjects/YieldAdapter/
 */
package php.runtime.util.generator;

/**
 * Defines objects that handle results from a Collector<>, with a function called immediately as
 * each value is gathered.
 */
public interface ResultHandler<T> {

    /**
     * This method is called by collectors whenever a result is collected.
     *
     * @param value The collected result
     * @throws CollectionAbortedException The client code requests that the collection is aborted
     */
    T handleResult(T value) throws CollectionAbortedException;
}