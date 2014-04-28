package php.runtime.ext.core.classes.util;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.lang.support.IComparableObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import java.util.Locale;

import static php.runtime.annotation.Reflection.*;

@Name("php\\util\\Locale")
public class WrapLocale extends BaseObject implements IComparableObject<WrapLocale> {
    protected Locale locale;

    public WrapLocale(Environment env, Locale locale) {
        super(env);
        this.locale = locale;
    }

    public WrapLocale(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public Locale getLocale() {
        return locale;
    }

    @Signature({
            @Arg("lang"),
            @Arg(value = "country", optional = @Optional("")),
            @Arg(value = "variant", optional = @Optional(""))
    })
    public Memory __construct(Environment env, Memory... args) {
        locale = new Locale(args[0].toString(), args[1].toString(), args[2].toString());
        return Memory.NULL;
    }

    @Signature
    public Memory __debugInfo(Environment env, Memory... args) {
        ArrayMemory r = new ArrayMemory();
        r.refOfIndex("*language").assign(locale.getLanguage());
        r.refOfIndex("*country").assign(locale.getCountry());
        r.refOfIndex("*variant").assign(locale.getVariant());

        return r.toConstant();
    }

    public static Locale getDefault(Environment env, Memory arg) {
        if (arg.isNull())
            return getDefault(env);
        else
            return arg.toObject(WrapLocale.class).locale;
    }

    public static Locale getDefault(Environment env) {
        Locale locale1 = env.getUserValue(WrapLocale.class.getName() + "#default", Locale.class);
        if (locale1 == null)
            return Locale.getDefault();

        return locale1;
    }

    @Signature(@Arg(value = "globally", optional = @Optional("false")))
    public static Memory getDefault(Environment env, Memory... args) {
        return new ObjectMemory(new WrapLocale(env, args[0].toBoolean() ? Locale.getDefault() : getDefault(env)));
    }

    @Signature({
            @Arg(value = "locale", nativeType = WrapLocale.class),
            @Arg(value = "globally", optional = @Optional("false"))
    })
    public static Memory setDefault(Environment env, Memory... args) {
        Locale locale = args[0].toObject(WrapLocale.class).locale;
        if (args[1].toBoolean())
            Locale.setDefault(locale);
        else
            env.setUserValue(WrapLocale.class.getName() + "#default", locale);
        return Memory.NULL;
    }

    @Signature
    public Memory getLanguage(Environment env, Memory... args) {
        return StringMemory.valueOf(locale.getLanguage());
    }

    @Signature
    public Memory getCountry(Environment env, Memory... args) {
        return StringMemory.valueOf(locale.getCountry());
    }

    @Signature
    public Memory getVariant(Environment env, Memory... args) {
        return StringMemory.valueOf(locale.getVariant());
    }

    @Signature
    public Memory getISO3Country(Environment env, Memory... args) {
        return StringMemory.valueOf(locale.getISO3Country());
    }

    @Signature
    public Memory getISO3Language(Environment env, Memory... args) {
        return StringMemory.valueOf(locale.getISO3Language());
    }

    @Signature(@Arg(value = "locale", nativeType = WrapLocale.class, optional = @Optional("null")))
    public Memory getDisplayName(Environment env, Memory... args) {
        return StringMemory.valueOf(args[0].isNull()
                ? locale.getDisplayName()
                : locale.getDisplayName(args[0].toObject(WrapLocale.class).locale)
        );
    }

    @Signature(@Arg(value = "locale", nativeType = WrapLocale.class, optional = @Optional("null")))
    public Memory getDisplayCountry(Environment env, Memory... args) {
        return StringMemory.valueOf(args[0].isNull()
                ? locale.getDisplayCountry()
                : locale.getDisplayCountry(args[0].toObject(WrapLocale.class).locale)
        );
    }

    @Signature(@Arg(value = "locale", nativeType = WrapLocale.class, optional = @Optional("null")))
    public Memory getDisplayLanguage(Environment env, Memory... args) {
        return StringMemory.valueOf(args[0].isNull()
                ? locale.getDisplayLanguage()
                : locale.getDisplayLanguage(args[0].toObject(WrapLocale.class).locale)
        );
    }

    @Signature(@Arg(value = "locale", nativeType = WrapLocale.class, optional = @Optional("null")))
    public Memory getDisplayVariant(Environment env, Memory... args) {
        return StringMemory.valueOf(args[0].isNull()
                ? locale.getDisplayVariant()
                : locale.getDisplayVariant(args[0].toObject(WrapLocale.class).locale)
        );
    }

    @Signature
    public Memory __toString(Environment env, Memory... args) {
        return StringMemory.valueOf(locale.toString());
    }

    @Signature
    public static Memory ENGLISH(Environment env, Memory... args) {
        return new ObjectMemory(new WrapLocale(env, Locale.ENGLISH));
    }

    @Signature
    public static Memory GERMAN(Environment env, Memory... args) {
        return new ObjectMemory(new WrapLocale(env, Locale.GERMAN));
    }

    @Signature
    public static Memory GERMANY(Environment env, Memory... args) {
        return new ObjectMemory(new WrapLocale(env, Locale.GERMANY));
    }

    @Signature
    public static Memory CANADA(Environment env, Memory... args) {
        return new ObjectMemory(new WrapLocale(env, Locale.CANADA));
    }

    @Signature
    public static Memory CANADA_FRENCH(Environment env, Memory... args) {
        return new ObjectMemory(new WrapLocale(env, Locale.CANADA_FRENCH));
    }

    @Signature
    public static Memory CHINA(Environment env, Memory... args) {
        return new ObjectMemory(new WrapLocale(env, Locale.CHINA));
    }

    @Signature
    public static Memory CHINESE(Environment env, Memory... args) {
        return new ObjectMemory(new WrapLocale(env, Locale.CHINESE));
    }

    @Signature
    public static Memory FRANCE(Environment env, Memory... args) {
        return new ObjectMemory(new WrapLocale(env, Locale.FRANCE));
    }

    @Signature
    public static Memory FRENCH(Environment env, Memory... args) {
        return new ObjectMemory(new WrapLocale(env, Locale.FRENCH));
    }

    @Signature
    public static Memory US(Environment env, Memory... args) {
        return new ObjectMemory(new WrapLocale(env, Locale.US));
    }

    @Signature
    public static Memory ITALIAN(Environment env, Memory... args) {
        return new ObjectMemory(new WrapLocale(env, Locale.ITALIAN));
    }

    @Signature
    public static Memory ITALY(Environment env, Memory... args) {
        return new ObjectMemory(new WrapLocale(env, Locale.ITALY));
    }

    @Signature
    public static Memory JAPAN(Environment env, Memory... args) {
        return new ObjectMemory(new WrapLocale(env, Locale.JAPAN));
    }

    @Signature
    public static Memory JAPANESE(Environment env, Memory... args) {
        return new ObjectMemory(new WrapLocale(env, Locale.JAPANESE));
    }

    @Signature
    public static Memory KOREA(Environment env, Memory... args) {
        return new ObjectMemory(new WrapLocale(env, Locale.KOREA));
    }

    @Signature
    public static Memory KOREAN(Environment env, Memory... args) {
        return new ObjectMemory(new WrapLocale(env, Locale.KOREAN));
    }

    @Signature
    public static Memory UK(Environment env, Memory... args) {
        return new ObjectMemory(new WrapLocale(env, Locale.UK));
    }

    @Signature
    public static Memory TAIWAN(Environment env, Memory... args) {
        return new ObjectMemory(new WrapLocale(env, Locale.TAIWAN));
    }

    @Signature
    public static Memory RUSSIAN(Environment env, Memory... args) {
        return new ObjectMemory(new WrapLocale(env, new Locale("ru")));
    }

    @Signature
    public static Memory RUSSIA(Environment env, Memory... args) {
        return new ObjectMemory(new WrapLocale(env, new Locale("ru", "RU")));
    }

    @Signature
    public static Memory ROOT(Environment env, Memory... args) {
        return new ObjectMemory(new WrapLocale(env, Locale.ROOT));
    }

    @Signature
    public static Memory getAvailableLocales(Environment env, Memory... args) {
        ArrayMemory r = new ArrayMemory();
        for(Locale el : Locale.getAvailableLocales()) {
            r.add(new WrapLocale(env, el));
        }
        return r.toConstant();
    }

    @Override
    public boolean __equal(WrapLocale iObject) {
        return locale.equals(iObject.getLocale());
    }

    @Override
    public boolean __identical(WrapLocale iObject) {
        return locale == iObject.getLocale();
    }

    @Override
    public boolean __greater(WrapLocale iObject) {
        return false;
    }

    @Override
    public boolean __greaterEq(WrapLocale iObject) {
        return false;
    }

    @Override
    public boolean __smaller(WrapLocale iObject) {
        return false;
    }

    @Override
    public boolean __smallerEq(WrapLocale iObject) {
        return false;
    }

    @Signature
    private Memory __clone(Environment env, Memory... args) {
        return Memory.NULL;
    }
}
