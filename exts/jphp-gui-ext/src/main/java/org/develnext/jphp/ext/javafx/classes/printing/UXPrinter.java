package org.develnext.jphp.ext.javafx.classes.printing;

import javafx.print.Printer;
import javafx.print.PrinterAttributes;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.stage.Window;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import java.util.Set;

@Reflection.Name(JavaFXExtension.NS + "printing\\UXPrinter")
public class UXPrinter extends BaseWrapper<Printer> {
    interface WrappedInterface {
        @Property String name();
    }

    public UXPrinter(Environment env, Printer wrappedObject) {
        super(env, wrappedObject);
    }

    public UXPrinter(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private void __construct() {
    }

    @Signature
    public PrinterJob createPrintJob() {
        return PrinterJob.createPrinterJob(getWrappedObject());
    }

    @Signature
    public boolean printWithDialog(Window owner, Node node) {
        PrinterJob printerJob = PrinterJob.createPrinterJob(getWrappedObject());

        if (printerJob.showPrintDialog(owner)) {
            boolean success = printerJob.printPage(node);

            if (success) {
                printerJob.endJob();
            }

            return success;
        } else {
            return false;
        }
    }

    @Signature
    public boolean print(Node node) {
        PrinterJob printerJob = PrinterJob.createPrinterJob(getWrappedObject());
        boolean success = printerJob.printPage(node);

        if (success) {
            printerJob.endJob();
        }

        return success;
    }

    @Signature
    public static Printer getDefault() {
        return Printer.getDefaultPrinter();
    }

    @Signature
    public static Set<Printer> getAll() {
        return Printer.getAllPrinters();
    }

    @Getter
    public PrinterAttributes getAttributes(Environment env, TraceInfo trace) throws Throwable {
        return getWrappedObject().getPrinterAttributes();
    }
}
