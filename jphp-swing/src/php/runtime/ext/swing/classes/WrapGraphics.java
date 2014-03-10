package php.runtime.ext.swing.classes;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.ext.swing.SwingExtension;
import php.runtime.ext.swing.classes.components.support.RootObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import java.awt.*;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "Graphics")
public class WrapGraphics extends RootObject {
    protected Graphics graphics;

    public WrapGraphics(Environment env) {
        super(env);
    }

    public WrapGraphics(Environment env, Graphics graphics) {
        super(env);
        this.graphics = graphics;
    }

    public WrapGraphics(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public void setGraphics(Graphics graphics) {
        this.graphics = graphics;
    }

    @Signature
    private Memory __construct(Environment env, Memory... args) {
        return Memory.NULL;
    }

    @Signature({
            @Arg(value = "image", typeClass = SwingExtension.NAMESPACE + "Image"),
            @Arg(value = "x", optional = @Optional(value = "0", type = HintType.INT)),
            @Arg(value = "y", optional = @Optional(value = "0", type = HintType.INT)),
            @Arg(value = "newWidth", optional = @Optional("NULL")),
            @Arg(value = "newHeight", optional = @Optional("NULL")),
    })
    public Memory drawImage(Environment env, Memory... args){
        if (args[3].isNull() && args[4].isNull())
            return graphics.drawImage(
                    args[0].toObject(WrapImage.class).getImage(),
                    args[1].toInteger(),
                    args[2].toInteger(),
                    null
            ) ? Memory.TRUE : Memory.FALSE;
        else {
            return graphics.drawImage(
                    args[0].toObject(WrapImage.class).getImage(),
                    args[1].toInteger(),
                    args[2].toInteger(),
                    args[3].toInteger(),
                    args[4].toInteger(),
                    null
            ) ? Memory.TRUE : Memory.FALSE;
        }
    }

    @Signature({@Arg("x1"), @Arg("y1"), @Arg("x2"), @Arg("y2")})
    public Memory drawLine(Environment env, Memory... args){
        graphics.drawLine(
                args[0].toInteger(), args[1].toInteger(), args[2].toInteger(), args[3].toInteger()
        );
        return Memory.NULL;
    }

    @Signature({@Arg("x"), @Arg("y"), @Arg("width"), @Arg("height")})
    public Memory drawRect(Environment env, Memory... args){
        graphics.drawRect(
                args[0].toInteger(), args[1].toInteger(), args[2].toInteger(), args[3].toInteger()
        );
        return Memory.NULL;
    }

    @Signature({@Arg("x"), @Arg("y"), @Arg("width"), @Arg("height")})
    public Memory fillRect(Environment env, Memory... args){
        graphics.fillRect(
                args[0].toInteger(), args[1].toInteger(), args[2].toInteger(), args[3].toInteger()
        );
        return Memory.NULL;
    }

    @Signature({@Arg("x"), @Arg("y"), @Arg("width"), @Arg("height"), @Arg("raised")})
    public Memory draw3DRect(Environment env, Memory... args){
        graphics.draw3DRect(
                args[0].toInteger(), args[1].toInteger(), args[2].toInteger(), args[3].toInteger(),
                args[4].toBoolean()
        );
        return Memory.NULL;
    }

    @Signature({@Arg("x"), @Arg("y"), @Arg("width"), @Arg("height"), @Arg("raised")})
    public Memory fill3DRect(Environment env, Memory... args){
        graphics.fill3DRect(
                args[0].toInteger(), args[1].toInteger(), args[2].toInteger(), args[3].toInteger(),
                args[4].toBoolean()
        );
        return Memory.NULL;
    }

    @Signature({@Arg("x"), @Arg("y"), @Arg("width"), @Arg("height"), @Arg("startAngle"), @Arg("arcAngle")})
    public Memory drawArc(Environment env, Memory... args){
        graphics.drawArc(
                args[0].toInteger(), args[1].toInteger(), args[2].toInteger(), args[3].toInteger(),
                args[4].toInteger(), args[5].toInteger()
        );
        return Memory.NULL;
    }

    @Signature({@Arg("x"), @Arg("y"), @Arg("width"), @Arg("height"), @Arg("startAngle"), @Arg("arcAngle")})
    public Memory fillArc(Environment env, Memory... args){
        graphics.fillArc(
                args[0].toInteger(), args[1].toInteger(), args[2].toInteger(), args[3].toInteger(),
                args[4].toInteger(), args[5].toInteger()
        );
        return Memory.NULL;
    }

    @Signature({@Arg("x"), @Arg("y"), @Arg("width"), @Arg("height")})
    public Memory drawOval(Environment env, Memory... args){
        graphics.drawOval(
                args[0].toInteger(), args[1].toInteger(), args[2].toInteger(), args[3].toInteger()
        );
        return Memory.NULL;
    }

    @Signature({@Arg("x"), @Arg("y"), @Arg("width"), @Arg("height")})
    public Memory fillOval(Environment env, Memory... args){
        graphics.fillOval(
                args[0].toInteger(), args[1].toInteger(), args[2].toInteger(), args[3].toInteger()
        );
        return Memory.NULL;
    }

    @Signature({@Arg("text"), @Arg("x"), @Arg("y")})
    public Memory drawText(Environment env, Memory... args){
        graphics.drawString(args[0].toString(), args[1].toInteger(), args[2].toInteger());
        return Memory.NULL;
    }

    protected static Polygon toPolygon(Environment env, ArrayMemory arr){
        int npoints = arr.size();
        int[] xPoints = new int[npoints];
        int[] yPoints = new int[npoints];

        int i = 0;
        for(Memory el : arr.values()){
            if (!el.isArray())
                env.exception("Polygon must contain only array type elements, given ", el.getRealType().toString());
            else {
                Memory[] xy = el.toValue(ArrayMemory.class).values();
                if (xy.length < 2)
                    env.exception("Polygon: invalid element count of array item - %s", xy.length);

                xPoints[i] = xy[0].toInteger();
                yPoints[i] = xy[0].toInteger();
            }
            i++;
        }
        return new Polygon(xPoints, yPoints, npoints);
    }

    @Signature(@Arg(value = "polygon", type = HintType.ARRAY))
    public Memory drawPolygon(Environment env, Memory... args){
        ArrayMemory arr = args[0].toValue(ArrayMemory.class);

        graphics.drawPolygon(toPolygon(env, arr));
        return Memory.NULL;
    }

    @Signature(@Arg(value = "polygon", type = HintType.ARRAY))
    public Memory fillPolygon(Environment env, Memory... args){
        ArrayMemory arr = args[0].toValue(ArrayMemory.class);

        graphics.fillPolygon(toPolygon(env, arr));
        return Memory.NULL;
    }

    @Signature(@Arg(value = "polyline", type = HintType.ARRAY))
    public Memory drawPolyline(Environment env, Memory... args){
        ArrayMemory arr = args[0].toValue(ArrayMemory.class);
        Polygon polygon = toPolygon(env, arr);
        graphics.drawPolyline(polygon.xpoints, polygon.ypoints, polygon.npoints);
        return Memory.NULL;
    }

    @Signature(@Arg("color"))
    public Memory setXORMode(Environment env, Memory... args){
        graphics.setXORMode(WrapColor.of(args[0]));
        return Memory.NULL;
    }

    @Signature(@Arg("str"))
    public Memory getTextWidth(Environment env, Memory... args){
        return LongMemory.valueOf(graphics.getFontMetrics().stringWidth(args[0].toString()));
    }

    @Signature
    public Memory getTextHeight(Environment env, Memory... args){
        return LongMemory.valueOf(graphics.getFontMetrics().getHeight());
    }

    @Signature
    public Memory dispose(Environment env, Memory... args){
        graphics.dispose();
        return Memory.NULL;
    }

    @Signature(@Arg("color"))
    public Memory __setColor(Environment env, Memory... args){
        graphics.setColor(WrapColor.of(args[0]));
        return Memory.NULL;
    }

    @Signature
    public Memory __getColor(Environment env, Memory... args){
        return new ObjectMemory(new WrapColor(env, graphics.getColor()));
    }

    @Signature(@Arg("font"))
    public Memory __setFont(Environment env, Memory... args){
        graphics.setFont(WrapFont.of(args[0]));
        return Memory.NULL;
    }

    @Signature
    public Memory __getFont(Environment env, Memory... args){
        return new ObjectMemory(new WrapFont(env, graphics.getFont()));
    }

    @Signature
    public Memory setPaintMode(Environment env, Memory... args){
        graphics.setPaintMode();
        return Memory.NULL;
    }

    @Signature({@Arg("x"), @Arg("y"), @Arg("width"), @Arg("height")})
    public Memory clipRect(Environment env, Memory... args){
        graphics.clipRect(args[0].toInteger(), args[1].toInteger(), args[2].toInteger(), args[3].toInteger());
        return Memory.NULL;
    }

    @Signature({@Arg("x"), @Arg("y"), @Arg("width"), @Arg("height"), @Arg("dx"), @Arg("dy")})
    public Memory copyArea(Environment env, Memory... args){
        graphics.copyArea(
                args[0].toInteger(), args[1].toInteger(), args[2].toInteger(), args[3].toInteger(),
                args[4].toInteger(), args[5].toInteger());
        return Memory.NULL;
    }

    @Signature({@Arg("x"), @Arg("y"), @Arg("width"), @Arg("height")})
    public Memory clearRect(Environment env, Memory... args){
        graphics.clearRect(args[0].toInteger(), args[1].toInteger(), args[2].toInteger(), args[3].toInteger());
        return Memory.NULL;
    }

    @Signature({@Arg("x"), @Arg("y")})
    public Memory translate(Environment env, Memory... args){
        graphics.translate(args[0].toInteger(), args[1].toInteger());
        return Memory.NULL;
    }

    @Signature({@Arg("x"), @Arg("y"), @Arg("w"), @Arg("h")})
    public Memory create(Environment env, Memory... args) {
        if (!args[0].isNull() || !args[1].isNull() || !args[2].isNull() || !args[3].isNull())
            return new ObjectMemory(new WrapGraphics(env, graphics.create(
                args[0].toInteger(), args[1].toInteger(), args[2].toInteger(), args[3].toInteger()
            )));
        else
            return new ObjectMemory(new WrapGraphics(env, graphics.create()));
    }
}
