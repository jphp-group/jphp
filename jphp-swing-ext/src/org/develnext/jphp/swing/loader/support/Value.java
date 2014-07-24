package org.develnext.jphp.swing.loader.support;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.common.StringUtils;
import php.runtime.launcher.Launcher;
import php.runtime.memory.support.MemoryUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class Value {
    protected final String value;

    public Value(String value) {
        this.value = value;
    }

    public boolean asBoolean() {
        return "1".equals(value);
    }

    public String asString() {
        return value;
    }

    public int asInteger() {
        return Integer.parseInt(value);
    }

    public Memory asMemory() {
        return MemoryUtils.valueOf(asString(), HintType.ANY);
    }

    public String[] asArray(boolean trim) {
        String[] r = StringUtils.split(value, ',');
        if (trim)
        for(int i = 0; i < r.length; i++)
            r[i] = r[i].trim();
        return r;
    }

    public String[] asArray() {
        return asArray(true);
    }

    public Point asPoint(){
        Dimension d = asDimension();
        return new Point(d.width, d.height);
    }

    public Dimension asDimension(){
        String[] tmp = asArray();
        switch (tmp.length){
            case 0: return new Dimension(0, 0);
            case 1: return new Dimension(Integer.parseInt(tmp[0]), 0);
            default:
            case 2: return new Dimension(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]));
        }
    }

    public Font asFont() {
        if (value.isEmpty())
            return null;

        return Font.decode(value);
    }

    public Color asColor() {
        if (value.isEmpty())
            return null;

        if (value.startsWith("#"))
            return Color.decode(value);
        else {
            String[] tmp = asArray();
            switch (tmp.length) {
                case 0: return new Color(0, 0, 0);
                case 1: return new Color(Integer.parseInt(tmp[0]), 0, 0);
                case 2: return new Color(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]), 0);
                case 3: return new Color(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]), Integer.parseInt(tmp[2]));
                default:
                case 4: return new Color(
                        Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]), Integer.parseInt(tmp[2]), Integer.parseInt(tmp[3])
                );
            }
        }
    }

    public InputStream asResource() {
        return Launcher.current().getResource(value);
    }

    public ImageIcon asIcon(){
        InputStream inputStream = asResource();
        if (inputStream == null)
            return null;

        try {
            return new ImageIcon(ImageIO.read(inputStream));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
