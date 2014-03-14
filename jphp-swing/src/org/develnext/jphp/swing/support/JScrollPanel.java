package org.develnext.jphp.swing.support;

import javax.swing.*;

public class JScrollPanel extends JScrollPane {

    public enum ScrollPolicy { ALWAYS, HIDDEN, AUTO }

    public void setVerScrollPolicy(ScrollPolicy type) {
        switch (type) {
            case ALWAYS: setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS); break;
            case HIDDEN: setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER); break;
            case AUTO: setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED); break;
        }
    }

    public ScrollPolicy getVerScrollPolicy(){
        switch (getVerticalScrollBarPolicy()){
            case VERTICAL_SCROLLBAR_ALWAYS:
                return ScrollPolicy.ALWAYS;
            case VERTICAL_SCROLLBAR_NEVER:
                return ScrollPolicy.HIDDEN;
            default:
                return ScrollPolicy.AUTO;
        }
    }

    public void setHorScrollPolicy(ScrollPolicy type) {
        switch (type) {
            case ALWAYS: setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_ALWAYS); break;
            case HIDDEN: setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER); break;
            case AUTO: setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED); break;
        }
    }

    public ScrollPolicy getHorScrollPolicy(){
        switch (getVerticalScrollBarPolicy()){
            case HORIZONTAL_SCROLLBAR_ALWAYS:
                return ScrollPolicy.ALWAYS;
            case HORIZONTAL_SCROLLBAR_NEVER:
                return ScrollPolicy.HIDDEN;
            default:
                return ScrollPolicy.AUTO;
        }
    }
}
