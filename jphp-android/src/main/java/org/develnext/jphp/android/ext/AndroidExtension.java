package org.develnext.jphp.android.ext;

import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.develnext.jphp.android.ext.classes.app.BootstrapActivity;
import org.develnext.jphp.android.ext.classes.WrapAndroid;
import org.develnext.jphp.android.ext.classes.WrapR;
import org.develnext.jphp.android.ext.classes.app.WrapActivity;
import org.develnext.jphp.android.ext.classes.view.WrapView;
import org.develnext.jphp.android.ext.classes.view.WrapViewGroup;
import org.develnext.jphp.android.ext.classes.widget.*;
import php.runtime.env.CompileScope;
import php.runtime.env.Environment;
import php.runtime.ext.support.Extension;

public class AndroidExtension extends Extension {
    public final static String NAMESPACE = "android\\";

    protected final BootstrapActivity bootstrapActivity;

    public AndroidExtension() {
        this(null);
    }

    public AndroidExtension(BootstrapActivity activity) {
        bootstrapActivity = activity;
    }

    @Override
    public String getVersion() {
        return "~";
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerClass(scope, WrapR.class);

        registerWrapperClass(scope, View.class, WrapView.class);
        registerWrapperClass(scope, ViewGroup.class, WrapViewGroup.class);
        registerWrapperClass(scope, FrameLayout.class, WrapFrameLayout.class);
        registerWrapperClass(scope, LinearLayout.class, WrapLinearLayout.class);
        registerWrapperClass(scope, RelativeLayout.class, WrapRelativeLayout.class);
        registerWrapperClass(scope, TextView.class, WrapTextView.class);

        registerClass(scope, WrapActivity.class);
        registerClass(scope, BootstrapActivity.class);
        registerWrapperClass(scope, Toast.class, WrapToast.class);

        registerClass(scope, WrapAndroid.class);
    }

    @Override
    public void onLoad(Environment env) {

    }
}
