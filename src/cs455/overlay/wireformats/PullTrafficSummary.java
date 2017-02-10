package cs455.overlay.wireformats;

import java.io.*;


public class PullTrafficSummary implements Event
{
    private final int type = MessageType.PULL_TRAFFIC_SUMMARY;


    public PullTrafficSummary(byte[] marshalledBytes) throws IOException
    {
        ByteArrayInputStream baInputStream = new ByteArrayInputStream(marshalledBytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(baInputStream));

        int type = din.readInt();
        if (type != MessageType.PULL_TRAFFIC_SUMMARY)
        {
            throw new IllegalArgumentException("Bytes didn't correspond to a RegisterResponse.");
        }

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

        dout.flush();
        marshalledBytes = baOutputStream.toByteArray();
        baOutputStream.close();
        dout.close();

        return marshalledBytes;
    }

    @Override
    public int getType()
    {
        return this.type;
    }
}
