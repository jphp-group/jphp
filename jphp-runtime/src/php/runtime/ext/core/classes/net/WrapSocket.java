package php.runtime.ext.core.classes.net;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.MiscStream;
import php.runtime.lang.BaseObject;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

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
    public Memory __construct(Environment env, Memory... args) throws IOException {
        if (args[0].isNull() && args[1].isNull())
            setSocket(new Socket());
        else {
            setSocket(new Socket(args[0].toString(), args[1].toInteger()));
        }
        return Memory.NULL;
    }

    @Signature
    public Memory getOutput(Environment env, Memory... args) throws IOException {
        return new ObjectMemory(new MiscStream(env, socket.getOutputStream()));
    }

    @Signature
    public Memory getInput(Environment env, Memory... args) throws IOException {
        return new ObjectMemory(new MiscStream(env, socket.getInputStream()));
    }

    @Signature
    public Memory close(Environment env, Memory... args) throws IOException {
        socket.close();
        return Memory.NULL;
    }

    @Signature
    public Memory shutdownInput(Environment env, Memory... args) throws IOException {
        socket.shutdownInput();
        return Memory.NULL;
    }

    @Signature
    public Memory shutdownOutput(Environment env, Memory... args) throws IOException {
        socket.shutdownOutput();
        return Memory.NULL;
    }

    @Signature
    public Memory isBound(Environment env, Memory... args) {
        return socket.isBound() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isClosed(Environment env, Memory... args) {
        return socket.isClosed() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isConnected(Environment env, Memory... args) {
        return socket.isConnected() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isInputShutdown(Environment env, Memory... args) {
        return socket.isInputShutdown() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isOutputShutdown(Environment env, Memory... args) {
        return socket.isOutputShutdown() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature({@Arg("hostname"), @Arg("port"), @Arg(value = "timeout", optional = @Optional("NULL"))})
    public Memory connect(Environment env, Memory... args) throws IOException {
        if (args[2].isNull())
            socket.connect(new InetSocketAddress(args[0].toString(), args[1].toInteger()));
        else
            socket.connect(new InetSocketAddress(args[0].toString(), args[1].toInteger()), args[2].toInteger());
        return Memory.NULL;
    }

    @Signature({@Arg("hostname"), @Arg("port")})
    public Memory bind(Environment env, Memory... args) throws IOException {
        socket.bind(new InetSocketAddress(args[0].toString(), args[1].toInteger()));
        return Memory.NULL;
    }

    @Signature
    public Memory bindDefault(Environment env, Memory... args) throws IOException {
        socket.bind(null);
        return Memory.NULL;
    }

    @Signature
    public Memory getLocalAddress(Environment env, Memory... args) {
        return new StringMemory(socket.getLocalAddress().toString());
    }

    @Signature
    public Memory getLocalPort(Environment env, Memory... args) {
        return LongMemory.valueOf(socket.getLocalPort());
    }

    @Signature
    public Memory getPort(Environment env, Memory... args) {
        return LongMemory.valueOf(socket.getPort());
    }

    @Signature
    public Memory getAddress(Environment env, Memory... args) {
        return StringMemory.valueOf(socket.getInetAddress().toString());
    }

    @Signature(@Arg("timeout"))
    public Memory setSoTimeout(Environment env, Memory... args) throws SocketException {
        socket.setSoTimeout(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature({@Arg("on"), @Arg("linger")})
    public Memory setSoLinger(Environment env, Memory... args) throws SocketException {
        socket.setSoLinger(args[0].toBoolean(), args[1].toInteger());
        return Memory.NULL;
    }

    @Signature(@Arg("on"))
    public Memory setReuseAddress(Environment env, Memory... args) throws SocketException {
        socket.setReuseAddress(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature(@Arg("on"))
    public Memory setTcpNoDelay(Environment env, Memory... args) throws SocketException {
        socket.setTcpNoDelay(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature(@Arg("on"))
    public Memory setKeepAlive(Environment env, Memory... args) throws SocketException {
        socket.setKeepAlive(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature(@Arg("on"))
    public Memory setOOBInline(Environment env, Memory... args) throws SocketException {
        socket.setOOBInline(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature(@Arg("size"))
    public Memory setReceiveBufferSize(Environment env, Memory... args) throws SocketException {
        socket.setReceiveBufferSize(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature(@Arg("size"))
    public Memory setSendBufferSize(Environment env, Memory... args) throws SocketException {
        socket.setSendBufferSize(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature({@Arg("connectTime"), @Arg("latency"), @Arg("bandWidth")})
    public Memory setPerformancePreferences(Environment env, Memory... args) throws SocketException {
        socket.setPerformancePreferences(args[0].toInteger(), args[1].toInteger(), args[2].toInteger());
        return Memory.NULL;
    }

    @Signature(@Arg("tc"))
    public Memory setTrafficClass(Environment env, Memory... args) throws SocketException {
        socket.setTrafficClass(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature(@Arg("data"))
    public Memory sendUrgentData(Environment env, Memory... args) throws IOException {
        socket.sendUrgentData(args[0].toInteger());
        return Memory.NULL;
    }
}
