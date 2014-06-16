package org.develnext.jphp.core.compiler.jvm.node;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;
import php.runtime.exceptions.CriticalException;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MethodNodeImpl extends MethodNode {

    public MethodNodeImpl() {
        super(Opcodes.ASM5);
        exceptions = new ArrayList();
        tryCatchBlocks = new ArrayList();
        localVariables = new ArrayList();
        attrs = new ArrayList();
    }

    public static MethodNodeImpl duplicate(MethodNode node) {
        MethodNodeImpl self = new MethodNodeImpl();

        self.name = node.name;
        self.desc = node.desc;
        self.access = node.access;
        self.annotationDefault = node.annotationDefault;
        self.attrs = node.attrs;
        self.exceptions = node.exceptions;
        self.tryCatchBlocks = node.tryCatchBlocks;
        self.localVariables = node.localVariables;

        self.instructions = node.instructions;

        self.invisibleAnnotations = node.invisibleAnnotations;
        self.invisibleParameterAnnotations = node.invisibleParameterAnnotations;
        self.visibleAnnotations = node.visibleAnnotations;
        self.visibleParameterAnnotations = node.visibleParameterAnnotations;
        self.maxLocals = node.maxLocals;
        self.maxStack = node.maxStack;

        self.localVariables = node.localVariables;

        // HACK
        // it's needed for copied trait method, else it will generate an error in duplicated usage
        try {
            Field field = node.getClass().getDeclaredField("visited");
            field.setAccessible(true);
            field.setBoolean(self, true);
        } catch (NoSuchFieldException e) {
            throw new CriticalException(e);
        } catch (IllegalAccessException e) {
            throw new CriticalException(e);
        }
        return self;
    }

    @Override
    public void accept(ClassVisitor cv) {
        super.accept(cv);
    }
}
