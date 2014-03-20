package org.develnext.jphp.swing.support;

import javax.swing.*;
import java.awt.*;

public class JFrameX extends JFrame implements RootWindow {

    public void moveToCenter() {
        setLocationRelativeTo(null);
    }

    @Override
    public void setBackground(Color bgColor) {
        getContentPane().setBackground(bgColor);
    }

    @Override
    public Color getBackground() {
        return getContentPane().getBackground();
    }
}
