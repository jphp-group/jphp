package ru.regenix.jphp.compiler.jvm.runtime.ob;

import ru.regenix.jphp.compiler.jvm.runtime.memory.Memory;
import ru.regenix.jphp.env.Environment;

import java.io.IOException;

public class OutBuffer {
    private StringBuilder builder;

    private Memory callback;
    private int chunkSize;
    private boolean erase;

    private String content;

    private final Environment environment;

    public OutBuffer(Environment environment, Memory callback, int chunkSize, boolean erase) {
        this.environment = environment;
        this.builder = new StringBuilder();
        this.callback = callback;
        this.chunkSize = chunkSize;
        this.erase = erase;
    }

    public OutBuffer(Environment environment, Memory callback, int chunkSize) {
        this(environment, callback, chunkSize, true);
    }

    public OutBuffer(Environment environment, Memory callback) {
        this(environment, callback, 0);
    }

    public OutBuffer(Environment environment) {
        this(environment, null);
    }

    public Memory getCallback() {
        return callback;
    }

    public void setCallback(Memory callback) {
        this.callback = callback;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public boolean isErase() {
        return erase;
    }

    public void setErase(boolean erase) {
        this.erase = erase;
    }

    public void write(String content){
        builder.append(content);
        this.content = null;
    }

    public void flush(){
        try {
            environment.getOutput().write(
                    getContent().getBytes(environment.getDefaultCharset())
            );

            builder = new StringBuilder();
            content = null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getContent(){
        if (content != null)
            return content;

        return content = this.builder.toString();
    }
}
