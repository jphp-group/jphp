package org.develnext.jphp.ext.javafx.classes.data;

import com.sun.prism.Graphics;
import com.sun.javafx.sg.prism.NGNode;

public final class NullPeerNGNode extends NGNode {
    @Override
    protected void renderContent(Graphics g) {

    }

    @Override
    protected boolean hasOverlappingContents() {
        return false;
    }
}