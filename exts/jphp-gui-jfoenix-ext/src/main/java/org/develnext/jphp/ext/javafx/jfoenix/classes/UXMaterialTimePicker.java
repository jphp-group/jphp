package org.develnext.jphp.ext.javafx.jfoenix.classes;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import org.develnext.jphp.ext.javafx.classes.UXComboBoxBase;
import org.develnext.jphp.ext.javafx.classes.UXDatePicker;
import org.develnext.jphp.ext.javafx.jfoenix.JFoenixExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Setter;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.time.WrapTime;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

@Namespace(JFoenixExtension.NS)
public class UXMaterialTimePicker extends UXComboBoxBase<JFXTimePicker> {
    interface WrappedInterface {
        @Reflection.Property("overlay") boolean overLay();
    }

    public UXMaterialTimePicker(Environment env, JFXTimePicker wrappedObject) {
        super(env, wrappedObject);
    }

    public UXMaterialTimePicker(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    @Reflection.Signature
    public void __construct() {
        __wrappedObject = new JFXTimePicker();
    }

    @Setter
    public void setFormat(String value) {
        getWrappedObject().setConverter(new CustomConverter(value));
    }

    @Getter
    public String getFormat() {
        initFormat();

        StringConverter<LocalTime> converter = getWrappedObject().getConverter();

        if (converter instanceof CustomConverter) {
            return ((CustomConverter) converter).format;
        }

        return null;
    }

    private void initFormat() {
        if (getWrappedObject().getConverter() == null) {
            getWrappedObject().setConverter(new CustomConverter("HH:mm"));
        }
    }

    @Getter
    public WrapTime getValueAsTime(Environment env) {
        initFormat();

        LocalTime value = getWrappedObject().getValue();

        if (value == null) {
            return null;
        }

        Instant instant = value.atDate(LocalDate.of(0, 0, 0)).
                atZone(ZoneId.systemDefault()).toInstant();

        return new WrapTime(env, Date.from(instant));
    }

    @Setter
    public void setValueAsTime(Environment env, @Reflection.Nullable WrapTime time) {
        initFormat();

        if (time == null) {
            getWrappedObject().setValue(null);
        } else {
            getWrappedObject().setValue(
                    Instant.ofEpochMilli(time.getDate().getTime()).atZone(ZoneId.systemDefault()).toLocalTime()
            );
        }
    }

    @Getter
    public Color getDefaultColor() {
        return (Color) getWrappedObject().getDefaultColor();
    }

    @Setter
    public void setDefaultColor(@Reflection.Nullable Color color) {
        getWrappedObject().setDefaultColor(color);
    }

    @Getter
    public boolean getHourView24() {
        return getWrappedObject().is24HourView();
    }

    @Setter
    public void setHourView24(boolean value) {
        getWrappedObject().setIs24HourView(value);
    }

    @Override
    protected Memory getValue(Environment env) {
        initFormat();

        if (getWrappedObject().getConverter() == null) {
            return StringMemory.valueOf(getWrappedObject().getValue().toString());
        }

        return StringMemory.valueOf(getWrappedObject().getConverter().toString(getWrappedObject().getValue()));
    }

    @Override
    @Reflection.Signature
    protected void setValue(Environment env, Memory value) {
        initFormat();

        if (value.toString().isEmpty()) {
            getWrappedObject().setValue(null);
        } else {
            try {
                if (getWrappedObject().getConverter() != null) {
                    getWrappedObject().setValue(getWrappedObject().getConverter().fromString(value.toString()));
                } else {
                    getWrappedObject().setValue(LocalTime.parse(value.toString()));
                }
            } catch (DateTimeParseException e) {
                getWrappedObject().setValue(null);
            }
        }
    }

    static class CustomConverter extends StringConverter<LocalTime> {
        private final DateTimeFormatter dateTimeFormatter;
        protected final String format;

        CustomConverter(String format) {
            this.format = format;
            this.dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        }

        @Override
        public String toString(LocalTime localDate)
        {
            if(localDate==null)
                return "";

            return dateTimeFormatter.format(localDate);
        }

        @Override
        public LocalTime fromString(String dateString)
        {
            if(dateString==null || dateString.trim().isEmpty())
            {
                return null;
            }

            return LocalTime.parse(dateString,dateTimeFormatter);
        }
    }
}
