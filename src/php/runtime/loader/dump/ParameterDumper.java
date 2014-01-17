package php.runtime.loader.dump;

import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.reflection.ParameterEntity;
import php.runtime.loader.dump.io.DumpInputStream;
import php.runtime.loader.dump.io.DumpOutputStream;

import java.io.*;

public class ParameterDumper extends Dumper<ParameterEntity> {
    public ParameterDumper(Context context, Environment env, boolean debugInformation) {
        super(context, env, debugInformation);
    }

    @Override
    public int getType() {
        return Types.PARAMETER;
    }

    @Override
    public void save(ParameterEntity entity, OutputStream output) throws IOException {
        DumpOutputStream print = new DumpOutputStream(output);

        // hint type
        print.writeEnum(entity.getType());

        // hint class type
        print.writeName(entity.getTypeClass());

        // & ref
        print.writeBoolean(entity.isReference());

        // name
        print.writeName(entity.getName());

        // trace
        print.writeTrace(debugInformation ? entity.getTrace() : null);

        // memory
        print.writeMemory(entity.getDefaultValue());

        // raw
        print.writeRawData(null);
    }

    @Override
    public ParameterEntity load(InputStream input) throws IOException {
        DumpInputStream data = new DumpInputStream(input);
        ParameterEntity entity = new ParameterEntity(context);

        entity.setType(data.readHintType());
        entity.setTypeClass(data.readName());
        entity.setReference(data.readBoolean());
        entity.setName(data.readName());
        entity.setTrace(data.readTrace(context));

        entity.setDefaultValue(data.readMemory());

        byte[] raw = data.readRawData();
        return entity;
    }
}
