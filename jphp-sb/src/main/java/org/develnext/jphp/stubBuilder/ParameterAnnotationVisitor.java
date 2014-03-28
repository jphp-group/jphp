package org.develnext.jphp.stubBuilder;

import org.develnext.jphp.stubBuilder.tree.StubPhpFunction;
import org.develnext.jphp.stubBuilder.tree.StubPhpVariable;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author VISTALL
 * @since 19:15/23.03.14
 */
public class ParameterAnnotationVisitor extends AnnotationVisitor {
    private StubPhpFunction function;

    public ParameterAnnotationVisitor(StubPhpFunction function) {
        super(Opcodes.ASM4);
        this.function = function;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String name, String desc) {
        return new AnnotationVisitor(Opcodes.ASM4) {
            @Override
            public void visit(String name, Object value) {
                if (name.equals(StubBuilderUtil.ValueMethod)) {
                    function.addParameter(new StubPhpVariable((String) value));
                } else if (name.equals("typeClass")) {
                    StubPhpVariable p = function.getParameters().get(function.getParameters().size() - 1);
                    p.setTypeClass((String) value);
                }
            }

            @Override
            public AnnotationVisitor visitAnnotation(String name, String desc) {
                if (name.equals("optional")) {
                    return new AnnotationVisitor(Opcodes.ASM4) {
                        @Override
                        public void visit(String name, Object value) {
                            if (name.equals(StubBuilderUtil.ValueMethod)) {
                                StubPhpVariable p = function.getParameters().get(function.getParameters().size() - 1);
                                p.setOptionalValue((String) value);
                            }
                        }
                    };
                }
                return null;
            }
        };
    }
}
