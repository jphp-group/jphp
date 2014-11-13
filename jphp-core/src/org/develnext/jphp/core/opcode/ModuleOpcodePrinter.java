package org.develnext.jphp.core.opcode;


import org.objectweb.asm.ClassReader;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.FunctionEntity;
import php.runtime.reflection.ModuleEntity;
import php.runtime.reflection.helper.ClosureEntity;
import php.runtime.reflection.helper.GeneratorEntity;

import java.io.*;

public class ModuleOpcodePrinter {

    private final ModuleEntity module;

    public ModuleOpcodePrinter(ModuleEntity module){
        this.module = module;
    }

    public ModuleEntity getModule() {
        return module;
    }

    public void toOutput(Writer writer, int flags){
        try {
            OpcodePrinter opcodePrinter = new OpcodePrinter(module);
            writer.write("#### Module class: " + module.getContext().getModuleName() + "\n");
            opcodePrinter.toWriter(writer, flags);
            writer.write("#### /Module class \n\n\n");

            for(ClosureEntity closure : module.getClosures()){
                opcodePrinter = new OpcodePrinter(closure);
                writer.write("#### Closure: " + closure.getInternalName() + "\n");
                opcodePrinter.toWriter(writer);
                writer.write("#### /Closure \n\n\n");
            }

            for(GeneratorEntity generator : module.getGenerators()){
                opcodePrinter = new OpcodePrinter(generator);
                writer.write("#### Generator: " + generator.getInternalName() + "\n");
                opcodePrinter.toWriter(writer);
                writer.write("#### /Generator \n\n\n");
            }

            for(ClassEntity clazz : module.getClasses()){
                if (clazz.isClass() || clazz.isTrait()){
                    opcodePrinter = new OpcodePrinter(clazz);
                    writer.write("#### " + (clazz.isTrait() ? "Trait" : "Class") + ": " + clazz.getName() + "\n");
                    opcodePrinter.toWriter(writer);
                    writer.write("#### /" + (clazz.isTrait() ? "Trait" : "Class") + " \n\n\n");
                }
            }

            for(FunctionEntity function : module.getFunctions()){
                opcodePrinter = new OpcodePrinter(function);
                writer.write("#### Function: " + function.getName() + "\n");
                opcodePrinter.toWriter(writer);
                writer.write("#### /Function \n\n\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void toOutput(Writer writer){
        toOutput(writer, ClassReader.SKIP_DEBUG);
    }

    public String toString(int flags){
        StringWriter stringWriter = new StringWriter();
        toOutput(stringWriter, flags);
        return stringWriter.toString();
    }

    @Override
    public String toString(){
        return toString(ClassReader.SKIP_DEBUG);
    }

    public void toFile(File file, int flags) throws IOException {
        FileWriter writer = new FileWriter(file);
        try {
            toOutput(writer, flags);
        } finally {
            writer.close();
        }
    }

    public void toFile(File file) throws IOException {
        toFile(file, ClassReader.SKIP_DEBUG);
    }
}
