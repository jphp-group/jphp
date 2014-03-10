package php.runtime.ext.swing.support;

import javax.swing.*;
import java.awt.*;

public class JImageX extends JComponent {
    protected ImageIcon image;
    protected boolean stretch;
    protected boolean centred;
    protected boolean proportional;
    protected boolean smooth;
    protected boolean mosaic;

    public JImageX() {
        //this.setBackground(new Color(0, 0, 0, 0));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            Graphics2D g2 = (Graphics2D) g.create();

            int w = image.getIconWidth();
            int h = image.getIconHeight();
            int x = 0;
            int y = 0;
            boolean smooth = false;

            Image img = image.getImage();
            if (isMosaic()) {
                int horCount = (int)Math.ceil(getWidth() / (float)w);
                int verCount = (int)Math.ceil(getHeight() / (float)h);
                for(int i = 0; i < verCount; i++){
                    for(int j = 0; j < horCount; j++) {
                        g2.drawImage(img, j * w, i * h, w, h, null);
                    }
                }
            } else {
                if (isStretch()) {
                    smooth = true;
                    if (isProportional()) {
                        float ratio = Math.min(getWidth() / (float)w, getHeight() / (float)h);
                        w = (int)(w * ratio);
                        h = (int)(h * ratio);
                    } else {
                        w = getWidth();
                        h = getHeight();
                    }
                }

                if (isCentred()) {
                    x = getWidth() / 2 - w / 2;
                    y = getHeight() / 2 - h / 2;
                }

                if (smooth && this.isSmooth())
                    img = image.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);

                g2.drawImage(img, x, y, w, h, null);
            }
        }
    }

    public ImageIcon getImage() {
        return image;
    }

    public void setImage(ImageIcon image) {
        this.image = image;
        this.repaint();
    }

    public boolean isStretch() {
        return stretch;
    }

    public void setStretch(boolean stretch) {
        this.stretch = stretch;
        this.repaint();
    }

    public boolean isCentred() {
        return centred;
    }

    public void setCentred(boolean centred) {
        this.centred = centred;
        this.repaint();
    }

    public boolean isProportional() {
        return proportional;
    }

    public void setProportional(boolean proportional) {
        this.proportional = proportional;
        this.repaint();
    }

    public boolean isSmooth() {
        return smooth;
    }

    public void setSmooth(boolean smooth) {
        this.smooth = smooth;
        this.repaint();
    }

    public boolean isMosaic() {
        return mosaic;
    }

    public void setMosaic(boolean mosaic) {
        this.mosaic = mosaic;
    }
}
