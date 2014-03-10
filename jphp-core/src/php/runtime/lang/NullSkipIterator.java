package php.runtime.lang;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class NullSkipIterator<T> implements Iterator<T> {

    protected final List<T> list;
    protected final ListIterator<T> iterator;

    public NullSkipIterator(List<T> list) {
        this.list = list;
        this.iterator = list.listIterator();
    }

    @Override
    public boolean hasNext() {
        int offset = 0;
        while (iterator.hasNext()){
            offset++;
            if (iterator.next() == null){

            } else
                break;
        }

        for(int i = 0; i < offset; i++)
            iterator.previous();

        return offset > 0;
    }

    @Override
    public T next() {
        T o = null;
        while (iterator.hasNext()){
            o = iterator.next();
            if (o != null)
                return o;
        }
        return o;
    }

    @Override
    public void remove() {
        iterator.remove();
    }
}
