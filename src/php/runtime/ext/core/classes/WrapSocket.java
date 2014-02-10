package php.runtime.ext.core.classes;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.ext.core.stream.MiscStream;
import php.runtime.ext.core.stream.Stream;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import java.io.IOException;
import java.net.Socket;

import static php.runtime.annotation.Reflection.*;

@Name("php\\net\\Socket")
public class WrapSocket extends BaseObject {
    protected Socket socket;

    public WrapSocket(Environment env, Socket socket) {
        super(env);
        setSocket(socket);
    }

    public WrapSocket(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Signature({
            @Arg(value = "host", optional = @Optional("NULL")),
            @Arg(value = "port", optional = @Optional("NULL"))
    })
    public Memory __construct(Environment env, Memory... args) {
        if (args[0].isNull() && args[1].isNull())
            setSocket(new Socket());
        else {
            try {
                setSocket(new Socket(args[0].toString(), args[1].toInteger()));
            } catch (IOException e) {
                Stream.exception(env, e.getMessage());
            }
        }

        return Memory.NULL;
    }

    @Signature
    public Memory getOutput(Environment env, Memory... args){
        try {
            return new ObjectMemory(new MiscStream(env, socket.getOutputStream()));
        } catch (IOException e) {
            Stream.exception(env, e.getMessage());
        }
        return Memory.NULL;
    }

    @Signature
    public Memory getInput(Environment env, Memory... args){
        try {
            return new ObjectMemory(new MiscStream(env, socket.getInputStream()));
        } catch (IOException e) {
            Stream.exception(env, e.getMessage());
        }

        return Memory.NULL;
    }

    @Signature
    public Memory close(Environment env, Memory... args) {
        try {
            socket.close();
        } catch (IOException e) {
            Stream.exception(env, e.getMessage());
        }

        return Memory.NULL;
    }

    @Signature
    public Memory shutdownInput(Environment env, Memory... args) {
        try {
            socket.shutdownInput();
        } catch (IOException e) {
            Stream.exception(env, e.getMessage());
        }
        return Memory.NULL;
    }

    @Signature
    public Memory shutdownOutput(Environment env, Memory... args) {
        try {
            socket.shutdownOutput();
        } catch (IOException e) {
            Stream.exception(env, e.getMessage());
        }
        return Memory.NULL;
    }
}
