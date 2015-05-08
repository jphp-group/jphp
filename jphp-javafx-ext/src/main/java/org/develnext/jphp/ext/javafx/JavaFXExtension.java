package org.develnext.jphp.ext.javafx;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import org.develnext.jphp.ext.javafx.bind.KeyCombinationMemoryOperation;
import org.develnext.jphp.ext.javafx.classes.*;
import org.develnext.jphp.ext.javafx.classes.event.*;
import org.develnext.jphp.ext.javafx.classes.layout.UXAnchorPane;
import org.develnext.jphp.ext.javafx.classes.layout.UXPane;
import org.develnext.jphp.ext.javafx.classes.layout.UXStackPane;
import org.develnext.jphp.ext.javafx.classes.paint.UXColor;
import org.develnext.jphp.ext.javafx.classes.text.UXFont;
import org.develnext.jphp.ext.javafx.support.EventProvider;
import org.develnext.jphp.ext.javafx.support.event.*;
import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;
import php.runtime.memory.support.MemoryOperation;

import java.awt.event.InputEvent;
import java.util.HashMap;
import java.util.Map;

public class JavaFXExtension extends Extension {
    public final static String NAMESPACE = "ext\\javafx\\";

    protected static final Map<String, EventProvider> eventProviders = new HashMap<String, EventProvider>();

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerMemoryOperation(KeyCombinationMemoryOperation.class);

        registerWrapperClass(scope, ObservableList.class, UXList.class);
        registerWrapperClass(scope, Application.class, UXApplication.class);

        registerWrapperClass(scope, Font.class, UXFont.class);
        registerWrapperClass(scope, Color.class, UXColor.class);
        registerWrapperClass(scope, Image.class, UXImage.class);

        registerWrapperClass(scope, Window.class, UXWindow.class);
        registerWrapperClass(scope, Stage.class, UXStage.class);
        registerWrapperClass(scope, PopupWindow.class, UXPopupWindow.class);
        registerWrapperClass(scope, Tooltip.class, UXTooltip.class);
        registerWrapperClass(scope, ContextMenu.class, UXContextMenu.class);
        registerWrapperClass(scope, MenuItem.class, UXMenuItem.class);
        registerWrapperClass(scope, Scene.class, UXScene.class);

        registerWrapperClass(scope, Node.class, UXNode.class);
        registerWrapperClass(scope, Parent.class, UXParent.class);
        registerWrapperClass(scope, Control.class, UXControl.class);

        registerWrapperClass(scope, Pane.class, UXPane.class);
        registerWrapperClass(scope, AnchorPane.class, UXAnchorPane.class);
        registerWrapperClass(scope, StackPane.class, UXStackPane.class);

        registerWrapperClass(scope, Labeled.class, UXLabeled.class);
        registerWrapperClass(scope, ButtonBase.class, UXButtonBase.class);
        registerWrapperClass(scope, Button.class, UXButton.class);
        registerWrapperClass(scope, CheckBox.class, UXCheckbox.class);
        registerWrapperClass(scope, ImageView.class, UXImageView.class);
        registerWrapperClass(scope, MenuBar.class, UXMenuBar.class);
        registerWrapperClass(scope, TextInputControl.class, UXTextInputControl.class);
        registerWrapperClass(scope, TextArea.class, UXTextArea.class);
        registerWrapperClass(scope, TextField.class, UXTextField.class);
        registerWrapperClass(scope, PasswordField.class, UXPasswordField.class);
        registerWrapperClass(scope, Label.class, UXLabel.class);
        registerWrapperClass(scope, Hyperlink.class, UXHyperlink.class);
        registerWrapperClass(scope, ComboBoxBase.class, UXComboBoxBase.class);
        registerWrapperClass(scope, ComboBox.class, UXComboBox.class);
        registerWrapperClass(scope, ColorPicker.class, UXColorPicker.class);
        registerWrapperClass(scope, ProgressIndicator.class, UXProgressIndicator.class);
        registerWrapperClass(scope, ProgressBar.class, UXProgressBar.class);
        registerWrapperClass(scope, HTMLEditor.class, UXHtmlEditor.class);
        registerWrapperClass(scope, WebEngine.class, UXWebEngine.class);
        registerWrapperClass(scope, WebView.class, UXWebView.class);

        MemoryOperation.registerWrapper(InputEvent.class, UXEvent.class);
        MemoryOperation.registerWrapper(ActionEvent.class, UXEvent.class);
        MemoryOperation.registerWrapper(InputMethodEvent.class, UXEvent.class);
        registerWrapperClass(scope, Event.class, UXEvent.class);

        registerWrapperClass(scope, MouseEvent.class, UXMouseEvent.class);
        registerWrapperClass(scope, WindowEvent.class, UXWindowEvent.class);
        registerWrapperClass(scope, ContextMenuEvent.class, UXContextMenuEvent.class);
        registerWrapperClass(scope, DragEvent.class, UXDragEvent.class);

        registerWrapperClass(scope, FXMLLoader.class, UXLoader.class);

        registerEvents();
    }

    protected void registerEvents() {
        registerEventProvider(new NodeEventProvider());
        registerEventProvider(new WindowEventProvider());
        registerEventProvider(new ContextMenuEventProvider());
        registerEventProvider(new MenuItemEventProvider());
        registerEventProvider(new MenuEventProvider());
        registerEventProvider(new ButtonBaseEventProvider());
        registerEventProvider(new ComboBoxBaseEventProvider());
    }

    protected void registerEventProvider(EventProvider eventProvider) {
        EventProvider.register(eventProvider);
    }
}
