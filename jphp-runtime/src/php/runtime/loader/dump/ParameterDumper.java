package php.runtime.loader.dump;

import php.runtime.common.StringUtils;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.loader.dump.io.DumpInputStream;
import php.runtime.loader.dump.io.DumpOutputStream;
import php.runtime.reflection.ParameterEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

        // mutable
        print.writeBoolean(entity.isMutable());

        // used
        print.writeBoolean(entity.isUsed());

        // variadic
        print.writeBoolean(entity.isVariadic());

        // nullable
        print.writeBoolean(entity.isNullable());

        // name
        print.writeName(entity.getName());

        // trace
        print.writeTrace(debugInformation ? entity.getTrace() : null);

        // memory
        print.writeMemory(entity.getDefaultValue());
        print.writeName(entity.getDefaultValueConstName());

        // raw
        print.writeRawData(null);
    }

    @Override
    public ParameterEntity load(InputStream input) throws IOException {
        DumpInputStream data = new DumpInputStream(input);
        ParameterEntity entity = new ParameterEntity(context);

        entity.setType(data.readHintType());
        String typeClass = data.readName();

        if (typeClass != null && !typeClass.isEmpty()) {
            entity.setTypeClass(typeClass);
        }

        entity.setReference(data.readBoolean());

        entity.setMutable(data.readBoolean());
        entity.setUsed(data.readBoolean());
        entity.setVariadic(data.readBoolean());
        entity.setNullable(data.readBoolean());

        entity.setName(data.readName());
        entity.setTrace(data.readTrace(context));

        entity.setDefaultValue(data.readMemory());
        entity.setDefaultValueConstName(data.readName());

        byte[] raw = data.readRawData();
        return entity;
    }
}
