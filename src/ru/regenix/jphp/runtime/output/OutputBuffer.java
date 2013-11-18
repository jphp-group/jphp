package ru.regenix.jphp.runtime.output;

import ru.regenix.jphp.runtime.memory.Memory;
import ru.regenix.jphp.runtime.env.Environment;

import java.io.*;

public class OutputBuffer {
    private OutputStream output;
    private StringBuilder builder;

    private Memory callback;
    private int chunkSize;
    private boolean erase;

    private final Environment environment;
    private final OutputBuffer parentOutput;

    public OutputBuffer(Environment environment, OutputBuffer parent, Memory callback, int chunkSize, boolean erase) {
        this.environment = environment;
        this.builder = new StringBuilder();

        this.callback = callback;
        this.chunkSize = chunkSize;
        this.erase = erase;
        this.parentOutput = parent;
    }

    public OutputBuffer(Environment environment, OutputBuffer parent, Memory callback, int chunkSize) {
        this(environment, parent, callback, chunkSize, true);
    }

    public OutputBuffer(Environment environment, OutputBuffer parent, Memory callback) {
        this(environment, parent, callback, 4096);
    }

    public OutputBuffer(Environment environment, OutputBuffer parent) {
        this(environment, parent, null);
    }

    public Memory getCallback() {
        return callback;
    }

    public void setOutput(OutputStream output) {
        this.output = output;
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
        if (this.output == null)
            this.builder.append(content);
        else {
            try {
                this.output.write(content.getBytes(environment.getDefaultCharset()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void write(InputStream input) throws IOException {
        makeBinary();

        byte[] buffer = new byte[1024];
        int len;
        while ((len = input.read(buffer)) != -1) {
            output.write(buffer, 0, len);
        }
    }

    private void makeBinary() throws IOException {
        if (output == null){
            output = new ByteArrayOutputStream();
            output.write(this.builder.toString().getBytes(environment.getDefaultCharset()));
            this.builder = null;
        }
    }

    public void flush() throws IOException {
        if (parentOutput != null){
            parentOutput.makeBinary();
            getOutput(parentOutput.output);
        } else {
            if (output != null)
                getOutput(output);
        }
    }

    public void getOutput(OutputStream out) throws IOException {
        if (output != null){
            if (output instanceof ByteArrayOutputStream)
                ((ByteArrayOutputStream) output).writeTo(out);
        } else {
            out.write(builder.toString().getBytes(environment.getDefaultCharset()));
        }
    }

    public byte[] getOutput(){
        if (output != null) {
            if (output instanceof ByteArrayOutputStream)
                return ((ByteArrayOutputStream) output).toByteArray();
            else
                return null;
        } else
            return builder.toString().getBytes(environment.getDefaultCharset());
    }

    public String getOutputAsString(){
        if (output == null)
            return builder.toString();
        else {
            if (output instanceof ByteArrayOutputStream)
                return new String(((ByteArrayOutputStream) output).toByteArray(), environment.getDefaultCharset());
            else
                return null;
        }
    }
}
