package ru.regenix.jphp.compiler.jvm.entity;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import ru.regenix.jphp.compiler.jvm.JvmCompiler;

abstract public class Entity {

    protected final JvmCompiler compiler;

    public Entity(JvmCompiler compiler){
        this.compiler = compiler;
    }

    abstract public void getResult();

    public JvmCompiler getCompiler() {
        return compiler;
    }

    protected Label writeLabel(MethodVisitor mv, int lineNumber){
        Label label = new Label();
        mv.visitLabel(label);
        if (lineNumber != -1)
            mv.visitLineNumber(lineNumber, label);

        return label;
    }

    protected Label writeLabel(MethodVisitor mv){
        return writeLabel(mv, -1);
    }
}
