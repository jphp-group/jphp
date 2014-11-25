package org.develnext.jphp.core.compiler.jvm.node;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import java.util.ArrayList;

public class ClassNodeImpl extends ClassNode {

    public ClassNodeImpl() {
        super(Opcodes.ASM5);
        version = Opcodes.V1_6; // todo: available to switch this value
        this.interfaces = new ArrayList();
        this.visibleAnnotations = new ArrayList();
    }
}
