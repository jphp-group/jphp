package php.runtime.ext.core.classes.util;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.ext.core.classes.stream.WrapIOException;
import php.runtime.lang.BaseObject;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.spl.iterator.Iterator;
import php.runtime.lang.support.IComparableObject;
import php.runtime.memory.DoubleMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import java.util.NoSuchElementException;
import java.util.Scanner;

import static php.runtime.annotation.Reflection.*;

@Name("php\\util\\Scanner")
public class WrapScanner extends BaseObject
        implements IComparableObject<WrapScanner>, Iterator {
    protected Scanner scanner;
    protected Memory current = null;
    protected Memory key = Memory.CONST_INT_M1;
    protected boolean valid = true;

    public WrapScanner(Environment env, Scanner scanner) {
        super(env);
        this.scanner = scanner;
    }

    public WrapScanner(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public Scanner getScanner() {
        return scanner;
    }

    @Signature({
            @Arg("source"),
            @Arg(value = "charset", optional = @Optional("null"))
    })
    public Memory __construct(Environment env, Memory... args) {
        if (args[0].instanceOf(Stream.class)) {
            if (args[1].isNull())
                scanner = new Scanner(Stream.getInputStream(env, args[0]));
            else
                scanner = new Scanner(Stream.getInputStream(env, args[0]), args[1].toString());
        } else {
            scanner = new Scanner(args[0].toString());
        }

        return Memory.NULL;
    }

    @Signature(@Arg(value = "pattern", nativeType = WrapRegex.class, optional = @Optional("null")))
    public Memory hasNext(Environment env, Memory... args) {
        if (args[0].isNull())
            return scanner.hasNext() ? Memory.TRUE : Memory.FALSE;
        else
            return scanner.hasNext(args[0].toObject(WrapRegex.class).getMatcher().pattern()) ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory getIOException(Environment env, Memory... args) {
        if (scanner.ioException() == null)
            return Memory.NULL;
        else
            return new ObjectMemory(new WrapIOException(env, scanner.ioException()));
    }

    @Override
    @Signature
    public Memory current(Environment env, Memory... args) {
        return current;
    }

    @Override
    @Signature
    public Memory key(Environment env, Memory... args) {
        return key;
    }

    @Signature(@Arg(value = "pattern", nativeType = WrapRegex.class, optional = @Optional("null")))
    public Memory next(Environment env, Memory... args) {
        try {
            key = key.inc();
            return current = StringMemory.valueOf(args.length == 0 || args[0].isNull()
                    ? scanner.next()
                    : scanner.next(args[0].toObject(WrapRegex.class).getMatcher().pattern())
            );
        } catch (NoSuchElementException e) {
            valid = false;
            current = Memory.NULL;
            key = Memory.NULL;
            return Memory.NULL;
        }
    }

    @Override
    @Signature
    public Memory rewind(Environment env, Memory... args) {
        if (current != null)
            env.exception("Iterator of the scanner can be used only one time");

        valid = true;
        next(env, Memory.NULL);

        return Memory.NULL;
    }

    @Override
    @Signature
    public Memory valid(Environment env, Memory... args) {
        return valid ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory nextLine(Environment env, Memory... args) {
        try {
            return StringMemory.valueOf(scanner.nextLine());
        } catch (NoSuchElementException e) {
            return Memory.NULL;
        }
    }

    @Signature
    public Memory hasNextLine(Environment env, Memory... args) {
        return scanner.hasNextLine() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg(value = "radix", optional = @Optional("null")))
    public Memory hasNextInt(Environment env, Memory... args) {
        return (args[0].isNull() ? scanner.hasNextLong() : scanner.hasNextInt(args[0].toInteger()))
                ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg(value = "radix", optional = @Optional("null")))
    public Memory nextInt(Environment env, Memory... args) {
        try {
            return LongMemory.valueOf(args[0].isNull()
                    ? scanner.nextLong()
                    : scanner.nextLong(args[0].toInteger()));
        } catch (NoSuchElementException e) {
            return Memory.NULL;
        }
    }

    @Signature
    public Memory nextDouble(Environment env, Memory... args) {
        try {
            return new DoubleMemory(scanner.nextDouble());
        } catch (NoSuchElementException e) {
            return Memory.NULL;
        }
    }

    @Signature
    public Memory hasNextDouble(Environment env, Memory... args) {
        return scanner.hasNextDouble() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg(value = "pattern", nativeType = WrapRegex.class))
    public Memory useDelimiter(Environment env, Memory... args) {
        scanner.useDelimiter(args[0].toObject(WrapRegex.class).getMatcher().pattern());
        return new ObjectMemory(this);
    }

    @Signature(@Arg(value = "locale", nativeType = WrapLocale.class))
    public Memory useLocale(Environment env, Memory... args) {
        scanner.useLocale(args[0].toObject(WrapLocale.class).getLocale());
        return new ObjectMemory(this);
    }

    @Signature(@Arg("value"))
    public Memory useRadix(Environment env, Memory... args) {
        scanner.useRadix(args[0].toInteger());
        return new ObjectMemory(this);
    }

    @Signature(@Arg(value = "regex", nativeType = WrapRegex.class))
    public Memory skip(Environment env, Memory... args) {
        try {
            scanner.skip(args[0].toObject(WrapRegex.class).getMatcher().pattern());
            return Memory.TRUE;
        } catch (NoSuchElementException e) {
            return Memory.FALSE;
        }
    }

    @Signature
    public Memory reset(Environment env, Memory... args) {
        scanner.reset();
        return Memory.NULL;
    }

    @Signature
    public Memory __destruct(Environment env, Memory... args) {
        scanner.close();
        return Memory.NULL;
    }

    @Override
    public boolean __equal(WrapScanner iObject) {
        return false;
    }

    @Override
    public boolean __identical(WrapScanner iObject) {
        return false;
    }

    @Override
    public boolean __greater(WrapScanner iObject) {
        return false;
    }

    @Override
    public boolean __greaterEq(WrapScanner iObject) {
        return false;
    }

    @Override
    public boolean __smaller(WrapScanner iObject) {
        return false;
    }

    @Override
    public boolean __smallerEq(WrapScanner iObject) {
        return false;
    }

    @Signature
    private Memory __clone(Environment env, Memory... args) {
        return Memory.NULL;
    }

    @Override
    public ForeachIterator getNewIterator(Environment env, boolean getReferences, boolean getKeyReferences) {
        return ObjectMemory.valueOf(this).getNewIterator(env, getReferences, getKeyReferences);
    }

    @Override
    public ForeachIterator getNewIterator(Environment env) {
        return ObjectMemory.valueOf(this).getNewIterator(env);
    }
}
