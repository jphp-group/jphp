package org.develnext.jphp.ext.javafx.bind;

import javafx.print.Paper;
import javafx.print.PrintResolution;
import javafx.print.PrinterAttributes;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.support.MemoryOperation;

public class PrinterAttributesMemoryOperation extends MemoryOperation<PrinterAttributes> {
    private final MemoryOperation<Paper> paperConverter = MemoryOperation.get(Paper.class, null);
    private final MemoryOperation<PrintResolution> resolutionConverter = MemoryOperation.get(PrintResolution.class, null);

    @Override
    public Class<?>[] getOperationClasses() {
        return new Class[] { PrinterAttributes.class };
    }

    @Override
    public PrinterAttributes convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        throw new IllegalStateException("Unable to convert memory to " + PrinterAttributes.class.getName());
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, PrinterAttributes attributes) throws Throwable {
        ArrayMemory r = new ArrayMemory();
        r.refOfIndex("defaultCopies").assign(attributes.getDefaultCopies());
        r.refOfIndex("maxCopies").assign(attributes.getMaxCopies());
        r.refOfIndex("defaultCollation").assign(attributes.getDefaultCollation().name());
        r.refOfIndex("defaultPageOrientation").assign(attributes.getDefaultPageOrientation().name());
        r.refOfIndex("defaultPaper").assign(paperConverter.unconvert(env, trace, attributes.getDefaultPaper()));

        r.refOfIndex("defaultPaperSource").assign(attributes.getDefaultPaperSource().getName());
        r.refOfIndex("defaultPrintColor").assign(attributes.getDefaultPrintColor().name());
        r.refOfIndex("defaultPrintQuality").assign(attributes.getDefaultPrintQuality().name());
        r.refOfIndex("defaultPrintResolution").assign(resolutionConverter.unconvert(env, trace, attributes.getDefaultPrintResolution()));

        r.refOfIndex("defaultPrintSides").assign(attributes.getDefaultPrintSides().name());

        return r;
    }
}
