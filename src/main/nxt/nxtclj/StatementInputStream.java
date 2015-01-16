package nxtclj;

import java.io.DataInputStream;

public class StatementInputStream
{
    protected DataInputStream dis;
    protected int[] buf;
    protected int read;
    protected int pos;

    public StatementInputStream(DataInputStream dis) {
        this.dis = dis;
        this.buf = new int[1];
        this.read = 0;
        this.pos = 0;
    }

    public int getPos() {
        return pos;
    }

    public void rewindTo(int pos) throws Exception {
        if (pos >= 0 && pos <= read) {
            this.pos = pos;
        }
        else {
            throw new Exception("Cannot rewind outside of the buffer");
        }
    }

    public int peek() throws Exception {
        int value = next();
        pos --;
        return value;
    }

    public int next() throws Exception {
        if (pos >= read) {
            if (dis.available() < 4) {
                int nextInt = dis.readInt(); // blocking
                buf[pos] = nextInt;
                read++;
            }
            int avail = dis.available() / 4;
            int[] newBuf = new int[read + avail + 1];
            int i = 0;
            for (; i < read; i++) {
                newBuf[i] = buf[i];
            }
            for (; i < read + avail; i++) {
                newBuf[i] = dis.readInt();
            }
            buf = newBuf;
            read += avail;
        }
        return buf[pos++];
    }

    public void flush() {
        buf = new int[1];
        read = 0;
        pos = 0;
    }

    public void close() throws Exception {
        dis.close();
    }
}

