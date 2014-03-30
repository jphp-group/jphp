package php.runtime.loader.dump;

import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.ConstantEntity;
import php.runtime.reflection.FunctionEntity;
import php.runtime.reflection.ModuleEntity;
import php.runtime.loader.dump.io.DumpException;
import php.runtime.loader.dump.io.DumpInputStream;
import php.runtime.loader.dump.io.DumpOutputStream;
import php.runtime.reflection.helper.ClosureEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ModuleDumper extends Dumper<ModuleEntity> {
    public final static int DUMP_STAMP = 479873682;
    public final static int DUMP_VERSION = 20140330;

    protected ConstantDumper constantDumper = new ConstantDumper(context, env, debugInformation);
    protected ClosureDumper closureDumper = new ClosureDumper(context, env, debugInformation);
    protected FunctionDumper functionDumper = new FunctionDumper(context, env, debugInformation);
    protected ClassDumper classDumper = new ClassDumper(context, null, env, debugInformation);

    public ModuleDumper(Context context, Environment env, boolean debugInformation) {
        super(context, env, debugInformation);
    }

    @Override
    public int getType() {
        return -1;
    }

    @Override
    public void save(ModuleEntity entity, OutputStream output) throws IOException {
        if (entity.getData() == null)
            throw new DumpException("Module '" + entity.getName() + "' not compiled");

        DumpOutputStream data = new DumpOutputStream(output);

        // version
        data.writeInt(DUMP_STAMP);
        data.writeInt(DUMP_VERSION);

        data.writeEnum(entity.getLangMode());
        // module name
        data.writeName(entity.getContext().getModuleName());
        data.writeName(entity.getInternalName());

        // trace
        data.writeTrace(entity.getTrace());

        // constants
        data.writeInt(entity.getConstants().size());
        for(ConstantEntity e : entity.getConstants()){
            constantDumper.save(e, output);
        }

        // closures
        data.writeInt(entity.getClosures().size());
        for(ClosureEntity e : entity.getClosures()){
            closureDumper.save(e, output);
        }

        // functions
        data.writeInt(entity.getFunctions().size());
        for(FunctionEntity e : entity.getFunctions()){
            functionDumper.save(e, output);
        }

        // classes
        data.writeInt(entity.getClasses().size());
        for(ClassEntity e : entity.getClasses()){
            classDumper.save(e, output);
        }

        // byte code
        data.writeInt(entity.getData().length);
        data.write(entity.getData());

        data.writeRawData(null, 1024 * 1024 * 5); // 5 mb
    }

    @Override
    public ModuleEntity load(InputStream input) throws IOException {
        DumpInputStream data = new DumpInputStream(input);

        int STAMP = data.readInt();
        if (STAMP != DUMP_STAMP)
            throw new DumpException("Invalid file format");

        int VERSION = data.readInt();
        if (VERSION != DUMP_VERSION)
            throw new DumpException("Invalid dump version - " + VERSION + ", only " + DUMP_VERSION);

        ModuleEntity entity = new ModuleEntity(context, data.readLangMode());
        entity.setName(data.readName());
        entity.setInternalName(data.readName());
        entity.setTrace(data.readTrace(context));

        // constants
        int count = data.readInt();
        for(int i = 0; i < count; i++){
            ConstantEntity el = constantDumper.load(input);
            el.setModule(entity);
            entity.addConstant(el);
        }

        // closures
        count = data.readInt();
        for(int i = 0; i < count; i++){
            ClosureEntity el = closureDumper.load(input);
            el.setModule(entity);
            entity.addClosure(el);
        }

        // functions
        count = data.readInt();
        for(int i = 0; i < count; i++){
            FunctionEntity el = functionDumper.load(input);
            el.setModule(entity);
            entity.addFunction(el);
        }

        // classes
        count = data.readInt();
        ClassDumper classDumper = new ClassDumper(context, entity, env, debugInformation);
        for(int i = 0; i < count; i++){
            ClassEntity el = classDumper.load(input);
            el.setModule(entity);
            entity.addClass(el);
        }

        // byte code
        int dataLength = data.readInt();
        byte[] code = new byte[dataLength];
        if (data.read(code) != dataLength)
            throw new DumpException("Cannot read module byte-code");

        entity.setData(code);

        data.readRawData(1024 * 1024 * 5);
        return entity;
    }
}
