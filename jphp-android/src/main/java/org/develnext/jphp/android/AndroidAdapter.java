package org.develnext.jphp.android;

import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.FunctionEntity;
import php.runtime.reflection.ModuleEntity;
import php.runtime.reflection.helper.ClosureEntity;
import php.runtime.reflection.helper.GeneratorEntity;
import php.runtime.reflection.support.Entity;

public class AndroidAdapter {
    protected final DexClient dexClient = new DexClient();

    private void _adaptModule(ModuleEntity entity) {
        for (FunctionEntity functionEntity : entity.getFunctions()) {
            adapt(functionEntity);
        }

        for (ClassEntity classEntity : entity.getClasses()) {
            adapt(classEntity);
        }

        for (ClosureEntity closureEntity : entity.getClosures()) {
            adapt(closureEntity);
        }

        for (GeneratorEntity generatorEntity : entity.getGenerators()) {
            adapt(generatorEntity);
        }
    }

    public DexClient adapt(Entity entity) {
        dexClient.classToDex(entity.getInternalName(), entity.getData());

        entity.setData(new byte[0]);

        if (entity instanceof ModuleEntity) {
            _adaptModule((ModuleEntity) entity);
        }

        return dexClient;
    }
}
