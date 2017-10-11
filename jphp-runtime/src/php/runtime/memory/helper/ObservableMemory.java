package php.runtime.memory.helper;

import php.runtime.Memory;
import php.runtime.memory.ReferenceMemory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ObservableMemory extends ReferenceMemory {
    public interface Observer {
        void update(ObservableMemory observable, Memory oldValue, Memory newValue);
    }

    private final Vector<Observer> observers;
    private final Map<Memory, Observer> observerMap;

    public ObservableMemory(Memory value) {
        super(value);
        this.observers = new Vector<>();
        this.observerMap = Collections.synchronizedMap(new LinkedHashMap<>());
    }

    public ObservableMemory() {
        this(Memory.NULL);
    }

    @Override
    public Memory setValue(Memory value) {
        Memory oldValue = getValue();

        Memory memory = super.setValue(value);

        if (!value.identical(oldValue)) {
            for (Observer observer : observers) {
                observer.update(this, oldValue, value);
            }
        }

        return memory;
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void addObserver(Observer observer, Memory key) {
        observers.add(observer);
        observerMap.put(key, observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public Observer removeObserver(Memory key) {
        Observer observer = observerMap.get(key);

        if (observer != null) {
            observers.remove(observer);
            observerMap.remove(key);

            return observer;
        }

        return null;
    }

    public void clearObservers() {
        observers.clear();
        observerMap.clear();
    }

    public Set<Memory> observerKeys() {
        return observerMap.keySet();
    }

    @Override
    public Memory toImmutable() {
        return this;
    }

    /*@Override
    public Memory toValue() {
        return this;
    }*/
}
