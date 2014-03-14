package org.develnext.jphp.core.compiler.jvm.misc;

import org.objectweb.asm.tree.LabelNode;

public class JumpItem {
    public final LabelNode breakLabel;
    public final LabelNode continueLabel;
    public final int stackSize;

    public JumpItem(LabelNode breakLabel, LabelNode continueLabel, int stackSize) {
        this.breakLabel = breakLabel;
        this.continueLabel = continueLabel;
        this.stackSize = stackSize;
    }
}
