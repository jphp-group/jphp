package org.develnext.jphp.stubBuilder;

import org.develnext.jphp.stubBuilder.tree.*;
import org.objectweb.asm.*;

/**
* @author VISTALL
* @since 19:52/23.03.14
*/
class ClassVisitorFromClassApi extends ClassVisitor {
    private final StubPhpFile file;
    private final StubPhpMemberWithChildren[] targetToCollectRef;
    private boolean accepted;

    public ClassVisitorFromClassApi(StubPhpFile file, StubPhpMemberWithChildren[] targetToCollectRef) {
        super(Opcodes.ASM4);
        this.file = file;
        this.targetToCollectRef = targetToCollectRef;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if (StubBuilderUtil.NameAnnotation.equals(desc)) {
            return new AnnotationVisitor(Opcodes.ASM4) {
                @Override
                public void visit(String name, Object value) {
                    if (StubBuilderUtil.ValueMethod.equals(name)) {
                        String qName = (String) value;
                        StubPhpClass phpClass = null;
                        String[] split = StubBuilderUtil.slitToNameAndNamespace(qName);
                        if (split[0] != null) {
                            StubPhpNamespace namespace = new StubPhpNamespace(split[0]);
                            file.add(namespace);
                            phpClass = new StubPhpClass(split[1]);
                            namespace.add(phpClass);
                        } else {
                            phpClass = new StubPhpClass(split[1]);
                            file.add(targetToCollectRef[0]);
                        }

                        targetToCollectRef[0] = phpClass;

                        file.setName(qName.replace("\\", "/") + ".php");
                        accepted = true;
                    }
                }
            };
        }
        return null;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        if(!accepted) {
            return null;
        }
        if((access & Opcodes.ACC_STATIC) != 0) {
            StubPhpConstant member = new StubPhpConstant(name);
            member.setOptionalValue(String.valueOf(value));
            targetToCollectRef[0].add(member);
        }
        return null;
    }

    @Override
    public MethodVisitor visitMethod(int access, final String name, String desc, String signature, String[] exceptions) {
        if(!accepted) {
            return null;
        }
        return new MethodVisitor(Opcodes.ASM4) {
            @Override
            public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                if (StubBuilderUtil.SignatureAnnotation.equals(desc)) {
                    final StubPhpFunction function = new StubPhpFunction(name);
                    targetToCollectRef[0].add(function);
                    return new AnnotationVisitor(Opcodes.ASM4) {
                        @Override
                        public AnnotationVisitor visitArray(String name) {
                            return new ParameterAnnotationVisitor(function);
                        }
                    };
                }
                return null;
            }
        };
    }

}
