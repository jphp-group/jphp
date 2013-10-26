package ru.regenix.jphp.compiler.jvm.entity;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import ru.regenix.jphp.compiler.jvm.JvmCompiler;
import ru.regenix.jphp.compiler.jvm.runtime.memory.Memory;
import ru.regenix.jphp.env.Environment;
import ru.regenix.jphp.lexer.tokens.stmt.ExprStmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.MethodStmtToken;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MethodEntity extends Entity {

    public final ClassEntity clazz;
    public final MethodStmtToken method;
    public final int id;

    private int stackSize = 0;
    private int stackMaxSize = 0;
    private Map<String, LocalVariable> localVariables;

    protected MethodVisitor mv;

    public MethodEntity(JvmCompiler compiler, ClassEntity clazz, MethodVisitor mv, String methodName) {
        super(compiler);
        this.clazz = clazz;
        this.mv = mv;
        this.method = null;
        this.id = compiler.getScope().addMethod(clazz.clazz, methodName);
        this.localVariables = new LinkedHashMap<String, LocalVariable>();
    }

    public MethodEntity(JvmCompiler compiler, ClassEntity clazz, MethodStmtToken method) {
        super(compiler);
        this.clazz = clazz;
        this.method = method;
        this.id = compiler.getScope().addMethod(method);
        this.localVariables = new LinkedHashMap<String, LocalVariable>();
    }

    public Map<String, LocalVariable> getLocalVariables() {
        return localVariables;
    }

    void push(int size){
        stackSize += size;
        if (stackSize > stackMaxSize)
            stackMaxSize = stackSize;
    }

    void push(){
        push(1);
    }

    void pop(int size){
        stackSize -= size;
    }

    void pop(){
        pop(1);
    }

    LocalVariable addLocalVariable(String variable, Label label, Class clazz){
        LocalVariable result;
        localVariables.put(variable, result = new LocalVariable(variable, localVariables.size(), label, clazz));
        return result;
    }

    LocalVariable addLocalVariable(String variable, Label label){
        return addLocalVariable(variable, label, Memory.class);
    }

    LocalVariable getLocalVariable(String variable){
        return localVariables.get(variable);
    }

    @Override
    public void getResult() {
        compiler.getScope().addMethod(method);

        if (mv == null){
            int access = 0;
            switch (method.getModifier()){
                case PRIVATE: access += Opcodes.ACC_PRIVATE;
                case PROTECTED: access += Opcodes.ACC_PROTECTED;
                case PUBLIC: access += Opcodes.ACC_PUBLIC;
            }

            if (method.isStatic()) access += Opcodes.ACC_STATIC;
            if (method.isAbstract()) access += Opcodes.ACC_ABSTRACT;
            if (method.isFinal()) access += Opcodes.ACC_FINAL;

            mv = clazz.cw.visitMethod(access, method.getName().getName(),
                    Type.getMethodDescriptor(
                            Type.getType(Memory.class),
                            Type.getType(Environment.class),
                            Type.getType(Memory[].class)
                    ),
                    null, null
            );
        }

        mv.visitCode();
        Label label = writeLabel(mv, method.getMeta().getStartLine());

        addLocalVariable("~env", label, Environment.class); // Environment env
        addLocalVariable("~args", label, Memory[].class);  // Memory[] arguments

        if (method.getBody() != null){
            for(ExprStmtToken instruction : method.getBody().getInstructions()){
                new ExpressionEntity(compiler, this, instruction).getResult();
            }
        }
        push();
        mv.visitInsn(Opcodes.ACONST_NULL);
        mv.visitInsn(Opcodes.ARETURN);

        Label endL = new Label();
        mv.visitLabel(endL);
        for(LocalVariable variable : localVariables.values()){
            mv.visitLocalVariable(
                    variable.name,
                    Type.getDescriptor(variable.clazz),
                    null,
                    variable.label,
                    endL,
                    variable.index
            );
        }
        mv.visitMaxs(this.stackMaxSize, this.localVariables.size());
        mv.visitEnd();
    }


    static class LocalVariable {
        public final String name;
        public final int index;
        public final Label label;
        public final Class clazz;

        public LocalVariable(String name, int index, Label label, Class clazz){
            this.name = name;
            this.index = index;
            this.label = label;
            this.clazz = clazz;
        }
    }
}
