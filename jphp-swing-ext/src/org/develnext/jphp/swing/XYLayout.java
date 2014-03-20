package org.develnext.jphp.swing;

import org.develnext.jphp.swing.misc.Align;
import org.develnext.jphp.swing.misc.Anchor;

import java.awt.*;
import java.util.HashMap;

public class XYLayout implements LayoutManager2 {
    protected final HashMap<Container, ContainerData> containerData;

    public XYLayout() {
        containerData = new HashMap<Container, ContainerData>();
    }

    public void addLayoutComponent(String name, Component component) {
    }

    public void addLayoutComponent(Component component, Object constraints) {
    }

    public void removeLayoutComponent(Component component) {
    }

    public Dimension preferredLayoutSize(Container target) {
        return new Dimension(0, 0);
    }

    public Dimension minimumLayoutSize(Container target) {
        return new Dimension(0, 0);
    }

    public void layoutContainer(Container target) {
        ContainerData data = containerData.get(target);
        if (data == null) {
            containerData.put(target, data = new ContainerData());
        }

        ComponentProperties targetInfo = SwingExtension.getProperties(target);

        int offsetW = target.getWidth() - data.lastWidth;
        int offsetH = target.getHeight() - data.lastHeight;

        Insets insets = target.getInsets();
        int count = target.getComponentCount();

        int clientTop = 0;
        int clientBottom = target.getHeight() - (insets.top + insets.bottom);
        int clientLeft = 0;
        int clientRight = target.getWidth() - (insets.left + insets.right);

        int maxWidth = 0;
        int maxHeight = 0;

        for (int i = 0; i < count; i++) {
            Component component = target.getComponent(i);
            if (component.isVisible()) {
                ComponentProperties info = SwingExtension.getProperties(component);

                if (info == null/* || data.lastHeight == -1 && data.lastWidth == -1*/) {
                    component.setBounds(
                            insets.left + component.getX(),
                            insets.top + component.getY(),
                            component.getWidth(),
                            component.getHeight());
                } else {
                    int x, y, w, h;
                    Align align = info.getAlign();

                    boolean aLeft = info.anchors.contains(Anchor.LEFT);
                    boolean aRight = info.anchors.contains(Anchor.RIGHT);
                    boolean aTop = info.anchors.contains(Anchor.TOP);
                    boolean aBottom = info.anchors.contains(Anchor.BOTTOM);

                    w = component.getWidth();
                    if (aLeft && aRight) {
                        x = insets.left + component.getX();
                        w = component.getWidth() + offsetW;
                    } else if (aLeft) {
                        x = insets.left + component.getX();
                    } else if (aRight) {
                        x = insets.left + component.getX() + offsetW;
                    } else {
                        x = insets.left + component.getX() + offsetW / 2;
                    }

                    h = component.getHeight();
                    if (aTop && aBottom) {
                        y = insets.top + component.getY();
                        h = component.getHeight() + offsetH;
                    } else if (aTop) {
                        y = insets.top + component.getY();
                    } else if (aBottom) {
                        y = insets.top + component.getY() + offsetH;
                    } else {
                        y = insets.top + component.getY() + offsetH / 2;
                    }

                    switch (align) {
                        case TOP:
                            x = clientLeft;
                            y = clientTop;
                            w = clientRight - clientLeft;

                            clientTop += h;
                            break;
                        case BOTTOM:
                            x = clientLeft;
                            y = clientBottom - h;
                            w = clientRight - clientLeft;

                            clientBottom -= h;
                            break;
                        case LEFT:
                            x = clientLeft;
                            y = clientTop;
                            h = clientBottom - clientTop;

                            clientLeft += w;
                            break;
                        case RIGHT:
                            x = clientRight - w;
                            y = clientTop;
                            h = clientBottom - clientTop;

                            clientRight -= w;
                            break;
                        case CLIENT:
                            x = clientLeft;
                            y = clientTop;
                            w = clientRight - clientLeft;
                            h = clientBottom - clientTop;
                            break;
                    }
                    component.setBounds(x, y, w, h);
                    component.setPreferredSize(new Dimension(w, h));

                    if (targetInfo != null && targetInfo.autoSize) {
                        if (x + w > maxWidth)
                            maxWidth = x + w;

                        if (y + h > maxHeight)
                            maxHeight = y + h;
                    }
                }
            }
        }

        data.lastHeight = target.getHeight();
        data.lastWidth = target.getWidth();

        if (targetInfo != null && targetInfo.autoSize) {
            target.setPreferredSize(new Dimension(maxWidth, maxHeight));
            target.setSize(maxWidth, maxHeight);
        }
    }

    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(2147483647, 2147483647);
    }

    public float getLayoutAlignmentX(Container target) {
        return 0.5F;
    }

    public float getLayoutAlignmentY(Container target) {
        return 0.5F;
    }

    public void invalidateLayout(Container target) {

    }

    protected class ContainerData {
        public int lastWidth = -1;
        public int lastHeight = -1;
    }
}
