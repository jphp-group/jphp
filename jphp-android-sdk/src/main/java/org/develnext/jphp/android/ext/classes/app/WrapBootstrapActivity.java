package org.develnext.jphp.android.ext.classes.app;

import android.os.Bundle;
import org.develnext.jphp.android.AndroidStandaloneLoader;
import org.develnext.jphp.android.ext.AndroidExtension;
import php.runtime.annotation.Reflection;

@Reflection.Name(AndroidExtension.NAMESPACE + "app\\BootstrapActivity")
public class WrapBootstrapActivity extends WrapActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreateClearly(savedInstanceState);

        AndroidStandaloneLoader.INSTANCE.run(this);

        getEnvironment().invokeMethodNoThrow(this, "onCreate");
    }
}
