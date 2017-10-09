package php.runtime.loader.dump;

import php.runtime.Information;
import php.runtime.loader.dump.io.DumpException;
import php.runtime.loader.dump.io.DumpInputStream;
import php.runtime.loader.dump.io.DumpOutputStream;
import php.runtime.reflection.ModuleEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class StandaloneLibraryDumper {
    private static final String HASH = "\1\7\4\3\3";

    private StandaloneLibrary library;

    public StandaloneLibraryDumper() {
        library = new StandaloneLibrary();
    }

    public void addModule(ModuleEntity entity) {
        library.addModule(new StandaloneLibrary.Module(entity));
    }

    public void addModules(ModuleEntity... entities) {
        for (ModuleEntity module : entities) {
            addModule(module);
        }
    }

    public void addModules(Iterable<ModuleEntity> entities) {
        for (ModuleEntity module : entities) {
            addModule(module);
        }
    }

    public StandaloneLibrary getLibrary() {
        return library;
    }

    public void save(OutputStream output) throws IOException {
        this.save(output, this.library);
    }

    public void save(OutputStream output, StandaloneLibrary library) throws IOException {
        DumpOutputStream data = new DumpOutputStream(output);

        data.writeName(HASH);
        data.writeName(Information.CORE_VERSION);
        data.writeName(Information.LIKE_PHP_VERSION);

        data.writeInt(library.getModules().size());

        for (StandaloneLibrary.Module module : library.getModules().values()) {
            data.writeName(module.getName());
            data.writeName(module.getInternalName());

            data.writeInt(module.getClasses().size());
            for (String entity : module.getClasses()) {
                data.writeName(entity);
            }

            data.writeInt(module.getFunctions().size());
            for (String entity : module.getFunctions()) {
                data.writeName(entity);
            }

            data.writeInt(module.getConstants().size());
            for (String entity : module.getConstants()) {
                data.writeName(entity);
            }
        }
    }

    public StandaloneLibrary load(InputStream input) throws IOException {
        StandaloneLibrary library = new StandaloneLibrary();

        DumpInputStream data = new DumpInputStream(input);

        String hash = data.readName();

        if (!HASH.equals(hash)) {
            throw new DumpException("Invalid standalone modules, invalid hash");
        }

        String coreVersion = data.readName();
        String likePhpVersion = data.readName();

        library.setCoreVersion(coreVersion);
        library.setLikePhpVersion(likePhpVersion);

        int count = data.readInt();

        for (int i = 0; i < count; i++) {
            String name = data.readName();
            String internalName = data.readName();

            // classes
            int classesSize = data.readInt();
            Set<String> classes = new HashSet<>();
            for (int j = 0; j < classesSize; j++) {
                classes.add(data.readName());
            }

            // functions
            int functionsSize = data.readInt();
            Set<String> functions = new HashSet<>();
            for (int j = 0; j < functionsSize; j++) {
                functions.add(data.readName());
            }

            // constants
            int constantSize = data.readInt();
            Set<String> constants = new HashSet<>();
            for (int j = 0; j < constantSize; j++) {
                constants.add(data.readName());
            }

            StandaloneLibrary.Module module = new StandaloneLibrary.Module(
                    name, internalName, classes, functions, constants
            );

            library.addModule(module);
        }

        return library;
    }
}
