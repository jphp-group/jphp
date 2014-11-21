package org.develnext.jphp.android.ext;


import android.app.Activity;
import org.develnext.jphp.android.ext.classes.WrapActivity;

public class JPHPActivity extends Activity {
    protected final WrapActivity wrapActivity;

    public JPHPActivity(WrapActivity wrapActivity) {
        this.wrapActivity = wrapActivity;
    }
}
