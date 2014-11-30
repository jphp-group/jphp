package org.develnext.jphp.android;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import php.runtime.env.Environment;
import php.runtime.loader.StandaloneLoader;

public class AndroidStandaloneLoader extends StandaloneLoader {
    public static final AndroidStandaloneLoader INSTANCE;

    protected Activity mainActivity;
    protected AssetManager assetManager;

    public AndroidStandaloneLoader() {
        super();
    }

    public void run(Activity mainActivity) {
        this.mainActivity = mainActivity;
        this.assetManager = mainActivity.getAssets();

        setClassLoader(mainActivity.getClassLoader());

        super.run();
    }

    public static Activity getMainActivity() {
        return INSTANCE.mainActivity;
    }

    public static Context getContext() {
        return INSTANCE.mainActivity.getApplicationContext();
    }

    public static Environment getEnvironment() {
        return INSTANCE == null ? null : INSTANCE.env;
    }

    static {
        INSTANCE = new AndroidStandaloneLoader();
    }
}
