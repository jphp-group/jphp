package ru.regenix.jphp.compiler.jvm;

import org.objectweb.asm.*;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceClassVisitor;
import ru.regenix.jphp.runtime.reflection.ClassEntity;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;


public class BytecodePrettyPrinter {
    /**
     * Gets us the bytecode method body of a given method.
     * @param clazz The class name to search for.
     * @param methodName The method name.
     * @param methodDescriptor The method's descriptor.
     *                         Can be null if one wishes to just get the first
     *                         method with the given name.
     * @throws IOException
     */
    public static String[] getMethod(ClassEntity clazz, String methodName, String methodDescriptor) throws IOException {
        return getMethod(clazz.getData(), methodName, methodDescriptor);
    }

    public static String[] getMethod(byte[] opcode, String methodName, String methodDescriptor) throws IOException {
        ClassReader classReader = new ClassReader(opcode);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        TraceClassVisitor traceClassVisitor = new TraceClassVisitor(null, new SourceCodeTextifier(), printWriter);
        MethodSelectorVisitor methodSelectorVisitor = new MethodSelectorVisitor(traceClassVisitor, methodName, methodDescriptor);
        classReader.accept(methodSelectorVisitor, ClassReader.SKIP_DEBUG);

        return toList(stringWriter.toString());
    }

    /**
     * Gets us the bytecode method body of a given method.
     * @param cw The class name to search for.
     * @param methodName The method name.
     * @throws IOException
     */
    public static String[] getMethod(ClassEntity cw, String methodName) throws IOException {
        return getMethod(cw, methodName, null);
    }

    private static String[] toList(String str) {
        //won't work correctly for all OSs
        String[] operations = str.split("[" + "\n" + "]");

        for (int i = 0; i < operations.length; ++i) {
            operations[i] = operations[i].trim();
        }

        return operations;
    }

    private static class MethodSelectorVisitor extends ClassVisitor {
        private final String methodName;
        private final String methodDescriptor;

        public MethodSelectorVisitor(ClassVisitor cv, String methodName, String methodDescriptor) {
            super(Opcodes.ASM4, cv);
            this.methodName = methodName;
            this.methodDescriptor = methodDescriptor;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc,
                String signature, String[] exceptions) {

            if (methodName.equals(name)) {
                if (methodDescriptor == null)
                    return new MaxVisitFilterMethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));

                if (methodDescriptor.equals(desc))
                    return new MaxVisitFilterMethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));
            }

            return null;
        }
    }

    private static class MaxVisitFilterMethodVisitor extends MethodVisitor {
        public MaxVisitFilterMethodVisitor(MethodVisitor mv) {
            super(Opcodes.ASM4, mv);
        }

        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
        }
    }


    private static class SourceCodeTextifier extends Printer {
        public SourceCodeTextifier() {
            this(Opcodes.ASM4);
        }

        protected SourceCodeTextifier(final int api) {
            super(api);
        }

        @Override
        public void visit(
            final int version,
            final int access,
            final String name,
            final String signature,
            final String superName,
            final String[] interfaces)
        {
        }

        @Override
        public void visitSource(final String file, final String debug) {
        }

        @Override
        public void visitOuterClass(
            final String owner,
            final String name,
            final String desc)
        {
        }

        @Override
        public Textifier visitClassAnnotation(
            final String desc,
            final boolean visible)
        {
            return new Textifier();
        }

        @Override
        public void visitClassAttribute(final Attribute attr) {
        }

        @Override
        public void visitInnerClass(
            final String name,
            final String outerName,
            final String innerName,
            final int access)
        {
        }

        @Override
        public Textifier visitField(
            final int access,
            final String name,
            final String desc,
            final String signature,
            final Object value)
        {
            return new Textifier();
        }

        @Override
        public Textifier visitMethod(
            final int access,
            final String name,
            final String desc,
            final String signature,
            final String[] exceptions)
        {
            Textifier t = new Textifier();
            text.add(t.getText());
            return t;
        }

        @Override
        public void visitClassEnd() {
        }

        @Override
        public void visit(final String name, final Object value) {
        }


        @Override
        public void visitEnum(
            final String name,
            final String desc,
            final String value)
        {
        }

        @Override
        public Textifier visitAnnotation(
            final String name,
            final String desc)
        {
            return new Textifier();
        }

        @Override
        public Textifier visitArray(
            final String name)
        {
            return new Textifier();
        }

        @Override
        public void visitAnnotationEnd() {
        }

        @Override
        public Textifier visitFieldAnnotation(
            final String desc,
            final boolean visible)
        {
            return new Textifier();
        }

        @Override
        public void visitFieldAttribute(final Attribute attr) {
            visitAttribute(attr);
        }

        @Override
        public void visitFieldEnd() {
        }

        @Override
        public Textifier visitAnnotationDefault() {
            return new Textifier();
        }

        @Override
        public Textifier visitMethodAnnotation(
            final String desc,
            final boolean visible)
        {
            return new Textifier();
        }

        @Override
        public Textifier visitParameterAnnotation(
            final int parameter,
            final String desc,
            final boolean visible)
        {
            return new Textifier();
        }

        @Override
        public void visitMethodAttribute(final Attribute attr) {
        }

        @Override
        public void visitCode() {
        }

        @Override
        public void visitFrame(
            final int type,
            final int nLocal,
            final Object[] local,
            final int nStack,
            final Object[] stack)
        {
        }

        @Override
        public void visitInsn(final int opcode) {
        }

        @Override
        public void visitIntInsn(final int opcode, final int operand) {
        }

        @Override
        public void visitVarInsn(final int opcode, final int var) {
        }

        @Override
        public void visitTypeInsn(final int opcode, final String type) {
        }

        @Override
        public void visitFieldInsn(
            final int opcode,
            final String owner,
            final String name,
            final String desc)
        {
        }

        @Override
        public void visitMethodInsn(
            final int opcode,
            final String owner,
            final String name,
            final String desc)
        {
        }

        @Override
        public void visitInvokeDynamicInsn(
            String name,
            String desc,
            Handle bsm,
            Object... bsmArgs)
        {
        }

        @Override
        public void visitJumpInsn(final int opcode, final Label label) {
        }

        @Override
        public void visitLabel(final Label label) {
        }

        @Override
        public void visitLdcInsn(final Object cst) {
        }

        @Override
        public void visitIincInsn(final int var, final int increment) {
        }

        @Override
        public void visitTableSwitchInsn(
            final int min,
            final int max,
            final Label dflt,
            final Label... labels)
        {
        }

        @Override
        public void visitLookupSwitchInsn(
            final Label dflt,
            final int[] keys,
            final Label[] labels)
        {
        }

        @Override
        public void visitMultiANewArrayInsn(final String desc, final int dims) {
        }

        @Override
        public void visitTryCatchBlock(
            final Label start,
            final Label end,
            final Label handler,
            final String type)
        {
        }

        @Override
        public void visitLocalVariable(
            final String name,
            final String desc,
            final String signature,
            final Label start,
            final Label end,
            final int index)
        {
        }

        @Override
        public void visitLineNumber(final int line, final Label start) {
        }

        @Override
        public void visitMaxs(final int maxStack, final int maxLocals) {
        }

        @Override
        public void visitMethodEnd() {
        }

        public void visitAttribute(final Attribute attr) {
        }
    }
}
