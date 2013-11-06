package ru.regenix.jphp.runtime;

final public class TypeUtils {

    private TypeUtils() {}

    public static String _2S(Object object){
        return object == null ? "" : object.toString();
    }

    public static boolean _2B(Object object){
        return false;
    }

    public static int _2I(Object object){
        return 0;
    }

    public static long _2L(Object object){
        return 0;
    }

    public static float _2F(Object object){
        return 0f;
    }

    public static double _2D(Object object){
        return 0.0;
    }

    public static boolean S2B(String value){
        return !(value == null || value.isEmpty() || value.equals("0"));
    }

    public static int S2I(String value, int base){
        if (value == null)
            return 0;

        try {
            return Integer.parseInt(value.trim(), base);
        } catch (NumberFormatException e){
            return 0;
        }
    }

    public static int S2I(String value){
        return S2I(value, 10);
    }

    public static long S2L(String value, int base){
        if (value == null)
            return 0L;

        try {
            return Long.parseLong(value.trim(), base);
        } catch (NumberFormatException e){
            return 0;
        }
    }

    public static long S2L(String value){
        return S2L(value, 10);
    }

    public static float S2F(String value){
        if (value == null)
            return 0.0f;

        try {
            return Float.parseFloat(value.trim());
        } catch (NumberFormatException e){
            return 0.0f;
        }
    }

    public static double S2D(String value){
        if (value == null)
            return 0.0;

        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e){
            return 0.0;
        }
    }

    public static long B2L(boolean value){
        return value ? 1L : 0L;
    }

    public static float B2F(boolean value){
        return value ? 1F : 0F;
    }
}
