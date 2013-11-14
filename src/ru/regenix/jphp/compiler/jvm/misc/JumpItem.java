package ru.regenix.jphp.compiler.jvm.misc;

import org.objectweb.asm.Label;

public class JumpItem {
    public final Label breakLabel;
    public final Label continueLabel;
    public final int stackSize;

    public JumpItem(Label breakLabel, Label continueLabel, int stackSize) {
        this.breakLabel = breakLabel;
        this.continueLabel = continueLabel;
        this.stackSize = stackSize;
    }
}
