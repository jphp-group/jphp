package org.develnext.jphp.ext.javafx.classes.printing;

import javafx.print.*;
import javafx.scene.Node;
import javafx.stage.Window;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NS + "printing\\UXPrinterJob")
public class UXPrinterJob extends BaseWrapper<PrinterJob> {
    interface WrappedInterface {
        boolean showPrintDialog(Window owner);
        boolean showPageSetupDialog(Window owner);

        PrinterJob.JobStatus getJobStatus();
    }

    public UXPrinterJob(Environment env, PrinterJob wrappedObject) {
        super(env, wrappedObject);
    }

    public UXPrinterJob(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(UXPrinter printer) {
        __wrappedObject = PrinterJob.createPrinterJob(printer.getWrappedObject());
    }

    @Signature
    public boolean print(Node node) {
        return getWrappedObject().printPage(node);
    }

    @Signature
    public void cancel() {
        getWrappedObject().cancelJob();
    }

    @Signature
    public boolean end() {
        return getWrappedObject().endJob();
    }

    @Signature
    public UXPrinter getPrinter(Environment env) {
        return new UXPrinter(env, getWrappedObject().getPrinter());
    }

    @Signature
    public String __toString() {
        return getWrappedObject().toString();
    }

    @Signature
    public void setSettings(ArrayMemory value) {
        JobSettings settings = getWrappedObject().getJobSettings();

        if (value.valueOfIndex("jobName").toBoolean())
            settings.setJobName(value.valueOfIndex("jobName").toString());

        if (value.containsKey("copies"))
            settings.setCopies(value.valueOfIndex("copies").toInteger());

        if (value.containsKey("collation"))
            settings.setCollation(Collation.valueOf(value.valueOfIndex("collation").toString()));

        if (value.containsKey("printSides"))
            settings.setPrintSides(PrintSides.valueOf(value.valueOfIndex("printSides").toString()));

        if (value.containsKey("printColor"))
            settings.setPrintColor(PrintColor.valueOf(value.valueOfIndex("printColor").toString()));

        if (value.containsKey("printQuality"))
            settings.setPrintQuality(PrintQuality.valueOf(value.valueOfIndex("printQuality").toString()));
    }
}
