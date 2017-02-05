package cs455.overlay.dijkstra;


public class NodeWeight
{
    public NodeDescriptor source;
    public NodeDescriptor destination;
    public int weight;

    public NodeWeight(NodeDescriptor source, NodeDescriptor destination, int weight)
    {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public NodeWeight(byte[] nodeWeightBytes)
    {
        String nodeWeightString = new String(nodeWeightBytes);
        String[] elements = nodeWeightString.split(" ");
        if (elements.length != 3)
            throw new IllegalArgumentException("Not a valid NodeWeight bytestream");

        this.source = new NodeDescriptor(elements[0]);
        this.destination = new NodeDescriptor(elements[1]);
        this.weight = Integer.parseInt(elements[2]);
    }

    @Override
    public String toString()
    {
        return source.toString() + " " + destination.toString() + " " + weight;
    }

    public byte[] getBytes()
    {
        return this.toString().getBytes();
    }
}
