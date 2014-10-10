/**
 * A "yield return" implementation for Java
 * By Jim Blackler (jimblackler@gmail.com)
 *
 * http://jimblackler.net/blog/?p=61
 * http://svn.jimblackler.net/jimblackler/trunk/IdeaProjects/YieldAdapter/
 */
package php.runtime.util.generator;

/**
 * Defines a class that collects values of type T and submits each value to a ResultHandler<>
 * object immediately on collection.
 */
public interface Collector<T> {

    /**
     * Perform the collection operation.
     *
     * @param handler The processor object to return results to.
     * @throws CollectionAbortedException The collection operation was aborted part way through.
     */
    void collect(ResultHandler<T> handler) throws CollectionAbortedException;

    void setIterator(YieldAdapterIterator iterator);
}