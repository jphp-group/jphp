package org.develnext.jphp.stubBuilder;

import org.develnext.jphp.stubBuilder.tree.StubPhpFile;
import org.develnext.jphp.stubBuilder.tree.StubPhpFunction;
import org.develnext.jphp.stubBuilder.tree.StubPhpVariable;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.signature.SignatureVisitor;

/**
 * @author VISTALL
 * @since 20:14/23.03.14
 */
class ClassVisitorForFunctionContainer extends ClassVisitor {
    public static final String FunctionsContainer = "php/runtime/ext/support/compile/FunctionsContainer";

    private final StubPhpFile file;
    private boolean accepted;

    public ClassVisitorForFunctionContainer(StubPhpFile file) {
        super(Opcodes.ASM4);
        this.file = file;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if (FunctionsContainer.equals(superName)) {
            String[] names = StubBuilderUtil.slitToNameAndNamespace(name);
            String phpFileName = StubBuilderUtil.genFileNameFromFullClassName(names[1]);
            file.setName(phpFileName);
            accepted = true;
        }
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        if (!accepted) {
            return null;
        }
        if ((access & Opcodes.ACC_STATIC) != 0) {
            final StubPhpFunction f = new StubPhpFunction(name);

            final int[] parameters = new int[1];
            SignatureReader signatureReader = new SignatureReader(desc);
            signatureReader.accept(new SignatureVisitor(Opcodes.ASM4) {
                @Override
                public SignatureVisitor visitParameterType() {
                    return new SignatureVisitor(Opcodes.ASM4) {
                        @Override
                        public void visitBaseType(char descriptor) {
                            parameters[0]++;
                        }

                        @Override
                        public void visitClassType(String name) {
                            parameters[0]++;
                        }
                    };
                }
            });

            file.add(f);
            return new MethodVisitor(Opcodes.ASM4) {
                @Override
                public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                    if(index == 0) {
                        return;
                    }
                    if(index > parameters[0]) {
                        return;
                    }
                    StubPhpVariable variable = new StubPhpVariable(name);
                    f.addParameter(variable);
                }
            };
        }
        return null;
    }
}
