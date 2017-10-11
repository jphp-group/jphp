package php.runtime.memory.support;

import php.runtime.Memory;
import php.runtime.common.collections.KeyValue;
import php.runtime.memory.ReferenceMemory;

import java.util.Map;

public class ArrayMapEntryMemory extends ReferenceMemory implements Map.Entry<Object, Memory>, KeyValue<Object, Memory> {
    /**
     * The next entry in the hash chain
     */
    protected ArrayMapEntryMemory next;

    /**
     * The entry before this one in the order
     */
    protected ArrayMapEntryMemory before;
    /**
     * The entry after this one in the order
     */
    protected ArrayMapEntryMemory after;

    /**
     * The hash code of the key
     */
    protected int hashCode;

    /**
     * The key
     */
    private Object key;

    public ArrayMapEntryMemory(ArrayMapEntryMemory next, int hashCode, Object key, Memory value) {
        super(value);

        this.next = next;
        this.hashCode = hashCode;
        this.key = key;
    }

    @Override
    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Map.Entry)) {
            return false;
        }

        Map.Entry other = (Map.Entry) obj;
        return (getKey() == null ? other.getKey() == null : getKey().equals(other.getKey())) && (getValue() == null ? other.getValue() == null : getValue().equals(other.getValue()));
    }

    public int hashCode() {
        return (getKey() == null ? 0 : getKey().hashCode()) ^ (getValue() == null ? 0 : getValue().hashCode());
    }
}
