package php.runtime.util;

import php.runtime.env.CallStackItem;
import php.runtime.env.Environment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StackTracer implements Iterable<CallStackItem> {
    protected final Environment env;
    protected final List<CallStackItem> result;

    public StackTracer(Environment env, int limit){
        this.env = env;
        this.result = new ArrayList<CallStackItem>();
        for(int i = 0; i < env.getCallStackTop(); i++){
            if (limit != 0 && i >= limit)
                break;
            result.add(env.peekCall(i));
        }
    }

    @Override
    public Iterator<CallStackItem> iterator() {
        return result.iterator();
    }

    public String toString(boolean withArgs) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (CallStackItem el : this){
            sb.append("#").append(i).append(" ");
            sb.append(el.toString(withArgs));
            if (i != result.size() - 1)
                sb.append("\n");

            i++;
        }
        return sb.toString();
    }
}
