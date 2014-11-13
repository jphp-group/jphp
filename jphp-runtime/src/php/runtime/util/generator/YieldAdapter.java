/**
 * A "yield return" implementation for Java
 * By Jim Blackler (jimblackler@gmail.com)
 *
 * http://jimblackler.net/blog/?p=61
 * http://svn.jimblackler.net/jimblackler/trunk/IdeaProjects/YieldAdapter/
 */
package php.runtime.util.generator;

/**
 * A class to convert methods that implement the Collector<> class into a standard Iterable<>.
 */
public interface YieldAdapter<T> {

    YieldAdapterIterable<T> adapt(Collector<T> client);
}
