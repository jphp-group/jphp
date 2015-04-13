package org.develnext.jphp.ext.libgdx.bind;

import com.badlogic.gdx.Graphics;
import org.develnext.jphp.ext.libgdx.classes.graphics.WrapDisplayMode;
import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.memory.support.operation.ArrayMemoryMemoryOperation;
import php.runtime.reflection.ParameterEntity;

import java.util.ArrayList;
import java.util.List;


public class DisplayModeArrayMemoryOperation extends MemoryOperation<Graphics.DisplayMode[]> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] {Graphics.DisplayMode[].class};
    }

    @Override
    public Graphics.DisplayMode[] convert(Environment env, TraceInfo trace, Memory arg) {
        ForeachIterator iterator = arg.getNewIterator(env);
        if (iterator == null) {
            return null;
        }

        List<Graphics.DisplayMode> tmp = new ArrayList<Graphics.DisplayMode>();
        while (iterator.next()) {
            tmp.add(iterator.getValue().toObject(WrapDisplayMode.class).getWrappedObject());
        }

        return tmp.toArray(new Graphics.DisplayMode[tmp.size()]);
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Graphics.DisplayMode[] arg) {
        ArrayMemory r = new ArrayMemory();
        for (Graphics.DisplayMode mode : arg) {
            r.add(new WrapDisplayMode(env, mode));
        }

        return r.toConstant();
    }
}