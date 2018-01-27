package php.runtime.ext.core.classes.net;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketException;

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
            @Arg(value = "port", optional = @Optional("NULL")),
            @Arg(value = "backLog", optional = @Optional(value = "50", type = HintType.INT))
    })
    public Memory __construct(Environment env, Memory... args) throws IOException {
        if (args[0].isNull())
            setSocket(new ServerSocket());
        else
            setSocket(new ServerSocket(args[0].toInteger(), args[1].toInteger()));
        return Memory.NULL;
    }

    @Signature
    public Memory accept(Environment env, Memory... args) throws IOException {
        return new ObjectMemory(new WrapSocket(env, socket.accept()));
    }

    @Signature({@Arg("hostname"), @Arg("port"),
            @Arg(value = "backLog", optional = @Optional(value = "50", type = HintType.INT))})
    public Memory bind(Environment env, Memory... args) throws IOException {
        socket.bind(new InetSocketAddress(args[0].toString(), args[1].toInteger()), args[2].toInteger());
        return Memory.NULL;
    }

    @Signature
    public Memory close(Environment env, Memory... args) throws IOException {
        socket.close();
        return Memory.NULL;
    }

    @Signature
    public Memory isClosed(Environment env, Memory... args) {
        return socket.isClosed() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isBound(Environment env, Memory... args) {
        return socket.isBound() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg("timeout"))
    public Memory setSoTimeout(Environment env, Memory... args) throws SocketException {
        socket.setSoTimeout(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature(@Arg("on"))
    public Memory setReuseAddress(Environment env, Memory... args) throws SocketException {
        socket.setReuseAddress(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature(@Arg("size"))
    public Memory setReceiveBufferSize(Environment env, Memory... args) throws SocketException {
        socket.setReceiveBufferSize(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature({@Arg("connectTime"), @Arg("latency"), @Arg("bandWidth")})
    public Memory setPerformancePreferences(Environment env, Memory... args) throws SocketException {
        socket.setPerformancePreferences(args[0].toInteger(), args[1].toInteger(), args[2].toInteger());
        return Memory.NULL;
    }

    @Signature
    public static Memory findAvailableLocalPort(Environment env, Memory... args) {
        try {
            ServerSocket serverSocket = new ServerSocket(0);
            serverSocket.close();
            return LongMemory.valueOf(serverSocket.getLocalPort());
        } catch (IOException e) {
            return Memory.FALSE;
        }
    }

    @Signature(@Arg("port"))
    public static Memory isAvailableLocalPort(Environment env, Memory... args) {
        try {
            ServerSocket serverSocket = new ServerSocket(args[0].toInteger());
            serverSocket.close();
            return Memory.TRUE;
        } catch (IOException e) {
            return Memory.FALSE;
        }
    }
}
