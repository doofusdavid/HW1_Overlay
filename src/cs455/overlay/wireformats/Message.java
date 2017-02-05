package cs455.overlay.wireformats;

import cs455.overlay.dijkstra.NodeDescriptor;
import cs455.overlay.util.NotImplementedException;

import java.io.*;
import java.util.ArrayList;

/**
 * Actual message being sent
 */
public class Message implements Event
{
    private final int type = MessageType.TASK_MESSAGE;
    private NodeDescriptor source;
    private NodeDescriptor destination;
    private int payload;
    private ArrayList<NodeDescriptor> routingPath;


    public Message()
    {
    }

    public Message(byte[] marshalledBytes) throws IOException
    {
        ByteArrayInputStream baInputStream = new ByteArrayInputStream(marshalledBytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(baInputStream));

        int type = din.readInt();
        if (type != MessageType.TASK_MESSAGE)
        {
            throw new IllegalArgumentException("Bytes didn't correspond to a Message.");
        }

        int sourceLength = din.readInt();
        byte[] sourceBytes = new byte[sourceLength];
        din.readFully(sourceBytes);
        this.source = new NodeDescriptor(sourceBytes);

        int destLength = din.readInt();
        byte[] destBytes = new byte[destLength];
        din.readFully(destBytes);
        this.destination = new NodeDescriptor(destBytes);

        this.payload = din.readInt();

        int routingPathLength = din.readInt();
        this.routingPath = new ArrayList<>(routingPathLength);

        for (int i = 0; i < routingPathLength; i++)
        {
            int routeNodeLength = din.readInt();
            byte[] routeBytes = new byte[routeNodeLength];
            din.readFully(routeBytes);
            this.routingPath.add(new NodeDescriptor(routeBytes));
        }

        baInputStream.close();
        din.close();
    }


    @Override
    public int getType()
    {
        throw new NotImplementedException();
    }

    @Override
    public byte[] getBytes() throws IOException
    {
        byte[] marshalledBytes = null;
        ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

        dout.writeInt(this.type);

        byte[] sourceBytes = source.getBytes();
        int sourceLength = sourceBytes.length;
        dout.writeInt(sourceLength);
        dout.write(sourceBytes);

        byte[] destBytes = destination.getBytes();
        int destLength = destBytes.length;
        dout.writeInt(destLength);
        dout.write(destBytes);

        dout.writeInt(this.payload);

        dout.writeInt(routingPath.size());
        for (NodeDescriptor node : routingPath)
        {
            String nodeDescriptor = node.toString();
            byte[] nodeBytes = nodeDescriptor.getBytes();
            int nodeLength = nodeBytes.length;
            dout.writeInt(nodeLength);
            dout.write(nodeBytes);
        }

        dout.flush();
        marshalledBytes = baOutputStream.toByteArray();
        baOutputStream.close();
        dout.close();

        return marshalledBytes;
    }

}
