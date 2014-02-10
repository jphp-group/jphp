package php.runtime.ext.core.classes;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.ext.core.stream.Stream;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import java.io.IOException;
import java.net.ServerSocket;

import static php.runtime.annotation.Reflection.*;

@Name("php\\net\\ServerSocket")
public class WrapServerSocket extends BaseObject {
    protected ServerSocket socket;

    public WrapServerSocket(Environment env) {
        super(env);
    }

    public WrapServerSocket(Environment env, ServerSocket socket) {
        super(env);
        setSocket(socket);
    }

    public WrapServerSocket(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public ServerSocket getSocket() {
        return socket;
    }

    public void setSocket(ServerSocket socket) {
        this.socket = socket;
    }

    @Signature({
            @Arg("port"),
            @Arg(value = "backLog", optional = @Optional(value = "50", type = HintType.INT))
    })
    public Memory __construct(Environment env, Memory... args) {
        try {
            setSocket(new ServerSocket(args[0].toInteger(), args[1].toInteger()));
        } catch (IOException e) {
            Stream.exception(env, e.getMessage());
        }
        return Memory.NULL;
    }

    @Signature
    public Memory accept(Environment env, Memory... args){
        try {
            return new ObjectMemory(new WrapSocket(env, socket.accept()));
        } catch (IOException e) {
            Stream.exception(env, e.getMessage());
        }
        return Memory.NULL;
    }
}
