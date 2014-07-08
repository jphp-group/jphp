package org.develnext.jphp.swing;

import php.runtime.Memory;
import php.runtime.common.StringUtils;
import php.runtime.env.Environment;
import org.develnext.jphp.swing.classes.events.*;
import org.develnext.jphp.swing.event.EventProvider;
import org.develnext.jphp.swing.misc.Align;
import org.develnext.jphp.swing.misc.Anchor;
import org.develnext.jphp.swing.misc.EventContainer;
import org.develnext.jphp.swing.misc.EventList;
import php.runtime.invoke.Invoker;
import php.runtime.memory.ObjectMemory;

import javax.swing.event.CaretEvent;
import java.awt.*;
import java.awt.event.*;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ComponentProperties {
    protected final WeakReference<Component> ref;
    protected String originGroups;
    protected Set<String> groups;
    protected int[] padding = new int[] {0, 0, 0, 0};
    public final Set<Anchor> anchors;

    protected Align align = Align.NONE;
    protected boolean autoSize;

    public final EventContainer eventContainer;
    protected boolean initEvents;

    protected Map<String, Object> data;

    public ComponentProperties(Component ref) {
        this.ref = new WeakReference<Component>(ref);
        this.groups = new HashSet<String>();
        this.eventContainer = new EventContainer();

        this.anchors = new HashSet<Anchor>();
        this.anchors.add(Anchor.LEFT);
        this.anchors.add(Anchor.TOP);
    }

    public String getOriginGroups() {
        return originGroups;
    }

    @SuppressWarnings("unchecked")
    public <T> T getData(String name, Class<T> clazz) {
        if (data == null)
            return null;

        return (T) data.get(name);
    }

    public void setData(String name, Object value) {
        if (data == null)
            data = new HashMap<String, Object>();

        data.put(name, value);
    }

    public void setGroups(String value){
        originGroups = value.trim();
        groups.clear();
        for(String group : StringUtils.split(originGroups, " ")){
            group = group.trim();
            if (group.isEmpty())
                continue;

            groups.add(group.toLowerCase());
        }
    }

    public boolean hasGroup(String group){
        return groups.contains(group.toLowerCase());
    }

    public boolean isValid(){
        return ref.get() != null;
    }

    public Component getComponent(){
        return ref.get();
    }

    public Align getAlign() {
        return align;
    }

    public void setAlign(Align align) {
        this.align = align == null ? Align.NONE : align;
    }

    public boolean isAutoSize() {
        return autoSize;
    }

    public void setAutoSize(boolean autoSize) {
        this.autoSize = autoSize;
    }

    public void setPadding(int top, int right, int bottom, int left) {
        this.padding = new int[] { top, right, bottom, left };
    }

    public int[] getPadding() {
        return padding;
    }

    public void updateEvents(final Environment env){
        if (!initEvents) {
            Component component = getComponent();

            Class<?> cls = component.getClass();
            do {
                EventProvider provider = SwingExtension.getEventProvider((Class<? extends Component>) cls);
                if (provider != null) {
                    provider.register(env, component, this);
                }

                if (cls == Component.class)
                    break;

                cls = cls.getSuperclass();
            } while (true);
            initEvents = true;
        }
    }

    public void triggerEvent(String name, Memory... args) {
        EventList list = eventContainer.get(name);
        if (list != null)
            for (Invoker invoker : list.values()){
                if (invoker != null){
                    invoker.callNoThrow(args);
                }
            }
    }

    protected void triggerMouse(Environment env, String name, MouseEvent e){
        EventList list = eventContainer.get(name);
        if (list != null && !list.isEmpty())
            triggerEvent(name, new ObjectMemory(WrapMouseEvent.of(env, e)));
    }

    protected void triggerMouseWheel(Environment env, String name, MouseEvent e){
        EventList list = eventContainer.get(name);
        if (list != null && !list.isEmpty())
            triggerEvent(name, new ObjectMemory(WrapMouseWheelEvent.of(env, e)));
    }

    protected void triggerFocus(Environment env, String name, FocusEvent e){
        EventList list = eventContainer.get(name);
        if (list != null && !list.isEmpty())
            triggerEvent(name, new ObjectMemory(WrapFocusEvent.of(env, e)));
    }

    protected void triggerKey(Environment env, String name, KeyEvent e){
        EventList list = eventContainer.get(name);
        if (list != null && !list.isEmpty())
            triggerEvent(name, new ObjectMemory(WrapKeyEvent.of(env, e)));
    }

    protected void triggerCaret(Environment env, String name, CaretEvent e){
        EventList list = eventContainer.get(name);
        if (list != null && !list.isEmpty())
            triggerEvent(name, new ObjectMemory(new WrapCaretEvent(env, e)));
    }

    protected void triggerWindow(Environment env, String name, WindowEvent e){
        EventList list = eventContainer.get(name);
        if (list != null && !list.isEmpty())
            triggerEvent(name, new ObjectMemory(WrapWindowEvent.of(env, e)));
    }

    protected void triggerItem(Environment env, String name, ItemEvent e) {
        EventList list = eventContainer.get(name);
        if (list != null && !list.isEmpty())
            triggerEvent(name, new ObjectMemory(new WrapItemEvent(env, e)));
    }
}
