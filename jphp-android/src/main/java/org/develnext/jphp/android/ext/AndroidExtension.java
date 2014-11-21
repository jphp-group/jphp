package org.develnext.jphp.android.ext;

import android.app.Activity;
import android.widget.Toast;
import org.develnext.jphp.android.BootstrapActivity;
import org.develnext.jphp.android.ext.classes.WrapActivity;
import org.develnext.jphp.android.ext.classes.WrapToast;
import php.runtime.env.CompileScope;
import php.runtime.env.Environment;
import php.runtime.ext.support.Extension;
import php.runtime.memory.ObjectMemory;

public class AndroidExtension extends Extension {
    public final static String NAMESPACE = "android\\";


    protected final BootstrapActivity bootstrapActivity;

    public AndroidExtension(BootstrapActivity activity) {
        bootstrapActivity = activity;
    }

    @Override
    public String getVersion() {
        return "~";
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerWrapperClass(scope, Activity.class, WrapActivity.class);
        registerWrapperClass(scope, Toast.class, WrapToast.class);
    }

    @Override
    public void onLoad(Environment env) {
        env.getGlobals().put("bootstrapActivity", new ObjectMemory(new WrapActivity(env, bootstrapActivity)));
    }
}
