package cs455.overlay.wireformats;

import java.io.*;

/**
 *  DeregisterRequest - When a node tears down, it must deregister itself from the Registry using this format
 */
public class DeregisterRequest implements Event
{
    private final int type = MessageType.DEREGISTER_REQUEST;
    public String IPAddress;
    public int Port;

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

        byte[] ipBytes = IPAddress.getBytes();
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

    public DeregisterRequest(byte[] marshalledBytes) throws IOException
    {
        ByteArrayInputStream baInputStream = new ByteArrayInputStream(marshalledBytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(baInputStream));

        int type = din.readInt();
        if (type != MessageType.DEREGISTER_REQUEST)
        {
            throw new IllegalArgumentException("Bytes didn't correspond to a DeRegisterRequest.");
        }

        int ipLength = din.readInt();
        byte[] ipBytes = new byte[ipLength];
        din.readFully(ipBytes);

        this.IPAddress = new String(ipBytes);

        this.Port = din.readInt();

        baInputStream.close();
        din.close();
    }

    public DeregisterRequest()
    {
    }
}
