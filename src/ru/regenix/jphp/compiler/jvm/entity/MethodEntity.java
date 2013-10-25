package ru.regenix.jphp.compiler.jvm.entity;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import ru.regenix.jphp.compiler.jvm.JvmCompiler;
import ru.regenix.jphp.lexer.tokens.stmt.MethodStmtToken;

import java.util.HashMap;
import java.util.Map;

public class MethodEntity extends Entity {

    public final ClassEntity clazz;
    public final MethodStmtToken method;
    public final int id;

    private int stackSize = 0;
    private int stackMaxSize = 0;
    private Map<String, LocalVariable> localVariables;

    protected MethodVisitor mv;

    public MethodEntity(JvmCompiler compiler, ClassEntity clazz, MethodVisitor mv) {
        super(compiler);
        this.clazz = clazz;
        this.mv = mv;
        this.method = null;
        this.id = compiler.getScope().addMethod(null);
        this.localVariables = new HashMap<String, LocalVariable>();
    }

    public MethodEntity(JvmCompiler compiler, ClassEntity clazz, MethodStmtToken method) {
        super(compiler);
        this.clazz = clazz;
        this.method = method;
        this.id = compiler.getScope().addMethod(method);
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

    void addLocalVariable(String variable){
        localVariables.put(variable, new LocalVariable(variable, localVariables.size()));
    }

    LocalVariable getLocalVariable(String variable){
        return localVariables.get(variable);
    }

    @Override
    public void getResult() {
        compiler.getScope().addMethod(method);
        push(); // env

        if (mv == null){
            mv = clazz.cw.visitMethod(Opcodes.ACC_PUBLIC, method.getName().getName(), "()V", null, null);
        }
    }


    static class LocalVariable {
        public final String name;
        public final int index;

        public LocalVariable(String name, int index){
            this.name = name;
            this.index = index;
        }

        private String getName() {
            return name;
        }

        private int getIndex() {
            return index;
        }
    }
}
