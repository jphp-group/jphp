package org.develnext.jphp.ext.httpserver.support;

import org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.StringMemory;
import php.runtime.memory.support.MemoryOperation;

import java.util.Map;

public class ExtensionConfigMemoryOperation extends MemoryOperation<ExtensionConfig>{
    @Override
    public Class<?>[] getOperationClasses(){
        return new Class[]{ExtensionConfig.class};
    }

    @Override
    public ExtensionConfig convert(Environment env, TraceInfo trace, Memory arg) throws Throwable{
//        if(arg.isArray()){
//
//        }
        return new ExtensionConfig(arg.toString());
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, ExtensionConfig arg) throws Throwable{
        ArrayMemory result = new ArrayMemory();
        result.refOfIndex("name").assign(arg.getName());

        ArrayMemory parameters = new ArrayMemory();
        for(Map.Entry<String, String> entry : arg.getParameters().entrySet()){
            parameters.refOfIndex(entry.getKey()).assign(entry.getValue());
        }
        result.refOfIndex("parameters").assign(parameters);

        return StringMemory.valueOf(arg.getParameterizedName());
    }
}
