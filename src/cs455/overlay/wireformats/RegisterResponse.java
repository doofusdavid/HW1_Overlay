package cs455.overlay.wireformats;

import java.io.*;

/**
 * RegisterResponse - Sent from Registry to MessagingNode after a MessagingNode
 * requests registration
 */
public class RegisterResponse implements Event
{

    private final int type = MessageType.REGISTER_RESPONSE;
    public byte statusCode;
    public String additionalInfo;

    public RegisterResponse()
    {
    }

    public RegisterResponse(byte[] marshalledBytes) throws IOException
    {
        ByteArrayInputStream baInputStream = new ByteArrayInputStream(marshalledBytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(baInputStream));

        int type = din.readInt();
        if (type != MessageType.REGISTER_RESPONSE)
        {
            throw new IllegalArgumentException("Bytes didn't correspond to a RegisterResponse.");
        }

        this.statusCode = din.readByte();

        int aiLength = din.readInt();
        byte[] aiBytes = new byte[aiLength];
        din.readFully(aiBytes);

        this.additionalInfo = new String (aiBytes);

        baInputStream.close();
        din.close();

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
