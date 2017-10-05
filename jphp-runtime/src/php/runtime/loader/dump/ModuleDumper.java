package php.runtime.loader.dump;

import php.runtime.common.LangMode;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.loader.dump.io.DumpException;
import php.runtime.loader.dump.io.DumpInputStream;
import php.runtime.loader.dump.io.DumpOutputStream;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.ConstantEntity;
import php.runtime.reflection.FunctionEntity;
import php.runtime.reflection.ModuleEntity;
import php.runtime.reflection.helper.ClosureEntity;
import php.runtime.reflection.helper.GeneratorEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ModuleDumper extends Dumper<ModuleEntity> {
    public final static int DUMP_STAMP = 479873682;
    public final static int DUMP_VERSION = 20141011;

    protected ConstantDumper constantDumper = new ConstantDumper(context, env, debugInformation);
    protected ClosureDumper closureDumper = new ClosureDumper(context, env, debugInformation);
    protected FunctionDumper functionDumper = new FunctionDumper(context, env, debugInformation);
    protected ClassDumper classDumper = new ClassDumper(context, null, env, debugInformation);
    protected GeneratorDumper generatorDumper = new GeneratorDumper(context, env, debugInformation);


    public ModuleDumper(Context context, Environment env, boolean debugInformation) {
        super(context, env, debugInformation);
    }

    @Override
    public void setIncludeData(boolean includeData) {
        super.setIncludeData(includeData);
        constantDumper.setIncludeData(includeData);
        closureDumper.setIncludeData(includeData);
        functionDumper.setIncludeData(includeData);
        classDumper.setIncludeData(includeData);
        generatorDumper.setIncludeData(includeData);
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

        // legacy code.
        data.writeEnum(LangMode.DEFAULT);

        // module name
        data.writeName(entity.getContext().getModuleName());
        data.writeName(entity.getInternalName());

        // strict types.
        data.writeBoolean(entity.isStrictTypes());

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

        // generators
        data.writeInt(entity.getGenerators().size());
        for(GeneratorEntity e : entity.getGenerators()){
            generatorDumper.save(e, output);
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

        if (includeData) {
            // byte code
            data.writeInt(entity.getData().length);
            data.write(entity.getData());
        } else {
            data.writeInt(0);
        }

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

        data.readLangMode(); // legacy

        ModuleEntity entity = new ModuleEntity(context);
        entity.setName(data.readName());
        entity.setInternalName(data.readName());
        entity.setStrictTypes(data.readBoolean());
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

        count = data.readInt();
        for(int i = 0; i < count; i++){
            GeneratorEntity el = generatorDumper.load(input);
            el.setModule(entity);
            entity.addGenerator(el);
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
        entity.setData(data.readRawData(Integer.MAX_VALUE));

        data.readRawData(1024 * 1024 * 5);
        return entity;
    }
}
