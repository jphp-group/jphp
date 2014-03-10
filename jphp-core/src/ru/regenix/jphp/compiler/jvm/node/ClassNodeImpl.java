package ru.regenix.jphp.compiler.jvm.node;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import java.util.ArrayList;

public class ClassNodeImpl extends ClassNode {

    public ClassNodeImpl() {
        super();
        version = Opcodes.V1_6; // todo: available to switch this value
        this.interfaces = new ArrayList();
    }
}
