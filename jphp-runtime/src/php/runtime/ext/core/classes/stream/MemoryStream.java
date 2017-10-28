package php.runtime.ext.core.classes.stream;


import java.util.Arrays;

public class MemoryStream {
    protected int position;
    protected int length;
    protected byte[] data;

    public MemoryStream(byte[] data) {
        this.data = data;
        this.length = data.length;
    }

    public MemoryStream() {
        this.data = new byte[0];
        this.length = 0;
    }

    public void write(byte[] buff, int offset, int length){
        if (position == this.length){
            // append
            this.length += length - offset;
            byte[] tmp = new byte[this.length];
            System.arraycopy(data, 0, tmp, 0, data.length);
            System.arraycopy(buff, 0, tmp, data.length, tmp.length - data.length);
            this.data = tmp;
        } else {
            // insert
            int newLen = position + length - offset;
            if (newLen > this.length){
                this.length = newLen;
                byte[] tmp = new byte[this.length];
                System.arraycopy(data, 0, tmp, 0, position);
                System.arraycopy(buff, 0, tmp, position, tmp.length - position);
                this.data = tmp;
            } else {
                int j = 0;
                for(int i = position; i < position + length - offset; i++, j++){
                    data[i] = buff[j];
                }
            }
        }
        this.position += length - offset;
    }

    public void write(byte[] buff){
        write(buff, 0, buff.length);
    }

    public byte[] read(int length){
        if (length < 0)
            return null;

        int to = position + length;
        if (to > this.length)
            to = this.length;

        if (position == to)
            return null;

        byte[] result = Arrays.copyOfRange(data, position, to);
        position = to;

        return result;
    }

    public byte[] readFully(){
        return read(length - position);
    }

    public boolean seek(int position){
        if (position < 0 || position >= length)
            return false;

        this.position = position;
        return true;
    }

    public boolean eof(){
        return position >= length;
    }

    public void close(){
        //data = new byte[0]; // fix bug with json format to + close.
    }
}
