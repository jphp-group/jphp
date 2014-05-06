package org.develnext.jphp.scripting.util;

import java.io.*;

public class ReaderInputStream extends InputStream {
    private final Reader reader;
    private final Writer writer;
    private final PipedInputStream inPipe;

    public ReaderInputStream(Reader reader) throws IOException {
        this(reader, null);
    }

    public ReaderInputStream(final Reader reader, String encoding) throws IOException {
        synchronized (this) {
            this.reader = reader;
            inPipe = new PipedInputStream();
            OutputStream outPipe = new PipedOutputStream(inPipe);
            writer = (encoding == null) ? new OutputStreamWriter(outPipe) : new OutputStreamWriter(outPipe, encoding);
        }
        new Thread(new Copier()).start();
    }

    public int read() throws IOException {
        return inPipe.read();
    }

    public int read(byte b[]) throws IOException {
        return inPipe.read(b);
    }

    public int read(byte b[], int off, int len) throws IOException {
        return inPipe.read(b, off, len);
    }

    public long skip(long n) throws IOException {
        return inPipe.skip(n);
    }

    public int available() throws IOException {
        return inPipe.available();
    }

    public synchronized void close() throws IOException {
        close(reader);
        close(writer);
        close(inPipe);
    }

    private static void close(Closeable cl) {
        try {
            cl.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class Copier implements Runnable {
        public void run() {
            char[] buffer = new char[8192];
            try {
                while (true) {
                    int n;
                    synchronized (ReaderInputStream.this) {
                        n = reader.read(buffer);
                    }
                    if (n == -1)
                        break;
                    synchronized (ReaderInputStream.this) {
                        writer.write(buffer, 0, n);
                        writer.flush();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                close(reader);
                close(writer);
            }
        }
    }
}