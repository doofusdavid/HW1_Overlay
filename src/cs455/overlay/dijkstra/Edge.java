package cs455.overlay.dijkstra;

public class Edge
{
    private final String id;
    private final NodeDescriptor source;
    private final NodeDescriptor destination;
    private final int weight;

    public Edge(String id, NodeDescriptor source, NodeDescriptor destination, int weight)
    {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public String getId()
    {
        return id;
    }

    public NodeDescriptor getDestination()
    {
        return destination;
    }

    public NodeDescriptor getSource()
    {
        return source;
    }

    public int getWeight()
    {
        return weight;
    }

    @Override
    public String toString()
    {
        return source + " " + destination;
    }


}