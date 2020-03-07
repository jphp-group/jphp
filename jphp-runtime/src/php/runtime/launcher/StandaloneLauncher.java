package php.runtime.launcher;

import php.runtime.exceptions.CriticalException;
import php.runtime.loader.StandaloneLoader;

import java.io.IOException;

public class StandaloneLauncher {

    public static void main(String[] args) {
        StandaloneLoader loader = new StandaloneLoader();
        loader.setArgv(args);
        loader.setClassLoader(StandaloneLauncher.class.getClassLoader());
        try {
            loader.loadLibrary();
            loader.run();
        } catch (IOException e) {
            throw new CriticalException(e);
        }
    }
}
