package cs455.overlay.wireformats;

import java.io.*;

/**
 * The registry informs nodes in the overlay when they should start sending messages to each other
 * using TaskInitiate
 */
public class TaskInitiate implements Event
{
    public final int type = MessageType.TASK_INITIATE;
    public int rounds;

    public TaskInitiate()
    {

    }


    @Override
    public int getType()
    {
        return this.type;
    }

    public TaskInitiate(byte[] marshalledBytes) throws IOException
    {
        ByteArrayInputStream baInputStream = new ByteArrayInputStream(marshalledBytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(baInputStream));

        int type = din.readInt();
        if (type != MessageType.TASK_INITIATE)
        {
            throw new IllegalArgumentException("Bytes didn't correspond to TaskInitiate");
        }

        this.rounds = din.readInt();

        baInputStream.close();
        din.close();
    }

    @Override
    public byte[] getBytes() throws IOException
    {
        byte[] marshalledBytes = null;
        ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

        dout.writeInt(this.type);
        dout.writeInt(this.rounds);

        dout.flush();
        marshalledBytes = baOutputStream.toByteArray();
        baOutputStream.close();
        dout.close();

        return marshalledBytes;
    }
}
