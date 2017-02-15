package cs455.overlay.wireformats;

import cs455.overlay.dijkstra.NodeDescriptor;

import java.io.*;


public class ConnectionResponse implements Event
{
    private final int type = MessageType.CONNECTION_RESPONSE;
    private byte status;
    private NodeDescriptor responseNode;

    public ConnectionResponse(byte status, NodeDescriptor responseNode)
    {
        this.status = status;
        this.responseNode = responseNode;
    }

    public ConnectionResponse(byte[] marshalledBytes) throws IOException
    {
        ByteArrayInputStream baInputStream = new ByteArrayInputStream(marshalledBytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(baInputStream));

        int type = din.readInt();
        if (type != MessageType.CONNECTION_RESPONSE)
        {
            throw new IllegalArgumentException("Bytes didn't correspond to a ConnectionResponse.");
        }

        this.status = din.readByte();

        int nodeLength = din.readInt();
        byte[] nodeBytes = new byte[nodeLength];
        din.readFully(nodeBytes);
        this.responseNode = new NodeDescriptor(nodeBytes);

        baInputStream.close();
        din.close();
    }

    public byte getStatus()
    {
        return status;
    }

    public NodeDescriptor getResponseNode()
    {
        return responseNode;
    }

    @Override
    public int getType()
    {
        return type;
    }

    @Override
    public byte[] getBytes() throws IOException
    {
        byte[] marshalledBytes = null;
        ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

        dout.writeInt(this.type);

        dout.writeByte(this.status);

        byte[] nodeBytes = responseNode.getBytes();
        int nodeLength = nodeBytes.length;
        dout.writeInt(nodeLength);
        dout.write(nodeBytes);

        dout.flush();
        marshalledBytes = baOutputStream.toByteArray();
        baOutputStream.close();
        dout.close();

        return marshalledBytes;

    }
}
