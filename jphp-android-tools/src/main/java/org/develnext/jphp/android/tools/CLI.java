package org.develnext.jphp.android.tools;

import java.io.File;

public class CLI {
    public static void main(String[] args) throws Throwable {
        AndroidApplicationCreator app = new AndroidApplicationCreator();

        if (args.length < 1) {
            System.err.println("Please pass a filename or directory path ...");
            return;
        }

        app.addFile(new File(args[0]));
        app.saveTo(new File(args[0] + ".jar"));
    }
}
