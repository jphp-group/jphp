package org.develnext.jphp.android.ext.classes.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import org.develnext.jphp.android.AndroidStandaloneLoader;
import org.develnext.jphp.android.ext.AndroidExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.annotation.Reflection.UseJavaLikeNames;
import php.runtime.env.Environment;
import php.runtime.exceptions.CriticalException;
import php.runtime.lang.IObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

@UseJavaLikeNames
@Name(AndroidExtension.NAMESPACE + "app\\Activity")
public class WrapActivity extends Activity implements IObject {
    private boolean isFinalized = false;
    private ClassEntity __class__;
    private ArrayMemory __props__ = new ArrayMemory();

    public WrapActivity() {
        super();
    }

    public WrapActivity(Environment env, ClassEntity clazz) {
        throw new RuntimeException("Unable to create object");
    }

    @Signature
    public Memory __construct(Environment env, Memory... args) {
        return Memory.NULL;
    }

    @Signature
    public void setContentView(View view) {
        super.setContentView(view);
    }

    @Signature
    public void setTitle(String title) {
        super.setTitle(title);
    }

    @Signature
    public void onCreate() {
        // nop.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getEnvironment().invokeMethodNoThrow(this, "onCreate");
    }

    final protected void onCreateClearly(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public ClassEntity getReflection() {
        if (__class__ != null) {
            return __class__;
        }

        Environment env = getEnvironment();
        if (env != null) {
            try {
                __class__ = env.fetchClass(getClass().getField("$CL").get(null).toString());
            } catch (IllegalAccessException e) {
                throw new CriticalException(e);
            } catch (NoSuchFieldException e) {
                throw new CriticalException(e);
            }
        }

        return __class__;
    }

    @Override
    public ArrayMemory getProperties() {
        return __props__;
    }

    @Override
    public Environment getEnvironment() {
        return AndroidStandaloneLoader.getEnvironment();
    }

    @Override
    public int getPointer() {
        return hashCode();
    }

    @Override
    public boolean isMock() {
        return __class__ == null;
    }

    @Override
    public void setAsMock() {
        __class__ = null;
    }

    @Override
    public boolean isFinalized() {
        return isFinalized;
    }

    @Override
    public void doFinalize() {
        isFinalized = true;
    }
}
