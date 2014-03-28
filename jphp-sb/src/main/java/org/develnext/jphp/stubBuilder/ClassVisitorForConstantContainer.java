package org.develnext.jphp.stubBuilder;

import org.develnext.jphp.stubBuilder.tree.StubPhpConstant;
import org.develnext.jphp.stubBuilder.tree.StubPhpFile;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author VISTALL
 * @since 20:15/23.03.14
 */
public class ClassVisitorForConstantContainer extends ClassVisitor {
    public static final String ConstantsContainer = "php/runtime/ext/support/compile/ConstantsContainer";

    private final StubPhpFile file;
    private boolean accepted;

    public ClassVisitorForConstantContainer(StubPhpFile file) {
        super(Opcodes.ASM4);
        this.file = file;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if (ConstantsContainer.equals(superName)) {
            String[] names = StubBuilderUtil.slitToNameAndNamespace(name);
            String phpFileName = StubBuilderUtil.genFileNameFromFullClassName(names[1]);
            file.setName(phpFileName);
            accepted = true;
        }
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        if (!accepted) {
            return null;
        }
        if ((access & Opcodes.ACC_STATIC) != 0) {
            StubPhpConstant member = new StubPhpConstant(name);
            member.setOptionalValue(String.valueOf(value));
            file.add(member);
        }
        return null;
    }
}
