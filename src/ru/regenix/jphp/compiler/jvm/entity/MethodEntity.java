package ru.regenix.jphp.compiler.jvm.entity;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import ru.regenix.jphp.compiler.jvm.JvmCompiler;
import ru.regenix.jphp.runtime.memory.Memory;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.lexer.tokens.stmt.ExprStmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.MethodStmtToken;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

public class MethodEntity extends Entity {

    public final ClassEntity clazz;
    public final MethodStmtToken method;
    public final int id;

    private int stackSize = 0;
    private int stackMaxSize = 0;
    private Stack<Integer> stackSizes = new Stack<Integer>();
    private Stack<Memory.Type> stackTypes = new Stack<Memory.Type>();

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

    void push(int size, Memory.Type type){
        stackSize += size;
        if (stackSize > stackMaxSize)
            stackMaxSize = stackSize;

        stackTypes.push(type);
        stackSizes.push(size);
    }

    int getStackSize(){
        return stackSize;
    }

    void push(Memory.Type type){
        switch (type){
            case INT:
            case DOUBLE: push(2, type); break;
            default:
                push(1, type);
        }
    }

    void pop(int size){
        stackSize -= size;
    }

    Memory.Type pop(){
        pop(stackSizes.pop());
        return stackTypes.pop();
    }

    void popAll(){
        stackSize = 0;
        stackSizes.clear();
        stackTypes.clear();
    }

    Memory.Type peek(){
        return stackTypes.peek();
    }

    LocalVariable addLocalVariable(String variable, Label label, Class clazz){
        LocalVariable result;
        localVariables.put(
                variable,
                result = new LocalVariable(variable, localVariables.size(), label, clazz)
        );
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
                case PRIVATE: access += Opcodes.ACC_PRIVATE; break;
                case PROTECTED: access += Opcodes.ACC_PROTECTED; break;
                case PUBLIC: access += Opcodes.ACC_PUBLIC; break;
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

        if (!method.isStatic())
            addLocalVariable("this", label, Object.class);

        addLocalVariable("~env", label, Environment.class); // Environment env
        addLocalVariable("~args", label, Memory[].class);  // Memory[] arguments

        if (method.getBody() != null){
            for(ExprStmtToken instruction : method.getBody().getInstructions()){
                new ExpressionEntity(compiler, this, instruction).getResult();
            }
        }
        push(Memory.Type.NULL);
        mv.visitInsn(Opcodes.ACONST_NULL);
        mv.visitInsn(Opcodes.ARETURN);

        Label endL = new Label();
        mv.visitLabel(endL);
        for(LocalVariable variable : localVariables.values()){
            String description = Type.getDescriptor(variable.clazz == null ? Object.class : variable.clazz);
            if (!method.isStatic() && variable.name.equals("this"))
                description = "L" + clazz.clazz.getFulledName('/') + ";";

            mv.visitLocalVariable(
                    variable.name,
                    description,
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
        public Class clazz;

        public boolean isReference;

        public LocalVariable(String name, int index, Label label, Class clazz){
            this.name = name;
            this.index = index;
            this.label = label;
            this.clazz = clazz;
        }

        Class getClazz() {
            return clazz;
        }

        void setClazz(Class clazz) {
            this.clazz = clazz;
        }

        boolean isReference() {
            return isReference;
        }

        void setReference(boolean reference) {
            isReference = reference;
        }
    }
}
