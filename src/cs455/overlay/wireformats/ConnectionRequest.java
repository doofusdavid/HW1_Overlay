package cs455.overlay.wireformats;

import cs455.overlay.dijkstra.NodeDescriptor;

import java.io.*;


public class ConnectionRequest implements Event
{
    private final int type = MessageType.CONNECTION_REQUEST;
    private NodeDescriptor sourceNode;

    public ConnectionRequest(NodeDescriptor nodeSource)
    {
        this.sourceNode = nodeSource;
    }

    public ConnectionRequest(byte[] marshalledBytes) throws IOException
    {
        ByteArrayInputStream baInputStream = new ByteArrayInputStream(marshalledBytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(baInputStream));

        int type = din.readInt();
        if (type != MessageType.CONNECTION_REQUEST)
        {
            throw new IllegalArgumentException("Bytes didn't correspond to a ConnectionRequest.");
        }

        int nodeLength = din.readInt();
        byte[] nodeBytes = new byte[nodeLength];
        din.readFully(nodeBytes);
        this.sourceNode = new NodeDescriptor(nodeBytes);

        baInputStream.close();
        din.close();

    }

    public NodeDescriptor getSourceNode()
    {
        return sourceNode;
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

        byte[] nodeBytes = sourceNode.getBytes();
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
