package php.runtime.ext.swing.support;

import javax.swing.*;
import java.awt.*;

public class JDialogX extends JDialog implements RootWindow {
    public JDialogX() {
    }

    public JDialogX(Frame owner) {
        super(owner);
    }

    public JDialogX(Dialog owner) {
        super(owner);
    }

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
