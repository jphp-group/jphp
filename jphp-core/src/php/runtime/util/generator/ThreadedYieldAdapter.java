/**
 * A "yield return" implementation for Java
 * By Jim Blackler (jimblackler@gmail.com)
 *
 * http://jimblackler.net/blog/?p=61
 * http://svn.jimblackler.net/jimblackler/trunk/IdeaProjects/YieldAdapter/
 */
package php.runtime.util.generator;

import java.util.NoSuchElementException;
import java.util.concurrent.SynchronousQueue;

/**
 * A class to convert methods that implement the Collector<> class into a standard Iterable<>, using
 * a new thread created for the collection process, and a SynchronousQueue<> object.
 */
public class ThreadedYieldAdapter<T> implements YieldAdapter<T> {

    /**
     * Message structure to pass values between threads.
     */
    class Message {

    }

    abstract private class StopMessage extends Message {

    }

    private class EndMessage extends StopMessage {

    }

    private class AbortedMessage extends StopMessage {

    }

    private static class VarContainer<T> {
        T var;

        public T getVar() {
            return var;
        }

        public void setVar(T var) {
            this.var = var;
        }
    }

    /**
     * The vehicle to pass the actual values.
     */
    class ValueMessage extends Message {

        ValueMessage(T value) {
            this.value = value;
        }

        final T value;
    }



    /**
     * Convert a method that implements the Collector<> class with a standard Iterable<>. This means
     * that the collecting method can use complex recursive logic, but still allows the calling code
     * to handle the results with a standard iterator. Results are returned immediately and do not
     * incur overhead of being stored in a list. Calculation overhead is only performed for the
     * results that are requested through the iterator.
     *
     * This is implemented using a new thread created for the collection process, and a
     * SynchronousQueue<> object.
     */
    public YieldAdapterIterable<T> adapt(final Collector<T> client) {

        return new YieldAdapterIterable<T>() {
            public YieldAdapterIterator<T> iterator() {

                final SynchronousQueue<Message> synchronousQueue = new SynchronousQueue<Message>();

                // Mechanism to ensure both threads don't run at the same time
                final SynchronousQueue<Object> returnQueue = new SynchronousQueue<Object>();

                final VarContainer<Thread> collectThread = new VarContainer<Thread>();

                final YieldAdapterIterator<T> iterator = new YieldAdapterIterator<T>() {
                    private Message messageWaiting = null;

                    public boolean hasNext() {

                        readNextMessage();
                        return !StopMessage.class.isAssignableFrom(messageWaiting.getClass());
                        // instanceof cannot be used because of generics restriction
                    }

                    public T next() {
                        readNextMessage();

                        if (StopMessage.class.isAssignableFrom(messageWaiting.getClass())) {
                            // instanceof cannot be used because of generics restriction
                            throw new NoSuchElementException();
                        }

                        final T value = currentValue = ((ValueMessage) messageWaiting).value;
                        messageWaiting = null; // for next time
                        return value;
                    }

                    private void readNextMessage() {
                        if (messageWaiting == null) { // do not run if value waiting to be put
                            try {
                              returnQueue.put(new Object()); // allow other thread to gather result
                              messageWaiting = synchronousQueue.take();

                            } catch (final InterruptedException e) {
                                messageWaiting = new EndMessage();
                            }
                        }
                    }

                    public void remove() {
                        throw new UnsupportedOperationException("Generators don't support remove()");
                    }

                    @Override
                    /**
                     * Iterator's finalize() can be used to tell when it is out of scope, and the
                     * collecting thread can be terminated.
                     */
                    protected void finalize() throws Throwable {
                        close();
                        super.finalize();
                    }

                    /**
                     * This can be manually called by the calling code to force release of
                     * resources at the earliest opportunity.
                     */
                    @Override
                    public void close() {
                        collectThread.getVar().interrupt();
                    }
                };

                // This thread is where the collecting logic is executed.
                collectThread.setVar(new Thread() {
                    @Override
                    public void run() {

                        // Important .. handling thread (main thread) gets to run first.
                        // This is because the collecting process should be run on demand in response to
                        // iterator access. Each result should be dealt with by the handling process before
                        // the collecting process is able to modify any resources that may be requred by
                        // results.

                        try {
                            returnQueue.take();
                        } catch (final InterruptedException e) {
                            //throw new RuntimeException("Error with yield adapter", e);
                            return;
                        }
                        try {
                            try {

                                client.collect(new ResultHandler<T>() {
                                    public T handleResult(final T value)
                                            throws CollectionAbortedException {
                                        try {
                                            T oldValue = iterator.getCurrentValue();
                                            synchronousQueue.put(new ValueMessage(value));
                                            returnQueue.take();  // wait for permission to continue
                                            return oldValue;
                                        } catch (final InterruptedException e) {
                                            // this thread has been aborted
                                            throw new CollectionAbortedException(e);
                                        }
                                    }
                                });

                                synchronousQueue.put(new EndMessage());
                                // Signal no more results to come

                            } catch (final CollectionAbortedException collectionAborted) {
                                if (!(collectionAborted
                                        .getCause() instanceof InterruptedException)) {
                                    // Collect was aborted by client
                                    // This is not sent on thread abort as there is nothing waiting
                                    // to receive it, and the thread will block.
                                    synchronousQueue.put(new AbortedMessage());
                                }
                            }

                        } catch (final InterruptedException e) {
                            // Operation was aborted internally (e.g.  iterator out of scope)
                        }
                    }
                });
                collectThread.getVar().setDaemon(true);
                collectThread.getVar().start();

                return iterator;
            }
        };
    }
}

