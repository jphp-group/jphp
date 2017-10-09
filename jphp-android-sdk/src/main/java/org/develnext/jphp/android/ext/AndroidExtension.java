package org.develnext.jphp.android.ext;

import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.develnext.jphp.android.ext.bind.ActivityMemoryOperation;
import org.develnext.jphp.android.ext.classes.WrapAndroid;
import org.develnext.jphp.android.ext.classes.WrapR;
import org.develnext.jphp.android.ext.classes.app.WrapActivity;
import org.develnext.jphp.android.ext.classes.app.WrapBootstrapActivity;
import org.develnext.jphp.android.ext.classes.text.WrapInputType;
import org.develnext.jphp.android.ext.classes.view.WrapView;
import org.develnext.jphp.android.ext.classes.view.WrapViewGroup;
import org.develnext.jphp.android.ext.classes.widget.*;
import org.develnext.jphp.android.ext.event.ClickEventProvider;
import org.develnext.jphp.android.ext.event.LongClickEventProvider;
import php.runtime.env.CompileScope;
import php.runtime.env.Environment;
import php.runtime.ext.support.Extension;
import php.runtime.invoke.Invoker;

import java.util.HashMap;
import java.util.Map;

public class AndroidExtension extends Extension {
    public final static String NAMESPACE = "php\\android\\";

    public final static Map<String, EventProvider> eventProviders = new HashMap<String, EventProvider>();

    @Override
    public Status getStatus() {
        return Status.EXPERIMENTAL;
    }

    @Override
    public String[] getPackageNames() {
        return new String[] {"android"};
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerMemoryOperation(ActivityMemoryOperation.class);

        registerClass(scope, WrapR.class);

        registerWrapperClass(scope, View.class, WrapView.class);
        registerWrapperClass(scope, ViewGroup.class, WrapViewGroup.class);
        registerWrapperClass(scope, FrameLayout.class, WrapFrameLayout.class);
        registerWrapperClass(scope, LinearLayout.class, WrapLinearLayout.class);
        registerWrapperClass(scope, RelativeLayout.class, WrapRelativeLayout.class);
        registerWrapperClass(scope, TextView.class, WrapTextView.class);
        registerWrapperClass(scope, Button.class, WrapButton.class);
        registerWrapperClass(scope, EditText.class, WrapEditText.class);
        registerWrapperClass(scope, ImageView.class, WrapImageView.class);

        registerClass(scope, WrapInputType.class);

        registerClass(scope, WrapActivity.class);
        registerClass(scope, WrapBootstrapActivity.class);
        registerWrapperClass(scope, Toast.class, WrapToast.class);

        registerClass(scope, WrapAndroid.class);

        // events
        registerEventProvider(new ClickEventProvider());
        registerEventProvider(new LongClickEventProvider());
    }

    @Override
    public void onLoad(Environment env) {

    }

    public static void registerEventProvider(EventProvider eventProvider) {
        eventProviders.put(eventProvider.getCode().toLowerCase(), eventProvider);
    }

    public static void bindEvent(Environment env, View view, String event, final Invoker invoker) {
        EventProvider provider = eventProviders.get(event.toLowerCase());

        if (provider != null) {
            provider.bind(env, view, invoker);
        }
    }

    public static void unbindEvent(Environment env, View view, String event) {
        EventProvider provider = eventProviders.get(event.toLowerCase());

        if (provider != null) {
            provider.unbind(env, view);
        }
    }

    public static void triggerEvent(Environment env, View view, String event) {
        EventProvider provider = eventProviders.get(event.toLowerCase());

        if (provider != null) {
            provider.trigger(env, view);
        }
    }
}
