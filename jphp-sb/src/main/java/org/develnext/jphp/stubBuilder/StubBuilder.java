package org.develnext.jphp.stubBuilder;

import org.develnext.jphp.stubBuilder.tree.StubPhpFile;
import org.develnext.jphp.stubBuilder.tree.StubPhpMemberWithChildren;
import org.objectweb.asm.*;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author VISTALL
 * @since 18:17/23.03.14
 */
public class StubBuilder {

    private InputStream inputStream;

    public StubBuilder(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public StubPhpFile generate() throws IOException {
        ClassReader reader = new ClassReader(inputStream);

        final StubPhpFile file = new StubPhpFile(null);

        final StubPhpMemberWithChildren[] targetToCollectRef = new StubPhpMemberWithChildren[1];

        reader.accept(new ClassVisitorFromClassApi(file, targetToCollectRef), ClassReader.SKIP_CODE);

        reader.accept(new ClassVisitorForFunctionContainer(file), ClassReader.SKIP_CODE);

        reader.accept(new ClassVisitorForConstantContainer(file), ClassReader.SKIP_CODE);

        if (file.getName() == null) {
            return null;
        }
        return file;
    }
}
