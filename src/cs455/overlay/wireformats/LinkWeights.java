package cs455.overlay.wireformats;

import cs455.overlay.dijkstra.NodeWeight;

import java.io.*;
import java.util.ArrayList;


public class LinkWeights implements Event
{
    private final int type = MessageType.LINK_WEIGHTS;
    public int numberOfLinks;
    public ArrayList<NodeWeight> nodeWeights;


    public LinkWeights()
    {
    }

    public LinkWeights(byte[] marshalledBytes) throws IOException
    {
        ByteArrayInputStream baInputStream = new ByteArrayInputStream(marshalledBytes);
        DataInputStream din = new DataInputStream(new BufferedInputStream(baInputStream));

        int type = din.readInt();
        if (type != MessageType.LINK_WEIGHTS)
        {
            throw new IllegalArgumentException("Bytes didn't correspond to a LinkWeight.");
        }

        this.numberOfLinks = din.readInt();
        this.nodeWeights = new ArrayList<>(this.numberOfLinks);

        for (int i = 0; i < this.numberOfLinks; i++)
        {
            int nodeWeightLength = din.readInt();
            byte[] nodeWeightBytes = new byte[nodeWeightLength];
            din.readFully(nodeWeightBytes);
            this.nodeWeights.add(new NodeWeight(nodeWeightBytes));
        }

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

        dout.writeInt(this.numberOfLinks);

        for (NodeWeight nw : nodeWeights)
        {
            String nwString = nw.toString();
            byte[] nwBytes = nwString.getBytes();
            int nwLength = nwBytes.length;
            dout.writeInt(nwLength);
            dout.write(nwBytes);
        }

        dout.flush();
        marshalledBytes = baOutputStream.toByteArray();
        baOutputStream.close();
        dout.close();

        return marshalledBytes;

    }
}
