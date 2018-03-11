package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.time.WrapTime;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

@Name(JavaFXExtension.NS + "UXDatePicker")
public class UXDatePicker extends UXComboBoxBase<DatePicker> {
    interface WrappedInterface {
        @Property
        TextField editor();

        @Property
        boolean showWeekNumbers();
    }

    public UXDatePicker(Environment env, DatePicker wrappedObject) {
        super(env, wrappedObject);
    }

    public UXDatePicker(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new DatePicker();
    }

    private void initFormat() {
        if (getWrappedObject().getConverter() == null) {
            getWrappedObject().setConverter(new CustomConverter("yyyy-MM-dd"));
        }
    }

    @Setter
    public void setFormat(String value) {
        getWrappedObject().setConverter(new CustomConverter(value));
    }

    @Getter
    public String getFormat() {
        initFormat();

        StringConverter<LocalDate> converter = getWrappedObject().getConverter();

        if (converter instanceof CustomConverter) {
            return ((CustomConverter) converter).format;
        }

        return null;
    }

    @Getter
    public WrapTime getValueAsTime(Environment env) {
        initFormat();

        LocalDate value = getWrappedObject().getValue();

        if (value == null) {
            return null;
        }

        return new WrapTime(env, Date.from(value.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
    }

    @Setter
    public void setValueAsTime(Environment env, @Nullable WrapTime time) {
        initFormat();

        if (time == null) {
            getWrappedObject().setValue(null);
        } else {
            getWrappedObject().setValue(
                    Instant.ofEpochMilli(time.getDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate()
            );
        }
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
    @Signature
    protected void setValue(Environment env, Memory value) {
        initFormat();

        if (value.toString().isEmpty()) {
            getWrappedObject().setValue(null);
        } else {
            try {
                if (getWrappedObject().getConverter() != null) {
                    getWrappedObject().setValue(getWrappedObject().getConverter().fromString(value.toString()));
                } else {
                    getWrappedObject().setValue(LocalDate.parse(value.toString()));
                }
            } catch (DateTimeParseException e) {
                getWrappedObject().setValue(null);
            }
        }
    }

    static class CustomConverter extends StringConverter<LocalDate> {
        private final DateTimeFormatter dateTimeFormatter;
        protected final String format;

        CustomConverter(String format) {
            this.format = format;
            this.dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        }

        @Override
        public String toString(LocalDate localDate)
        {
            if(localDate==null)
                return "";

            return dateTimeFormatter.format(localDate);
        }

        @Override
        public LocalDate fromString(String dateString)
        {
            if(dateString==null || dateString.trim().isEmpty())
            {
                return null;
            }
            return LocalDate.parse(dateString,dateTimeFormatter);
        }
    }
}
