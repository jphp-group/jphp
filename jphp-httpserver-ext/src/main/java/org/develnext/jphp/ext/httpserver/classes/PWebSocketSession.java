package org.develnext.jphp.ext.httpserver.classes;

import org.develnext.jphp.ext.httpserver.HttpServerExtension;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.common.WebSocketSession;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.WrapFuture;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseWrapper;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Abstract
@Name("WebSocketSession")
@Namespace(HttpServerExtension.NS)
public class PWebSocketSession extends BaseWrapper<Session> {
    public PWebSocketSession(Environment env, Session wrappedObject) {
        super(env, wrappedObject);
    }

    public PWebSocketSession(Environment env, WebSocketSession wrappedObject) {
        super(env, wrappedObject);
    }

    public PWebSocketSession(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public long idleTimeout() {
        return getWrappedObject().getIdleTimeout();
    }

    @Signature
    public String remoteAddress() {
        return getWrappedObject().getRemoteAddress().toString();
    }

    @Signature
    public void sendPartialText(String text, boolean isLast) throws IOException {
        getWrappedObject().getRemote().sendPartialString(text, isLast);
    }

    @Signature
    public void sendText(String text, @Nullable Invoker callback) throws IOException {
        if (callback == null) {
            getWrappedObject().getRemote().sendString(text);
        } else {
            getWrappedObject().getRemote().sendString(text, new WriteCallback() {
                @Override
                public void writeFailed(Throwable x) {
                    callback.callAny(x);
                }

                @Override
                public void writeSuccess() {
                    callback.callAny();
                }
            });
        }
    }

    @Signature
    public void flush() throws IOException {
        getWrappedObject().getRemote().flush();
    }

    @Signature
    public BatchMode batchMode() {
        return getWrappedObject().getRemote().getBatchMode();
    }

    @Signature
    public void batchMode(BatchMode mode) {
        getWrappedObject().getRemote().setBatchMode(mode);
    }

    @Signature
    public void disconnect() throws IOException {
        getWrappedObject().disconnect();
    }

    @Signature
    public boolean isOpen() {
        return getWrappedObject().isOpen();
    }

    @Signature
    public boolean isSecure() {
        return getWrappedObject().isSecure();
    }

    @Signature
    public Memory policy(Environment env) {
        return ArrayMemory.ofNullableBean(env, getWrappedObject().getPolicy());
    }

    @Signature
    public String protocolVersion() {
        return getWrappedObject().getProtocolVersion();
    }

    @Signature
    public void close() {
        close(StatusCode.NORMAL);
    }

    @Signature
    public void close(int statusCode) {
        close(statusCode, null);
    }

    @Signature
    public void close(int statusCode, String reason) {
        getWrappedObject().close(statusCode, reason);
    }

}
