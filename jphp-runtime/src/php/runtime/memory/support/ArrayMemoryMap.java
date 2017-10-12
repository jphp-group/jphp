package php.runtime.memory.support;

import php.runtime.Memory;
import php.runtime.common.Pair;
import php.runtime.common.collections.*;
import php.runtime.common.collections.iterators.*;
import php.runtime.common.collections.list.UnmodifiableList;

import java.io.Serializable;
import java.util.*;

public class ArrayMemoryMap extends AbstractMap<Object, Memory>
        implements IterableMap<Object, Memory>, OrderedMap<Object, Memory>, Serializable {
    /**
     * Serialisation version
     */
    private static final long serialVersionUID = 9077238323521161066L;

    protected static final String NO_NEXT_ENTRY = "No next() entry in the iteration";
    protected static final String NO_PREVIOUS_ENTRY = "No previous() entry in the iteration";
    protected static final String REMOVE_INVALID = "remove() can only be called once after next()";
    protected static final String GETKEY_INVALID = "getKey() can only be called after next() and before remove()";
    protected static final String GETVALUE_INVALID = "getValue() can only be called after next() and before remove()";
    protected static final String SETVALUE_INVALID = "setValue() can only be called after next() and before remove()";

    /**
     * The default capacity to use
     */
    protected static final int DEFAULT_CAPACITY = 11;
    /**
     * The default threshold to use
     */
    protected static final int DEFAULT_THRESHOLD = 8;
    /**
     * The default load factor to use
     */
    protected static final float DEFAULT_LOAD_FACTOR = 0.75f;
    /**
     * The maximum capacity allowed
     */
    protected static final int MAXIMUM_CAPACITY = 1 << 30;
    /**
     * An object for masking null
     */
    protected static final Object NULL = new Object();

    /**
     * Load factor, normally 0.75
     */
    protected transient float loadFactor;
    /**
     * The size of the map
     */
    protected transient int size;
    /**
     * Map entries
     */
    protected transient ArrayMapEntryMemory[] data;
    /**
     * Size at which to rehash
     */
    protected transient int threshold;
    /**
     * Modification count for iterators
     */
    protected transient int modCount;
    /**
     * Entry set
     */
    protected transient EntrySet entrySet;
    /**
     * Key set
     */
    protected transient KeySet keySet;
    /**
     * Values
     */
    protected transient Values values;

    protected transient ArrayMapEntryMemory header;

    /**
     * Constructor only used in deserialization, do not use otherwise.
     */
    public ArrayMemoryMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR, DEFAULT_THRESHOLD);
    }

    /**
     * Constructor which performs no validation on the passed in parameters.
     *
     * @param initialCapacity the initial capacity, must be a power of two
     * @param loadFactor      the load factor, must be &gt; 0.0f and generally &lt; 1.0f
     * @param threshold       the threshold, must be sensible
     */
    protected ArrayMemoryMap(int initialCapacity, float loadFactor, int threshold) {
        super();
        this.loadFactor = loadFactor;
        this.data = new ArrayMapEntryMemory[initialCapacity];
        this.threshold = threshold;
        init();
    }

    /**
     * Constructs a new, empty map with the specified initial capacity and
     * default load factor.
     *
     * @param initialCapacity the initial capacity
     * @throws IllegalArgumentException if the initial capacity is less than one
     */
    public ArrayMemoryMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Constructs a new, empty map with the specified initial capacity and
     * load factor.
     *
     * @param initialCapacity the initial capacity
     * @param loadFactor      the load factor
     * @throws IllegalArgumentException if the initial capacity is less than one
     * @throws IllegalArgumentException if the load factor is less than or equal to zero
     */
    public ArrayMemoryMap(int initialCapacity, float loadFactor) {
        super();
        if (initialCapacity < 1) {
            throw new IllegalArgumentException("Initial capacity must be greater than 0");
        }
        if (loadFactor <= 0.0f || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Load factor must be greater than 0");
        }
        this.loadFactor = loadFactor;
        this.threshold = calculateThreshold(initialCapacity, loadFactor);
        initialCapacity = calculateNewCapacity(initialCapacity);
        this.data = new ArrayMapEntryMemory[initialCapacity];
        init();
    }

    /**
     * Constructor copying elements from another map.
     *
     * @param map the map to copy
     * @throws NullPointerException if the map is null
     */
    public ArrayMemoryMap(ArrayMemoryMap map) {
        this(Math.max(2 * map.size(), DEFAULT_CAPACITY), DEFAULT_LOAD_FACTOR);
        putAll(map);
    }

    /**
     * Initialise subclasses during construction, cloning or deserialization.
     */
    protected void init() {
        header = new ArrayMapEntryMemory(null, -1, null, null);
        header.before = header.after = header;
    }

    /**
     * Gets the key at the specified index.
     *
     * @param index the index to retrieve
     * @return the key at the specified index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public Object get(int index) {
        return getEntry(index).getKey();
    }

    /**
     * Gets the value at the specified index.
     *
     * @param index the index to retrieve
     * @return the key at the specified index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public Memory getValue(int index) {
        return getEntry(index).getValue();
    }

    /**
     * Gets the index of the specified key.
     *
     * @param key the key to find the index of
     * @return the index, or -1 if not found
     */
    public int indexOf(Object key) {
        int i = 0;
        for (ArrayMapEntryMemory entry = header.after; entry != header; entry = entry.after, i++) {
            if (isEqualKey(key, entry.getKey())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Removes the element at the specified index.
     *
     * @param index the index of the object to remove
     * @return the previous value corresponding the <code>key</code>,
     *         or <code>null</code> if none existed
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public Memory remove(int index) {
        return remove(get(index));
    }

    /**
     * Gets an unmodifiable List view of the keys.
     * <p/>
     * The returned list is unmodifiable because changes to the values of
     * the list (using {@link java.util.ListIterator#set(Object)}) will
     * effectively remove the value from the list and reinsert that value at
     * the end of the list, which is an unexpected side effect of changing the
     * value of a list.  This occurs because changing the key, changes when the
     * mapping is added to the map and thus where it appears in the list.
     * <p/>
     * An alternative to this method is to use {@link #keySet()}.
     *
     * @return The ordered list of keys.
     * @see #keySet()
     */
    public List<Object> asList() {
        return new LinkedMapList(this);
    }

    static class LinkedMapList extends AbstractList<Object> {

        final ArrayMemoryMap parent;

        LinkedMapList(ArrayMemoryMap parent) {
            this.parent = parent;
        }

        public int size() {
            return parent.size();
        }

        public Object get(int index) {
            return parent.get(index);
        }

        public boolean contains(Object obj) {
            return parent.containsKey(obj);
        }

        public int indexOf(Object obj) {
            return parent.indexOf(obj);
        }

        public int lastIndexOf(Object obj) {
            return parent.indexOf(obj);
        }

        public boolean containsAll(Collection<?> coll) {
            return parent.keySet().containsAll(coll);
        }

        public Object remove(int index) {
            throw new UnsupportedOperationException();
        }

        public boolean remove(Object obj) {
            throw new UnsupportedOperationException();
        }

        public boolean removeAll(Collection<?> coll) {
            throw new UnsupportedOperationException();
        }

        public boolean retainAll(Collection<?> coll) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            throw new UnsupportedOperationException();
        }

        public Object[] toArray() {
            return parent.keySet().toArray();
        }

        public <T> T[] toArray(T[] array) {
            return parent.keySet().toArray(array);
        }

        public Iterator<Object> iterator() {
            return UnmodifiableIterator.decorate(parent.keySet().iterator());
        }

        public ListIterator<Object> listIterator() {
            return UnmodifiableListIterator.decorate(super.listIterator());
        }

        public ListIterator<Object> listIterator(int fromIndex) {
            return UnmodifiableListIterator.decorate(super.listIterator(fromIndex));
        }

        public List<Object> subList(int fromIndexInclusive, int toIndexExclusive) {
            return UnmodifiableList.decorate(super.subList(fromIndexInclusive, toIndexExclusive));
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the value mapped to the key specified.
     *
     * @param key the key
     * @return the mapped value, null if no match
     */
    public Memory get(Object key) {
        int hashCode = hash((key == null) ? NULL : key);
        ArrayMapEntryMemory entry = data[hashIndex(hashCode, data.length)]; // no local for hash index
        while (entry != null) {
            if (entry.hashCode == hashCode && isEqualKey(key, entry.getKey())) {
                return entry.getValue();
            }
            entry = entry.next;
        }
        return null;
    }

    /**
     * @return the size
     */
    public int size() {
        return size;
    }

    /**
     * @return true if the map is currently size zero
     */
    public boolean isEmpty() {
        return (size == 0);
    }
    /**
     * Checks whether the map contains the specified key.
     *
     * @param key the key to search for
     * @return true if the map contains the key
     */
    public boolean containsKey(Object key) {
        int hashCode = hash((key == null) ? NULL : key);
        ArrayMapEntryMemory entry = data[hashIndex(hashCode, data.length)]; // no local for hash index
        while (entry != null) {
            if (entry.hashCode == hashCode && isEqualKey(key, entry.getKey())) {
                return true;
            }
            entry = entry.next;
        }
        return false;
    }

    /**
     * Checks whether the map contains the specified value.
     *
     * @param value the value to search for
     * @return true if the map contains the value
     */
    public boolean containsValue(Object value) {
        // override uses faster iterator
        if (value == null) {
            for (ArrayMapEntryMemory entry = header.after; entry != header; entry = entry.after) {
                if (entry.getValue() == null) {
                    return true;
                }
            }
        } else {
            for (ArrayMapEntryMemory entry = header.after; entry != header; entry = entry.after) {
                if (isEqualValue(value, entry.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Puts a key-value mapping into this map.
     *
     * @param key   the key to add
     * @param value the value to add
     * @return the value previously mapped to this key, null if none
     */
    public Memory put(Object key, Memory value) {
        return putWithEntry(key, value).getA();
    }

    public Pair<Memory, ArrayMapEntryMemory> putWithEntry(Object key, Memory value) {
        int hashCode = hash((key == null) ? NULL : key);
        int index = hashIndex(hashCode, data.length);
        ArrayMapEntryMemory entry = data[index];

        while (entry != null) {
            if (entry.hashCode == hashCode && isEqualKey(key, entry.getKey())) {
                Memory oldValue = entry.getValue();
                updateEntry(entry, value);
                return new Pair<>(oldValue, entry);
            }

            entry = entry.next;
        }

        entry = addMapping(index, hashCode, key, value);
        return new Pair<>(null, entry);
    }

    /**
     * Puts all the values from the specified map into this map.
     *
     * @param map the map to add
     * @throws NullPointerException if the map is null
     */
    public void putAll(ArrayMemoryMap map) {
        int mapSize = map.size();
        if (mapSize == 0) {
            return;
        }
        int newSize = (int) ((size + mapSize) / loadFactor + 1);
        ensureCapacity(calculateNewCapacity(newSize));
        // Have to cast here because of compiler inference problems.
        for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
            ArrayMapEntryMemory entry = (ArrayMapEntryMemory) it.next();
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Removes the specified mapping from this map.
     *
     * @param key the mapping to remove
     * @return the value mapped to the removed key, null if key not in map
     */
    public Memory remove(Object key) {
        int hashCode = hash((key == null) ? NULL : key);
        int index = hashIndex(hashCode, data.length);
        ArrayMapEntryMemory entry = data[index];
        ArrayMapEntryMemory previous = null;
        while (entry != null) {
            if (entry.hashCode == hashCode && isEqualKey(key, entry.getKey())) {
                Memory oldValue = entry.getValue();
                removeMapping(entry, index, previous);
                return oldValue;
            }
            previous = entry;
            entry = entry.next;
        }
        return null;
    }

    /**
     * Clears the map, resetting the size to zero and nullifying references
     * to avoid garbage collection issues.
     */
    public void clear() {
        modCount++;
        ArrayMapEntryMemory[] data = this.data;
        for (int i = data.length - 1; i >= 0; i--) {
            data[i] = null;
        }
        size = 0;

        header.before = header.after = header;
    }

    /**
     * Gets the first key in the map, which is the most recently inserted.
     *
     * @return the most recently inserted key
     */
    public Object firstKey() {
        if (size == 0) {
            throw new NoSuchElementException("Map is empty");
        }

        return header.after.getKey();
    }

    /**
     * Gets the last key in the map, which is the first inserted.
     *
     * @return the eldest key
     */
    public Object lastKey() {
        if (size == 0) {
            throw new NoSuchElementException("Map is empty");
        }

        return header.before.getKey();
    }

    /**
     * Gets the next key in sequence.
     *
     * @param key the key to get after
     * @return the next key
     */
    public Object nextKey(Object key) {
        ArrayMapEntryMemory entry = (ArrayMapEntryMemory) getEntry(key);
        return (entry == null || entry.after == header ? null : entry.after.getKey());
    }

    /**
     * Gets the previous key in sequence.
     *
     * @param key the key to get before
     * @return the previous key
     */
    public Object previousKey(Object key) {
        ArrayMapEntryMemory entry = (ArrayMapEntryMemory) getEntry(key);
        return (entry == null || entry.before == header ? null : entry.before.getKey());
    }

    /**
     * Gets the hash code for the key specified.
     * This implementation uses the additional hashing routine from JDK1.4.
     * Subclasses can override this to return alternate hash codes.
     *
     * @param key the key to get a hash code for
     * @return the hash code
     */
    protected int hash(Object key) {
        // same as JDK 1.4
        /*int h = key.hashCode();
        h += ~(h << 9);
        h ^= (h >>> 14);
        h += (h << 4);
        h ^= (h >>> 10);*/
        return key.hashCode();
    }

    /**
     * Compares two keys, in internal converted form, to see if they are equal.
     * This implementation uses the equals method.
     * Subclasses can override this to match differently.
     *
     * @param key1 the first key to compare passed in from outside
     * @param key2 the second key extracted from the entry via <code>entry.key</code>
     * @return true if equal
     */
    protected boolean isEqualKey(Object key1, Object key2) {
        return (key1 == key2 || ((key1 != null) && key1.equals(key2)));
    }

    /**
     * Compares two values, in external form, to see if they are equal.
     * This implementation uses the equals method and assumes neither value is null.
     * Subclasses can override this to match differently.
     *
     * @param value1 the first value to compare passed in from outside
     * @param value2 the second value extracted from the entry via <code>getValue()</code>
     * @return true if equal
     */
    protected boolean isEqualValue(Object value1, Object value2) {
        return (value1 == value2 || value1.equals(value2));
    }

    /**
     * Gets the index into the data storage for the hashCode specified.
     * This implementation uses the least significant bits of the hashCode.
     * Subclasses can override this to return alternate bucketing.
     *
     * @param hashCode the hash code to use
     * @param dataSize the size of the data to pick a bucket from
     * @return the bucket index
     */
    protected int hashIndex(int hashCode, int dataSize) {
        return hashCode & (dataSize - 1);
    }

    /**
     * Gets the entry mapped to the key specified.
     * <p/>
     * This method exists for subclasses that may need to perform a multi-step
     * process accessing the entry. The public methods in this class don't use this
     * method to gain a small performance boost.
     *
     * @param key the key
     * @return the entry, null if no match
     */
    public ArrayMapEntryMemory getEntry(Object key) {
        int hashCode = hash((key == null) ? NULL : key);
        ArrayMapEntryMemory entry = data[hashIndex(hashCode, data.length)]; // no local for hash index
        while (entry != null) {
            if (entry.hashCode == hashCode && isEqualKey(key, entry.getKey())) {
                return entry;
            }
            entry = entry.next;
        }
        return null;
    }

    protected ArrayMapEntryMemory getEntry(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index " + index + " is less than zero");
        }
        if (index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + " is invalid for size " + size);
        }

        ArrayMapEntryMemory entry;
        if (index < (size / 2)) {
            // Search forwards
            entry = header.after;
            for (int currentIndex = 0; currentIndex < index; currentIndex++) {
                entry = entry.after;
            }
        } else {
            // Search backwards
            entry = header;
            for (int currentIndex = size; currentIndex > index; currentIndex--) {
                entry = entry.before;
            }
        }
        return entry;
    }

    /**
     * Updates an existing key-value mapping to change the value.
     * <p/>
     * This implementation calls <code>setValue()</code> on the entry.
     * Subclasses could override to handle changes to the map.
     *
     * @param entry    the entry to update
     * @param newValue the new value to store
     */
    protected void updateEntry(ArrayMapEntryMemory entry, Memory newValue) {
        entry.setValue(newValue);
    }

    /**
     * Reuses an existing key-value mapping, storing completely new data.
     * <p/>
     * This implementation sets all the data fields on the entry.
     * Subclasses could populate additional entry fields.
     *
     * @param entry     the entry to update, not null
     * @param hashIndex the index in the data array
     * @param hashCode  the hash code of the key to add
     * @param key       the key to add
     * @param value     the value to add
     */
    protected void reuseEntry(ArrayMapEntryMemory entry, int hashIndex, int hashCode, Object key, Memory value) {
        entry.next = data[hashIndex];
        entry.hashCode = hashCode;
        entry.setKey(key);
        entry.setValue(value);
    }

    /**
     * Adds a new key-value mapping into this map.
     * <p/>
     * This implementation calls <code>createEntry()</code>, <code>addEntry()</code>
     * and <code>checkCapacity()</code>.
     * It also handles changes to <code>modCount</code> and <code>size</code>.
     * Subclasses could override to fully control adds to the map.
     *
     * @param hashIndex the index into the data array to store at
     * @param hashCode  the hash code of the key to add
     * @param key       the key to add
     * @param value     the value to add
     */
    protected ArrayMapEntryMemory addMapping(int hashIndex, int hashCode, Object key, Memory value) {
        modCount++;
        ArrayMapEntryMemory entry = new ArrayMapEntryMemory(data[hashIndex], hashCode, key, value);
        addEntry(entry, hashIndex);
        size++;
        checkCapacity();

        return entry;
    }

    /**
     * Adds an entry into this map.
     * <p/>
     * This implementation adds the entry to the data storage table.
     * Subclasses could override to handle changes to the map.
     *
     * @param entry     the entry to add
     * @param hashIndex the index into the data array to store at
     */
    protected void addEntry(ArrayMapEntryMemory entry, int hashIndex) {
        ArrayMapEntryMemory link = entry;
        link.after = header;
        link.before = header.before;
        header.before.after = link;
        header.before = link;
        data[hashIndex] = entry;
    }


    /**
     * Removes a mapping from the map.
     * <p/>
     * This implementation calls <code>removeEntry()</code> and <code>destroyEntry()</code>.
     * It also handles changes to <code>modCount</code> and <code>size</code>.
     * Subclasses could override to fully control removals from the map.
     *
     * @param entry     the entry to remove
     * @param hashIndex the index into the data structure
     * @param previous  the previous entry in the chain
     */
    protected void removeMapping(ArrayMapEntryMemory entry, int hashIndex, ArrayMapEntryMemory previous) {
        modCount++;
        removeEntry(entry, hashIndex, previous);
        size--;
        destroyEntry(entry);
    }

    /**
     * Removes an entry from the chain stored in a particular index.
     * <p/>
     * This implementation removes the entry from the data storage table.
     * The size is not updated.
     * Subclasses could override to handle changes to the map.
     *
     * @param entry     the entry to remove
     * @param hashIndex the index into the data structure
     * @param previous  the previous entry in the chain
     */
    protected void removeEntry(ArrayMapEntryMemory entry, int hashIndex, ArrayMapEntryMemory previous) {
        ArrayMapEntryMemory link = entry;
        link.before.after = link.after;
        link.after.before = link.before;
        link.after = null;
        link.before = null;

        if (previous == null) {
            data[hashIndex] = entry.next;
        } else {
            previous.next = entry.next;
        }
    }

    /**
     * Gets the <code>before</code> field from a <code>LinkEntry</code>.
     * Used in subclasses that have no visibility of the field.
     *
     * @param entry the entry to query, must not be null
     * @return the <code>before</code> field of the entry
     * @throws NullPointerException if the entry is null
     * @since Commons Collections 3.1
     */
    protected ArrayMapEntryMemory entryBefore(ArrayMapEntryMemory entry) {
        return entry.before;
    }

    /**
     * Gets the <code>after</code> field from a <code>LinkEntry</code>.
     * Used in subclasses that have no visibility of the field.
     *
     * @param entry the entry to query, must not be null
     * @return the <code>after</code> field of the entry
     * @throws NullPointerException if the entry is null
     * @since Commons Collections 3.1
     */
    protected ArrayMapEntryMemory entryAfter(ArrayMapEntryMemory entry) {
        return entry.after;
    }


    /**
     * Kills an entry ready for the garbage collector.
     * <p/>
     * This implementation prepares the HashEntry for garbage collection.
     * Subclasses can override this to implement caching (override clear as well).
     *
     * @param entry the entry to destroy
     */
    protected void destroyEntry(ArrayMapEntryMemory entry) {
        entry.next = null;
        entry.setKey(null);
        entry.setValue(null);
    }

    /**
     * Checks the capacity of the map and enlarges it if necessary.
     * <p/>
     * This implementation uses the threshold to check if the map needs enlarging
     */
    protected void checkCapacity() {
        if (size >= threshold) {
            int newCapacity = data.length * 2;
            if (newCapacity <= MAXIMUM_CAPACITY) {
                ensureCapacity(newCapacity);
            }
        }
    }

    /**
     * Changes the size of the data structure to the capacity proposed.
     *
     * @param newCapacity the new capacity of the array (a power of two, less or equal to max)
     */
    protected void ensureCapacity(int newCapacity) {
        int oldCapacity = data.length;
        if (newCapacity <= oldCapacity) {
            return;
        }
        if (size == 0) {
            threshold = calculateThreshold(newCapacity, loadFactor);
            data = new ArrayMapEntryMemory[newCapacity];
        } else {
            ArrayMapEntryMemory oldEntries[] = data;
            ArrayMapEntryMemory newEntries[] = new ArrayMapEntryMemory[newCapacity];

            modCount++;
            for (int i = oldCapacity - 1; i >= 0; i--) {
                ArrayMapEntryMemory entry = oldEntries[i];
                if (entry != null) {
                    oldEntries[i] = null;  // gc
                    do {
                        ArrayMapEntryMemory next = entry.next;
                        int index = hashIndex(entry.hashCode, newCapacity);
                        entry.next = newEntries[index];
                        newEntries[index] = entry;
                        entry = next;
                    } while (entry != null);
                }
            }
            threshold = calculateThreshold(newCapacity, loadFactor);
            data = newEntries;
        }
    }

    /**
     * Calculates the new capacity of the map.
     * This implementation normalizes the capacity to a power of two.
     *
     * @param proposedCapacity the proposed capacity
     * @return the normalized new capacity
     */
    protected int calculateNewCapacity(int proposedCapacity) {
        int newCapacity = 1;
        if (proposedCapacity > MAXIMUM_CAPACITY) {
            newCapacity = MAXIMUM_CAPACITY;
        } else {
            while (newCapacity < proposedCapacity) {
                newCapacity <<= 1;  // multiply by two
            }
            if (newCapacity > MAXIMUM_CAPACITY) {
                newCapacity = MAXIMUM_CAPACITY;
            }
        }
        return newCapacity;
    }

    /**
     * Calculates the new threshold of the map, where it will be resized.
     * This implementation uses the load factor.
     *
     * @param newCapacity the new capacity
     * @param factor      the load factor
     * @return the new resize threshold
     */
    protected int calculateThreshold(int newCapacity, float factor) {
        return (int) (newCapacity * factor);
    }

    /**
     * Gets the <code>next</code> field from a <code>HashEntry</code>.
     * Used in subclasses that have no visibility of the field.
     *
     * @param entry the entry to query, must not be null
     * @return the <code>next</code> field of the entry
     * @throws NullPointerException if the entry is null
     * @since Commons Collections 3.1
     */
    protected ArrayMapEntryMemory entryNext(ArrayMapEntryMemory entry) {
        return entry.next;
    }

    /**
     * Gets the <code>hashCode</code> field from a <code>HashEntry</code>.
     * Used in subclasses that have no visibility of the field.
     *
     * @param entry the entry to query, must not be null
     * @return the <code>hashCode</code> field of the entry
     * @throws NullPointerException if the entry is null
     * @since Commons Collections 3.1
     */
    protected int entryHashCode(ArrayMapEntryMemory entry) {
        return entry.hashCode;
    }

    /**
     * Gets the <code>key</code> field from a <code>HashEntry</code>.
     * Used in subclasses that have no visibility of the field.
     *
     * @param entry the entry to query, must not be null
     * @return the <code>key</code> field of the entry
     * @throws NullPointerException if the entry is null
     * @since Commons Collections 3.1
     */
    protected Object entryKey(ArrayMapEntryMemory entry) {
        return entry.getKey();
    }

    /**
     * Gets the <code>value</code> field from a <code>HashEntry</code>.
     * Used in subclasses that have no visibility of the field.
     *
     * @param entry the entry to query, must not be null
     * @return the <code>value</code> field of the entry
     * @throws NullPointerException if the entry is null
     * @since Commons Collections 3.1
     */
    protected Memory entryValue(ArrayMapEntryMemory entry) {
        return entry.getValue();
    }

    /**
     * Gets an iterator over the map.
     * Changes made to the iterator affect this map.
     * <p/>
     * A MapIterator returns the keys in the map. It also provides convenient
     * methods to get the key and value, and set the value.
     * It avoids the need to create an entrySet/keySet/values object.
     * It also avoids creating the Map.Entry object.
     *
     * @return the map iterator
     */
    public MapIterator<Object, Memory> mapIterator() {
        if (size == 0) {
            return EmptyMapIterator.INSTANCE;
        }
        return new LinkMapIterator(this);
    }

    public Iterator entriesIterator() {
        return new EntriesIterator(this);
    }

    /**
     * Gets a bidirectional iterator over the map.
     * Changes made to the iterator affect this map.
     * <p/>
     * A MapIterator returns the keys in the map. It also provides convenient
     * methods to get the key and value, and set the value.
     * It avoids the need to create an entrySet/keySet/values object.
     *
     * @return the map iterator
     */
    public OrderedMapIterator<Object, Memory> orderedMapIterator() {
        if (size == 0) {
            return EmptyOrderedMapIterator.INSTANCE;
        }
        return new LinkMapIterator(this);
    }


    /**
     * Gets the entrySet view of the map.
     * Changes made to the view affect this map.
     * To simply iterate through the entries, use {@link #mapIterator()}.
     *
     * @return the entrySet view
     */
    public Set<Entry<Object, Memory>> entrySet() {
        if (entrySet == null) {
            entrySet = new EntrySet(this);
        }

        return entrySet;
    }

    /**
     * Creates an entry set iterator.
     * Subclasses can override this to return iterators with different properties.
     *
     * @return the entrySet iterator
     */
    protected Iterator<Entry<Object, Memory>> createEntrySetIterator() {
        if (size() == 0) {
            return EmptyIterator.INSTANCE;
        }
        return new EntrySetIterator(this);
    }

    /**
     * Gets the keySet view of the map.
     * Changes made to the view affect this map.
     * To simply iterate through the keys, use {@link #mapIterator()}.
     *
     * @return the keySet view
     */
    public Set<Object> keySet() {
        if (keySet == null) {
            keySet = new KeySet(this);
        }
        return keySet;
    }

    /**
     * Creates a key set iterator.
     * Subclasses can override this to return iterators with different properties.
     *
     * @return the keySet iterator
     */
    protected Iterator<Object> createKeySetIterator() {
        if (size() == 0) {
            return EmptyIterator.INSTANCE;
        }
        return new KeySetIterator(this);
    }

    /**
     * Gets the values view of the map.
     * Changes made to the view affect this map.
     * To simply iterate through the values, use {@link #mapIterator()}.
     *
     * @return the values view
     */
    public Collection<Memory> values() {
        if (values == null) {
            values = new Values(this);
        }
        return values;
    }

    /**
     * Creates a values iterator.
     * Subclasses can override this to return iterators with different properties.
     *
     * @return the values iterator
     */
    protected Iterator<Memory> createValuesIterator() {
        if (size() == 0) {
            return EmptyIterator.INSTANCE;
        }
        return new ValuesIterator(this);
    }

    /**
     * MapIterator implementation.
     */
    protected static class LinkMapIterator extends LinkIterator
            implements OrderedMapIterator<Object, Memory>, OrderedIterator<Object>, ResettableIterator<Object> {

        protected LinkMapIterator(ArrayMemoryMap parent) {
            super(parent);
        }

        public Object next() {
            return super.nextEntry().getKey();
        }

        public Object previous() {
            return super.previousEntry().getKey();
        }

        public Object getKey() {
            ArrayMapEntryMemory current = currentEntry();
            if (current == null) {
                throw new IllegalStateException(GETKEY_INVALID);
            }
            return current.getKey();
        }

        public Memory getValue() {
            ArrayMapEntryMemory current = currentEntry();
            if (current == null) {
                throw new IllegalStateException(GETVALUE_INVALID);
            }
            return current.getValue();
        }

        public Memory setValue(Memory value) {
            ArrayMapEntryMemory current = currentEntry();
            if (current == null) {
                throw new IllegalStateException(SETVALUE_INVALID);
            }
            return current.setValue(value);
        }
    }

    protected static class EntriesIterator extends LinkIterator implements OrderedIterator<ArrayMapEntryMemory>, ResettableIterator<ArrayMapEntryMemory> {
        protected EntriesIterator(ArrayMemoryMap parent) {
            super(parent);
        }

        public ArrayMapEntryMemory next() {
            return super.nextEntry();
        }
        public ArrayMapEntryMemory previous() {
            return super.previousEntry();
        }
    }

    protected static class ValuesIterator extends LinkIterator implements OrderedIterator<Memory>, ResettableIterator<Memory> {
        protected ValuesIterator(ArrayMemoryMap parent) {
            super(parent);
        }

        public Memory next() {
            return super.nextEntry().getValue();
        }
        public Memory previous() {
            return super.previousEntry().getValue();
        }
    }

    protected static class KeySetIterator extends LinkIterator implements OrderedIterator<Object>, ResettableIterator<Object> {
        protected KeySetIterator(ArrayMemoryMap parent) {
            super(parent);
        }

        public Object next() {
            return super.nextEntry().getKey();
        }
        public Object previous() {
            return super.previousEntry().getKey();
        }
    }

    protected static class EntrySetIterator extends LinkIterator
            implements OrderedIterator<Entry<Object, Memory>>, ResettableIterator<Entry<Object, Memory>> {
        protected EntrySetIterator(ArrayMemoryMap parent) {
            super(parent);
        }

        public ArrayMapEntryMemory next() {
            return super.nextEntry();
        }
        public ArrayMapEntryMemory previous() {
            return super.previousEntry();
        }
    }


    protected static class EntrySet extends AbstractSet<Entry<Object, Memory>> {
        /**
         * The parent map
         */
        protected final ArrayMemoryMap parent;

        protected EntrySet(ArrayMemoryMap parent) {
            super();
            this.parent = parent;
        }

        public int size() {
            return parent.size();
        }

        public void clear() {
            parent.clear();
        }

        public boolean contains(ArrayMapEntryMemory entry) {
            ArrayMapEntryMemory e = entry;
            ArrayMapEntryMemory match = parent.getEntry(e.getKey());
            return (match != null && match.equals(e));
        }

        public boolean remove(Object obj) {
            if (!(obj instanceof ArrayMapEntryMemory)) {
                return false;
            }
            if (!contains(obj)) {
                return false;
            }
            ArrayMapEntryMemory entry = (ArrayMapEntryMemory) obj;
            Object key = entry.getKey();
            parent.remove(key);
            return true;
        }

        public Iterator<Entry<Object, Memory>> iterator() {
            return parent.createEntrySetIterator();
        }
    }

    /**
     * KeySet implementation.
     */
    protected static class KeySet extends AbstractSet<Object> {
        /**
         * The parent map
         */
        protected final ArrayMemoryMap parent;

        protected KeySet(ArrayMemoryMap parent) {
            super();
            this.parent = parent;
        }

        public int size() {
            return parent.size();
        }

        public void clear() {
            parent.clear();
        }

        public boolean contains(Object key) {
            return parent.containsKey(key);
        }

        public boolean remove(Object key) {
            boolean result = parent.containsKey(key);
            parent.remove(key);
            return result;
        }

        public Iterator<Object> iterator() {
            return parent.createKeySetIterator();
        }
    }


    /**
     * Values implementation.
     */
    protected static class Values extends AbstractCollection<Memory> {
        /**
         * The parent map
         */
        protected final ArrayMemoryMap parent;

        protected Values(ArrayMemoryMap parent) {
            super();
            this.parent = parent;
        }

        public int size() {
            return parent.size();
        }

        public void clear() {
            parent.clear();
        }

        public boolean contains(Object value) {
            return parent.containsValue(value);
        }

        public Iterator<Memory> iterator() {
            return parent.createValuesIterator();
        }
    }

    /**
     * Base Iterator that iterates in link order.
     */
    protected static abstract class LinkIterator {
        /**
         * The parent map
         */
        protected final ArrayMemoryMap parent;
        /**
         * The current (last returned) entry
         */
        protected ArrayMapEntryMemory last;
        /**
         * The next entry
         */
        protected ArrayMapEntryMemory next;
        /**
         * The modification count expected
         */
        protected int expectedModCount;

        protected LinkIterator(ArrayMemoryMap parent) {
            super();
            this.parent = parent;
            this.next = parent.header.after;
            this.expectedModCount = parent.modCount;
        }

        public boolean hasNext() {
            return (next != parent.header);
        }

        public boolean hasPrevious() {
            return (next.before != parent.header);
        }

        protected ArrayMapEntryMemory nextEntry() {
            if (parent.modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            if (next == parent.header) {
                throw new NoSuchElementException(NO_NEXT_ENTRY);
            }
            last = next;
            next = next.after;
            return last;
        }

        protected ArrayMapEntryMemory previousEntry() {
            if (parent.modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            ArrayMapEntryMemory previous = next.before;
            if (previous == parent.header) {
                throw new NoSuchElementException(NO_PREVIOUS_ENTRY);
            }
            next = previous;
            last = previous;
            return last;
        }

        protected ArrayMapEntryMemory currentEntry() {
            return last;
        }

        public void remove() {
            if (last == null) {
                throw new IllegalStateException(REMOVE_INVALID);
            }
            if (parent.modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            parent.remove(last.getKey());
            last = null;
            expectedModCount = parent.modCount;
        }

        public void reset() {
            last = null;
            next = parent.header.after;
        }

        public String toString() {
            if (last != null) {
                return "Iterator[" + last.getKey() + "=" + last.getValue() + "]";
            } else {
                return "Iterator[]";
            }
        }
    }
}
