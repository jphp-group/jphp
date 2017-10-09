package org.develnext.jphp.android.ext.classes.app;

import android.os.Bundle;
import org.develnext.jphp.android.AndroidStandaloneLoader;
import org.develnext.jphp.android.ext.AndroidExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Name;
import php.runtime.env.Environment;
import php.runtime.launcher.LaunchException;
import php.runtime.reflection.ClassEntity;

@Name(AndroidExtension.NAMESPACE + "app\\BootstrapActivity")
public class WrapBootstrapActivity extends WrapActivity {
    private ClassEntity __class__;

    public WrapBootstrapActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreateClearly(savedInstanceState);

        AndroidStandaloneLoader.INSTANCE.run(this);

        Environment environment = AndroidStandaloneLoader.getEnvironment();

        if (environment != null) {
            try {
                __class__ = environment.fetchClass(
                        ((String) getClass().getField("$CL").get(null))
                );
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        } else {
            throw new LaunchException("Environment is not initialized for bootstrap activity");
        }

        getEnvironment().invokeMethodNoThrow(this, "onCreate");
    }

    @Override
    public ClassEntity getReflection() {
        return __class__;
    }
}
