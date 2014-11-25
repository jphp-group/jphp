package org.develnext.jphp.android.ext.classes.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import org.develnext.jphp.android.AndroidApplication;
import org.develnext.jphp.android.ext.AndroidExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.env.CompileScope;
import php.runtime.exceptions.CriticalException;
import php.runtime.ext.support.Extension;

import java.io.*;
import java.util.Scanner;

@Name(AndroidExtension.NAMESPACE + "app\\BootstrapActivity")
public class BootstrapActivity extends WrapActivity {
    protected AndroidApplication application;
    protected CompileScope scope;

    public BootstrapActivity() {
        super();

        scope       = new CompileScope();
        application = new AndroidApplication(scope);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        application.setMainActivity(this);

        try {
            application.loadLibrary();
            application.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        super.onCreateClearly(savedInstanceState);
        getEnvironment().invokeMethodNoThrow(this, "onCreate");
    }
}
