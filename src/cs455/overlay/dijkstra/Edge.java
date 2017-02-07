package cs455.overlay.dijkstra;

public class Edge
{
    private final NodeDescriptor source;
    private final NodeDescriptor destination;
    private final int weight;

    public Edge(NodeDescriptor source, NodeDescriptor destination, int weight)
    {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
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
        return source + " " + destination + " " + weight + "\n";
    }

    public boolean contains(NodeDescriptor node)
    {
        if (this.source.equals(node) || this.destination.equals(node))
            return true;
        else
            return false;
    }


    public boolean exists(Object obj)
    {
        Edge edgeTest = (Edge) obj;
        if (this.source.equals(edgeTest.source))
        {
            if (this.destination.equals(edgeTest.destination))
            {
                return true;
            }
        } else if (this.source.equals(edgeTest.destination))
        {
            if (this.destination.equals(edgeTest.source))
            {
                return true;
            }
        }
        return false;
    }
}