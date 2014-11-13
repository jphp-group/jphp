package org.develnext.jphp.core.opcode;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.TraceClassVisitor;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.FunctionEntity;
import php.runtime.reflection.ModuleEntity;
import php.runtime.reflection.helper.ClosureEntity;

import java.io.*;

public class OpcodePrinter {

    private ClassReader classReader;

    private OpcodePrinter(byte[] data){
        classReader = new ClassReader(data);
    }

    public OpcodePrinter(ModuleEntity module){
        this(module.getData());
    }

    public OpcodePrinter(ClassEntity clazz){
        this(clazz.getData());
    }

    public OpcodePrinter(ClosureEntity closure){
        this(closure.getData());
    }

    public OpcodePrinter(FunctionEntity function){
        this(function.getData());
    }

    public void toFile(File file, int flags) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        try {
            toWriter(fileWriter, flags);
        } finally {
            fileWriter.close();
        }
    }

    public void toFile(File file) throws IOException {
        toFile(file, ClassReader.SKIP_DEBUG);
    }

    public String toString(int flags){
        StringWriter stringWriter = new StringWriter();
        toWriter(stringWriter, flags);
        return stringWriter.toString();
    }

    @Override
    public String toString(){
        return toString(ClassReader.SKIP_DEBUG);
    }

    public void toWriter(Writer writer, int flags){
        PrintWriter printWriter = new PrintWriter(writer);
        TraceClassVisitor traceClassVisitor = new TraceClassVisitor(null, printWriter);
        classReader.accept(traceClassVisitor, flags);
    }

    public void toWriter(Writer writer){
        toWriter(writer, ClassReader.SKIP_DEBUG);
    }
}

