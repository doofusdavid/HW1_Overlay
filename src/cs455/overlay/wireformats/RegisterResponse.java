package cs455.overlay.wireformats;

import java.io.*;

/**
 * Created by david on 1/29/17.
 */
public class RegisterResponse implements Event
{

    private int type;
    public byte statusCode;
    public String additionalInfo;

    public RegisterResponse()
    {
        this.type = MessageType.REGISTER_RESPONSE;
    }

    public RegisterResponse(byte[] marshalledBytes) throws IOException
    {
        ByteArrayInputStream baInputStream = new ByteArrayInputStream(marshalledBytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(baInputStream));

        this.type = din.readInt();

        this.statusCode = din.readByte();

        int aiLength = din.readInt();
        byte[] aiBytes = new byte[aiLength];
        din.readFully(aiBytes);

        this.additionalInfo = new String (aiBytes);

        int ipLength = din.readInt();
        byte[] ipBytes = new byte[ipLength];
        din.readFully(ipBytes);

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
