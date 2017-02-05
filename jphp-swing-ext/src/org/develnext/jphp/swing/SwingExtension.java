package org.develnext.jphp.swing;

import org.develnext.jphp.swing.classes.*;
import org.develnext.jphp.swing.classes.components.*;
import org.develnext.jphp.swing.classes.components.support.*;
import org.develnext.jphp.swing.classes.components.text.WrapStyle;
import org.develnext.jphp.swing.classes.components.tree.UITree;
import org.develnext.jphp.swing.classes.components.tree.WrapTreeModel;
import org.develnext.jphp.swing.classes.components.tree.WrapTreeNode;
import org.develnext.jphp.swing.classes.events.*;
import org.develnext.jphp.swing.event.*;
import org.develnext.jphp.swing.loader.*;
import org.develnext.jphp.swing.loader.support.BaseTag;
import org.develnext.jphp.swing.loader.support.Tag;
import org.develnext.jphp.swing.loader.support.propertyreaders.*;
import org.develnext.jphp.swing.support.*;
import php.runtime.Memory;
import php.runtime.common.collections.map.HashedMap;
import php.runtime.env.CompileScope;
import php.runtime.env.Environment;
import php.runtime.ext.support.Extension;
import php.runtime.memory.StringMemory;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class SwingExtension extends Extension {
    public final static String NAMESPACE = "php\\swing\\";

    public final static Scope scope = new Scope();

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
    public Status getStatus() {
        return Status.DEPRECATED;
    }

    @Override
    public String[] getPackageNames() {
        return new String[] { "swing" };
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

    @Deprecated
    public void registerNativeClass(CompileScope scope, Class<? extends UIElement> clazz, Class<?> swingClazz) {
        registerClass(scope, clazz, swingClazz);
    }

    public void registerClass(CompileScope scope, Class<? extends UIElement> clazz, Class<?> swingClazz) {
        super.registerClass(scope, clazz);
        swingClasses.put(swingClazz, clazz);
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerClass(scope, WrapUIManager.class);
        registerClass(scope, WrapSwingUtilities.class);
        registerClass(scope, RootObject.class);
        registerClass(scope, WrapSwingWorker.class);
        /*registerClass(scope, WrapScopeValue.class);
        registerWrapperClass(scope, Scope.class, WrapScope.class);*/

        registerClass(scope, WrapTimer.class);
        registerClass(scope, WrapFont.class);
        registerClass(scope, WrapColor.class);
        registerClass(scope, WrapGraphics.class);
        registerClass(scope, WrapImage.class);
        registerClass(scope, WrapBorder.class);
        registerClass(scope, WrapStyle.class);

        registerClass(scope, WrapComponentEvent.class);
        registerClass(scope, WrapKeyEvent.class);
        registerClass(scope, WrapMouseEvent.class);
        registerClass(scope, WrapMouseWheelEvent.class);
        registerClass(scope, WrapFocusEvent.class);
        registerClass(scope, WrapCaretEvent.class);
        registerClass(scope, WrapWindowEvent.class);
        registerClass(scope, WrapItemEvent.class);
        registerClass(scope, WrapSimpleEvent.class);

        registerClass(scope, UIElement.class);
        registerClass(scope, UIContainer.class);
        registerClass(scope, UIWindow.class);
        registerClass(scope, UIDialog.class, JDialogX.class);
        registerClass(scope, UIForm.class, JFrameX.class);
        registerClass(scope, UIPanel.class, JPanel.class);
        registerClass(scope, UIDesktopPanel.class, JDesktopPane.class);
        registerClass(scope, UIInternalForm.class, JInternalFrame.class);
        registerClass(scope, UIImage.class, JImageX.class);

        registerClass(scope, UILabel.class, JLabel.class);

        registerClass(scope, UIAbstractButton.class);
        registerClass(scope, UIButton.class, JButton.class);
        registerClass(scope, UIToggleButton.class, JToggleButton.class);
        registerClass(scope, UIRadioButton.class, JRadioButton.class);
        registerClass(scope, UICheckbox.class, JCheckBox.class);
        registerClass(scope, UIScrollPanel.class, JScrollPanelX.class);

        registerClass(scope, UITextElement.class);
        registerClass(scope, UIEdit.class, JTextFieldX.class);
        registerClass(scope, UIPasswordEdit.class, JPasswordFieldX.class);
        registerClass(scope, UITextArea.class, JTextAreaX.class);
        registerClass(scope, UIRichTextArea.class, JRichTextAreaX.class);

        registerClass(scope, UIEditorArea.class, JEditorPaneX.class);
        registerClass(scope, UICombobox.class, JComboBox.class);
        registerClass(scope, UIListbox.class, JListbox.class);
        registerClass(scope, UISlider.class, JSlider.class);
        registerClass(scope, UIProgress.class, JProgressBar.class);
        registerClass(scope, UIUnknown.class, Component.class);

        registerClass(scope, UIMenuBar.class, JMenuBar.class);
        registerClass(scope, UIMenuItem.class, JMenuItem.class);
        registerClass(scope, UICheckboxMenuItem.class, JCheckBoxMenuItem.class);
        registerClass(scope, UIMenu.class, JMenu.class);
        registerClass(scope, UIPopupMenu.class, JPopupMenu.class);

        registerClass(scope, UIFileChooser.class, JFileChooser.class);
        registerClass(scope, UIColorChooser.class, JColorChooser.class);
        registerClass(scope, UIToolBar.class, JToolBar.class);

        registerClass(scope, UITree.class, JTreeX.class);
        registerClass(scope, WrapTreeNode.class);
        registerClass(scope, WrapTreeModel.class);
        registerClass(scope, UITable.class, JTableX.class);
        registerClass(scope, UITabs.class, JTabbedPane.class);

        registerClass(scope, WrapUIReader.class);

        registerEventProvider(new ComponentEventProvider());
        registerEventProvider(new WindowEventProvider());
        registerEventProvider(new JTextComponentEventProvider());
        registerEventProvider(new JTextAreaEventProvider());
        registerEventProvider(new JComboBoxEventProvider());
        registerEventProvider(new JSliderEventProvider());
        registerEventProvider(new JEditorPaneXEventProvider());
        registerEventProvider(new JTreeXEventProvider());
        registerEventProvider(new JMenuEventProvider());
        registerEventProvider(new JMenuItemEventProvider());
        registerEventProvider(new JScrollableComponentEventProvider());
        registerEventProvider(new JPopupMenuEventProvider());
        registerEventProvider(new JTabbedPaneEventProvider());

        registerReaderTag(new IncludeTag());
        registerReaderTag(new StyleTag());

        registerReaderTag(new UIFormTag());
        registerReaderTag(new UIDialogTag());
        registerReaderTag(new UIButtonTag());
        registerReaderTag(new UIToggleButtonTag());
        registerReaderTag(new UICheckboxTag());
        registerReaderTag(new UIEditTag());
        registerReaderTag(new UIPasswordEditTag());
        registerReaderTag(new UITextAreaTag());
        registerReaderTag(new UIRichTextAreaTag());
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
        registerReaderTag(new UITreeTag());
        registerReaderTag(new UITableTag());
        registerReaderTag(new UITabsTag());

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
        registerPropertyReaders(new JTreeXPropertyReaders());
        registerPropertyReaders(new JTabbedPanePropertyReaders());
        registerPropertyReaders(new JFramePropertyReaders());
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
        return getProperties(component, false);
    }

    public static ComponentProperties getProperties(Component component, boolean autoCreated){
        if (component.getName() == null) {
            return null;
        }

        ComponentProperties properties = allComponents.get(component.getName());
        if (properties == null && autoCreated) {
            registerComponent(component);
            return allComponents.get(component.getName());
        }
        return properties;
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
