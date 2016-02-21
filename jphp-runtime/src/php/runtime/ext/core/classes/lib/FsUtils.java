package php.runtime.ext.core.classes.lib;

import php.runtime.Memory;
import php.runtime.annotation.*;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.annotation.Runtime;
import php.runtime.annotation.Runtime.FastMethod;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import java.io.File;
import java.io.IOException;

@Name("php\\lib\\fs")
public class FsUtils extends BaseObject {
    public FsUtils(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private Memory __construct(Environment env, Memory... args) { return Memory.NULL; }

    @Signature
    public static String abs(String path) {
        try {
            return new File(path).getCanonicalPath();
        } catch (IOException e) {
            return new File(path).getAbsolutePath();
        }
    }

    @Signature
    public static String name(String path) {
        return new File(path).getName();
    }

    @Signature
    public static String ext(String path) {
        String name = new File(path).getName();

        int indexOf = name.lastIndexOf('.');

        if (indexOf > -1) {
            return name.substring(indexOf + 1);
        } else {
            return null;
        }
    }

    @Signature
    public static String nameNoExt(String path) {
        String name = new File(path).getName();

        int indexOf = name.lastIndexOf('.');

        if (indexOf > -1) {
            name = name.substring(0, indexOf);
        }

        return name;
    }

    @Signature
    public static long size(String path) {
        return new File(path).length();
    }

    @Signature
    public static long time(String path) {
        return new File(path).lastModified();
    }

    @Signature
    public static boolean isFile(String path) {
        return new File(path).isFile();
    }

    @Signature
    public static boolean isDir(String path) {
        return new File(path).isDirectory();
    }

    @Signature
    public static boolean isHidden(String path) {
        return new File(path).isHidden();
    }

    @Signature
    public static String normalize(String path) {
        return new File(path).getPath();
    }

    @Signature
    public static String parent(String path) {
        return new File(path).getParent();
    }

    @Signature
    public static boolean exists(String path) {
        return new File(path).exists();
    }

    @Signature
    @FastMethod
    public static Memory separator(Environment env, Memory... args) {
        return StringMemory.valueOf(File.separator);
    }

    @Signature
    @FastMethod
    public static Memory pathSeparator(Environment env, Memory... args) {
        return StringMemory.valueOf(File.pathSeparator);
    }

    @Signature
    public static boolean makeDir(String path) {
        return new File(path).mkdirs();
    }

    @Signature
    public static boolean makeFile(String path) {
        String parent = parent(path);

        if (parent != null && !isDir(path)) {
            if (!makeDir(parent)) {
                return false;
            }
        }

        try {
            return new File(path).createNewFile();
        } catch (IOException e) {
            return false;
        }
    }


    @Signature
    public static boolean delete(String path) {
        return new File(path).delete();
    }
}
