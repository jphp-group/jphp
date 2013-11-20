package ru.regenix.jphp.compiler.jvm.node;

import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;

public class MethodNodeImpl extends MethodNode {

    public MethodNodeImpl() {
        super();
        exceptions = new ArrayList();
        tryCatchBlocks = new ArrayList();
        localVariables = new ArrayList();
        attrs = new ArrayList();
    }
}
