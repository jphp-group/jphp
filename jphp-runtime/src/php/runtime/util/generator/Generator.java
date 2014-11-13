package php.runtime.util.generator;

/**
 * This class implements something akin to Python's "Generators".
 *
 * <p>A Generator is a function which returns an iterator. It looks like a normal 
 * function except that it contains yield statements for producing a series a
 * values usable in a for-loop or that can be retrieved one at a time with the
 * next() function. Each "yield" temporarily suspends processing, remembering the
 * location execution state (including local variables and pending try-statements).
 * When the generator resumes, it picks-up where it left-off (in contrast to functions
 * which start fresh on every invocation).</p>
 *
 * @author dom
 */
public abstract class Generator<T> implements Iterable<T> {

    private ResultHandler<T> resultHandler;

    /**
     * Yield a single result. To be called from within your "run" method.
     * 
     * @param t
     */
    public final T yield(final T t) {
        try {
            return resultHandler.handleResult(t);
        } catch (CollectionAbortedException ex) {
            // suppress
            return null;
        }
    }

    public final T yield() {
        return yield(null);
    }

    /**
     * The method that will generate your results. Call yield() in here.
     */
    protected abstract void run(YieldAdapterIterator<T> yieldAdapter);

    @Override
    public final YieldAdapterIterator<T> iterator() {
        final ThreadedYieldAdapter<T> yieldAdapter = new ThreadedYieldAdapter<T>();
        Collector<T> collector = new Collector<T>() {

            protected YieldAdapterIterator iterator;

            @Override
            public void collect(final ResultHandler<T> handler) throws CollectionAbortedException {
                resultHandler = handler;
                run(iterator);
            }

            public void setIterator(YieldAdapterIterator iterator) {
                this.iterator = iterator;
            }
        };

        YieldAdapterIterator<T> iterator = yieldAdapter.adapt(collector).iterator();
        collector.setIterator(iterator);
        return iterator;
    }
}
