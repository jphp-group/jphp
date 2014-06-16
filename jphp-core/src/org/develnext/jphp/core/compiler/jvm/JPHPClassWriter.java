package org.develnext.jphp.core.compiler.jvm;

import org.objectweb.asm.ClassWriter;

public class JPHPClassWriter extends ClassWriter  {
    public JPHPClassWriter(boolean isTrait) {
        super(COMPUTE_FRAMES);
    }
}
