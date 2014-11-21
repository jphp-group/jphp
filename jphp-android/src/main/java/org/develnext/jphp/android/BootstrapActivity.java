package org.develnext.jphp.android;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;
import org.develnext.jphp.android.ext.AndroidExtension;
import php.runtime.common.Function;
import php.runtime.env.CompileScope;

import java.io.*;

public class BootstrapActivity extends Activity {
    protected final static String LIBRARY_DEX_JAR = "application.jar";
    protected final int BUF_SIZE = 1024;

    protected AndroidApplication application;
    protected CompileScope scope;

    protected Function<Void> onCreate = null;

    public BootstrapActivity() {
        super();

        scope       = new CompileScope();
        application = new AndroidApplication(scope);

        scope.registerExtension(new AndroidExtension(this));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        application.setMainActivity(this);

        File appJar = new File(getDir("dex", Context.MODE_PRIVATE), LIBRARY_DEX_JAR);

        extractDexTo(appJar);
        try {
            application.loadLibrary(appJar);
            application.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

/*
        try {
            String message = application.loadLibrary(appJar).get(0)
                    .includeNoThrow(.getEnvironment()).toString();

            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    private void extractDexTo(File tJarInternalStoragePath) {
        BufferedInputStream aJarInputStream = null;
        BufferedOutputStream aJarOutputStream = null;
        OutputStream aDexOutputStream = null;

        try {
            aJarInputStream = new BufferedInputStream(getAssets().open(LIBRARY_DEX_JAR));
            aJarOutputStream = new BufferedOutputStream(new FileOutputStream(tJarInternalStoragePath));
            byte[] buf = new byte[BUF_SIZE];
            int len;
            while ((len = aJarInputStream.read(buf, 0, BUF_SIZE)) > 0)
            {
                aJarOutputStream.write(buf, 0, len);
            }
            aJarOutputStream.close();
            aJarInputStream.close();
        } catch (IOException e) {
            if (aDexOutputStream != null) {
                try {
                    aJarOutputStream.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }

            if (aJarInputStream != null) {
                try {
                    aJarInputStream.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }

}
