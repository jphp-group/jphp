package php.runtime.lang.spl;

import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.IObject;

@Reflection.NotRuntime
@Reflection.Name("Traversable")
public interface Traversable extends IObject {
    ForeachIterator getNewIterator(Environment env, boolean getReferences, boolean getKeyReferences);
    ForeachIterator getNewIterator(Environment env);
}
