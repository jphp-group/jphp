package org.develnext.jphp.swing.classes;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.Stream;
import org.develnext.jphp.swing.SwingExtension;
import org.develnext.jphp.swing.classes.components.support.RootObject;
import php.runtime.memory.*;
import php.runtime.reflection.ClassEntity;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.AttributedCharacterIterator;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "Font")
public class WrapFont extends RootObject {
    public final static int PLAIN = Font.PLAIN;
    public final static int BOLD = Font.BOLD;
    public final static int ITALIC = Font.ITALIC;

    protected Font font;

    public WrapFont(Environment env) {
        super(env);
    }

    public WrapFont(Environment env, Font font) {
        super(env);
        setFont(font);
    }

    public WrapFont(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Font getFont() {
        return font;
    }

    @Signature({@Arg("name"), @Arg("style"), @Arg("size")})
    public Memory __construct(Environment env, Memory... args) {
        setFont(new Font(args[0].toString(), args[1].toInteger(), args[2].toInteger()));
        return Memory.NULL;
    }

    @Signature
    protected Memory __getFamily(Environment env, Memory... args){
        return StringMemory.valueOf(font.getFamily(env.getLocale()));
    }

    @Signature
    public Memory __getFontName(Environment env, Memory... args){
        return StringMemory.valueOf(font.getFontName(env.getLocale()));
    }

    @Signature
    public Memory __getName(Environment env, Memory... args){
        return StringMemory.valueOf(font.getName());
    }

    @Signature
    public Memory __getItalicAngle(Environment env, Memory... args){
        return DoubleMemory.valueOf(font.getItalicAngle());
    }

    @Signature
    public Memory __getSize(Environment env, Memory... args){
        return LongMemory.valueOf(font.getSize());
    }

    @Signature
    public Memory __getSize2D(Environment env, Memory... args){
        return DoubleMemory.valueOf(font.getSize2D());
    }

    @Signature
    public Memory __getStyle(Environment env, Memory... args){
        return LongMemory.valueOf(font.getStyle());
    }

    @Signature
    public Memory __getNumGlyphs(Environment env, Memory... args){
        return LongMemory.valueOf(font.getNumGlyphs());
    }

    @Signature
    public Memory __getAttributes(Environment env, Memory... args){
        ArrayMemory r = new ArrayMemory();
        for(AttributedCharacterIterator.Attribute el : font.getAvailableAttributes()){
            r.add(new StringMemory(el.toString()));
        }
        return r.toConstant();
    }

    @Signature
    public Memory isBold(Environment env, Memory... args){
        return font.isBold() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isItalic(Environment env, Memory... args){
        return font.isItalic() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isPlain(Environment env, Memory... args){
        return font.isPlain() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isTransformed(Environment env, Memory... args){
        return font.isTransformed() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg("symbol"))
    public Memory getBaselineFor(Environment env, Memory... args){
        return LongMemory.valueOf(font.getBaselineFor(args[0].toChar()));
    }

    @Signature(@Arg("symbol"))
    public Memory canDisplay(Environment env, Memory... args){
        return TrueMemory.valueOf(font.canDisplay(args[0].toChar()));
    }

    @Signature(@Arg("string"))
    public Memory canDisplayUpTo(Environment env, Memory... args){
        return TrueMemory.valueOf(font.canDisplayUpTo(args[0].toString()));
    }

    @Signature(@Arg("str"))
    public static Memory decode(Environment env, Memory... args){
        Font font = Font.decode(args[0].toString());
        if (font == null)
            return Memory.NULL;

        WrapFont wrapFont = new WrapFont(env);
        wrapFont.setFont(font);
        return new ObjectMemory(wrapFont);
    }

    @Signature(@Arg("name"))
    public static Memory get(Environment env, Memory... args){
        Font font = Font.getFont(args[0].toString());
        if (font == null)
            return Memory.NULL;

        WrapFont wrapFont = new WrapFont(env);
        wrapFont.setFont(font);
        return new ObjectMemory(wrapFont);
    }

    @Signature({@Arg("source"), @Arg(value = "trueType", optional = @Optional(value = "true", type = HintType.BOOLEAN))})
    public static Memory create(Environment env, Memory... args) throws IOException {
        Font r = null;
        if (args[0].instanceOf(Stream.class)){
            Stream stream = args[0].toObject(Stream.class);
            Memory result = stream.readFully(env);

            ByteArrayInputStream inputStream = new ByteArrayInputStream(result.getBinaryBytes());
            try {
                r = Font.createFont(
                        args[1].toBoolean() ? Font.TRUETYPE_FONT : Font.TYPE1_FONT,
                        inputStream
                );
            } catch (FontFormatException e) {
                env.exception(env.trace(), e.getMessage());
            }
        } else {
            File file = new File(args[0].toString());
            try {
                r = Font.createFont(
                        args[1].toBoolean() ? Font.TRUETYPE_FONT : Font.TYPE1_FONT,
                        file
                );
            } catch (FontFormatException e) {
                env.exception(env.trace(), e.getMessage());
            }
        }

        if (r == null)
            return Memory.NULL;

        WrapFont wrapFont = new WrapFont(env);
        wrapFont.setFont(r);
        return new ObjectMemory(wrapFont);
    }

    public static Font of(Memory arg){
        if (arg.isNull())
            return null;
        else if (arg.instanceOf(WrapFont.class)){
            WrapFont font = arg.toObject(WrapFont.class);
            return font.getFont();
        } else {
            return Font.decode(arg.toString());
        }
    }
}
