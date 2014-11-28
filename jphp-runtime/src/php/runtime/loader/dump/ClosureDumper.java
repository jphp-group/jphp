package php.runtime.loader.dump;

import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.lang.Closure;
import php.runtime.loader.dump.io.DumpException;
import php.runtime.loader.dump.io.DumpInputStream;
import php.runtime.loader.dump.io.DumpOutputStream;
import php.runtime.reflection.ParameterEntity;
import php.runtime.reflection.helper.ClosureEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ClosureDumper extends Dumper<ClosureEntity> {
    protected ParameterDumper parameterDumper = new ParameterDumper(context, env, debugInformation);
    protected final MethodDumper methodDumper = new MethodDumper(context, env, debugInformation);

    public ClosureDumper(Context context, Environment env, boolean debugInformation) {
        super(context, env, debugInformation);
    }

    @Override
    public int getType() {
        return Types.CLOSURE;
    }

    @Override
    public void save(ClosureEntity entity, OutputStream output) throws IOException {
        if (!entity.isLoaded())
            throw new DumpException("Closure not loaded");

        DumpOutputStream data = new DumpOutputStream(output);

        data.writeName(entity.getInternalName());
        data.writeBoolean(entity.isReturnReference());

        methodDumper.save(entity.methodMagicInvoke, output);

        data.writeInt(entity.parameters == null ? 0 : entity.parameters.length);
        if (entity.parameters != null)
            for(ParameterEntity param : entity.parameters){
                parameterDumper.save(param, output);
            }


        data.writeInt(entity.uses == null ? 0 : entity.uses.length);
        if (entity.uses != null)
            for(ParameterEntity param : entity.uses){
                parameterDumper.save(param, output);
            }

        if (includeData) {
            data.writeRawData(entity.getData(), Integer.MAX_VALUE);
        } else {
            data.writeRawData(null);
        }
    }

    @Override
    public ClosureEntity load(InputStream input) throws IOException {
        DumpInputStream data = new DumpInputStream(input);
        ClosureEntity entity = new ClosureEntity(context);
        entity.setParent(env.scope.fetchUserClass(Closure.class));

        entity.setInternalName(data.readName());
        entity.setReturnReference(data.readBoolean());

        entity.methodMagicInvoke = methodDumper.load(input);
        entity.methodMagicInvoke.setClazz(entity);
        entity.addMethod(entity.methodMagicInvoke, null);

        int paramCount = data.readInt();
        if (paramCount < 0)
            throw new DumpException("Invalid param count");

        entity.parameters = new ParameterEntity[paramCount];
        for(int i = 0; i < paramCount; i++){
            ParameterEntity param = parameterDumper.load(input);
            param.setTrace(entity.getTrace());
            entity.parameters[i] = param;
        }

        paramCount = data.readInt();
        if (paramCount < 0)
            throw new DumpException("Invalid param count");

        entity.uses = new ParameterEntity[paramCount];
        for(int i = 0; i < paramCount; i++){
            ParameterEntity param = parameterDumper.load(input);
            param.setTrace(entity.getTrace());
            entity.uses[i] = param;
        }

        entity.setData(data.readRawData(Integer.MAX_VALUE));
        return entity;
    }
}
