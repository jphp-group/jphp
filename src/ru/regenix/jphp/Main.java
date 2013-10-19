package ru.regenix.jphp;

import org.objectweb.asm.*;

import java.lang.reflect.InvocationTargetException;

import static org.objectweb.asm.Opcodes.*;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException,
            IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        int b = 33 * 99;
        double x = b / 0.2;

        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;
        AnnotationVisitor av0;

        cw.visit(V1_7, ACC_PUBLIC, "net/php/MyClass", null, "java/lang/Object", null);
        cw.visitSource("Main.java", null);

        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(3, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
            mv.visitInsn(RETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "Lnet/php/Main;", null, l0, l1, 0);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }

        {
            mv = cw.visitMethod(ACC_PUBLIC, "send", "()V", null, null);
            mv.visitCode();

            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn("Hello World!");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");

            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 1);

            mv.visitEnd();
        }
        cw.visitEnd();

        MyClassLoader classLoader = new MyClassLoader();
        Class<?> cl = classLoader.loadClass("net.php.MyClass", cw.toByteArray());

        Object obj = cl.newInstance();
        cl.getMethod("send").invoke(obj);
    }
}
