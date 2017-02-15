package cs455.overlay.wireformats;

import java.io.*;

/**
 * DeregisterResponse - A messaging node responds to the registry with this message
 */
public class DeregisterResponse implements Event
{
    private final int type = MessageType.DEREGISTER_RESPONSE;
    private byte statusCode;
    private String additionalInfo;

    public DeregisterResponse(byte statusCode, String additionalInfo)
    {
        this.statusCode = statusCode;
        this.additionalInfo = additionalInfo;
    }

    public DeregisterResponse(byte[] marshalledBytes) throws IOException
    {
        ByteArrayInputStream baInputStream = new ByteArrayInputStream(marshalledBytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(baInputStream));

        int type = din.readInt();
        if (type != MessageType.DEREGISTER_RESPONSE)
        {
            throw new IllegalArgumentException("Bytes didn't correspond to a DeRegisterResponse.");
        }

        this.statusCode = din.readByte();

        int aiLength = din.readInt();
        byte[] aiBytes = new byte[aiLength];
        din.readFully(aiBytes);

        this.additionalInfo = new String(aiBytes);

        int ipLength = din.readInt();
        byte[] ipBytes = new byte[ipLength];
        din.readFully(ipBytes);

        baInputStream.close();
        din.close();
    }

    public byte getStatusCode()
    {
        return statusCode;
    }

    public String getAdditionalInfo()
    {
        return additionalInfo;
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

        dout.writeByte(this.statusCode);

        byte[] aiBytes = additionalInfo.getBytes();
        int aiLength = aiBytes.length;
        dout.writeInt(aiLength);
        dout.write(aiBytes);

        dout.flush();
        marshalledBytes = baOutputStream.toByteArray();
        baOutputStream.close();
        dout.close();

        return marshalledBytes;
    }
}
