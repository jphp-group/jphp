package org.develnext.jphp.ext.compress.support;

import java.io.IOException;
import java.io.InputStream;

public class ConstrainedInputStream extends InputStream {
  private final InputStream decorated;
  private long length;

  public ConstrainedInputStream(InputStream decorated, long length) {
    this.decorated = decorated;
    this.length = length;
  }

  @Override public int read() throws IOException {
    return (length-- <= 0) ? -1 : decorated.read();
  }

  // TODO: override other methods if you feel it's necessary
  // optionally, extend FilterInputStream instead
}