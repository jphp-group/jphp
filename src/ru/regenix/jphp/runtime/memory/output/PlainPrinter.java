package ru.regenix.jphp.runtime.memory.output;

import java.io.Writer;

public class PlainPrinter extends PrintR {

    public PlainPrinter(Writer writer) {
        super(writer);
        PRINT_INDENT = 0;
    }

    @Override
    protected void writeArrayHeader() {
        printer.write("Array ");
    }

    @Override
    protected void writeObjectHeader(String name) {
        printer.write(name);
        printer.write(" Object ");
    }

    @Override
    protected void writeOpen() {
        printer.write("(");
    }

    @Override
    protected void writeClose() {
        printer.write(")");
    }

    @Override
    protected void writeSeparator(boolean isLast) {
        if (!isLast)
            printer.write(",");
    }
}
