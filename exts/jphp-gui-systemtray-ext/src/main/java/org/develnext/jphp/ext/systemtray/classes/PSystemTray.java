package org.develnext.jphp.ext.systemtray.classes;

import org.develnext.jphp.ext.systemtray.SystemTrayExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import java.awt.*;
import java.util.*;

@Reflection.Name("SystemTray")
@Reflection.Namespace(SystemTrayExtension.NS)
public class PSystemTray extends BaseObject {

    public PSystemTray(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private void __construct() {
    }

    @Signature
    public static void add(TrayIcon icon) throws AWTException {
        SystemTray.getSystemTray().add(icon);
    }

    @Signature
    public static void remove(TrayIcon icon) throws AWTException {
        SystemTray.getSystemTray().remove(icon);
    }

    @Signature
    public static double[] getTrayIconSize() throws AWTException {
        Dimension iconSize = SystemTray.getSystemTray().getTrayIconSize();
        return new double[] { iconSize.getWidth(), iconSize.getHeight() };
    }

    @Signature
    public static java.util.List<TrayIcon> getTrayIcons() throws AWTException {
        return Arrays.asList(SystemTray.getSystemTray().getTrayIcons());
    }

    @Signature
    public static boolean isSupported() {
        return SystemTray.isSupported();
    }
}
