package php.runtime.ext.swing;

import php.runtime.common.StringUtils;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;

final public class BorderUtils {
    public static Border decode(String str) {
        String[] tmp = StringUtils.split(str, ',');
        String type = "empty";

        Map<String, String> args = new HashMap<String, String>();

        for (String aTmp : tmp) {
            String arg = aTmp.trim();
            String[] entry = StringUtils.split(arg, ":", 2);
            if (entry.length == 1)
                type = entry[0].toLowerCase();
            else {
                args.put(entry[0].toLowerCase(), entry[1]);
            }
        }
        Color color = null;
        if (args.containsKey("color"))
            color = Color.decode(args.get("color"));

        int size = 1;
        if (args.containsKey("size")) {
            try {
                size = Integer.parseInt(args.get("size"));
            } catch (NumberFormatException e) {
                size = 1;
            }
        }

        if (type == null || type.equals("empty")) {
            return BorderFactory.createEmptyBorder();
        } else if (type.equals("line")) {
            return new LineBorder(color == null ? Color.GRAY : color, size, args.containsKey("rounded"));
        } else if (type.equals("bevel") || type.equals("bevel-lowered") || type.equals("bevel-raised")) {
            int t = BevelBorder.LOWERED;
            if (type.endsWith("-raised"))
                t = BevelBorder.RAISED;

            if (color == null)
                return BorderFactory.createBevelBorder(t);
            else {
                Color shadowColor = color;
                if (args.containsKey("shadow-color"))
                    shadowColor = Color.decode(args.get("shadow-color"));

                return BorderFactory.createBevelBorder(t, color, shadowColor);
            }
        } else if (type.equals("etched")) {
            return BorderFactory.createEtchedBorder();
        } else if (type.equals("etched-lowered") || type.equals("etched-raised")) {
            int t = EtchedBorder.LOWERED;
            if (type.endsWith("-raised"))
                t = EtchedBorder.RAISED;

            return BorderFactory.createEtchedBorder(t);
        } else if (type.equals("soft-bevel") || type.equals("soft-bevel-lowered") || type.equals("soft-bevel-raised")) {
            int t = BevelBorder.LOWERED;
            if (type.endsWith("-raised"))
                t = BevelBorder.RAISED;

            if (color == null) {
                if (t == BevelBorder.RAISED) {
                    return new SoftBevelBorder(BevelBorder.RAISED);
                }
                return new SoftBevelBorder(BevelBorder.LOWERED);
            } else {
                Color shadowColor = color;
                if (args.containsKey("shadow-color"))
                    shadowColor = Color.decode(args.get("shadow-color"));

                return new SoftBevelBorder(t, color, shadowColor);
            }
        } else
            return BorderFactory.createEmptyBorder();
    }
}
