package org.develnext.jphp.android.ext.classes.widget;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import org.develnext.jphp.android.AndroidStandaloneLoader;
import org.develnext.jphp.android.ext.AndroidExtension;
import org.develnext.jphp.android.ext.classes.app.WrapActivity;
import org.develnext.jphp.android.ext.classes.view.WrapView;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

import java.io.IOException;
import java.io.InputStream;

@Name(AndroidExtension.NAMESPACE + "widget\\ImageView")
public class WrapImageView extends WrapView {
    public WrapImageView(Environment env, ImageView wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapImageView(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public ImageView getWrappedObject() {
        return (ImageView) super.getWrappedObject();
    }

    @Override
    @Signature
    public void __construct(WrapActivity activity) {
        __wrappedObject = new ImageView(activity);
        __wrappedObject.setId(idCounter.getAndIncrement());
    }

    @Signature
    public void setImageAsset(String fileName) throws IOException {
        InputStream inputStream = AndroidStandaloneLoader.getContext().getAssets().open(fileName);
        getWrappedObject().setImageDrawable(Drawable.createFromStream(inputStream, fileName));
    }
}
