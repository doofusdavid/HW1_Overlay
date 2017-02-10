package cs455.overlay.wireformats;

import java.io.*;

/**
 * When a node has completed its task of sending its messages, it notifies
 * the registry using TaskComplete
 */
public class TaskComplete implements Event
{
    private final int type = MessageType.TASK_COMPLETE;
    private String IPAddress;
    private int Port;

    public TaskComplete(String IPAddress, int port)
    {
        this.IPAddress = IPAddress;
        Port = port;
    }

    public TaskComplete(byte[] marshalledBytes) throws IOException
    {
        ByteArrayInputStream baInputStream = new ByteArrayInputStream(marshalledBytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(baInputStream));

        int type = din.readInt();
        if (type != MessageType.TASK_COMPLETE)
        {
            throw new IllegalArgumentException("Bytes didn't correspond to TaskComplete");
        }

        int ipLength = din.readInt();
        byte[] ipBytes = new byte[ipLength];
        din.readFully(ipBytes);
        this.IPAddress = new String(ipBytes);

        this.Port = din.readInt();

        baInputStream.close();
        din.close();

    }

    public String getIPAddress()
    {
        return IPAddress;
    }

    public int getPort()
    {
        return Port;
    }

    @Override
    public int getType()
    {
        return this.type;
    }

    @Override
    public byte[] getBytes() throws IOException
    {
        byte[] marshalledBytes = null;
        ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

        dout.writeInt(this.type);

        byte[] ipBytes = this.IPAddress.getBytes();
        int ipLength = ipBytes.length;
        dout.writeInt(ipLength);
        dout.write(ipBytes);

        dout.writeInt(this.Port);

        dout.flush();
        marshalledBytes = baOutputStream.toByteArray();
        baOutputStream.close();
        dout.close();

        return marshalledBytes;

    }
}
