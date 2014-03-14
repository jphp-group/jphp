package org.develnext.jphp.swing.support;

import java.awt.*;

public interface RootWindow {
    void moveToCenter();

    boolean isResizable();
    void setResizable(boolean value);

    boolean isUndecorated();
    void setUndecorated(boolean value);

    String getTitle();
    void setTitle(String value);

    void setDefaultCloseOperation(int operation);
    int getDefaultCloseOperation();

    Container getContentPane();
}
