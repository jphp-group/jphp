package org.develnext.jphp.android;

import android.app.Activity;
import android.os.Bundle;

public class BootstrapActivity extends Activity {
    protected final AndroidStandaloneLoader loader;

    public BootstrapActivity() {
        super();
        loader = AndroidStandaloneLoader.INSTANCE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loader.run(this);
    }
}
