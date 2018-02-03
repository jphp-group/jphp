package php.runtime.loader.dump;

import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.loader.dump.io.DumpInputStream;
import php.runtime.loader.dump.io.DumpOutputStream;
import php.runtime.reflection.DocumentComment;
import php.runtime.reflection.PropertyEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PropertyDumper extends Dumper<PropertyEntity> {
    public PropertyDumper(Context context, Environment env, boolean debugInformation) {
        super(context, env, debugInformation);
    }

    @Override
    public int getType() {
        return Types.PROPERTY;
    }

    @Override
    public void save(PropertyEntity entity, OutputStream output) throws IOException {
        DumpOutputStream print = new DumpOutputStream(output);

        DocumentComment docComment = entity.getDocComment();

        if (docComment != null) {
            print.writeUTF(docComment.toString());
        } else {
            print.writeUTF("");
        }

        // static
        print.writeBoolean(entity.isStatic());

        // modifier
        print.writeEnum(entity.getModifier());

        // name
        print.writeName(entity.getName());

        // trace
        print.writeTrace(debugInformation ? entity.getTrace() : null);

        // def
        print.writeBoolean(entity.isDefault());

        // def value
        print.writeMemory(entity.getDefaultValue());

        // raw
        print.writeRawData(null);
    }

    @Override
    public PropertyEntity load(InputStream input) throws IOException {
        PropertyEntity property = new PropertyEntity(context);
        DumpInputStream data = new DumpInputStream(input);

        String docComment = data.readUTF();

        if (!docComment.isEmpty()) {
            property.setDocComment(new DocumentComment(docComment));
        }

        property.setStatic(data.readBoolean());
        property.setModifier(data.readModifier());
        property.setName(data.readName());
        property.setTrace(data.readTrace(context));
        property.setDefault(data.readBoolean());
        property.setDefaultValue(data.readMemory());

        byte[] raw = data.readRawData();
        return property;
    }
}
