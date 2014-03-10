package php.runtime.ext.swing.support;

import javax.swing.*;
import java.awt.*;

public class JListbox extends JScrollableComponent<JList> {

    @Override
    protected JList newComponent() {
        JList l = new JList(new DefaultListModel());
        l.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return l;
    }

    public void setSelectionMode(int mode){
        component.setSelectionMode(mode);
    }

    public int getSelectionMode() {
        return component.getSelectionMode();
    }

    public void setSelectedIndex(int index) {
        component.setSelectedIndex(index);
    }

    public boolean isMultiple() {
        return component.getSelectionMode() == ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
    }

    public void setMultiple(boolean value) {
        component.setSelectionMode(value
                ? ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
                : ListSelectionModel.SINGLE_SELECTION);
    }

    public int getSelectedIndex(){
        return component.getSelectedIndex();
    }

    public void setVisibleRowCount(int cnt) {
        component.setVisibleRowCount(cnt);
    }

    public int getVisibleRowCount() {
        return component.getVisibleRowCount();
    }

    public int getMaxSelectionIndex() {
        return component.getMaxSelectionIndex();
    }

    public int getMinSelectionIndex() {
        return component.getMinSelectionIndex();
    }

    public ListModel<String> getModel() {
        return component.getModel();
    }

    public void setSelectedIndices(int[] values) {
        component.setSelectedIndices(values);
    }

    public int[] getSelectedIndices() {
        return component.getSelectedIndices();
    }

    public Color getSelectionBackground() {
        return component.getSelectionBackground();
    }

    public void setSelectionBackground(Color color) {
        component.setSelectionBackground(color);
    }

    public Color getSelectionForeground() {
        return component.getSelectionForeground();
    }

    public void setSelectionForeground(Color color) {
        component.setSelectionForeground(color);
    }


}
