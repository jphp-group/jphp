package php.runtime.lang;

import php.runtime.annotation.Reflection;

@Reflection.Ignore
public interface Resource {
    String getResourceType();
}
