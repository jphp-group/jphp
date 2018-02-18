package org.develnext.jphp.ext.image;

import org.develnext.jphp.ext.image.bind.BufferedImageMemoryOperation;
import org.develnext.jphp.ext.image.bind.ColorMemoryOperation;
import org.develnext.jphp.ext.image.bind.DrawOptionsMemoryOperation;
import org.develnext.jphp.ext.image.bind.FontMemoryOperation;
import org.develnext.jphp.ext.image.classes.PColor;
import org.develnext.jphp.ext.image.classes.PFont;
import org.develnext.jphp.ext.image.classes.PImage;
import org.develnext.jphp.ext.image.classes.PImageDraw;
import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;
import php.runtime.memory.support.MemoryOperation;

public class GraphicExtension extends Extension {
    public static final String NS = "php\\graphic";

    @Override
    public Status getStatus() {
        return Status.BETA;
    }

    @Override
    public String[] getPackageNames() {
        return new String[] { "std", "graphic" };
    }

    @Override
    public void onRegister(CompileScope scope) {
        MemoryOperation.register(new ColorMemoryOperation());
        MemoryOperation.register(new DrawOptionsMemoryOperation());
        MemoryOperation.register(new FontMemoryOperation());
        MemoryOperation.register(new BufferedImageMemoryOperation());

        registerClass(scope, PImage.class);
        registerClass(scope, PImageDraw.class);
        registerClass(scope, PColor.class);
        registerClass(scope, PFont.class);
    }
}
