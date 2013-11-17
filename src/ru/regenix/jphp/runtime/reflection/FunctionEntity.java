package ru.regenix.jphp.runtime.reflection;

import ru.regenix.jphp.runtime.env.Context;

public class FunctionEntity extends AbstractFunctionEntity {

    private byte[] data;
    protected ModuleEntity module;

    protected FunctionEntity(Context context) {
        super(context);
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public boolean isDeprecated(){
        return false; // TODO
    }

    public ModuleEntity getModule() {
        return module;
    }

    public void setModule(ModuleEntity module) {
        this.module = module;
    }
}
