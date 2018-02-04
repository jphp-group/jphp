package php.runtime.loader.dump;

import php.runtime.common.Messages;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.loader.dump.io.DumpException;
import php.runtime.loader.dump.io.DumpInputStream;
import php.runtime.loader.dump.io.DumpOutputStream;
import php.runtime.reflection.*;
import php.runtime.reflection.helper.GeneratorEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ClassDumper extends Dumper<ClassEntity> {
    protected final PropertyDumper propertyDumper = new PropertyDumper(context, env, debugInformation);
    protected final ConstantDumper constantDumper = new ConstantDumper(context, env, debugInformation);
    protected final MethodDumper methodDumper = new MethodDumper(context, env, debugInformation);

    protected final ModuleEntity module;

    public ClassDumper(Context context, ModuleEntity module, Environment env, boolean debugInformation) {
        super(context, env, debugInformation);
        this.module = module;
    }

    @Override
    public int getType() {
        return Types.CLASS;
    }

    @Override
    public void save(ClassEntity entity, OutputStream output) throws IOException {
        if (entity.getType() == ClassEntity.Type.CLASS && entity.getData() == null)
            throw new DumpException("Class '" + entity.getName() + "' not compiled");

        DumpOutputStream printer = new DumpOutputStream(output);

        DocumentComment docComment = entity.getDocComment();

        if (docComment != null) {
            printer.writeUTF(docComment.toString());
        } else {
            printer.writeUTF("");
        }

        printer.writeBoolean(entity.isStatic());

        // type
        printer.writeEnum(entity.getType());

        printer.writeBoolean(entity.isAbstract());
        printer.writeBoolean(entity.isFinal());

        // print name
        printer.writeName(entity.getName());
        printer.writeName(entity.getCompiledInternalName());

        printer.writeTrace(debugInformation ? entity.getTrace() : null);

        // parent
        ClassEntity parent = entity.getParent();
        if (entity.getParent() != null){
            printer.writeName(parent.getName());
        } else {
            printer.writeName(null); // write null
        }

        // constants
        List<ConstantEntity> constants = new ArrayList<ConstantEntity>();
        for(ConstantEntity el : entity.getConstants()){
            if (el.isOwned(entity))
                constants.add(el);
        }

        printer.writeInt(constants.size());
        for(ConstantEntity el : constants){
            constantDumper.save(el, output);
        }

        // properties
        List<PropertyEntity> properties = new ArrayList<PropertyEntity>();
        for(PropertyEntity el : entity.getStaticProperties()){
            if (el.isOwned(entity))
                properties.add(el);
        }
        for(PropertyEntity el : entity.getProperties()){
            if (el.isOwned(entity))
                properties.add(el);
        }

        printer.writeInt(properties.size());
        for(PropertyEntity el : properties){
            propertyDumper.save(el, output);
        }

        // methods
        printer.writeInt(entity.getMethodCounts());

        List<MethodEntity> methods = entity.getOwnedMethods();
        printer.writeInt(methods.size());
        for (MethodEntity el : methods){
            methodDumper.save(el, output);
        }

        // interfaces
        printer.writeInt(entity.getInterfaces().size());
        for(ClassEntity el : entity.getInterfaces().values()){
            printer.writeName(el.getName());
        }

        // traits
        printer.writeInt(entity.getTraits().size());
        for(ClassEntity el : entity.getTraits().values()){
            printer.writeName(el.getName());
        }

        if (includeData) {
            printer.writeRawData(entity.getData(), Integer.MAX_VALUE);
        } else {
            printer.writeRawData(null);
        }

        // addition raw data
        printer.writeRawData(null);
    }

    @Override
    public ClassEntity load(InputStream input)  throws IOException {
        return load(input, ClassEntity.class);
    }

    public <T extends ClassEntity> T load(InputStream input, Class<T> clazz) throws IOException {
        DumpInputStream data = new DumpInputStream(input);
        T entity   = null;
        try {
            entity = clazz.getConstructor(Context.class).newInstance(context);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        entity.setId(env.scope.nextClassIndex());

        String docComment = data.readUTF();

        if (!docComment.isEmpty()) {
            entity.setDocComment(new DocumentComment(docComment));
        }

        entity.setStatic(data.readBoolean());
        entity.setType(data.readClassType());
        entity.setAbstract(data.readBoolean());
        entity.setFinal(data.readBoolean());
        entity.setName(data.readName());
        entity.setInternalName(data.readName());
        entity.setTrace(data.readTrace(context));

        // parent
        String parent = data.readName();

        if (parent != null){
            ClassEntity parentEntity = module == null ? null : module.findClass(parent);
            if (parentEntity == null) {
                parentEntity = env.fetchClass(parent, true);
            }

            if (parentEntity == null) {
                env.error(env.trace(), ErrorType.E_ERROR, Messages.ERR_CLASS_NOT_FOUND, parent);
            } else {
                ClassEntity.ExtendsResult result = entity.setParent(parentEntity, false);

                if (clazz != GeneratorEntity.class)
                    result.check(env);
            }
        }

        // constants
        int constantCount = data.readInt();

        for(int i = 0; i < constantCount; i++){
            ConstantEntity el = constantDumper.load(input);
            el.setClazz(entity);
            entity.addConstant(el).check(env);
        }

        // properties
        int propertyCount = data.readInt();

        for(int i = 0; i < propertyCount; i++){
            PropertyEntity el = propertyDumper.load(input);
            el.setClazz(entity);
            ClassEntity.PropertyResult result = entity.addProperty(el);
            result.check(env);
        }

        // methods
        entity.__setMethodCounts(data.readInt());

        int methodCount = data.readInt();

        for(int i = 0; i < methodCount; i++){
            MethodEntity el = methodDumper.load(input);
            el.setClazz(entity);
            ClassEntity.SignatureResult result = entity.addMethod(el, null);
            result.check(env);
        }

        entity.updateParentBody().check(env);

        // interfaces
        int interfaceCount = data.readInt();

        for(int i = 0; i < interfaceCount; i++){
            String name = data.readName();

            ClassEntity interfaceEntity = module == null ? null : module.findClass(name);
            if (interfaceEntity == null)
                interfaceEntity = env.fetchClass(name, true);

            if (interfaceEntity == null)
                env.error(env.trace(), ErrorType.E_ERROR, Messages.ERR_INTERFACE_NOT_FOUND, name);

            ClassEntity.ImplementsResult result = entity.addInterface(interfaceEntity);
            result.check(env);
        }

        int traitCount = data.readInt();

        for(int i = 0; i < traitCount; i++) {
            String name = data.readName();

            ClassEntity traitEntity = module == null ? null : module.findClass(name);
            if (traitEntity == null)
                traitEntity = env.fetchClass(name, true);

            if (traitEntity == null)
                env.error(env.trace(), ErrorType.E_ERROR, Messages.ERR_TRAIT_NOT_FOUND, name);
            else if (!traitEntity.isTrait())
                env.error(env.trace(), ErrorType.E_ERROR, Messages.ERR_CANNOT_USE_NON_TRAIT, name);

            entity.addTrait(traitEntity);
        }

        entity.setData(data.readRawData(Integer.MAX_VALUE));
        data.readRawData();

        entity.doneDeclare();
        return entity;
    }
}
