package org.develnext.jphp.core.compiler.jvm.node;

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

    public MethodNodeImpl(MethodNode node) {
        super();
        this.name = node.name;
        this.desc = node.desc;
        this.access = node.access;
        this.annotationDefault = node.annotationDefault;
        this.attrs = node.attrs;
        this.exceptions = node.exceptions;
        this.tryCatchBlocks = node.tryCatchBlocks;
        this.localVariables = node.localVariables;
        this.instructions = node.instructions;
        this.invisibleAnnotations = node.invisibleAnnotations;
        this.invisibleParameterAnnotations = node.invisibleParameterAnnotations;
        this.visibleAnnotations = node.visibleAnnotations;
        this.visibleParameterAnnotations = node.visibleParameterAnnotations;
        this.maxLocals = node.maxLocals;
        this.maxStack = node.maxStack;
        this.localVariables = node.localVariables;
    }
}
