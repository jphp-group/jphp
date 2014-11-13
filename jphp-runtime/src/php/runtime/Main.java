package php.runtime;

import php.runtime.memory.LongMemory;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Main {

    public static void main(String[] args) {

        Generator<Memory> generator = new Generator<Memory>(){

            @Override
            protected void run() throws InterruptedException {
                for(int i = 0; i < 1000000; i++) {
                    yield(LongMemory.valueOf(i));
                }
            }
        };

        long t = System.currentTimeMillis();
        for(Memory it : generator) {

        }
        System.out.println(System.currentTimeMillis() - t);
    }

    public static abstract class Generator<T> implements Iterable<T> {

        private class Condition {
            private boolean isSet;
            public synchronized void set() {
                isSet = true;
                notify();
            }
            public synchronized void await() throws InterruptedException {
                try {
                    if (isSet)
                        return;
                    wait();
                } finally {
                    isSet = false;
                }
            }
        }

        static ThreadGroup THREAD_GROUP;

        Thread producer;
        private boolean hasFinished;
        private final Condition itemAvailableOrHasFinished = new Condition();
        private final Condition itemRequested = new Condition();
        private T nextItem;
        private boolean nextItemAvailable;

        @Override
        public Iterator<T> iterator() {
            return new Iterator<T>() {
                @Override
                public boolean hasNext() {
                    waitForNext();
                    return !hasFinished;
                }
                @Override
                public T next() {
                    waitForNext();
                    if (hasFinished)
                        throw new NoSuchElementException();
                    nextItemAvailable = false;
                    return nextItem;
                }
                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
                private void waitForNext() {
                    if (nextItemAvailable || hasFinished)
                        return;
                    if (producer == null)
                        startProducer();
                    itemRequested.set();
                    try {
                        itemAvailableOrHasFinished.await();
                    } catch (InterruptedException e) {
                        hasFinished = true;
                    }
                }
            };
        }

        protected abstract void run() throws InterruptedException;

        protected void yield(T element) throws InterruptedException {
            nextItem = element;
            nextItemAvailable = true;
            itemAvailableOrHasFinished.set();
            itemRequested.await();
        }

        private void startProducer() {
            assert producer == null;
            if (THREAD_GROUP == null)
                THREAD_GROUP = new ThreadGroup("generators");
            producer = new Thread(THREAD_GROUP, new Runnable() {
                @Override
                public void run() {
                    try {
                        itemRequested.await();
                        Generator.this.run();
                    } catch (InterruptedException e) {
                        // No need to do anything here; Remaining steps in run()
                        // will cleanly shut down the thread.
                    }
                    hasFinished = true;
                    itemAvailableOrHasFinished.set();
                }
            });
            producer.setDaemon(true);
            producer.start();
        }

        @Override
        protected void finalize() throws Throwable {
            producer.interrupt();
            producer.join();
            super.finalize();
        }
    }
}
