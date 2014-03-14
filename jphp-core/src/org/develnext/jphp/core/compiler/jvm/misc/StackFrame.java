package org.develnext.jphp.core.compiler.jvm.misc;


import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.LabelNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StackFrame {
    public final LabelNode start;
    public final LabelNode end;
    public final FrameNode node;

    private Map<Integer, LocalVariable> variables;

    public StackFrame(FrameNode node, LabelNode start, LabelNode end){
        this.node  = node;
        if (this.node.local == null)
            this.node.local = new ArrayList();

        this.start = start;
        this.end   = end;
        this.variables = new HashMap<Integer, LocalVariable>();
    }

    public void addVariable(LocalVariable variable){
        if (!hasVariable(variable))
            variable.addStackFrame(this);

        variables.put(variable.index, variable);
    }

    public boolean hasVariable(LocalVariable variable){
        return variables.containsKey(variable.index);
    }
}
