package ru.regenix.jphp.compiler.jvm.node;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

public class ClassNodeImpl extends ClassNode {

    public ClassNodeImpl() {
        super();
        version = Opcodes.V1_6;
    }
}
