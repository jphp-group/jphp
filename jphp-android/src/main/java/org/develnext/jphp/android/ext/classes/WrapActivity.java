package org.develnext.jphp.android.ext.classes;

import android.app.Activity;
import android.widget.Toast;
import org.develnext.jphp.android.ext.AndroidExtension;
import org.develnext.jphp.android.ext.JPHPActivity;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.annotation.Reflection.WrapInterface;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

@Name(AndroidExtension.NAMESPACE + "app\\Activity")
@WrapInterface(WrapActivity.Methods.class)
public class WrapActivity extends BaseWrapper<Activity> {
    public WrapActivity(Environment env, Activity wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapActivity(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new JPHPActivity(this);
    }

    interface Methods {
        boolean isChild();
        Activity getParent();
    }
}
