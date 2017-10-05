package php.runtime.loader.dump;

import php.runtime.Memory;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.loader.dump.io.DumpException;
import php.runtime.loader.dump.io.DumpInputStream;
import php.runtime.loader.dump.io.DumpOutputStream;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.DocumentComment;
import php.runtime.reflection.MethodEntity;
import php.runtime.reflection.ParameterEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MethodDumper extends Dumper<MethodEntity> {
    protected ParameterDumper parameterDumper = new ParameterDumper(context, env, debugInformation);

    protected MethodDumper(Context context, Environment env, boolean debugInformation) {
        super(context, env, debugInformation);
    }

    @Override
    public int getType() {
        return Types.METHOD;
    }

    @Override
    public void save(MethodEntity entity, OutputStream output) throws IOException {
        DumpOutputStream print = new DumpOutputStream(output);

        DocumentComment docComment = entity.getDocComment();

        if (docComment != null) {
            print.writeUTF(docComment.toString());
        } else {
            print.writeUTF("");
        }

        // static
        print.writeBoolean(entity.isStatic());

        // final
        print.writeBoolean(entity.isFinal());

        // abstract
        print.writeBoolean(entity.isAbstract());

        // abstractable
        print.writeBoolean(entity.isAbstractable());

        // immutable
        print.writeBoolean(entity.isImmutable());
        print.writeMemory(entity.getImmutableResult());

        print.writeBoolean(entity.isEmpty());

        // ref
        print.writeBoolean(entity.isReturnReference());

        // type hinting.
        print.writeEnum(entity.getReturnType());
        print.writeName(entity.getReturnTypeClass());
        print.writeBoolean(entity.isReturnTypeNullable());

        // uses stack trace
        print.writeBoolean(entity.isUsesStackTrace());

        // modifier
        print.writeEnum(entity.getModifier());

        // name
        print.writeName(entity.getName());
        print.writeName(entity.getInternalName());

        // trace
        print.writeTrace(debugInformation ? entity.getTrace() : null);

        print.writeInt(entity.getParameters() == null ? 0 : entity.getParameters().length);

        if (entity.getParameters() != null)
        for(ParameterEntity param : entity.getParameters()){
            parameterDumper.save(param, output);
        }

        // raw data
        print.writeRawData(null);
    }

    @Override
    public MethodEntity load(InputStream input) throws IOException {
        DumpInputStream data = new DumpInputStream(input);
        MethodEntity entity = new MethodEntity(context);

        String docComment = data.readUTF();

        if (!docComment.isEmpty()) {
            entity.setDocComment(new DocumentComment(docComment));
        }

        // static
        entity.setStatic( data.readBoolean() );

        // final
        entity.setFinal( data.readBoolean() );

        // abstract
        entity.setAbstract( data.readBoolean() );
        entity.setAbstractable(data.readBoolean());

        entity.setImmutable(data.readBoolean());
        entity.setResult(data.readMemory());

        entity.setEmpty(data.readBoolean());

        // ref
        entity.setReturnReference( data.readBoolean() );

        // type hinting
        entity.setReturnType(data.readHintType());
        String returnTypeClass = data.readName();

        if (returnTypeClass != null && !returnTypeClass.isEmpty()) {
            entity.setReturnTypeClass(returnTypeClass);
        }

        entity.setReturnTypeNullable(data.readBoolean());

        // uses stack trace
        entity.setUsesStackTrace( data.readBoolean() );

        // modifier
        entity.setModifier(data.readModifier());

        // name
        entity.setName(data.readName());
        entity.setInternalName(data.readName());

        // trace
        entity.setTrace(data.readTrace(context));

        int paramCount = data.readInt();
        if (paramCount < 0)
            throw new DumpException("Invalid param count");

        entity.setParameters(new ParameterEntity[paramCount]);
        for(int i = 0; i < paramCount; i++){
            ParameterEntity param = parameterDumper.load(input);
            param.setTrace(entity.getTrace());
            entity.getParameters()[i] = param;
        }

        data.readRawData();
        return entity;
    }
}
