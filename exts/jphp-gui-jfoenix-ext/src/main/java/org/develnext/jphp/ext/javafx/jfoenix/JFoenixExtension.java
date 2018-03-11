package org.develnext.jphp.ext.javafx.jfoenix;

import com.jfoenix.controls.*;
import javafx.scene.control.Tab;
import org.develnext.jphp.ext.javafx.jfoenix.classes.*;
import org.develnext.jphp.ext.javafx.jfoenix.event.JFXTabPaneEventProvider;
import org.develnext.jphp.ext.javafx.jfoenix.support.JFXTabPaneFixed;
import org.develnext.jphp.ext.javafx.jfoenix.support.JFXTextAreaFixed;
import org.develnext.jphp.ext.javafx.support.EventProvider;
import org.develnext.jphp.ext.javafx.support.event.TabEventProvider;
import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;

public class JFoenixExtension extends Extension {
    public final static String NS = "php\\gui";

    @Override
    public Status getStatus() {
        return Status.EXPERIMENTAL;
    }

    @Override
    public String[] getPackageNames() {
        return new String[] { "gui", "javafx" };
    }

    @Override
    public void onRegister(CompileScope compileScope) {
        registerWrapperClass(compileScope, JFXButton.class, UXMaterialButton.class);
        registerWrapperClass(compileScope, JFXToggleButton.class, UXMaterialToggleButton.class);
        registerWrapperClass(compileScope, JFXCheckBox.class, UXMaterialCheckbox.class);
        registerWrapperClass(compileScope, JFXComboBox.class, UXMaterialComboBox.class);
        registerWrapperClass(compileScope, JFXTextField.class, UXMaterialTextField.class);
        registerWrapperClass(compileScope, JFXPasswordField.class, UXMaterialPasswordField.class);
        registerWrapperClass(compileScope, JFXTextAreaFixed.class, UXMaterialTextArea.class);
        registerWrapperClass(compileScope, JFXProgressBar.class, UXMaterialProgressBar.class);
        registerWrapperClass(compileScope, JFXTabPaneFixed.class, UXMaterialTabPane.class);
        registerWrapperClass(compileScope, JFXColorPicker.class, UXMaterialColorPicker.class);
        registerWrapperClass(compileScope, JFXDatePicker.class, UXMaterialDatePicker.class);
        registerWrapperClass(compileScope, JFXTimePicker.class, UXMaterialTimePicker.class);
        registerWrapperClass(compileScope, JFXSlider.class, UXMaterialSlider.class);
        registerWrapperClass(compileScope, JFXSpinner.class, UXMaterialProgressIndicator.class);

        EventProvider.register(new JFXTabPaneEventProvider());
    }
}
