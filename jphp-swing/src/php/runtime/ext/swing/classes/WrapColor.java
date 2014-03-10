package php.runtime.ext.swing.classes;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.ext.swing.SwingExtension;
import php.runtime.ext.swing.classes.components.support.RootObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;

import java.awt.*;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "Color")
public class WrapColor extends RootObject {
    protected Color color;

    public WrapColor(Environment env) {
        super(env);
    }

    public WrapColor(Environment env, Color color) {
        super(env);
        setColor(color);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Signature
    public Memory __getRGB(Environment env, Memory... args){
        return LongMemory.valueOf(color.getRGB());
    }

    @Signature
    public Memory __getAlpha(Environment env, Memory... args){
        return LongMemory.valueOf(color.getAlpha());
    }

    @Signature
    public Memory __getRed(Environment env, Memory... args){
        return LongMemory.valueOf(color.getRed());
    }

    @Signature
    public Memory __getGreen(Environment env, Memory... args){
        return LongMemory.valueOf(color.getGreen());
    }

    @Signature
    public Memory __getBlue(Environment env, Memory... args){
        return LongMemory.valueOf(color.getBlue());
    }

    @Signature
    public Memory brighter(Environment env, Memory... args){
        return new ObjectMemory(new WrapColor(env, color.brighter()));
    }

    @Signature
    public Memory darker(Environment env, Memory... args){
        return new ObjectMemory(new WrapColor(env, color.darker()));
    }

    @Signature({
            @Arg("rgb"),
            @Arg(value = "hasAlpha", optional = @Optional(value = "false", type = HintType.BOOLEAN))
    })
    public Memory __construct(Environment env, Memory... args){
        color = new Color(args[0].toInteger(), args[1].toBoolean());
        return Memory.NULL;
    }

    @Signature({
            @Arg("r"), @Arg("g"), @Arg("b"),
            @Arg(value = "alpha", optional = @Optional(value = "255", type = HintType.INT))
    })
    public static Memory rgb(Environment env, Memory... args) {
        return new ObjectMemory(new WrapColor(env, new Color(
            args[0].toInteger(), args[1].toInteger(), args[2].toInteger(),
            args[3].toInteger()
        )));
    }

    @Signature({
            @Arg("r"), @Arg("g"), @Arg("b"),
            @Arg(value = "alpha", optional = @Optional(value = "1", type = HintType.DOUBLE))
    })
    public static Memory floatRgb(Environment env, Memory... args){
        return new ObjectMemory(new WrapColor(env, new Color(
                args[0].toFloat(), args[1].toFloat(), args[2].toFloat(),
                args[3].toFloat()
        )));
    }

    @Signature(@Arg("nm"))
    public static Memory decode(Environment env, Memory... args){
        try {
            String nm = args[0].toString();
            return new ObjectMemory(new WrapColor(env, Color.decode(nm)));
        } catch (NumberFormatException e){
            return Memory.NULL;
        }
    }

    public static Color of(Memory arg){
        if (arg.isNull())
            return null;
        else if (arg.isArray()){
            Memory[] values = arg.toValue(ArrayMemory.class).values();
            switch (values.length){
                case 0: return new Color(0, 0, 0);
                case 1: return new Color(values[0].toInteger(), 0, 0);
                case 2: return new Color(values[0].toInteger(), values[1].toInteger(), 0);
                case 3: return new Color(values[0].toInteger(), values[1].toInteger(), values[2].toInteger());
                default:
                case 4: return new Color(
                        values[0].toInteger(), values[1].toInteger(), values[2].toInteger(), values[3].toInteger()
                );
            }
        }
        else if (arg.instanceOf(WrapColor.class))
            return arg.toObject(WrapColor.class).getColor();
        else
            return new Color(arg.toInteger());
    }
}
