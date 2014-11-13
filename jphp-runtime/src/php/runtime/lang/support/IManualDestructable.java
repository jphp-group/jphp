package php.runtime.lang.support;


import php.runtime.env.Environment;

public interface IManualDestructable {
    void onManualDestruct(Environment env);
}
