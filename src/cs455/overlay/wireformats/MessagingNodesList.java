package cs455.overlay.wireformats;

import cs455.overlay.dijkstra.NodeDescriptor;

import java.io.*;
import java.util.ArrayList;


public class MessagingNodesList implements Event
{
    private int type = MessageType.MESSAGING_NODES_LIST;
    private int numberOfPeers;
    private ArrayList<NodeDescriptor> neighborNodes;

    public MessagingNodesList(int numberOfPeers, ArrayList<NodeDescriptor> neighborNodes)
    {
        this.numberOfPeers = numberOfPeers;
        this.neighborNodes = neighborNodes;
    }

    public MessagingNodesList(byte[] marshalledBytes) throws IOException
    {
        ByteArrayInputStream baInputStream = new ByteArrayInputStream(marshalledBytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(baInputStream));

        int type = din.readInt();
        if (type != MessageType.MESSAGING_NODES_LIST)
        {
            throw new IllegalArgumentException("Bytes didn't correspond to a MessagingNodeList.");
        }
        this.numberOfPeers = din.readInt();
        this.neighborNodes = new ArrayList<>(this.numberOfPeers);

        for (int i = 0; i < this.numberOfPeers; i++)
        {
            int nodeLength = din.readInt();
            byte[] nodeBytes = new byte[nodeLength];
            din.readFully(nodeBytes);
            this.neighborNodes.add(new NodeDescriptor(nodeBytes));
        }

        baInputStream.close();
        din.close();

    }

    public MessagingNodesList()
    {

    }

    public int getNumberOfPeers()
    {
        return numberOfPeers;
    }

    public ArrayList<NodeDescriptor> getNeighborNodes()
    {
        return neighborNodes;
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

        dout.writeInt(this.numberOfPeers);

        for (NodeDescriptor node : this.neighborNodes)
        {
            String nodeString = node.toString();
            byte[] nodeBytes = nodeString.getBytes();
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
