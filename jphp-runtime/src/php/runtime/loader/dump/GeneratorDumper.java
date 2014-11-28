package php.runtime.loader.dump;

import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.loader.dump.io.DumpException;
import php.runtime.loader.dump.io.DumpInputStream;
import php.runtime.loader.dump.io.DumpOutputStream;
import php.runtime.reflection.ParameterEntity;
import php.runtime.reflection.helper.GeneratorEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class GeneratorDumper extends Dumper<GeneratorEntity> {

    protected final ClassDumper classDumper;
    protected ParameterDumper parameterDumper = new ParameterDumper(context, env, debugInformation);

    public GeneratorDumper(Context context, Environment env, boolean debugInformation) {
        super(context, env, debugInformation);
        classDumper = new ClassDumper(context, null, env, debugInformation);
    }

    @Override
    public int getType() {
        return Types.GENERATOR;
    }

    @Override
    public void setIncludeData(boolean includeData) {
        super.setIncludeData(includeData);
        classDumper.setIncludeData(includeData);
    }

    @Override
    public void save(GeneratorEntity entity, OutputStream output) throws IOException {
        classDumper.save(entity, output);

        DumpOutputStream printer = new DumpOutputStream(output);

        printer.writeBoolean(entity.isReturnReference());
        printer.writeInt(entity.parameters == null ? 0 : entity.parameters.length);

        if (entity.parameters != null) {
            for (ParameterEntity param : entity.parameters) {
                parameterDumper.save(param, output);
            }
        }
    }

    @Override
    public GeneratorEntity load(InputStream input) throws IOException {
        GeneratorEntity entity = classDumper.load(input, GeneratorEntity.class);

        DumpInputStream data = new DumpInputStream(input);

        entity.setReturnReference(data.readBoolean());

        int paramCount = data.readInt();
        if (paramCount < 0)
            throw new DumpException("Invalid param count");

        entity.parameters = new ParameterEntity[paramCount];
        for(int i = 0; i < paramCount; i++){
            ParameterEntity param = parameterDumper.load(input);
            param.setTrace(entity.getTrace());
            entity.parameters[i] = param;
        }

        return entity;
    }
}
