package php.runtime.ext.swing;

import php.runtime.Memory;
import php.runtime.common.collections.map.HashedMap;
import php.runtime.env.CompileScope;
import php.runtime.env.Environment;
import php.runtime.ext.support.Extension;
import php.runtime.ext.swing.classes.*;
import php.runtime.ext.swing.classes.components.*;
import php.runtime.ext.swing.classes.components.support.*;
import php.runtime.ext.swing.classes.components.support.UIElement;
import php.runtime.ext.swing.classes.events.*;
import php.runtime.ext.swing.classes.events.WrapKeyEvent;
import php.runtime.ext.swing.classes.events.WrapMouseEvent;
import php.runtime.ext.swing.event.*;
import php.runtime.ext.swing.loader.*;
import php.runtime.ext.swing.loader.support.BaseTag;
import php.runtime.ext.swing.loader.support.Tag;
import php.runtime.ext.swing.loader.support.propertyreaders.*;
import php.runtime.ext.swing.support.*;
import php.runtime.memory.StringMemory;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class SwingExtension extends Extension {
    public final static String NAMESPACE = "php\\swing\\";

    public final static AtomicLong componentIndex = new AtomicLong();
    public final static WeakHashMap<String, ComponentProperties> allComponents =
            new WeakHashMap<String, ComponentProperties>();

    public final static Map<Class<?>, Class<? extends UIElement>> swingClasses =
            new HashedMap<Class<?>, Class<? extends UIElement>>();

    public final static Map<String, BaseTag> readerTags = new HashMap<String, BaseTag>();
    public final static Map<Class<? extends Component>, PropertyReaders> readers = new HashMap<Class<? extends Component>, PropertyReaders>();
    protected final static Map<Class<? extends Component>, EventProvider> eventProviders = new HashMap<Class<? extends Component>, EventProvider>();
    protected final static Map<String, ButtonGroup> buttonGroups = new HashMap<String, ButtonGroup>();

    @Override
    public String getName() {
        return "Swing";
    }

    @Override
    public String getVersion() {
        return "~";
    }

    @SuppressWarnings("unchecked")
    public void registerPropertyReaders(PropertyReaders readers) {
        SwingExtension.readers.put(readers.getRegisterClass(), readers);
    }

    public void registerReaderTag(BaseTag tag) {
        Tag name = tag.getClass().getAnnotation(Tag.class);
        if (name == null)
            throw new IllegalArgumentException("Invalid tag object without @Tag annotation");

        readerTags.put(name.value(), tag);
    }

    @SuppressWarnings("unchecked")
    public void registerEventProvider(EventProvider eventProvider) {
        eventProviders.put(eventProvider.getComponentClass(), eventProvider);
    }

    public void registerNativeClass(CompileScope scope, Class<? extends UIElement> clazz, Class<?> swingClazz) {
        super.registerNativeClass(scope, clazz);
        swingClasses.put(swingClazz, clazz);
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerNativeClass(scope, WrapUIManager.class);
        registerNativeClass(scope, WrapSwingUtilities.class);
        registerNativeClass(scope, RootObject.class);

        registerNativeClass(scope, WrapTimer.class);
        registerNativeClass(scope, WrapFont.class);
        registerNativeClass(scope, WrapColor.class);
        registerNativeClass(scope, WrapGraphics.class);
        registerNativeClass(scope, WrapImage.class);
        registerNativeClass(scope, WrapBorder.class);

        registerNativeClass(scope, WrapComponentEvent.class);
        registerNativeClass(scope, WrapKeyEvent.class);
        registerNativeClass(scope, WrapMouseEvent.class);
        registerNativeClass(scope, WrapMouseWheelEvent.class);
        registerNativeClass(scope, WrapFocusEvent.class);
        registerNativeClass(scope, WrapCaretEvent.class);
        registerNativeClass(scope, WrapWindowEvent.class);
        registerNativeClass(scope, WrapItemEvent.class);
        registerNativeClass(scope, WrapSimpleEvent.class);

        registerNativeClass(scope, UIElement.class);
        registerNativeClass(scope, UIContainer.class);
        registerNativeClass(scope, UIWindow.class);
        registerNativeClass(scope, UIDialog.class, JDialogX.class);
        registerNativeClass(scope, UIForm.class, JFrameX.class);
        registerNativeClass(scope, UIPanel.class, JPanel.class);
        registerNativeClass(scope, UIImage.class, JImageX.class);

        registerNativeClass(scope, UILabel.class, JLabel.class);

        registerNativeClass(scope, UIAbstractButton.class);
        registerNativeClass(scope, UIButton.class, JButton.class);
        registerNativeClass(scope, UIToggleButton.class, JToggleButton.class);
        registerNativeClass(scope, UIRadioButton.class, JRadioButton.class);
        registerNativeClass(scope, UICheckbox.class, JCheckBox.class);
        registerNativeClass(scope, UIScrollPanel.class, JScrollPanel.class);

        registerNativeClass(scope, UITextElement.class);
        registerNativeClass(scope, UIEdit.class, JTextFieldX.class);
        registerNativeClass(scope, UITextArea.class, JTextAreaX.class);
        registerNativeClass(scope, UIEditorArea.class, JEditorPaneX.class);
        registerNativeClass(scope, UICombobox.class, JComboBox.class);
        registerNativeClass(scope, UIListbox.class, JListbox.class);
        registerNativeClass(scope, UISlider.class, JSlider.class);
        registerNativeClass(scope, UIProgress.class, JProgressBar.class);
        registerNativeClass(scope, UIUnknown.class, Component.class);

        registerNativeClass(scope, UIMenuBar.class, JMenuBar.class);
        registerNativeClass(scope, UIMenuItem.class, JMenuItem.class);
        registerNativeClass(scope, UICheckboxMenuItem.class, JCheckBoxMenuItem.class);
        registerNativeClass(scope, UIMenu.class, JMenu.class);
        registerNativeClass(scope, UIPopupMenu.class, JPopupMenu.class);

        registerNativeClass(scope, UIFileChooser.class, JFileChooser.class);
        registerNativeClass(scope, UIColorChooser.class, JColorChooser.class);
        registerNativeClass(scope, UIToolBar.class, JToolBar.class);

        registerNativeClass(scope, WrapUIReader.class);

        registerEventProvider(new ComponentEventProvider());
        registerEventProvider(new WindowEventProvider());
        registerEventProvider(new JTextComponentEventProvider());
        registerEventProvider(new JTextAreaEventProvider());
        registerEventProvider(new JComboBoxEventProvider());
        registerEventProvider(new JSliderEventProvider());
        registerEventProvider(new JEditorPaneXEventProvider());

        registerReaderTag(new IncludeTag());
        registerReaderTag(new UIFormTag());
        registerReaderTag(new UIDialogTag());
        registerReaderTag(new UIButtonTag());
        registerReaderTag(new UIToggleButtonTag());
        registerReaderTag(new UICheckboxTag());
        registerReaderTag(new UIEditTag());
        registerReaderTag(new UITextAreaTag());
        registerReaderTag(new UILabelTag());
        registerReaderTag(new UIImageTag());
        registerReaderTag(new UIPanelTag());
        registerReaderTag(new UIScrollPanelTag());
        registerReaderTag(new UIComboboxTag());
        registerReaderTag(new UIListboxTag());
        registerReaderTag(new UIRadioButtonTag());
        registerReaderTag(new UISliderTag());
        registerReaderTag(new UIEditorAreaTag());
        registerReaderTag(new UIMenuItemTag());
        registerReaderTag(new UICheckboxMenuItemTag());
        registerReaderTag(new UIMenuTag());
        registerReaderTag(new UIMenuBarTag());
        registerReaderTag(new UIPopupMenuTag());
        registerReaderTag(new UIToolBarTag());
        registerReaderTag(new UIProgressTag());

        registerPropertyReaders(new ComponentPropertyReaders());
        registerPropertyReaders(new JComponentPropertyReaders());
        registerPropertyReaders(new ContainerPropertyReaders());
        registerPropertyReaders(new WindowPropertyReaders());
        registerPropertyReaders(new AbstractButtonPropertyReaders());
        registerPropertyReaders(new JTextComponentPropertyReaders());
        registerPropertyReaders(new JLabelPropertyReaders());
        registerPropertyReaders(new JImageXPropertyReaders());
        registerPropertyReaders(new JTextAreaPropertyReaders());
        registerPropertyReaders(new JScrollPanelPropertyReaders());
        registerPropertyReaders(new JComboboxPropertyReaders());
        registerPropertyReaders(new JListboxPropertyReaders());
        registerPropertyReaders(new JSliderPropertyReaders());
        registerPropertyReaders(new JEditorPaneXPropertyReaders());
        registerPropertyReaders(new JToolBarPropertyReaders());
        registerPropertyReaders(new JProgressBarPropertyReaders());
    }

    @Override
    public void onLoad(Environment env) {

    }

    public static void registerComponent(Component component){
        component.setName(String.valueOf(componentIndex.getAndIncrement()));
        allComponents.put(component.getName(), new ComponentProperties(component));
    }

    public static EventProvider getEventProvider(Class<? extends Component> clazz) {
        return eventProviders.get(clazz);
    }

    public static EventProvider isAllowedEventType(Component component, String code) {
        Class cls = component.getClass();
        do {
            EventProvider eventProvider = getEventProvider(cls);
            if (eventProvider != null && eventProvider.isAllowedEventType(component, code))
                return eventProvider;

            if (cls == Component.class)
                break;
            cls = cls.getSuperclass();
        } while (true);
        return null;
    }

    public static ComponentProperties getProperties(Component component){
        return allComponents.get(component.getName());
    }

    public static ComponentProperties getProperties(String uid){
        return allComponents.get(uid);
    }

    public static ButtonGroup getOrCreateButtonGroup(String name) {
        name = name.toLowerCase();
        ButtonGroup group = buttonGroups.get(name);
        if (group == null) {
            synchronized (buttonGroups) {
                group = buttonGroups.get(name);
                if (group == null) {
                    group = new ButtonGroup();
                    buttonGroups.put(name, group);
                }
            }
        }
        return group;
    }

    public static Memory fromDirection(int r){
        switch (r){
            case SwingConstants.CENTER: return new StringMemory("center");
            case SwingConstants.LEFT: return new StringMemory("left");
            case SwingConstants.RIGHT: return new StringMemory("right");
            case SwingConstants.TOP: return new StringMemory("top");
            case SwingConstants.BOTTOM: return new StringMemory("bottom");
            case SwingConstants.LEADING: return new StringMemory("leading");
            case SwingConstants.TRAILING: return new StringMemory("trailing");
        }
        return Memory.NULL;
    }

    public static int toDirection(Memory arg){
        if (arg.isNumber())
            return arg.toInteger();
        else {
            Memory longm = StringMemory.toLong(arg.toString());
            if (longm != null)
                return longm.toInteger();

            String nm = arg.toString().toLowerCase();
            if ("center".equals(nm))
                return SwingConstants.CENTER;

            if ("left".equals(nm))
                return SwingConstants.LEFT;

            if ("right".equals(nm))
                return SwingConstants.RIGHT;

            if ("top".equals(nm))
                return SwingConstants.TOP;

            if ("bottom".equals(nm))
                return SwingConstants.BOTTOM;

            if ("leading".equals(nm))
                return SwingConstants.LEADING;

            if ("trailing".equals(nm))
                return SwingConstants.TRAILING;

            return -1;
        }
    }
}
